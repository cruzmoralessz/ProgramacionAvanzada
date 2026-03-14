package vista;

import controlador.ProductoCONTROL;
import entidades.Categoria;
import entidades.Proveedor;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class FrmProducto extends JFrame {

    private final ProductoCONTROL CONTROL;
    private String accion;
    private String nombreAnterior;

    private JTabbedPane tabGeneral;
    private JPanel panelListado, panelMantenimiento;
    private JTable tabla;
    private JTextField txtId, txtNombre, txtPrecio, txtStock;
    private JComboBox<Object> cboCategoria; 
    private JComboBox<Object> cboProveedor;
    private JButton btnNuevo, btnEditar, btnGuardar, btnCancelar;

    public FrmProducto() {
        setTitle("Gestión de Productos");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));

        this.CONTROL = new ProductoCONTROL();
        this.accion = "guardar";

        initComponents();
        tabGeneral.setEnabledAt(1, false);
        
        listar();
        cargarCategoria();
        cargarProveedor();
    }

    private void cargarCategoria() {
        DefaultComboBoxModel<Object> item = CONTROL.seleccionarCategoria();
        cboCategoria.setModel(item);
    }

    private void cargarProveedor() {
        DefaultComboBoxModel<Object> item = CONTROL.seleccionarProveedor();
        cboProveedor.setModel(item);
    }

    private void listar() {
        tabla.setModel(CONTROL.listar());
        TableRowSorter<javax.swing.table.TableModel> orden = new TableRowSorter<>(tabla.getModel());
        tabla.setRowSorter(orden);
    }

    private void initComponents() {
        tabGeneral = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabGeneral, BorderLayout.CENTER);

        panelListado = new JPanel();
        panelListado.setLayout(null);
        tabGeneral.addTab("Listado", null, panelListado, null);

        btnNuevo = new JButton("Nuevo");
        btnNuevo.setBounds(450, 20, 90, 30);
        panelListado.add(btnNuevo);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(560, 20, 90, 30);
        panelListado.add(btnEditar);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 70, 640, 380);
        panelListado.add(scrollPane);

        tabla = new JTable();
        scrollPane.setViewportView(tabla);

        panelMantenimiento = new JPanel();
        panelMantenimiento.setLayout(null);
        tabGeneral.addTab("Mantenimiento", null, panelMantenimiento, null);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(50, 30, 100, 20);
        panelMantenimiento.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(160, 30, 150, 25);
        panelMantenimiento.add(txtId);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(50, 75, 100, 20);
        panelMantenimiento.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(160, 75, 300, 25);
        panelMantenimiento.add(txtNombre);

        JLabel lblCat = new JLabel("Categoría:");
        lblCat.setBounds(50, 120, 100, 20);
        panelMantenimiento.add(lblCat);

        cboCategoria = new JComboBox<>();
        cboCategoria.setBounds(160, 120, 300, 25);
        panelMantenimiento.add(cboCategoria);

        JLabel lblProv = new JLabel("Proveedor:");
        lblProv.setBounds(50, 165, 100, 20);
        panelMantenimiento.add(lblProv);

        cboProveedor = new JComboBox<>();
        cboProveedor.setBounds(160, 165, 300, 25);
        panelMantenimiento.add(cboProveedor);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(50, 210, 100, 20);
        panelMantenimiento.add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(160, 210, 150, 25);
        panelMantenimiento.add(txtPrecio);

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setBounds(50, 255, 100, 20);
        panelMantenimiento.add(lblStock);

        txtStock = new JTextField();
        txtStock.setBounds(160, 255, 150, 25);
        panelMantenimiento.add(txtStock);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(160, 320, 100, 30);
        panelMantenimiento.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(280, 320, 100, 30);
        panelMantenimiento.add(btnCancelar);

        btnNuevo.addActionListener(e -> {
            limpiar();
            tabGeneral.setEnabledAt(0, false);
            tabGeneral.setEnabledAt(1, true);
            tabGeneral.setSelectedIndex(1);
            accion = "guardar";
            btnGuardar.setText("Guardar");
            txtId.setEditable(true);
        });

        btnEditar.addActionListener(e -> {
            if (tabla.getSelectedRowCount() == 1) {
                txtId.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 0)));
                txtNombre.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 1)));
                nombreAnterior = txtNombre.getText();
                txtPrecio.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 2)));
                txtStock.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 3)));

                Categoria cat = new Categoria();
                cat.setId(Integer.parseInt(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 4))));
                cat.setNombre(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 5)));
                cboCategoria.getModel().setSelectedItem(cat);

                Proveedor prov = new Proveedor();
                prov.setRut(Integer.parseInt(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 6))));
                prov.setNombre(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 7)));
                cboProveedor.getModel().setSelectedItem(prov);

                tabGeneral.setEnabledAt(0, false);
                tabGeneral.setEnabledAt(1, true);
                tabGeneral.setSelectedIndex(1);
                accion = "editar";
                btnGuardar.setText("Editar");
                txtId.setEditable(false);
            } else {
                mensajeError("Seleccione un registro a Editar.");
            }
        });

        btnGuardar.addActionListener(e -> guardarAccion());

        btnCancelar.addActionListener(e -> {
            limpiar();
            tabGeneral.setEnabledAt(0, true);
            tabGeneral.setEnabledAt(1, false);
            tabGeneral.setSelectedIndex(0);
        });
    }

    private void guardarAccion() {
        if (txtId.getText().trim().isEmpty() || txtId.getText().trim().length() > 11) {
            mensajeError("Debes ingresar un ID válido.");
            return;
        }
        if (txtNombre.getText().trim().isEmpty() || txtNombre.getText().trim().length() > 45) {
            mensajeError("Debes ingresar un nombre válido.");
            return;
        }
        if (txtPrecio.getText().trim().isEmpty() || txtStock.getText().trim().isEmpty()) {
            mensajeError("Precio y Stock son obligatorios.");
            return;
        }
        if (cboCategoria.getSelectedItem() == null || cboProveedor.getSelectedItem() == null) {
            mensajeError("Debes seleccionar una Categoría y un Proveedor.");
            return;
        }

        Categoria cat = (Categoria) cboCategoria.getSelectedItem();
        Proveedor prov = (Proveedor) cboProveedor.getSelectedItem();

        String resp;
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());

            if (accion.equals("editar")) {
                resp = CONTROL.actualizar(id, txtNombre.getText().trim(), nombreAnterior, precio, stock, cat.getId(), prov.getRut());
            } else {
                resp = CONTROL.insertar(id, txtNombre.getText().trim(), precio, stock, cat.getId(), prov.getRut());
            }

            if (resp.equals("OK")) {
                mensajeOK(accion.equals("editar") ? "Actualizado correctamente" : "Registrado correctamente");
                limpiar();
                listar();
                tabGeneral.setEnabledAt(0, true);
                tabGeneral.setEnabledAt(1, false);
                tabGeneral.setSelectedIndex(0);
            } else {
                mensajeError(resp);
            }
        } catch (NumberFormatException ex) {
            mensajeError("Error en formato de números (ID, Precio o Stock).");
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        if (cboCategoria.getItemCount() > 0) cboCategoria.setSelectedIndex(0);
        if (cboProveedor.getItemCount() > 0) cboProveedor.setSelectedIndex(0);
        accion = "guardar";
    }

    private void mensajeError(String m) { JOptionPane.showMessageDialog(this, m, "Sistema", JOptionPane.ERROR_MESSAGE); }
    private void mensajeOK(String m) { JOptionPane.showMessageDialog(this, m, "Sistema", JOptionPane.INFORMATION_MESSAGE); }
}