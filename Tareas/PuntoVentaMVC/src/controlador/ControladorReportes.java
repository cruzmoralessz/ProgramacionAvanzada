package controlador;

import modelo.GestionProductos;
import modelo.GestionProveedores;
import modelo.Producto;
import modelo.Proveedor;
import vista.FormularioReportes;
import vista.VentanaPrincipal;
import archivo.GestorExcel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import javax.swing.JOptionPane;

public class ControladorReportes {
    private VentanaPrincipal mainUI;
    private FormularioReportes vistaReportes;
    private GestionProductos modeloProductos;
    private GestionProveedores modeloProveedores;

    public ControladorReportes(VentanaPrincipal mainUI, GestionProductos modeloProductos, GestionProveedores modeloProveedores) {
        this.mainUI = mainUI;
        this.modeloProductos = modeloProductos;
        this.modeloProveedores = modeloProveedores;

        this.mainUI.getItemReportes().addActionListener(e -> abrirVistaReportes());
    }

    private void abrirVistaReportes() {
        if (vistaReportes == null || vistaReportes.isClosed()) {
            vistaReportes = new FormularioReportes();

            vistaReportes.btnExportarInventario.addActionListener(e -> exportarInventario());
            vistaReportes.btnExportarProveedores.addActionListener(e -> exportarProveedores());
            vistaReportes.btnExportarVentas.addActionListener(e -> exportarVentas());

            mainUI.getDesktopPane().add(vistaReportes);
            vistaReportes.setVisible(true);
            
            cargarReporteInventario();
            cargarReporteProveedores();
            cargarReporteVentas();
        } else {
            vistaReportes.toFront();
            cargarReporteInventario();
            cargarReporteProveedores();
            cargarReporteVentas();
        }
    }

    private void cargarReporteInventario() {
        vistaReportes.modInventario.setRowCount(0);
        double valorTotalInvertido = 0.0;

        for (Producto p : modeloProductos.listar()) {
            double valorEnAlmacen = p.getPrecioCompra() * p.getCantidadAlmacen();
            valorTotalInvertido += valorEnAlmacen;
            
            vistaReportes.modInventario.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getCategoria(), p.getCantidadAlmacen(), 
                String.format("$%.2f", p.getPrecioCompra()), 
                String.format("$%.2f", valorEnAlmacen)
            });
        }
        vistaReportes.lblValorTotalInventario.setText(String.format("Valor Total Invertido: $%.2f", valorTotalInvertido));
    }

    private void cargarReporteProveedores() {
        vistaReportes.modProveedores.setRowCount(0);
        for (Proveedor p : modeloProveedores.listar()) {
            vistaReportes.modProveedores.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getTerminosPago(), 
                String.format("$%.2f", p.getLimiteCredito()), 
                p.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    private void cargarReporteVentas() {
        vistaReportes.modVentas.setRowCount(0);
        double ingresosTotales = 0.0;
        
        File fControl = new File("control_tickets.json");
        if (!fControl.exists()) return;

        try (Reader r = new FileReader(fControl)) {
            JsonArray listaFolios = JsonParser.parseReader(r).getAsJsonArray();
            
            for (int i = 0; i < listaFolios.size(); i++) {
                String nombreFolio = listaFolios.get(i).getAsString();
                File fTicket = new File(nombreFolio + ".json");
                
                if (fTicket.exists()) {
                    try (Reader rTicket = new FileReader(fTicket)) {
                        JsonObject ticketJson = JsonParser.parseReader(rTicket).getAsJsonObject();
                        
                        String folio = ticketJson.get("folio").getAsString();
                        String fecha = ticketJson.get("fecha").getAsString();
                        double subtotal = ticketJson.get("subtotal").getAsDouble();
                        double iva = ticketJson.get("iva").getAsDouble();
                        double total = ticketJson.get("total").getAsDouble();
                        
                        ingresosTotales += total;
                        
                        vistaReportes.modVentas.addRow(new Object[]{
                            folio, fecha, String.format("$%.2f", subtotal), 
                            String.format("$%.2f", iva), String.format("$%.2f", total)
                        });
                    }
                }
            }
            vistaReportes.lblTotalIngresos.setText(String.format("Ingresos Totales Registrados: $%.2f", ingresosTotales));
            
        } catch (Exception e) {
            System.out.println("ERROR al cargar estadisticas de ventas");
        }
    }

    private void exportarInventario() {
        if (modeloProductos.listar().isEmpty()) {
            JOptionPane.showMessageDialog(vistaReportes, "No hay productos para exportar");
            return;
        }
        GestorExcel generadorExcel = new GestorExcel();
        generadorExcel.generarReporte(modeloProductos.listar());
        JOptionPane.showMessageDialog(vistaReportes, "Reporte de Inventario exportado");
    }

    private void exportarProveedores() {
        if (modeloProveedores.listar().isEmpty()) {
            JOptionPane.showMessageDialog(vistaReportes, "No hay proveedores para exportar");
            return;
        }
        GestorExcel generadorExcel = new GestorExcel();
        generadorExcel.generarReporteProveedores(modeloProveedores.listar());
        JOptionPane.showMessageDialog(vistaReportes, "Directorio exportado");
    }

    private void exportarVentas() {
        if (vistaReportes.modVentas.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vistaReportes, "No hay ventas registradas para exportar");
            return;
        }
        GestorExcel generadorExcel = new GestorExcel();
        generadorExcel.generarReporteVentas(vistaReportes.modVentas, vistaReportes.lblTotalIngresos.getText());
        JOptionPane.showMessageDialog(vistaReportes, "Historial exportado");
    }
}