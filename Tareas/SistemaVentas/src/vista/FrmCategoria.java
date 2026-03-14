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

import controlador.CategoriaCONTROL;

public class FrmCategoria extends JFrame {

    private final CategoriaCONTROL CONTROL;
    private String accion;
    private String nombreAnterior;

    private JTabbedPane tabGeneral;
    private JPanel panelListado;
    private JPanel panelMantenimiento;
    private JTable tabla;
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JButton btnNuevo, btnEditar, btnGuardar, btnCancelar;

    public FrmCategoria() {
        setTitle("Gestión de Categorías");
        setSize(600, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));

        this.CONTROL = new CategoriaCONTROL();
        this.accion = "guardar";

        initComponents();

        tabGeneral.setEnabledAt(1, false);
        listar();
    }

    private void initComponents() {
        tabGeneral = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabGeneral, BorderLayout.CENTER);

        panelListado = new JPanel();
        panelListado.setLayout(null); 
        tabGeneral.addTab("Listado", null, panelListado, null);

        btnNuevo = new JButton("Nuevo");
        btnNuevo.setBounds(350, 20, 90, 30);
        panelListado.add(btnNuevo);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(460, 20, 90, 30);
        panelListado.add(btnEditar);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 70, 530, 310);
        panelListado.add(scrollPane);

        tabla = new JTable();
        scrollPane.setViewportView(tabla);

        panelMantenimiento = new JPanel();
        panelMantenimiento.setLayout(null);
        tabGeneral.addTab("Mantenimiento", null, panelMantenimiento, null);

        JLabel lblId = new JLabel("ID Categoría:");
        lblId.setBounds(50, 50, 100, 20);
        panelMantenimiento.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(150, 50, 150, 25);
        panelMantenimiento.add(txtId);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(50, 100, 100, 20);
        panelMantenimiento.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(150, 100, 250, 25);
        panelMantenimiento.add(txtNombre);

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setBounds(50, 150, 100, 20);
        panelMantenimiento.add(lblDesc);

        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(150, 150, 250, 25);
        panelMantenimiento.add(txtDescripcion);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(150, 230, 100, 30);
        panelMantenimiento.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(280, 230, 100, 30);
        panelMantenimiento.add(btnCancelar);

        btnNuevo.addActionListener(e -> {
            tabGeneral.setEnabledAt(0, false);
            tabGeneral.setEnabledAt(1, true);
            tabGeneral.setSelectedIndex(1);
            accion = "guardar";
            btnGuardar.setText("Guardar");
            txtId.setEditable(true);
        });

        btnEditar.addActionListener(e -> {
            if (tabla.getSelectedRowCount() == 1) {
                String id = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 0));
                String nombre = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 1));
                String descripcion = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 2));
                nombreAnterior = nombre;

                txtId.setText(id);
                txtNombre.setText(nombre);
                txtDescripcion.setText(descripcion);

                tabGeneral.setEnabledAt(0, false);
                tabGeneral.setEnabledAt(1, true);
                tabGeneral.setSelectedIndex(1);
                accion = "editar";
                btnGuardar.setText("Editar");
                txtId.setEditable(false);
            } else {
                mensajeError("Seleccione un registro de la tabla para Editar.");
            }
        });

        btnGuardar.addActionListener(e -> guardarAccion());

        btnCancelar.addActionListener(e -> {
            limpiar();
            tabGeneral.setEnabledAt(0, true);
            tabGeneral.setEnabledAt(1, false);
            tabGeneral.setSelectedIndex(0);
            accion = "guardar";
        });
    }

    private void guardarAccion() {
        if (txtId.getText().trim().isEmpty()) {
            mensajeError("Debes ingresar un ID válido.");
            txtId.requestFocus();
            return;
        }
        if (txtNombre.getText().trim().isEmpty() || txtNombre.getText().trim().length() > 45) {
            mensajeError("Debes ingresar un nombre válido (máx 45 caracteres).");
            txtNombre.requestFocus();
            return;
        }

        int id;
        try {
            id = Integer.parseInt(txtId.getText().trim());
        } catch (NumberFormatException ex) {
            mensajeError("El ID debe ser un número entero válido.");
            txtId.requestFocus();
            return;
        }

        String resp;
        if (accion.equals("editar")) {
            resp = CONTROL.actualizar(id, txtNombre.getText().trim(), nombreAnterior, txtDescripcion.getText().trim());
        } else {
            resp = CONTROL.insertar(id, txtNombre.getText().trim(), txtDescripcion.getText().trim());
        }

        if (resp.equals("OK")) {
            mensajeOK(accion.equals("editar") ? "Categoría actualizada correctamente" : "Categoría registrada correctamente");
            limpiar();
            listar();
            tabGeneral.setEnabledAt(0, true);
            tabGeneral.setEnabledAt(1, false);
            tabGeneral.setSelectedIndex(0);
        } else {
            mensajeError(resp);
        }
    }

    private void listar() {
        tabla.setModel(CONTROL.listar());
        TableRowSorter<javax.swing.table.TableModel> orden = new TableRowSorter<>(tabla.getModel());
        tabla.setRowSorter(orden);
    }

    private void limpiar() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        accion = "guardar";
    }

    private void mensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema", JOptionPane.ERROR_MESSAGE);
    }

    private void mensajeOK(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema", JOptionPane.INFORMATION_MESSAGE);
    }
}