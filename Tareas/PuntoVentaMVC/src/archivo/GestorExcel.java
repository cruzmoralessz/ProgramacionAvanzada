package archivo;

import modelo.Producto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

public class GestorExcel {
    
    public void generarReporte(ArrayList<Producto> inventario) {
        try (Workbook workbook = new XSSFWorkbook()) {
            
            Sheet hojaCompleta = workbook.createSheet("Listado Completo");
            crearEncabezados(hojaCompleta);
            llenarDatos(hojaCompleta, inventario);

            Sheet hojaCategoria = workbook.createSheet("Por Categoría");
            crearEncabezados(hojaCategoria);
            
            ArrayList<Producto> listaOrdenada = new ArrayList<>(inventario);
            listaOrdenada.sort(Comparator.comparing(Producto::getCategoria));
            llenarDatos(hojaCategoria, listaOrdenada);

            for (int i = 0; i < 9; i++) {
                hojaCompleta.autoSizeColumn(i);
                hojaCategoria.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream("Reportes_Inventario.xlsx")) {
                workbook.write(fileOut);
            }

        } catch (Exception e) {
            System.err.println("Error al generar Excel: " + e.getMessage());
        }
    }

    private void crearEncabezados(Sheet hoja) {
        Row filaEncabezado = hoja.createRow(0);
        String[] encabezados = {"SKU", "Nombre", "Descripción", "Categoría", "Costo", "Ganancia %", "Precio Final", "Almacén", "Unidad"};
        
        for (int i = 0; i < encabezados.length; i++) {
            Cell celda = filaEncabezado.createCell(i);
            celda.setCellValue(encabezados[i]);
        }
    }

    private void llenarDatos(Sheet hoja, ArrayList<Producto> lista) {
        int numFila = 1; 
        for (Producto p : lista) {
            Row fila = hoja.createRow(numFila++);
            fila.createCell(0).setCellValue(p.getId());
            fila.createCell(1).setCellValue(p.getNombre());
            fila.createCell(2).setCellValue(p.getDescripcion());
            fila.createCell(3).setCellValue(p.getCategoria());
            fila.createCell(4).setCellValue(p.getPrecioCompra());
            fila.createCell(5).setCellValue(p.getPorcentajeGanancia());
            fila.createCell(6).setCellValue(p.getPrecioVenta());
            fila.createCell(7).setCellValue(p.getCantidadAlmacen());
            fila.createCell(8).setCellValue(p.getUnidadMedida());
        }
    }
    
    public void generarReporteProveedores(ArrayList<modelo.Proveedor> proveedores) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet hoja = workbook.createSheet("Directorio Proveedores");
            
            Row filaEncabezado = hoja.createRow(0);
            String[] encabezados = {"Código", "Razón Social", "RTN", "Teléfono", "Correo", "Dirección", "Términos", "Límite Crédito", "Estado"};
            for (int i = 0; i < encabezados.length; i++) {
                filaEncabezado.createCell(i).setCellValue(encabezados[i]);
            }

            int numFila = 1;
            for (modelo.Proveedor p : proveedores) {
                Row fila = hoja.createRow(numFila++);
                fila.createCell(0).setCellValue(p.getId());
                fila.createCell(1).setCellValue(p.getNombre());
                fila.createCell(2).setCellValue(p.getRtn());
                fila.createCell(3).setCellValue(p.getTelefono());
                fila.createCell(4).setCellValue(p.getCorreo());
                fila.createCell(5).setCellValue(p.getDireccion());
                fila.createCell(6).setCellValue(p.getTerminosPago());
                fila.createCell(7).setCellValue(p.getLimiteCredito());
                fila.createCell(8).setCellValue(p.isActivo() ? "Activo" : "Inactivo");
            }

            for (int i = 0; i < encabezados.length; i++) hoja.autoSizeColumn(i);

            try (FileOutputStream fileOut = new FileOutputStream("Reporte_Proveedores.xlsx")) {
                workbook.write(fileOut);
            }
        } catch (Exception e) {
            System.err.println("Error al generar Excel de Proveedores: " + e.getMessage());
        }
    }

    public void generarReporteVentas(javax.swing.table.DefaultTableModel modeloVentas, String textoTotal) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet hoja = workbook.createSheet("Historial de Ventas");
            
            Row filaEncabezado = hoja.createRow(0);
            for (int i = 0; i < modeloVentas.getColumnCount(); i++) {
                filaEncabezado.createCell(i).setCellValue(modeloVentas.getColumnName(i));
            }

            for (int f = 0; f < modeloVentas.getRowCount(); f++) {
                Row fila = hoja.createRow(f + 1);
                for (int c = 0; c < modeloVentas.getColumnCount(); c++) {
                    fila.createCell(c).setCellValue(modeloVentas.getValueAt(f, c).toString());
                }
            }
            
            Row filaTotal = hoja.createRow(modeloVentas.getRowCount() + 2);
            filaTotal.createCell(3).setCellValue("TOTAL GLOBAL:");
            filaTotal.createCell(4).setCellValue(textoTotal);

            for (int i = 0; i < modeloVentas.getColumnCount(); i++) hoja.autoSizeColumn(i);

            try (FileOutputStream fileOut = new FileOutputStream("Reporte_Ventas.xlsx")) {
                workbook.write(fileOut);
            }
        } catch (Exception e) {
            System.err.println("Error al generar Excel de Ventas: " + e.getMessage());
        }
    }
}