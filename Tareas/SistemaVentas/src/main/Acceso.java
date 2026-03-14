package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

// El import de org.apache... fue eliminado

public class Acceso {

    public static List<String> cargarArchivo(String ruta) {
        List<String> lista = null;
        FileReader fi;
        BufferedReader bu;
        String linea;
        File carpeta = new File("Archivos");
        File archivo = new File("Archivos/" + ruta);
        try {
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
            lista = new ArrayList<>();
            fi = new FileReader("Archivos/" + ruta);
            bu = new BufferedReader(fi);
            while ((linea = bu.readLine()) != null) {
                lista.add(linea);
            }
            bu.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar archivo: " + archivo.getName() + " ->" + e.getMessage());
        }
        return lista;
    }

}