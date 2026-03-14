package vista;

import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableRowSorter;

import controlador.ClienteControl;
import controlador.TelefonoControl;
import entidades.Cliente;

public class FrmTelefono extends JFrame {

    private final TelefonoControl CONTROL;
    private final ClienteControl CONTROLCLI;
    private String accion;
    private String numeroAnterior;

    private JTabbedPane tabGeneral;
    private JPanel panelListado, panelMantenimiento;
    private JTable tabla;
    private JTextField txtId, txtNumero;
    
    private JComboBox<Object> cboCliente; 
    private JButton btnNuevo, btnEditar, btnGuardar, btnCancelar;

    public FrmTelefono() {
        setTitle("Gestión de Teléfonos");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));

        this.CONTROL = new TelefonoControl();
        this.CONTROLCLI = new ClienteControl();
        this.accion = "guardar";

        initComponents();
        tabGeneral.setEnabledAt(1, false);
        
        listar();
        cargarClientes();
    }

    private void cargarClientes() {
        DefaultComboBoxModel<Object> items = this.CONTROLCLI.seleccionar();
        this.cboCliente.setModel(items);
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

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(50, 40, 100, 20);
        panelMantenimiento.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(160, 40, 100, 25);
        panelMantenimiento.add(txtId);

        JLabel lblNum = new JLabel("Número:");
        lblNum.setBounds(50, 90, 100, 20);
        panelMantenimiento.add(lblNum);

        txtNumero = new JTextField();
        txtNumero.setBounds(160, 90, 250, 25);
        panelMantenimiento.add(txtNumero);

        JLabel lblCli = new JLabel("Cliente:");
        lblCli.setBounds(50, 140, 100, 20);
        panelMantenimiento.add(lblCli);

        cboCliente = new JComboBox<>();
        cboCliente.setBounds(160, 140, 250, 25);
        panelMantenimiento.add(cboCliente);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(160, 210, 100, 30);
        panelMantenimiento.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(280, 210, 100, 30);
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
                txtNumero.setText(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 1)));
                numeroAnterior = txtNumero.getText();

                Cliente cli = new Cliente();
                cli.setRut(Integer.parseInt(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 2))));
                cli.setNombre(String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 3)));
                cboCliente.getModel().setSelectedItem(cli);

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
        if (txtId.getText().trim().isEmpty()) {
            mensajeError("El ID es obligatorio.");
            return;
        }
        if (txtNumero.getText().trim().isEmpty()) {
            mensajeError("El número es obligatorio.");
            return;
        }
        if (cboCliente.getSelectedItem() == null) {
            mensajeError("Debe seleccionar un Cliente.");
            return;
        }

        Cliente cliente = (Cliente) cboCliente.getSelectedItem();
        String resp;

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            
            if (accion.equals("editar")) {
                resp = CONTROL.actualizar(id, txtNumero.getText().trim(), numeroAnterior, cliente.getRut());
            } else {
                resp = CONTROL.insertar(id, txtNumero.getText().trim(), cliente.getRut());
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
            mensajeError("El ID debe ser un número entero.");
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtNumero.setText("");
        if (cboCliente.getItemCount() > 0) cboCliente.setSelectedIndex(0);
        accion = "guardar";
    }

    private void mensajeError(String m) { JOptionPane.showMessageDialog(this, m, "Sistema", JOptionPane.ERROR_MESSAGE); }
    private void mensajeOK(String m) { JOptionPane.showMessageDialog(this, m, "Sistema", JOptionPane.INFORMATION_MESSAGE); }
}