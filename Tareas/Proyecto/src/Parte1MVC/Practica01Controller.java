package Parte1MVC;

import java.awt.EventQueue;

public class Practica01Controller {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Practica01View view = new Practica01View();
                view.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}