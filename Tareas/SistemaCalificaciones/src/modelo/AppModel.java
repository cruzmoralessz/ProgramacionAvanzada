package modelo;

import java.io.*;
import java.util.*;

public class AppModel {
    private final String ARCHIVO_CALIFICACIONES = "calificaciones.csv";
    private final String ARCHIVO_CATALOGO = "catalogo_academias.csv";
    private final double MINIMA_APROBATORIA = 7.0;

    private List<String[]> datosCalificaciones = new ArrayList<>();
    private List<String[]> catalogoAcademias = new ArrayList<>();

    public AppModel() {
        crearArchivosEjemploSiNoExisten();
        cargarCatalogo();
    }

    public void cargarCalificaciones() {
        datosCalificaciones.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CALIFICACIONES))) {
            String linea;
            boolean cabecera = true;
            while ((linea = br.readLine()) != null) {
                if (cabecera) { cabecera = false; continue; }
                datosCalificaciones.add(linea.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object[][] calcularIndicadoresAcademicos() {
        Map<String, List<Double>> agrupacion = new HashMap<>();
        
        for (String[] fila : datosCalificaciones) {
            if (fila.length < 10) continue; 
            
            try {
                String clave = fila[3].trim() + "," + fila[4].trim() + "," + fila[2].trim(); 
                
                double calificacion = Double.parseDouble(fila[9].trim());
                
                agrupacion.computeIfAbsent(clave, k -> new ArrayList<>()).add(calificacion);
            } catch (NumberFormatException e) {
                System.out.println("Se ignoró una calificación no numérica: " + fila[9]);
            }
        }

        Object[][] resultados = new Object[agrupacion.size()][9];
        int i = 0;
        for (Map.Entry<String, List<Double>> entry : agrupacion.entrySet()) {
            String[] claves = entry.getKey().split(",");
            List<Double> califs = entry.getValue();

            int numAlumnos = califs.size();
            int numReprobados = 0;
            double sumaGeneral = 0;
            double sumaAprobados = 0;

            for (double c : califs) {
                sumaGeneral += c;
                if (c < MINIMA_APROBATORIA) {
                    numReprobados++;
                } else {
                    sumaAprobados += c;
                }
            }

            int numAprobados = numAlumnos - numReprobados;
            double promedio = sumaGeneral / numAlumnos;
            double porcAprobados = ((double) numAprobados / numAlumnos) * 100;
            double porcReprobados = ((double) numReprobados / numAlumnos) * 100;
            double promedioAcreditados = (numAprobados > 0) ? (sumaAprobados / numAprobados) : 0;

            resultados[i++] = new Object[]{
                claves[0], claves[1], claves[2], 
                String.format("%.2f", promedio), numAlumnos, numReprobados, 
                String.format("%.2f%%", porcAprobados), String.format("%.2f%%", porcReprobados), 
                String.format("%.2f", promedioAcreditados)
            };
        }
        return resultados;
    }

    public Object[][] generarDatosCedula(Object[][] datosGrupos) {
        Map<String, List<Object[]>> porAsignatura = new HashMap<>();
        for (Object[] fila : datosGrupos) {
            String asignatura = (String) fila[1];
            porAsignatura.computeIfAbsent(asignatura, k -> new ArrayList<>()).add(fila);
        }

        Object[][] resultados = new Object[porAsignatura.size()][7];
        int i = 0;
        
        for (Map.Entry<String, List<Object[]>> entry : porAsignatura.entrySet()) {
            String asignatura = entry.getKey();
            List<Object[]> grupos = entry.getValue();

            String academia = obtenerAcademiaPorAsignatura(asignatura);
            int numGrupos = grupos.size();
            double sumaPromedios = 0;
            int totalAlumnosAsig = 0;
            int totalReprobadosAsig = 0;
            Set<String> profesores = new HashSet<>();

            for (Object[] g : grupos) {
                sumaPromedios += Double.parseDouble(((String) g[3]).replace(",", "."));
                totalAlumnosAsig += (int) g[4];
                totalReprobadosAsig += (int) g[5];
                profesores.add((String) g[0]);
            }

            double promedioGeneral = sumaPromedios / numGrupos;
            
            int gruposMayorPromedio = 0;
            for (Object[] g : grupos) {
                double promGrupo = Double.parseDouble(((String) g[3]).replace(",", "."));
                if (promGrupo > promedioGeneral) gruposMayorPromedio++;
            }

            double porcMayorPromedio = ((double) gruposMayorPromedio / numGrupos) * 100;
            double porcReprobacionGlobal = ((double) totalReprobadosAsig / totalAlumnosAsig) * 100;

            resultados[i++] = new Object[]{
                academia, asignatura, numGrupos, 
                String.format("%.2f", promedioGeneral), 
                String.format("%.2f%%", porcMayorPromedio), 
                String.format("%.2f%%", porcReprobacionGlobal), 
                String.join(" / ", profesores)
            };
        }
        return resultados;
    }

    private String obtenerAcademiaPorAsignatura(String asignatura) {
        for (String[] fila : catalogoAcademias) {
            if (fila[1].trim().equalsIgnoreCase(asignatura.trim())) {
                return fila[0];
            }
        }
        return "Sin Academia Asignada";
    }

    public void cargarCatalogo() {
        catalogoAcademias.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CATALOGO))) {
            String linea;
            boolean cabecera = true;
            while ((linea = br.readLine()) != null) {
                if (cabecera) { cabecera = false; continue; }
                catalogoAcademias.add(linea.split(","));
            }
        } catch (IOException e) {
        }
    }

    public void guardarCatalogo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_CATALOGO))) {
            pw.println("Academia,Asignatura");
            for (String[] fila : catalogoAcademias) {
                pw.println(fila[0] + "," + fila[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void agregarCatalogo(String academia, String asignatura) {
        catalogoAcademias.add(new String[]{academia, asignatura});
        guardarCatalogo();
    }

    public void eliminarCatalogo(int index) {
        if (index >= 0 && index < catalogoAcademias.size()) {
            catalogoAcademias.remove(index);
            guardarCatalogo();
        }
    }

    public List<String[]> getCatalogo() {
        return catalogoAcademias;
    }

    private void crearArchivosEjemploSiNoExisten() {
        File fCalifs = new File(ARCHIVO_CALIFICACIONES);
        if (!fCalifs.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fCalifs))) {
                pw.println("Profesor,Asignatura,Grupo,Calificacion");
                pw.println("Juan Perez,Programacion Java,A,85.5");
                pw.println("Juan Perez,Programacion Java,A,60.0");
                pw.println("Maria Lopez,Bases de Datos,B,90.0");
                pw.println("Maria Lopez,Bases de Datos,B,50.0");
            } catch (Exception e) {}
        }
        
        File fCat = new File(ARCHIVO_CATALOGO);
        if (!fCat.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fCat))) {
                pw.println("Academia,Asignatura");
                pw.println("Sistemas,Programacion Java");
                pw.println("Sistemas,Bases de Datos");
            } catch (Exception e) {}
        }
    }
}