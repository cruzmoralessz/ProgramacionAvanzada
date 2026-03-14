package main;

import javax.swing.SwingUtilities;

import controlador.AppController;
import modelo.AppModel;
import vista.MainView;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppModel modelo = new AppModel();
            MainView vista = new MainView();
            AppController controlador = new AppController(modelo, vista);
            
            vista.setVisible(true);
        });
    }
}