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
                
                if (datos.length >= 9) { // ¡Ojo! Cambié de 8 a 9 porque agregaste la rutaImagen al toString
                    try {
                        String id = datos[0].trim();
                        String nom = datos[1].trim();
                        String desc = datos[2].trim();
                        String cat = datos[3].trim();
                        double pcomp = Double.parseDouble(datos[4].trim());
                        double pvent = Double.parseDouble(datos[5].trim());
                        int stock = Integer.parseInt(datos[6].trim());
                        boolean estado = Boolean.parseBoolean(datos[7].trim());
                        String rutaImagen = datos[8].trim(); // Leemos la nueva columna

                        Producto p = null; // Declaramos la variable, pero aún no la instanciamos

                        // Dependiendo de la categoría leída, creamos la clase hija correspondiente
                        switch (cat) {
                            case "Abarrotes": p = new modelo.Abarrote(id, nom, desc, pcomp, pvent, stock, estado, rutaImagen); break;
                            case "Bebidas": p = new modelo.Bebida(id, nom, desc, pcomp, pvent, stock, estado, rutaImagen); break;
                            case "Lácteos y Huevo": p = new modelo.Lacteo(id, nom, desc, pcomp, pvent, stock, estado, rutaImagen); break;
                            case "Frutas y Verduras": p = new modelo.FrutaVerdura(id, nom, desc, pcomp, pvent, stock, estado, rutaImagen); break;
                            case "Carnes y Pescados": p = new modelo.CarnePescado(id, nom, desc, pcomp, pvent, stock, estado, rutaImagen); break;
                            case "Salchichonería": p = new modelo.Salchichoneria(id, nom, desc, pcomp, pvent, stock, estado, rutaImagen); break;
                            case "Panadería y Tortillería": p = new modelo.Panaderia(id, nom, desc, pcomp, pvent, stock, estado, rutaImagen); break;
                            case "Limpieza del Hogar": p = new modelo.Limpieza(id, nom, desc, pcomp, pvent, stock, estado, rutaImagen); break;
                            case "Cuidado Personal": p = new modelo.CuidadoPersonal(id, nom, desc, pcomp, pvent, stock, estado, rutaImagen); break;
                            case "Snacks y Dulcería": p = new modelo.Snack(id, nom, desc, pcomp, pvent, stock, estado, rutaImagen); break;
                            case "Mascotas": p = new modelo.Mascota(id, nom, desc, pcomp, pvent, stock, estado, rutaImagen); break;
                            default: 
                                System.err.println("Categoría desconocida para el producto: " + id); 
                                continue; // Saltamos este producto si la categoría no cuadra
                        }
                        
                        lista.add(p);
                        
                    } catch (NumberFormatException ex) {
                        System.err.println("Un producto ignorado por error numerico: " + linea);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("error al leer csv: " + e.getMessage());
        }
        return lista;
    }
}