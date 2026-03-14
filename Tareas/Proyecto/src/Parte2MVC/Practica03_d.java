package Parte2MVC;

import Parte2.ListaInsumos;

public class Practica03_d {
    
    public static void main(String[] args) {
        ListaInsumos modeloInsumos = new ListaInsumos();
        ListaCategorias modeloCategorias = new ListaCategorias();
        
        VistaPractica03 vista = new VistaPractica03();
        
        ControladorPractica03 controlador = new ControladorPractica03(vista, modeloInsumos, modeloCategorias);
        
        controlador.iniciar();
    }
}