package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import archivo.GestorJSON;
import modelo.GestionProveedores;
import modelo.Proveedor;
import vista.FormularioProveedor;
import vista.VentanaPrincipal;

public class ControladorProveedor implements ActionListener {
    private VentanaPrincipal mainUI;
    private FormularioProveedor vistaProveedor;
    private GestionProveedores gestionModelo;
    private GestorJSON managerArchivos;

    public ControladorProveedor(VentanaPrincipal vistaPrincipal, GestionProveedores modelo, GestorJSON persistencia) {
        this.mainUI = vistaPrincipal;
        this.gestionModelo = modelo;
        this.managerArchivos = persistencia;

        this.mainUI.getItemProveedores().addActionListener(e -> abrirVistaProveedores());
    }

    private void abrirVistaProveedores() {
        if (vistaProveedor == null || vistaProveedor.isClosed()) {
            vistaProveedor = new FormularioProveedor();
            
            vistaProveedor.btnGuardar.addActionListener(this);
            vistaProveedor.btnLimpiar.addActionListener(this);
            vistaProveedor.btnEliminar.addActionListener(this);
            vistaProveedor.btnBuscar.addActionListener(this);

            mainUI.getDesktopPane().add(vistaProveedor);
            vistaProveedor.setVisible(true);
            recargarTabla();
        } else {
            vistaProveedor.toFront();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (vistaProveedor != null) {
            if (e.getSource() == vistaProveedor.btnGuardar) registrarProveedor();
            if (e.getSource() == vistaProveedor.btnLimpiar) limpiarCampos();
            if (e.getSource() == vistaProveedor.btnEliminar) borrarProveedor();
            if (e.getSource() == vistaProveedor.btnBuscar) buscarEnTabla();
        }
    }

    private void registrarProveedor() {
        try {
            String id = vistaProveedor.txtId.getText().trim();
            String nom = vistaProveedor.txtNombre.getText().trim();
            String rtn = vistaProveedor.txtRtn.getText().trim();
            String tel = vistaProveedor.txtTelefono.getText().trim();
            String correo = vistaProveedor.txtCorreo.getText().trim();
            String dir = vistaProveedor.txtDireccion.getText().trim();
            String terminos = vistaProveedor.cbTerminos.getSelectedItem().toString();
            
            if (id.isEmpty() || nom.isEmpty()) {
                JOptionPane.showMessageDialog(vistaProveedor, "Codigo y Razon Social son obligatorios");
                return;
            }

            double limite = Double.parseDouble(vistaProveedor.txtLimiteCredito.getText().trim());
            boolean activo = vistaProveedor.rbActivo.isSelected();

            Proveedor p = new Proveedor(id, nom, rtn, tel, correo, dir, terminos, limite, activo);

            if (gestionModelo.existe(id)) {
                gestionModelo.actualizar(p);
                JOptionPane.showMessageDialog(vistaProveedor, "Proveedor actualizado");
            } else {
                gestionModelo.insertar(p);
                JOptionPane.showMessageDialog(vistaProveedor, "Proveedor guardado");
            }

            managerArchivos.exportarProveedoresJSON(gestionModelo.listar());
            limpiarCampos();
            recargarTabla();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaProveedor, "Limite de credito debe ser un numero valido", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        vistaProveedor.txtId.setText("");
        vistaProveedor.txtNombre.setText("");
        vistaProveedor.txtRtn.setText("");
        vistaProveedor.txtTelefono.setText("");
        vistaProveedor.txtCorreo.setText("");
        vistaProveedor.txtLimiteCredito.setText("");
        vistaProveedor.txtDireccion.setText("");
        vistaProveedor.cbTerminos.setSelectedIndex(0);
        vistaProveedor.rbActivo.setSelected(true);
        vistaProveedor.txtId.requestFocus();
    }

    private void borrarProveedor() {
        int fila = vistaProveedor.tablaProveedores.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vistaProveedor, "Selecciona un proveedor de la tabla");
            return;
        }
        
        String idTarget = vistaProveedor.tablaProveedores.getValueAt(fila, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(vistaProveedor, "Eliminar al proveedor? " + idTarget + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            gestionModelo.eliminar(idTarget);
            managerArchivos.exportarProveedoresJSON(gestionModelo.listar());
            recargarTabla();
            JOptionPane.showMessageDialog(vistaProveedor, "Eliminado");
        }
    }

    private void buscarEnTabla() {
        String filtro = vistaProveedor.txtBuscar.getText().toLowerCase().trim();
        vistaProveedor.modeloTabla.setRowCount(0);
        
        for (Proveedor p : gestionModelo.listar()) {
            if (p.getNombre().toLowerCase().contains(filtro) || p.getId().toLowerCase().contains(filtro)) {
                vistaProveedor.modeloTabla.addRow(new Object[]{
                    p.getId(), p.getNombre(), p.getTelefono(), p.getTerminosPago(), p.getLimiteCredito(), p.isActivo() ? "Activo" : "Inactivo"
                });
            }
        }
    }

    private void recargarTabla() {
        vistaProveedor.modeloTabla.setRowCount(0);
        for (Proveedor p : gestionModelo.listar()) {
            vistaProveedor.modeloTabla.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getTelefono(), p.getTerminosPago(), p.getLimiteCredito(), p.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }
}