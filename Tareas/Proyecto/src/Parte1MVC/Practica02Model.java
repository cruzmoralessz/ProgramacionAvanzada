package Parte1MVC;

public class Practica02Model {
    public String texto, password, area, formateado, combo;
    public int spinner, slider;

    public String generarResumen() {
        return "Resumen de datos:\n" +
               "Texto: " + texto + "\n" +
               "Pass: " + password + "\n" +
               "Area: " + area + "\n" +
               "Formateado: " + formateado + "\n" +
               "Spinner: " + spinner + "\n" +
               "Slider: " + slider + "\n" +
               "Combo: " + combo;
    }
}