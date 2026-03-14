package vista;

import controlador.ClienteControl;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrmCliente extends JFrame {

    private final ClienteControl CONTROL;
    private String accion;
    private String nombreAnterior;

    private JTabbedPane tabGeneral;
    private JPanel panelListado;
    private JPanel panelMantenimiento;
    private JTable tabla;
    private JTextField txtRut;
    private JTextField txtNombre;
    private JButton btnNuevo, btnEditar, btnGuardar, btnCancelar;

    public FrmCliente() {
        setTitle("Gestión de Clientes");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));

        this.CONTROL = new ClienteControl();
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
        scrollPane.setBounds(20, 70, 530, 280);
        panelListado.add(scrollPane);

        tabla = new JTable();
        scrollPane.setViewportView(tabla);

        panelMantenimiento = new JPanel();
        panelMantenimiento.setLayout(null);
        tabGeneral.addTab("Mantenimiento", null, panelMantenimiento, null);

        JLabel lblRut = new JLabel("RUT:");
        lblRut.setBounds(50, 50, 80, 20);
        panelMantenimiento.add(lblRut);

        txtRut = new JTextField();
        txtRut.setBounds(140, 50, 200, 25);
        panelMantenimiento.add(txtRut);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(50, 100, 80, 20);
        panelMantenimiento.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(140, 100, 250, 25);
        panelMantenimiento.add(txtNombre);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(140, 180, 100, 30);
        panelMantenimiento.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(270, 180, 100, 30);
        panelMantenimiento.add(btnCancelar);

        btnNuevo.addActionListener(e -> {
            tabGeneral.setEnabledAt(0, false);
            tabGeneral.setEnabledAt(1, true);
            tabGeneral.setSelectedIndex(1);
            accion = "guardar";
            btnGuardar.setText("Guardar");
            txtRut.setEditable(true);
        });

        btnEditar.addActionListener(e -> {
            if (tabla.getSelectedRowCount() == 1) {
                String rut = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 0));
                String nombre = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 1));
                nombreAnterior = nombre;

                txtRut.setText(rut);
                txtNombre.setText(nombre);

                tabGeneral.setEnabledAt(0, false);
                tabGeneral.setEnabledAt(1, true);
                tabGeneral.setSelectedIndex(1);
                accion = "editar";
                btnGuardar.setText("Editar");
                txtRut.setEditable(false);
            } else {
                mensajeError("Seleccione un registro a Editar");
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
        if (txtRut.getText().trim().length() == 0 || txtRut.getText().trim().length() > 11) {
            mensajeError("Debes ingresar un RUT válido (máx 11 caracteres).");
            txtRut.requestFocus();
            return;
        }
        if (txtNombre.getText().trim().length() == 0 || txtNombre.getText().trim().length() > 45) {
            mensajeError("Debes ingresar un nombre válido (máx 45 caracteres).");
            txtNombre.requestFocus();
            return;
        }

        String resp;
        if (accion.equals("editar")) {
            resp = CONTROL.actualizar(Integer.parseInt(txtRut.getText().trim()), txtNombre.getText().trim(), nombreAnterior);
        } else {
            resp = CONTROL.insertar(Integer.parseInt(txtRut.getText().trim()), txtNombre.getText().trim());
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
    }

    private void listar() {
        tabla.setModel(CONTROL.listar());
        TableRowSorter orden = new TableRowSorter(tabla.getModel());
        tabla.setRowSorter(orden);
    }

    private void limpiar() {
        txtRut.setText("");
        txtNombre.setText("");
        accion = "guardar";
    }

    private void mensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema", JOptionPane.ERROR_MESSAGE);
    }

    private void mensajeOK(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema", JOptionPane.INFORMATION_MESSAGE);
    }
}