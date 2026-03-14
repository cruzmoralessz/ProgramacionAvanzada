package archivo;

import modelo.Producto;
import java.io.*;
import java.util.ArrayList;

public class GestorCSV {
    private final String RUTA_ARCHIVO = "productos.csv";

    public void exportarCSV(ArrayList<Producto> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Producto p : lista) {
                bw.write(p.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar CSV: " + e.getMessage());
        }
    }

    public ArrayList<Producto> importarCSV() {
        ArrayList<Producto> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        
        if (!archivo.exists()) {
            return lista; 
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue; //no leer lineas en white
                
                String[] datos = linea.split(",");
                
                if (datos.length >= 8) { 
                    try {
                        Producto p = new Producto(
                            datos[0].trim(), // ID
                            datos[1].trim(), // nom
                            datos[2].trim(), // desc
                            datos[3].trim(), // cat
                            Double.parseDouble(datos[4].trim()), // pcomp
                            Double.parseDouble(datos[5].trim()), // pvent
                            Integer.parseInt(datos[6].trim()),   // stock
                            Boolean.parseBoolean(datos[7].trim()) // estado
                        );
                        lista.add(p);
                    } catch (NumberFormatException ex) {
                        System.err.println("Un producto ignorado por error numerico" + linea);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("error al leer csv: " + e.getMessage());
        }
        return lista;
    }
}