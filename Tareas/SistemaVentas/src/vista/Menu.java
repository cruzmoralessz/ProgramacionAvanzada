package vista;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    public Menu() {
        setTitle("Sistema de Ventas MVC - Menú Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuMantenimiento = new JMenu("Mantenimiento");
        menuBar.add(menuMantenimiento);

        JMenuItem itemCliente = new JMenuItem("Gestión de Clientes");
        itemCliente.addActionListener(e -> new FrmCliente().setVisible(true));
        menuMantenimiento.add(itemCliente);

        JMenuItem itemCategoria = new JMenuItem("Gestión de Categorías");
        itemCategoria.addActionListener(e -> new FrmCategoria().setVisible(true));
        menuMantenimiento.add(itemCategoria);

        JMenuItem itemProveedor = new JMenuItem("Gestión de Proveedores");
        itemProveedor.addActionListener(e -> new FrmProveedor().setVisible(true));
        menuMantenimiento.add(itemProveedor);

        JMenuItem itemProducto = new JMenuItem("Gestión de Productos");
        itemProducto.addActionListener(e -> new FrmProducto().setVisible(true));
        menuMantenimiento.add(itemProducto);

        JMenu menuDatosExtra = new JMenu("Datos Extra");
        menuBar.add(menuDatosExtra);

        JMenuItem itemTelefono = new JMenuItem("Teléfonos de Clientes");
        itemTelefono.addActionListener(e -> new FrmTelefono().setVisible(true));
        menuDatosExtra.add(itemTelefono);

        JMenuItem itemDirCliente = new JMenuItem("Direcciones de Clientes");
        itemDirCliente.addActionListener(e -> new FrmDireccionCliente().setVisible(true));
        menuDatosExtra.add(itemDirCliente);

        JMenuItem itemDirProveedor = new JMenuItem("Direcciones de Proveedores");
        itemDirProveedor.addActionListener(e -> new FrmDireccionProveedor().setVisible(true));
        menuDatosExtra.add(itemDirProveedor);

        JMenu menuOperaciones = new JMenu("Operaciones");
        menuBar.add(menuOperaciones);

        JMenuItem itemVenta = new JMenuItem("Punto de Venta");
        itemVenta.addActionListener(e -> new FrmVenta().setVisible(true));
        menuOperaciones.add(itemVenta);

        JPanel panelFondo = new JPanel();
        panelFondo.setLayout(new BorderLayout());
        panelFondo.setBackground(new Color(45, 52, 54));

        JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Ventas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitulo.setForeground(Color.WHITE);
        panelFondo.add(lblTitulo, BorderLayout.CENTER);

        getContentPane().add(panelFondo, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new Menu().setVisible(true);
        });
    }
}