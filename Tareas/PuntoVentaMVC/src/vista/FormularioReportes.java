package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormularioReportes extends JInternalFrame {
    public JTabbedPane pestanas;

    public JTable tablaInventario;
    public DefaultTableModel modInventario;
    public JLabel lblValorTotalInventario;
    public JButton btnExportarInventario;

    public JTable tablaProveedores;
    public DefaultTableModel modProveedores;
    public JButton btnExportarProveedores;

    public JTable tablaVentas;
    public DefaultTableModel modVentas;
    public JLabel lblTotalIngresos;
    public JButton btnExportarVentas;

    public FormularioReportes() {
        super("Reportes y Estadísticas", true, true, true, true);
        setSize(900, 550);
        setLayout(new BorderLayout());

        pestanas = new JTabbedPane();

        // INVENTARIO
        JPanel pnlInventario = new JPanel(new BorderLayout());
        pnlInventario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        modInventario = new DefaultTableModel(new String[]{"SKU", "Nombre", "Categoría", "Stock Actual", "Costo Unitario", "Valor Total en Almacén"}, 0);
        tablaInventario = new JTable(modInventario);
        
        JPanel pnlSurInv = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        lblValorTotalInventario = new JLabel("Valor Total Invertido: $0.00");
        lblValorTotalInventario.setFont(new Font("Arial", Font.BOLD, 14));
        btnExportarInventario = new JButton("Exportar a Excel");
        pnlSurInv.add(lblValorTotalInventario);
        pnlSurInv.add(btnExportarInventario);
        
        pnlInventario.add(new JScrollPane(tablaInventario), BorderLayout.CENTER);
        pnlInventario.add(pnlSurInv, BorderLayout.SOUTH);

        // PROVEEDORES
        JPanel pnlProveedores = new JPanel(new BorderLayout());
        pnlProveedores.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        modProveedores = new DefaultTableModel(new String[]{"Código", "Razón Social", "Términos de Pago", "Límite de Crédito", "Estado"}, 0);
        tablaProveedores = new JTable(modProveedores);
        
        JPanel pnlSurProv = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btnExportarProveedores = new JButton("Exportar a Excel");
        pnlSurProv.add(btnExportarProveedores);
        
        pnlProveedores.add(new JScrollPane(tablaProveedores), BorderLayout.CENTER);
        pnlProveedores.add(pnlSurProv, BorderLayout.SOUTH);

        // PESTAÑA VENTAS
        JPanel pnlVentas = new JPanel(new BorderLayout());
        pnlVentas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        modVentas = new DefaultTableModel(new String[]{"Folio Ticket", "Fecha y Hora", "Subtotal", "IVA", "Total Cobrado"}, 0);
        tablaVentas = new JTable(modVentas);
        
        JPanel pnlSurVentas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        lblTotalIngresos = new JLabel("Ingresos Totales Registrados: $0.00");
        lblTotalIngresos.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnExportarVentas = new JButton("Exportar a Excel");
        
        pnlSurVentas.add(lblTotalIngresos);
        pnlSurVentas.add(btnExportarVentas);
        
        pnlVentas.add(new JScrollPane(tablaVentas), BorderLayout.CENTER);
        pnlVentas.add(pnlSurVentas, BorderLayout.SOUTH);

        pestanas.addTab("Valor de Inventario", pnlInventario);
        pestanas.addTab("Directorio de Proveedores", pnlProveedores);
        pestanas.addTab("Historial de Ventas", pnlVentas);

        add(pestanas, BorderLayout.CENTER);
    }
}