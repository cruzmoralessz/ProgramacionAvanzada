package main;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import controlador.AppS;
import controlador.CategoriaC;
import modelo.StorageType;
import vista.CategoriaIF;

public class Practica03_b {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppS service = new AppS();
            service.setStorageType(StorageType.TXT);

            JFrame frame = new JFrame("Parte1 - Practica03_b");
            frame.setSize(700, 470);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JDesktopPane desktopPane = new JDesktopPane();
            frame.setContentPane(desktopPane);

            CategoriaIF child = new CategoriaIF();
            desktopPane.add(child);
            new CategoriaC(child, service);
            child.setVisible(true);

            frame.setVisible(true);
        });
    }
}
