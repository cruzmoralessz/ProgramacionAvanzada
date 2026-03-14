package vista;

import java.awt.BorderLayout;
import java.awt.Font;
import java.time.LocalDate;

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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controlador.VentaCONTROL;
import entidades.Cliente;
import entidades.Producto;

public class FrmVenta extends JFrame {

    private final VentaCONTROL CONTROL;
    private String accion;
    private DefaultTableModel modeloDetalles;

    private JTabbedPane tabGeneral;
    private JPanel panelListado, panelMantenimiento;
    private JTable tabla, tablaDetalle;
    private JTextField txtId, txtDescuento, txtTotal, txtFecha;
    private JComboBox<Object> cboCliente, cboProducto;
    private JButton btnNuevo, btnAnularVenta, btnVerDetalles, btnGuardar, btnCancelar;
    private JButton btnAgregarProducto, btnQuitarProducto;

    public FrmVenta() {
        setTitle("Punto de Venta");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));

        this.CONTROL = new VentaCONTROL();
        this.accion = "guardar";

        initComponents();

        tabGeneral.setEnabledAt(1, false);
        listar();
        cargarClientes();
        cargarProductos();

        txtFecha.setText(fechaSistema());
        txtDescuento.setText("0.00");
        txtTotal.setText("0.00");
    }

    private void listar() {
        tabla.setModel(CONTROL.listar());
        TableRowSorter<javax.swing.table.TableModel> orden = new TableRowSorter<>(tabla.getModel());
        tabla.setRowSorter(orden);
    }

    private void cargarClientes() {
        DefaultComboBoxModel<Object> items = CONTROL.seleccionarCliente();
        cboCliente.setModel(items);
    }

    private void cargarProductos() {
        DefaultComboBoxModel<Object> items = CONTROL.seleccionarProducto();
        cboProducto.setModel(items);
    }

    private void limpiar() {
        txtId.setText("");
        txtDescuento.setText("0.00");
        txtTotal.setText("0.00");
        txtFecha.setText(fechaSistema());
        crearDetalles();
        accion = "guardar";
        if(cboCliente.getItemCount() > 0) cboCliente.setSelectedIndex(0);
        if(cboProducto.getItemCount() > 0) cboProducto.setSelectedIndex(0);
    }

    private String fechaSistema() {
        return LocalDate.now().toString();
    }

    private void mensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema", JOptionPane.ERROR_MESSAGE);
    }

    private void mensajeOK(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema", JOptionPane.INFORMATION_MESSAGE);
    }

    private void crearDetalles() {
        modeloDetalles = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == 3 || columna == 4;
            }

            @Override
            public Object getValueAt(int row, int col) {
                if (col == 5) {
                    Double cantD;
                    try {
                        cantD = Double.parseDouble(String.valueOf(super.getValueAt(row, 3)));
                    } catch (NumberFormatException e) {
                        cantD = 1.0;
                    }
                    Double precioD = Double.parseDouble(String.valueOf(super.getValueAt(row, 4)));
                    if (cantD != null && precioD != null) {
                        return String.format("%.2f", cantD * precioD).replace(",", ".");
                    } else {
                        return 0;
                    }
                }
                return super.getValueAt(row, col);
            }

            @Override
            public void setValueAt(Object aValue, int row, int col) {
                super.setValueAt(aValue, row, col);
                try {
                    int cantD = Integer.parseInt(String.valueOf(getValueAt(row, 3)));
                    int stockD = Integer.parseInt(String.valueOf(getValueAt(row, 2)));
                    if (cantD > stockD) {
                        super.setValueAt(stockD, row, 3);
                        mensajeError("La Cantidad a vender no puede superar el stock. Usted puede vender como máximo: " + stockD);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
                calcularTotales();
                fireTableDataChanged();
            }
        };
        modeloDetalles.setColumnIdentifiers(new Object[]{"Id Producto", "Producto", "Stock", "Cantidad", "Precio", "Sub Total"});
    }

    private void calcularTotales() {
        double total = 0.00;
        int items = modeloDetalles.getRowCount();
        if (items > 0) {
            for (int i = 0; i < items; i++) {
                total += Double.parseDouble(String.valueOf(modeloDetalles.getValueAt(i, 5)));
            }
        }
        try {
            double desc = Double.parseDouble(txtDescuento.getText());
            total = total - desc;
        } catch(Exception e) {}

        txtTotal.setText(String.format("%.2f", Math.max(0, total)).replace(",", "."));
    }

    public void agregarDetalles(String id, String nombre, String stock, String precio) {
        boolean existe = false;
        for (int i = 0; i < modeloDetalles.getRowCount(); i++) {
            String idT = String.valueOf(modeloDetalles.getValueAt(i, 0));
            if (idT.equals(id)) {
                existe = true;
                break;
            }
        }
        if (existe) {
            mensajeError("¡El producto ya se ha agregado al carrito!");
        } else {
            modeloDetalles.addRow(new Object[]{id, nombre, stock, "1", precio, precio});
            calcularTotales();
        }
    }

    private void initComponents() {
        tabGeneral = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabGeneral, BorderLayout.CENTER);

        panelListado = new JPanel();
        panelListado.setLayout(null);
        tabGeneral.addTab("Listado", null, panelListado, null);

        btnNuevo = new JButton("Nueva Venta");
        btnNuevo.setBounds(330, 20, 110, 30);
        panelListado.add(btnNuevo);

        btnAnularVenta = new JButton("Anular Venta");
        btnAnularVenta.setBounds(460, 20, 110, 30);
        panelListado.add(btnAnularVenta);

        btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.setBounds(590, 20, 110, 30);
        panelListado.add(btnVerDetalles);

        JScrollPane scrollListado = new JScrollPane();
        scrollListado.setBounds(20, 70, 740, 480);
        panelListado.add(scrollListado);

        tabla = new JTable();
        scrollListado.setViewportView(tabla);

        panelMantenimiento = new JPanel();
        panelMantenimiento.setLayout(null);
        tabGeneral.addTab("Mantenimiento", null, panelMantenimiento, null);

        JLabel lblId = new JLabel("ID Venta:");
        lblId.setBounds(30, 20, 80, 20);
        panelMantenimiento.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(100, 20, 100, 25);
        panelMantenimiento.add(txtId);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(230, 20, 50, 20);
        panelMantenimiento.add(lblFecha);

        txtFecha = new JTextField();
        txtFecha.setBounds(280, 20, 100, 25);
        txtFecha.setEditable(false);
        panelMantenimiento.add(txtFecha);

        JLabel lblDesc = new JLabel("Descuento:");
        lblDesc.setBounds(400, 20, 80, 20);
        panelMantenimiento.add(lblDesc);

        txtDescuento = new JTextField();
        txtDescuento.setBounds(480, 20, 80, 25);
        panelMantenimiento.add(txtDescuento);

        txtDescuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calcularTotales();
            }
        });

        JLabel lblCli = new JLabel("Cliente:");
        lblCli.setBounds(30, 60, 60, 20);
        panelMantenimiento.add(lblCli);

        cboCliente = new JComboBox<>();
        cboCliente.setBounds(100, 60, 300, 25);
        panelMantenimiento.add(cboCliente);

        JLabel lblProd = new JLabel("Producto:");
        lblProd.setBounds(30, 100, 70, 20);
        panelMantenimiento.add(lblProd);

        cboProducto = new JComboBox<>();
        cboProducto.setBounds(100, 100, 300, 25);
        panelMantenimiento.add(cboProducto);

        btnAgregarProducto = new JButton("Agregar al Carrito");
        btnAgregarProducto.setBounds(420, 100, 150, 25);
        panelMantenimiento.add(btnAgregarProducto);

        JScrollPane scrollDetalle = new JScrollPane();
        scrollDetalle.setBounds(30, 150, 720, 250);
        panelMantenimiento.add(scrollDetalle);

        crearDetalles(); 
        tablaDetalle = new JTable(modeloDetalles);
        scrollDetalle.setViewportView(tablaDetalle);

        btnQuitarProducto = new JButton("Quitar Seleccionado");
        btnQuitarProducto.setBounds(30, 420, 160, 30);
        panelMantenimiento.add(btnQuitarProducto);

        JLabel lblTot = new JLabel("TOTAL: $");
        lblTot.setBounds(580, 420, 60, 30);
        lblTot.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelMantenimiento.add(lblTot);

        txtTotal = new JTextField();
        txtTotal.setBounds(650, 420, 100, 30);
        txtTotal.setFont(new Font("Tahoma", Font.BOLD, 14));
        txtTotal.setEditable(false);
        panelMantenimiento.add(txtTotal);

        btnGuardar = new JButton("Guardar Venta");
        btnGuardar.setBounds(250, 490, 130, 35);
        panelMantenimiento.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(400, 490, 120, 35);
        panelMantenimiento.add(btnCancelar);

        btnNuevo.addActionListener(e -> {
            limpiar();
            tabGeneral.setEnabledAt(0, false);
            tabGeneral.setEnabledAt(1, true);
            tabGeneral.setSelectedIndex(1);
            accion = "guardar";
            btnGuardar.setVisible(true);
            txtId.setEditable(true);
        });

        btnAgregarProducto.addActionListener(e -> {
            if (cboProducto.getSelectedItem() != null) {
                Producto p = (Producto) cboProducto.getSelectedItem();
                agregarDetalles(String.valueOf(p.getId()), p.getNombre(), String.valueOf(p.getStock()), String.valueOf(p.getPrecio()));
            } else {
                mensajeError("Debe seleccionar un producto.");
            }
        });

        btnQuitarProducto.addActionListener(e -> {
            if (tablaDetalle.getSelectedRowCount() == 1) {
                modeloDetalles.removeRow(tablaDetalle.getSelectedRow());
                calcularTotales();
            } else {
                mensajeError("Seleccione un registro del carrito para Quitar.");
            }
        });

        btnGuardar.addActionListener(e -> {
            if (txtId.getText().trim().isEmpty() || txtId.getText().trim().length() > 11) {
                mensajeError("Debes ingresar un ID válido.");
                txtId.requestFocus();
                return;
            }
            if (txtDescuento.getText().trim().isEmpty()) {
                mensajeError("Debes ingresar un descuento.");
                txtDescuento.requestFocus();
                return;
            }
            if (cboCliente.getSelectedItem() == null) {
                mensajeError("Por favor selecciona un Cliente.");
                return;
            }
            if (modeloDetalles.getRowCount() == 0) {
                mensajeError("Por favor agrega productos al carrito.");
                return;
            }

            Cliente cliente = (Cliente) cboCliente.getSelectedItem();
            String resp = CONTROL.insertar(Integer.parseInt(txtId.getText().trim()), txtFecha.getText(), 
                                           Double.parseDouble(txtDescuento.getText()), cliente.getRut(), 
                                           Double.parseDouble(txtTotal.getText()), modeloDetalles);
                                           
            if (resp.equals("OK")) {
                mensajeOK("Venta registrada correctamente");
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
            btnGuardar.setVisible(true);
        });

        btnAnularVenta.addActionListener(e -> {
            if (tabla.getSelectedRowCount() == 1) {
                String id = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 0));
                String fecha = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 1));
                String descuento = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 2));
                String idCliente = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 3));
                String total = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 5));

                int resultado = JOptionPane.showConfirmDialog(this, "¿Estás seguro de anular esta venta?", "Sistema", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (resultado == 0) {
                    String msg = CONTROL.anular(Integer.parseInt(id), fecha, Double.parseDouble(descuento), Integer.parseInt(idCliente), Double.parseDouble(total));
                    if (msg.equals("OK")) {
                        mensajeOK("Se anuló la venta correctamente");
                        listar();
                    } else {
                        mensajeError(msg);
                    }
                }
            } else {
                mensajeError("Seleccione una venta de la tabla para Anular.");
            }
        });

        btnVerDetalles.addActionListener(e -> {
            if (tabla.getSelectedRowCount() == 1) {
                String id = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 0));
                String fecha = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 1));
                String descuento = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 2));
                String idCliente = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 3));
                String nombreCliente = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 4));
                String total = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 5));

                txtId.setText(id);
                txtDescuento.setText(descuento);
                txtFecha.setText(fecha);
                cboCliente.getModel().setSelectedItem(new Cliente(Integer.parseInt(idCliente), nombreCliente));
                txtTotal.setText(total);

                modeloDetalles = CONTROL.listarDetalle(Integer.parseInt(id));
                tablaDetalle.setModel(modeloDetalles);
                calcularTotales();

                tabGeneral.setEnabledAt(1, true);
                tabGeneral.setEnabledAt(0, false);
                tabGeneral.setSelectedIndex(1);
                btnGuardar.setVisible(false); 
                txtId.setEditable(false);
            } else {
                mensajeError("Seleccione una venta para Ver Detalles.");
            }
        });
    }
}