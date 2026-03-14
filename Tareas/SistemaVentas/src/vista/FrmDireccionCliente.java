package vista;

import controlador.DireccionClienteCONTROL;
import controlador.ClienteControl;
import entidades.Cliente;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class FrmDireccionCliente extends JFrame {

    private final DireccionClienteCONTROL CONTROL;
    private final ClienteControl CONTROLCLI;
    private String accion;

    private JTabbedPane tabGeneral;
    private JPanel panelListado, panelMantenimiento;
    private JTable tabla;
    private JTextField txtCalle, txtNumero, txtCiudad, txtComuna;
    private JComboBox<Object> cboCliente;
    private JButton btnNuevo, btnEditar, btnGuardar, btnCancelar;

    public FrmDireccionCliente() {
        setTitle("Direcciones de Clientes");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));

        this.CONTROL = new DireccionClienteCONTROL();
        this.CONTROLCLI = new ClienteControl();
        this.accion = "guardar";

        initComponents();
        tabGeneral.setEnabledAt(1, false);
        
        listar();
        cargarClientes();
    }

    private void cargarClientes() {
        DefaultComboBoxModel<Object> items = CONTROLCLI.seleccionar();
        cboCliente.setModel(items);
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

        panelMantenimiento = new JPanel();
        panelMantenimiento.setLayout(null);
        tabGeneral.addTab("Mantenimiento", null, panelMantenimiento, null);

        JLabel lblCli = new JLabel("Cliente:");
        lblCli.setBounds(50, 30, 100, 20);
        panelMantenimiento.add(lblCli);

        cboCliente = new JComboBox<>();
        cboCliente.setBounds(150, 30, 300, 25);
        panelMantenimiento.add(cboCliente);

        JLabel lblCalle = new JLabel("Calle:");
        lblCalle.setBounds(50, 80, 100, 20);
        panelMantenimiento.add(lblCalle);

        txtCalle = new JTextField();
        txtCalle.setBounds(150, 80, 300, 25);
        panelMantenimiento.add(txtCalle);

        JLabel lblNum = new JLabel("Número:");
        lblNum.setBounds(50, 130, 100, 20);
        panelMantenimiento.add(lblNum);

        txtNumero = new JTextField();
        txtNumero.setBounds(150, 130, 150, 25);
        panelMantenimiento.add(txtNumero);

        JLabel lblCiudad = new JLabel("Ciudad:");
        lblCiudad.setBounds(50, 180, 100, 20);
        panelMantenimiento.add(lblCiudad);

        txtCiudad = new JTextField();
        txtCiudad.setBounds(150, 180, 200, 25);
        panelMantenimiento.add(txtCiudad);

        JLabel lblComuna = new JLabel("Comuna:");
        lblComuna.setBounds(50, 230, 100, 20);
        panelMantenimiento.add(lblComuna);

        txtComuna = new JTextField();
        txtComuna.setBounds(150, 230, 200, 25);
        panelMantenimiento.add(txtComuna);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(150, 300, 100, 30);
        panelMantenimiento.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(270, 300, 100, 30);
        panelMantenimiento.add(btnCancelar);

        btnNuevo.addActionListener(e -> {
            limpiar();
            tabGeneral.setEnabledAt(0, false);
            tabGeneral.setEnabledAt(1, true);
            tabGeneral.setSelectedIndex(1);
            accion = "guardar";
            btnGuardar.setText("Guardar");
            cboCliente.setEnabled(true);
        });

        btnEditar.addActionListener(e -> {
            if (tabla.getSelectedRowCount() == 1) {
                Cliente cli = new Cliente();
                cli.setRut(Integer.parseInt(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 0))));
                cli.setNombre(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 1)));
                cboCliente.getModel().setSelectedItem(cli);

                txtCalle.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 2)));
                txtNumero.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 3)));
                txtCiudad.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 4)));
                txtComuna.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 5)));

                tabGeneral.setEnabledAt(0, false);
                tabGeneral.setEnabledAt(1, true);
                tabGeneral.setSelectedIndex(1);
                accion = "editar";
                btnGuardar.setText("Editar");
                cboCliente.setEnabled(false);
            } else {
                mensajeError("Seleccione una dirección de la tabla para Editar.");
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
        if (txtCalle.getText().trim().isEmpty() || txtNumero.getText().trim().isEmpty()) {
            mensajeError("La calle y el número son obligatorios.");
            return;
        }
        if (cboCliente.getSelectedItem() == null) {
            mensajeError("Debe seleccionar un Cliente.");
            return;
        }

        Cliente seleccionado = (Cliente) cboCliente.getSelectedItem();
        String resp;
        String numeroStr = txtNumero.getText().trim();

        if (accion.equals("editar")) {
            resp = CONTROL.actualizar(seleccionado.getRut(), txtCalle.getText().trim(), numeroStr, txtCiudad.getText().trim(), txtComuna.getText().trim());
        } else {
            resp = CONTROL.insertar(seleccionado.getRut(), txtCalle.getText().trim(), numeroStr, txtCiudad.getText().trim(), txtComuna.getText().trim());
        }

        if (resp.equals("OK")) {
            mensajeOK(accion.equals("editar") ? "Actualizado correctamente" : "Guardado correctamente");
            limpiar();
            listar();
            tabGeneral.setEnabledAt(0, true);
            tabGeneral.setEnabledAt(1, false);
            tabGeneral.setSelectedIndex(0);
        } else {
            mensajeError(resp);
        }
    }

    private void limpiar() {
        txtCalle.setText("");
        txtNumero.setText("");
        txtCiudad.setText("");
        txtComuna.setText("");
        if (cboCliente.getItemCount() > 0) cboCliente.setSelectedIndex(0);
        accion = "guardar";
    }

    private void mensajeError(String m) { JOptionPane.showMessageDialog(this, m, "Sistema", JOptionPane.ERROR_MESSAGE); }
    private void mensajeOK(String m) { JOptionPane.showMessageDialog(this, m, "Sistema", JOptionPane.INFORMATION_MESSAGE); }
}