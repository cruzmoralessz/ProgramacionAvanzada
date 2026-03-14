package vista;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableRowSorter;

import controlador.ProveedorControl;

public class FrmProveedor extends JFrame {

    private final ProveedorControl CONTROL;
    private String accion;
    private String nombreAnterior;

    // Componentes
    private JTabbedPane tabGeneral;
    private JPanel panelListado, panelMantenimiento;
    private JTable tabla;
    private JTextField txtRut, txtNombre, txtTelefono, txtPaginaWeb;
    private JButton btnNuevo, btnEditar, btnGuardar, btnCancelar;

    public FrmProveedor() {
        setTitle("Gestión de Proveedores");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));

        this.CONTROL = new ProveedorControl();
        this.accion = "guardar";

        initComponents();
        tabGeneral.setEnabledAt(1, false);
        listar();
    }

    private void initComponents() {
        tabGeneral = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabGeneral, BorderLayout.CENTER);

        // --- LISTADO ---
        panelListado = new JPanel();
        panelListado.setLayout(null);
        tabGeneral.addTab("Listado", null, panelListado, null);

        btnNuevo = new JButton("Nuevo");
        btnNuevo.setBounds(400, 20, 90, 30);
        panelListado.add(btnNuevo);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(510, 20, 90, 30);
        panelListado.add(btnEditar);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 70, 580, 330);
        panelListado.add(scrollPane);

        tabla = new JTable();
        scrollPane.setViewportView(tabla);

        // --- MANTENIMIENTO ---
        panelMantenimiento = new JPanel();
        panelMantenimiento.setLayout(null);
        tabGeneral.addTab("Mantenimiento", null, panelMantenimiento, null);

        JLabel lblRut = new JLabel("RUT:");
        lblRut.setBounds(50, 40, 100, 20);
        panelMantenimiento.add(lblRut);

        txtRut = new JTextField();
        txtRut.setBounds(160, 40, 200, 25);
        panelMantenimiento.add(txtRut);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(50, 90, 100, 20);
        panelMantenimiento.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(160, 90, 300, 25);
        panelMantenimiento.add(txtNombre);

        JLabel lblTel = new JLabel("Teléfono:");
        lblTel.setBounds(50, 140, 100, 20);
        panelMantenimiento.add(lblTel);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(160, 140, 200, 25);
        panelMantenimiento.add(txtTelefono);

        JLabel lblWeb = new JLabel("Página Web:");
        lblWeb.setBounds(50, 190, 100, 20);
        panelMantenimiento.add(lblWeb);

        txtPaginaWeb = new JTextField();
        txtPaginaWeb.setBounds(160, 190, 300, 25);
        panelMantenimiento.add(txtPaginaWeb);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(160, 260, 100, 30);
        panelMantenimiento.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(280, 260, 100, 30);
        panelMantenimiento.add(btnCancelar);

        // --- LÓGICA DE EVENTOS ---
        btnNuevo.addActionListener(e -> {
            limpiar();
            tabGeneral.setEnabledAt(0, false);
            tabGeneral.setEnabledAt(1, true);
            tabGeneral.setSelectedIndex(1);
            accion = "guardar";
            btnGuardar.setText("Guardar");
            txtRut.setEditable(true);
        });

        btnEditar.addActionListener(e -> {
            if (tabla.getSelectedRowCount() == 1) {
                txtRut.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 0)));
                txtNombre.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 1)));
                txtTelefono.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 2)));
                txtPaginaWeb.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 3)));
                nombreAnterior = txtNombre.getText();

                tabGeneral.setEnabledAt(0, false);
                tabGeneral.setEnabledAt(1, true);
                tabGeneral.setSelectedIndex(1);
                accion = "editar";
                btnGuardar.setText("Editar");
                txtRut.setEditable(false);
            } else {
                mensajeError("Seleccione un registro.");
            }
        });

        btnGuardar.addActionListener(e -> {
            if (txtRut.getText().isEmpty() || txtNombre.getText().isEmpty()) {
                mensajeError("RUT y Nombre son obligatorios.");
                return;
            }
            
            String resp;
            int rut = Integer.parseInt(txtRut.getText());
            if (accion.equals("editar")) {
                resp = CONTROL.actualizar(rut, txtNombre.getText(), nombreAnterior, txtTelefono.getText(), txtPaginaWeb.getText());
            } else {
                resp = CONTROL.insertar(rut, txtNombre.getText(), txtTelefono.getText(), txtPaginaWeb.getText());
            }

            if (resp.equals("OK")) {
                mensajeOK("Operación realizada con éxito");
                limpiar();
                listar();
                tabGeneral.setEnabledAt(0, true);
                tabGeneral.setEnabledAt(1, false);
                tabGeneral.setSelectedIndex(0);
            } else {
                mensajeError(resp);
            }
        });

        btnCancelar.addActionListener(e -> {
            limpiar();
            tabGeneral.setEnabledAt(0, true);
            tabGeneral.setEnabledAt(1, false);
            tabGeneral.setSelectedIndex(0);
        });
    }

    private void listar() {
        tabla.setModel(CONTROL.listar());
        TableRowSorter orden = new TableRowSorter(tabla.getModel());
        tabla.setRowSorter(orden);
    }

    private void limpiar() {
        txtRut.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtPaginaWeb.setText("");
    }

    private void mensajeError(String m) { JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE); }
    private void mensajeOK(String m) { JOptionPane.showMessageDialog(this, m, "Éxito", JOptionPane.INFORMATION_MESSAGE); }
}