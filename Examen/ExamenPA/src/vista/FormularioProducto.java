package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormularioProducto extends JInternalFrame {
    // COMPONENTES LADO IZQUIERDO
    public JTextField txtId, txtNombre, txtPrecioCompra, txtPrecioVenta, txtStock;
    public JTextArea txtDescripcion;
    public JComboBox<String> cbCategoria;
    public JRadioButton rbActivo, rbInactivo;
    public JButton btnGuardar, btnLimpiar;
    public JLabel lblImagenProducto;

    // DEL LADO DERECHO
    public JTable tablaProductos;
    public DefaultTableModel modeloTabla;
    public JComboBox<String> cbFiltroCategoria;
    public JButton btnBuscarFiltro, btnMostrarTodos, btnExportarLista;

    public FormularioProducto() {
        super("Gestión de Productos", true, true, true, true);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        // LADO LEFT
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("ALTA Y EDICION"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // INICIALIZAR COMPONENTES
        lblImagenProducto = new JLabel("Foto", SwingConstants.CENTER);
        lblImagenProducto.setPreferredSize(new Dimension(100, 100));
        lblImagenProducto.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        txtId = new JTextField(15);
        txtNombre = new JTextField(15);
        
        txtDescripcion = new JTextArea(3, 15);
        txtDescripcion.setLineWrap(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);

        String[] categorias = {"Abarrotes", "Bebidas", "Lácteos y Huevo", "Frutas y Verduras", "Carnes y Pescados", "Salchichonería", "Panadería y Tortillería", "Limpieza del Hogar", "Cuidado Personal", "Snacks y Dulcería", "Mascotas"};
        cbCategoria = new JComboBox<>(categorias);

        txtPrecioCompra = new JTextField(15);
        txtPrecioVenta = new JTextField(15);
        txtStock = new JTextField(15);

        rbActivo = new JRadioButton("Activo", true);
        rbInactivo = new JRadioButton("Desactivado");
        ButtonGroup bgEstado = new ButtonGroup();
        bgEstado.add(rbActivo); bgEstado.add(rbInactivo);
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelEstado.setBorder(BorderFactory.createTitledBorder("Estado Actual"));
        panelEstado.add(rbActivo); panelEstado.add(rbInactivo);

        // grid
        int fila = 0;

        // centarr la foto
        gbc.gridx = 0; gbc.gridy = fila++; gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(lblImagenProducto, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gbc.gridx = 0; gbc.gridy = fila; panelFormulario.add(new JLabel("ID Producto:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila++; panelFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = fila; panelFormulario.add(new JLabel("Nombre del Producto:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila++; panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = fila; panelFormulario.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila++; panelFormulario.add(scrollDesc, gbc);

        gbc.gridx = 0; gbc.gridy = fila; panelFormulario.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila++; panelFormulario.add(cbCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = fila; panelFormulario.add(new JLabel("Precio Compra:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila++; panelFormulario.add(txtPrecioCompra, gbc);

        gbc.gridx = 0; gbc.gridy = fila; panelFormulario.add(new JLabel("Precio Venta:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila++; panelFormulario.add(txtPrecioVenta, gbc);

        gbc.gridx = 0; gbc.gridy = fila; panelFormulario.add(new JLabel("Stock Inicial:"), gbc);
        gbc.gridx = 1; gbc.gridy = fila++; panelFormulario.add(txtStock, gbc);

        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2; 
        panelFormulario.add(panelEstado, gbc);
        fila++;

        JPanel panelBotonesIzq = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton("Guardar Cambios");
        btnLimpiar = new JButton("Limpiar Formulario");
        panelBotonesIzq.add(btnGuardar); panelBotonesIzq.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = fila; gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.NORTH;
        panelFormulario.add(panelBotonesIzq, gbc);

        // LADO RIGHT
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(BorderFactory.createTitledBorder("CATALOGO PRODUCTOS"));

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Código", "Nombre", "Categoría", "Stock", "P.Venta", "Estado"}, 0);
        tablaProductos = new JTable(modeloTabla);
        
        JPanel panelFiltrosDerecha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltrosDerecha.setBorder(BorderFactory.createTitledBorder("ACCIONES Y FILTROS"));
        cbFiltroCategoria = new JComboBox<>(categorias);
        btnBuscarFiltro = new JButton("Buscar");
        btnMostrarTodos = new JButton("Mostrar Todos");
        btnExportarLista = new JButton("Exportar Lista");
        
        panelFiltrosDerecha.add(new JLabel("Buscar por Categoría:"));
        panelFiltrosDerecha.add(cbFiltroCategoria);
        panelFiltrosDerecha.add(btnBuscarFiltro);
        panelFiltrosDerecha.add(btnMostrarTodos);
        panelFiltrosDerecha.add(btnExportarLista);

        panelDerecho.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        panelDerecho.add(panelFiltrosDerecha, BorderLayout.SOUTH);

        panelFormulario.setPreferredSize(new Dimension(350, 0));
        add(panelFormulario, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);
    }
}