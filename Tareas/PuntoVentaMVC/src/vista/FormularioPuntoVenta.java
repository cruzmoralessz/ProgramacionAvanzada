package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormularioPuntoVenta extends JInternalFrame {
    public JComboBox<String> cbProductos;
    public JTextField txtCantidad;
    public JButton btnAñadir, btnEliminar;
    public JTable tablaTicket;
    public DefaultTableModel modeloTicket;
    public JTextField txtSubtotal, txtIva, txtTotal;
    public JButton btnLimpiarCarrito, btnProcesarPago;
    public JLabel lblInfoTicket;
    public JLabel lblImagenProducto;

    public FormularioPuntoVenta() {
        super("Nueva Venta Estándar", true, true, true, true);
        setSize(900, 600);
        setLayout(new BorderLayout());

        // LADO UP
        JPanel panelNorte = new JPanel(new BorderLayout());
        
        String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        lblInfoTicket = new JLabel("  Fecha: " + fechaActual + "   |   Cajero Activo   |   Ticket #001");
        lblInfoTicket.setFont(new Font("Arial", Font.BOLD, 12));
        panelNorte.add(lblInfoTicket, BorderLayout.NORTH);

        JPanel panelSeleccion = new JPanel(new GridBagLayout());
        panelSeleccion.setBorder(BorderFactory.createTitledBorder("--- BÚSQUEDA Y SELECCIÓN DE PRODUCTO ---"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        cbProductos = new JComboBox<>();
        txtCantidad = new JTextField(5);
        btnAñadir = new JButton("Añadir a Carrito");
        btnEliminar = new JButton("Eliminar Seleccionado");

        gbc.gridx = 0; gbc.gridy = 0; panelSeleccion.add(new JLabel("Buscar Producto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; panelSeleccion.add(cbProductos, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0; panelSeleccion.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; panelSeleccion.add(txtCantidad, gbc);
        
        JPanel panelBotonesProd = new JPanel(new FlowLayout());
        panelBotonesProd.add(btnAñadir); 
        panelBotonesProd.add(btnEliminar);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4; panelSeleccion.add(panelBotonesProd, gbc);

        lblImagenProducto = new JLabel("Sin Foto", SwingConstants.CENTER);
        lblImagenProducto.setPreferredSize(new Dimension(150, 150));
        lblImagenProducto.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel panelCentroNorte = new JPanel(new BorderLayout());
        panelCentroNorte.add(panelSeleccion, BorderLayout.CENTER);
        panelCentroNorte.add(lblImagenProducto, BorderLayout.EAST);

        panelNorte.add(panelCentroNorte, BorderLayout.CENTER);

        // LADO MIDDLE
        modeloTicket = new DefaultTableModel(new String[]{"SKU", "Descripción", "Cantidad", "Precio Unitario", "Subtotal"}, 0);
        tablaTicket = new JTable(modeloTicket);
        JScrollPane scrollTicket = new JScrollPane(tablaTicket);
        scrollTicket.setBorder(BorderFactory.createTitledBorder("Detalles Transacción Actual"));

        // LADO DOWN
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelTotales = new JPanel(new GridLayout(3, 2, 5, 5));
        txtSubtotal = new JTextField("0.00"); txtSubtotal.setEditable(false); txtSubtotal.setHorizontalAlignment(JTextField.RIGHT);
        txtIva = new JTextField("0.00"); txtIva.setEditable(false); txtIva.setHorizontalAlignment(JTextField.RIGHT);
        txtTotal = new JTextField("0.00"); txtTotal.setEditable(false); txtTotal.setHorizontalAlignment(JTextField.RIGHT);
        
        panelTotales.add(new JLabel("Subtotal:")); panelTotales.add(txtSubtotal);
        panelTotales.add(new JLabel("Impuesto (16%):")); panelTotales.add(txtIva);
        panelTotales.add(new JLabel("Total a Pagar:")); panelTotales.add(txtTotal);

        JPanel panelBotonesPago = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        btnLimpiarCarrito = new JButton("Cancelar Venta");
        btnProcesarPago = new JButton("COBRAR TICKET");
        btnProcesarPago.setBackground(new Color(0, 153, 51));
        btnProcesarPago.setForeground(Color.WHITE);
        btnProcesarPago.setFont(new Font("Arial", Font.BOLD, 14));
        
        panelBotonesPago.add(btnLimpiarCarrito);
        panelBotonesPago.add(btnProcesarPago);

        panelSur.add(panelTotales, BorderLayout.WEST);
        panelSur.add(panelBotonesPago, BorderLayout.EAST);

        add(panelNorte, BorderLayout.NORTH);
        add(scrollTicket, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);
    }
}