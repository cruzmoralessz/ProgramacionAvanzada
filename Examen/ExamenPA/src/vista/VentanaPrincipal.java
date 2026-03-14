package vista;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {
	private JDesktopPane desktopPane;
	private JMenuItem itemProductos, itemPuntoVenta, itemInventario, itemSalir;

	public VentanaPrincipal() {
		setTitle("Sistema MDI - Abarrotes");
		setSize(1024, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		desktopPane = new JDesktopPane();
		setContentPane(desktopPane);

		JMenuBar menuBar = new JMenuBar();

		// ARCHIVO
		JMenu menuArchivo = new JMenu("SISTEMA");
		itemSalir = new JMenuItem("Salir");
		menuArchivo.add(itemSalir);

		// MODULOS
		JMenu menuModulos = new JMenu("MODULOS");
		itemProductos = new JMenuItem("Productos");
		itemPuntoVenta = new JMenuItem("Punto de Venta");
		itemInventario = new JMenuItem("Inventario");

		menuModulos.add(itemProductos);
		menuModulos.add(itemPuntoVenta);
		menuModulos.add(itemInventario);

		menuBar.add(menuArchivo);
		menuBar.add(menuModulos);
		setJMenuBar(menuBar);

		// SALIR
		itemSalir.addActionListener(e -> System.exit(0));
	}

	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}

	public JMenuItem getItemProductos() {
		return itemProductos;
	}

	public JMenuItem getItemPuntoVenta() {
		return itemPuntoVenta;
	}

	public JMenuItem getItemInventario() {
		return itemInventario;
	}
}