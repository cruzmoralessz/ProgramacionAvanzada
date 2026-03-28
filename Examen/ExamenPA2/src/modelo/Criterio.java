package modelo;

public class Criterio {
    private String nombreCriterio;
    private int puntaje; 
    private String observaciones;

    public Criterio() {
    }

    public Criterio(String nombreCriterio, int puntaje, String observaciones) {
        this.nombreCriterio = nombreCriterio;
        this.puntaje = puntaje;
        this.observaciones = observaciones;
    }

    public String getNombreCriterio() {
        return nombreCriterio;
    }

    public void setNombreCriterio(String nombreCriterio) {
        this.nombreCriterio = nombreCriterio;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}