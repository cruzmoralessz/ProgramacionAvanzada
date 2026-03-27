package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormularioInventario extends JInternalFrame {
    // COMPONENTES IZQUIERDA
    public JTextField txtFiltroId, txtFiltroNombre;
    public JComboBox<String> cbFiltroTipo;
    public JRadioButton rbTodos, rbDisponible, rbAgotado;
    public JButton btnBuscar, btnLimpiarFiltros;
    
    // COMPONENTES DERECHA
    public JTable tablaInventario;
    public DefaultTableModel modeloInventario;
    public JButton btnCrearNuevo, btnModificar, btnEliminar;

    public FormularioInventario() {
        super("Auditoría e Inventario", true, true, true, true);
        setSize(900, 500);
        setLayout(new BorderLayout());

        // LADO IZQUIERDO
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setBorder(BorderFactory.createTitledBorder("FILTROS Y BÚSQUEDA"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        txtFiltroId = new JTextField(12);
        txtFiltroNombre = new JTextField(12);
        
        String[] categoriasFiltro = {
            "Todos", "Despensa Básica", "Lácteos y Huevo", "Bebidas y Líquidos", 
            "Botanas y Dulces", "Frutas y Verduras", "Carnes y Salchichonería", 
            "Cuidado del Hogar", "Higiene y Cuidado Personal", "Alimentos Preparados/Enlatados"
        };
        cbFiltroTipo = new JComboBox<>(categoriasFiltro);
        
        rbTodos = new JRadioButton("Todos", true);
        rbDisponible = new JRadioButton("Disponible");
        rbAgotado = new JRadioButton("Agotado");
        ButtonGroup bgEstado = new ButtonGroup();
        bgEstado.add(rbTodos); bgEstado.add(rbDisponible); bgEstado.add(rbAgotado);
        
        JPanel panelRadios = new JPanel(new GridLayout(3, 1));
        panelRadios.setBorder(BorderFactory.createTitledBorder("Estado en Almacén"));
        panelRadios.add(rbTodos); panelRadios.add(rbDisponible); panelRadios.add(rbAgotado);

        int f = 0;
        gbc.gridx = 0; gbc.gridy = f; panelFiltros.add(new JLabel("SKU:"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelFiltros.add(txtFiltroId, gbc);
        
        gbc.gridx = 0; gbc.gridy = f; panelFiltros.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelFiltros.add(txtFiltroNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = f; panelFiltros.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelFiltros.add(cbFiltroTipo, gbc);
        
        gbc.gridx = 0; gbc.gridy = f++; gbc.gridwidth = 2; panelFiltros.add(panelRadios, gbc);

        JPanel panelBotonesFiltro = new JPanel(new FlowLayout());
        btnBuscar = new JButton("Buscar");
        btnLimpiarFiltros = new JButton("Limpiar Filtros");
        panelBotonesFiltro.add(btnBuscar); panelBotonesFiltro.add(btnLimpiarFiltros);
        
        gbc.gridx = 0; gbc.gridy = f; gbc.weighty = 1.0; 
        panelFiltros.add(panelBotonesFiltro, gbc);

        // LADO DERECHO
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(BorderFactory.createTitledBorder("INVENTARIO ACTUAL"));

        modeloInventario = new DefaultTableModel(new String[]{"SKU", "Nombre", "Categoría", "Almacén", "Precio Venta", "Estado"}, 0);
        tablaInventario = new JTable(modeloInventario);
        
        JPanel panelAccionesSel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelAccionesSel.setBorder(BorderFactory.createTitledBorder("ACCIONES"));
        btnCrearNuevo = new JButton("Crear");
        btnModificar = new JButton("Modificar / Ajustar");
        btnEliminar = new JButton("Eliminar");
        panelAccionesSel.add(btnCrearNuevo); panelAccionesSel.add(btnModificar); panelAccionesSel.add(btnEliminar);

        panelDerecho.add(new JScrollPane(tablaInventario), BorderLayout.CENTER);
        panelDerecho.add(panelAccionesSel, BorderLayout.SOUTH);

        panelFiltros.setPreferredSize(new Dimension(320, 0));
        add(panelFiltros, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);
    }
}