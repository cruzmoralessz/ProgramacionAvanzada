package archivo;

import modelo.*;
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
                if (linea.trim().isEmpty()) continue; 
                
                String[] datos = linea.split(",");
                
                if (datos.length >= 11) {
                    try {
                        String id = datos[0].trim();
                        String nom = datos[1].trim();
                        String desc = datos[2].trim();
                        String cat = datos[3].trim();
                        double pcomp = Double.parseDouble(datos[4].trim());
                        double ganancia = Double.parseDouble(datos[5].trim());
                        int almacen = Integer.parseInt(datos[7].trim());
                        String unidad = datos[8].trim();
                        boolean estado = Boolean.parseBoolean(datos[9].trim());
                        String rutaImagen = datos[10].trim(); 

                        Producto p = null; 

                        switch (cat) {
                            case "Despensa Básica": p = new DespensaBasica(id, nom, desc, pcomp, ganancia, almacen, unidad, estado); break;
                            case "Lácteos y Huevo": p = new LacteoHuevo(id, nom, desc, pcomp, ganancia, almacen, unidad, estado); break;
                            case "Bebidas y Líquidos": p = new BebidaLiquido(id, nom, desc, pcomp, ganancia, almacen, unidad, estado); break;
                            case "Botanas y Dulces": p = new BotanaDulce(id, nom, desc, pcomp, ganancia, almacen, unidad, estado); break;
                            case "Frutas y Verduras": p = new FrutaVerdura(id, nom, desc, pcomp, ganancia, almacen, unidad, estado); break;
                            case "Carnes y Salchichonería": p = new CarneSalchichoneria(id, nom, desc, pcomp, ganancia, almacen, unidad, estado); break;
                            case "Cuidado del Hogar": p = new CuidadoHogar(id, nom, desc, pcomp, ganancia, almacen, unidad, estado); break;
                            case "Higiene y Cuidado Personal": p = new HigieneCuidadoPersonal(id, nom, desc, pcomp, ganancia, almacen, unidad, estado); break;
                            case "Alimentos Preparados/Enlatados": p = new AlimentoPreparado(id, nom, desc, pcomp, ganancia, almacen, unidad, estado); break;
                            default: 
                                System.err.println("Categoria desconocida para el producto: " + id); 
                                continue; 
                        }
                        
                        p.setRutaImagen(rutaImagen);
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