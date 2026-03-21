package archivo;

import modelo.Producto;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class GestorJSON {
    private final String BD_JSON = "productos.json";
    private Gson traductorGson;

    public GestorJSON() {
        JsonDeserializer<Producto> lectorPersonalizado = new JsonDeserializer<Producto>() {
            @Override
            public Producto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject obj = json.getAsJsonObject();
                String cat = obj.get("categoria").getAsString();

                // instanciar segun el json
                switch (cat) {
                    case "Abarrotes": return context.deserialize(json, modelo.Abarrote.class);
                    case "Bebidas": return context.deserialize(json, modelo.Bebida.class);
                    case "Lácteos y Huevo": return context.deserialize(json, modelo.Lacteo.class);
                    case "Frutas y Verduras": return context.deserialize(json, modelo.FrutaVerdura.class);
                    case "Carnes y Pescados": return context.deserialize(json, modelo.CarnePescado.class);
                    case "Salchichonería": return context.deserialize(json, modelo.Salchichoneria.class);
                    case "Panadería y Tortillería": return context.deserialize(json, modelo.Panaderia.class);
                    case "Limpieza del Hogar": return context.deserialize(json, modelo.Limpieza.class);
                    case "Cuidado Personal": return context.deserialize(json, modelo.CuidadoPersonal.class);
                    case "Snacks y Dulcería": return context.deserialize(json, modelo.Snack.class);
                    case "Mascotas": return context.deserialize(json, modelo.Mascota.class);
                    default: throw new JsonParseException("Error de categoria: " + cat);
                }
            }
        };

        this.traductorGson = new GsonBuilder()
                .registerTypeAdapter(Producto.class, lectorPersonalizado)
                .setPrettyPrinting()
                .create();
    }

    public void exportarJSON(ArrayList<Producto> miLista) {
        try (Writer w = new FileWriter(BD_JSON)) {
            traductorGson.toJson(miLista, w);
        } catch (IOException ex) {
            System.out.println("ERROOR al guardar: " + ex.getMessage());
        }
    }

    public ArrayList<Producto> importarJSON() {
        ArrayList<Producto> inventarioTemp = new ArrayList<>();
        File arch = new File(BD_JSON);

        if (arch.exists()) {
            try (Reader r = new FileReader(arch)) {
                Type tipoEsperado = new TypeToken<ArrayList<Producto>>(){}.getType();
                inventarioTemp = traductorGson.fromJson(r, tipoEsperado);
                if (inventarioTemp == null) inventarioTemp = new ArrayList<>();
            } catch (Exception ex) {
                System.out.println("ERROR cargando base de datos: " + ex.getMessage());
            }
        }
        return inventarioTemp;
    }
    
    // para tickets
    public void guardarTicketJSON(int numTicket, String fecha, double sub, double iva, double totalFinal, javax.swing.table.DefaultTableModel tabla) {
        JsonObject jsonVenta = new JsonObject();
        String codigoFolio = String.format("FOLIO%03d", numTicket); 
        
        jsonVenta.addProperty("folio", codigoFolio);
        jsonVenta.addProperty("fecha", fecha);
        
        JsonArray prodsComprados = new JsonArray();
        for (int i = 0; i < tabla.getRowCount(); i++) {
            JsonObject filaProd = new JsonObject();
            filaProd.addProperty("cantidad", Integer.parseInt(tabla.getValueAt(i, 2).toString()));
            filaProd.addProperty("nombre", tabla.getValueAt(i, 1).toString());
            filaProd.addProperty("precioUnitario", Double.parseDouble(tabla.getValueAt(i, 3).toString()));
            filaProd.addProperty("subtotal", Double.parseDouble(tabla.getValueAt(i, 4).toString()));
            prodsComprados.add(filaProd);
        }
        
        jsonVenta.add("productos", prodsComprados);
        jsonVenta.addProperty("subtotal", sub);
        jsonVenta.addProperty("iva", iva);
        jsonVenta.addProperty("total", totalFinal);

        try (Writer w = new FileWriter(codigoFolio + ".json")) {
            traductorGson.toJson(jsonVenta, w);
        } catch (IOException e) {
            System.out.println("no se pudo hacer el ticket");
        }
        
        actualizarRegistroFolios(codigoFolio);
    }

    private void actualizarRegistroFolios(String nuevoFolio) {
        File fileFolios = new File("control_tickets.json");
        JsonArray listaFolios = new JsonArray();
        
        if (fileFolios.exists()) {
            try (Reader r = new FileReader(fileFolios)) {
                listaFolios = JsonParser.parseReader(r).getAsJsonArray();
            } catch (Exception e) {}
        }
        
        listaFolios.add(nuevoFolio);
        
        try (Writer w = new FileWriter(fileFolios)) {
            traductorGson.toJson(listaFolios, w);
        } catch (IOException e) {}
    }
    
    // para que no se reinicie el contador
    public int obtenerSiguienteNumeroTicket() {
        File f = new File("control_tickets.json");
        if (!f.exists()) return 1; 
        
        try (Reader r = new FileReader(f)) {
            JsonArray arr = JsonParser.parseReader(r).getAsJsonArray();
            return arr.size() + 1; 
        } catch (Exception e) {
            return 1;
        }
    }
}