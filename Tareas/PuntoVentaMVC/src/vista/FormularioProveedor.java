package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormularioProveedor extends JInternalFrame {
    public JTextField txtId, txtNombre, txtRtn, txtTelefono, txtCorreo, txtLimiteCredito;
    public JTextArea txtDireccion;
    public JComboBox<String> cbTerminos;
    public JRadioButton rbActivo, rbInactivo;
    public JButton btnGuardar, btnLimpiar;

    public JTable tablaProveedores;
    public DefaultTableModel modeloTabla;
    public JTextField txtBuscar;
    public JButton btnBuscar, btnEliminar;

    public FormularioProveedor() {
        super("Gestión de Proveedores", true, true, true, true);
        setSize(950, 550);
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("DATOS DEL PROVEEDOR"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        txtId = new JTextField(15);
        txtNombre = new JTextField(15);
        txtRtn = new JTextField(15);
        txtTelefono = new JTextField(15);
        txtCorreo = new JTextField(15);
        txtLimiteCredito = new JTextField(15);
        
        txtDireccion = new JTextArea(3, 15);
        txtDireccion.setLineWrap(true);
        JScrollPane scrollDir = new JScrollPane(txtDireccion);

        cbTerminos = new JComboBox<>(new String[]{"Contado", "Crédito 15 días", "Crédito 30 días", "Crédito 60 días"});

        rbActivo = new JRadioButton("Activo", true);
        rbInactivo = new JRadioButton("Inactivo");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbActivo); bg.add(rbInactivo);
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelEstado.add(rbActivo); panelEstado.add(rbInactivo);

        int f = 0;
        gbc.gridx = 0; gbc.gridy = f; panelForm.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelForm.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = f; panelForm.add(new JLabel("Razón Social:"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelForm.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = f; panelForm.add(new JLabel("RFC:"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelForm.add(txtRtn, gbc);

        gbc.gridx = 0; gbc.gridy = f; panelForm.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelForm.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = f; panelForm.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelForm.add(txtCorreo, gbc);

        gbc.gridx = 0; gbc.gridy = f; panelForm.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelForm.add(scrollDir, gbc);

        gbc.gridx = 0; gbc.gridy = f; panelForm.add(new JLabel("Términos:"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelForm.add(cbTerminos, gbc);

        gbc.gridx = 0; gbc.gridy = f; panelForm.add(new JLabel("Límite Crédito ($):"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelForm.add(txtLimiteCredito, gbc);

        gbc.gridx = 0; gbc.gridy = f; panelForm.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridy = f++; panelForm.add(panelEstado, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton("Guardar Proveedor");
        btnLimpiar = new JButton("Limpiar");
        panelBotones.add(btnGuardar); panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = f; gbc.gridwidth = 2; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.NORTH;
        panelForm.add(panelBotones, gbc);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(BorderFactory.createTitledBorder("LISTA DE PROVEEDORES"));

        modeloTabla = new DefaultTableModel(new String[]{"Código", "Nombre", "Teléfono", "Términos", "Límite", "Estado"}, 0);
        tablaProveedores = new JTable(modeloTabla);

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(15);
        btnBuscar = new JButton("Buscar");
        btnEliminar = new JButton("Eliminar Seleccionado");
        panelFiltros.add(new JLabel("Buscar por Nombre/Cod:"));
        panelFiltros.add(txtBuscar);
        panelFiltros.add(btnBuscar);
        panelFiltros.add(btnEliminar);

        panelDerecho.add(new JScrollPane(tablaProveedores), BorderLayout.CENTER);
        panelDerecho.add(panelFiltros, BorderLayout.SOUTH);

        panelForm.setPreferredSize(new Dimension(350, 0));
        add(panelForm, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);
    }
}