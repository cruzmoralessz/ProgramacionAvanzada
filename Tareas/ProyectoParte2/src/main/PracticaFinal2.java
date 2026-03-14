package main;

import javax.swing.SwingUtilities;

import controlador.AppS;
import controlador.MainC;
import vista.MenuPF;

public class PracticaFinal2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppS service = new AppS();
            MenuPF frame = new MenuPF();
            new MainC(frame, service);
            frame.setVisible(true);
        });
    }
}
