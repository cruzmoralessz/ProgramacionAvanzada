package archivo;

import modelo.Producto;
// Herramientas de Apache POI para crear el Excel
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

            for (int i = 0; i < 6; i++) {
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
        String[] encabezados = {"ID", "Nombre", "Descripción", "Categoría", "Precio Venta", "Stock"};
        
        for (int i = 0; i < encabezados.length; i++) {
            Cell celda = filaEncabezado.createCell(i);
            celda.setCellValue(encabezados[i]);
        }
    }

    private void llenarDatos(Sheet hoja, ArrayList<Producto> lista) {
        int numFila = 1; //porque la 0 son los encabezados 
        for (Producto p : lista) {
            Row fila = hoja.createRow(numFila++);
            fila.createCell(0).setCellValue(p.getId());
            fila.createCell(1).setCellValue(p.getNombre());
            fila.createCell(2).setCellValue(p.getDescripcion());
            fila.createCell(3).setCellValue(p.getCategoria());
            fila.createCell(4).setCellValue(p.getPrecioVenta());
            fila.createCell(5).setCellValue(p.getStock());
        }
    }
}