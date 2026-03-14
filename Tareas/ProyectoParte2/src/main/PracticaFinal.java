package main;

import javax.swing.SwingUtilities;

import controlador.AppS;
import controlador.MainC;
import modelo.StorageType;
import vista.MenuPF;

public class PracticaFinal {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppS service = new AppS();
            service.setStorageType(StorageType.TXT);
            MenuPF frame = new MenuPF();
            new MainC(frame, service);
            frame.setVisible(true);
        });
    }
}
