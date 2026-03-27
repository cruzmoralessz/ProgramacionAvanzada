package vista;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {
	private JDesktopPane desktopPane;
	private JMenuItem itemProductos, itemPuntoVenta, itemInventario, itemProveedores, itemReportes, itemSalir;

	public VentanaPrincipal() {
		setTitle("Punto de Venta - Abarrotes MVC");
		setSize(1024, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		desktopPane = new JDesktopPane();
		setContentPane(desktopPane);

		JMenuBar menuBar = new JMenuBar();

		JMenu menuArchivo = new JMenu("SISTEMA");
		itemSalir = new JMenuItem("Salir");
		menuArchivo.add(itemSalir);

		JMenu menuFacturacion = new JMenu("FACTURACION");
		itemPuntoVenta = new JMenuItem("Punto de Venta");
		itemProductos = new JMenuItem("Productos");
		itemProveedores = new JMenuItem("Proveedores");
		itemReportes = new JMenuItem("Reportes");

		menuFacturacion.add(itemPuntoVenta);
		menuFacturacion.add(itemProductos);
		menuFacturacion.add(itemProveedores);
		menuFacturacion.add(itemReportes);

		JMenu menuInventario = new JMenu("GESTION");
		itemInventario = new JMenuItem("Inventario");
		menuInventario.add(itemInventario);

		menuBar.add(menuArchivo);
		menuBar.add(menuFacturacion);
		menuBar.add(menuInventario);
		setJMenuBar(menuBar);

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
	
	public JMenuItem getItemProveedores() {
		return itemProveedores;
	}
	
	public JMenuItem getItemReportes() {
		return itemReportes;
	}
}