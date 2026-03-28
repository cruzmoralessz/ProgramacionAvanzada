package modelo;

public class Alumno {
    private String matricula;
    private String nombre;
    private int calificacionRubrica;

    public Alumno() {
    }

    public Alumno(String matricula, String nombre) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.calificacionRubrica = 0;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCalificacionRubrica() {
        return calificacionRubrica;
    }

    public void setCalificacionRubrica(int calificacionRubrica) {
        this.calificacionRubrica = calificacionRubrica;
    }

    @Override
    public String toString() {
        return nombre;
    }
}