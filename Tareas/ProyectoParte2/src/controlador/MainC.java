package controlador;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import modelo.StorageType;
import vista.CategoriaIF;
import vista.InsumoIF;
import vista.MenuPF;
import vista.ObraIF;

public class MainC {
    private final MenuPF view;
    private final AppS service;

    public MainC(MenuPF view, AppS service) {
        this.view = view;
        this.service = service;

        this.view.applyStorageType(this.service.getStorageType());

        this.view.miCategorias.addActionListener(e -> openCategorias());
        this.view.miInsumos.addActionListener(e -> openInsumos());
        this.view.miObras.addActionListener(e -> openObras());
        this.view.miTxt.addActionListener(e -> changeStorage(StorageType.TXT));
        this.view.miXml.addActionListener(e -> changeStorage(StorageType.XML));
        this.view.miSalir.addActionListener(e -> this.view.dispose());
    }

    private void changeStorage(StorageType storageType) {
        this.service.setStorageType(storageType);
        this.view.applyStorageType(storageType);
    }

    private void openCategorias() {
        CategoriaIF frame = new CategoriaIF();
        new CategoriaC(frame, this.service);
        openFrame(frame);
    }

    private void openInsumos() {
        InsumoIF frame = new InsumoIF();
        new InsumoC(frame, this.service);
        openFrame(frame);
    }

    private void openObras() {
        ObraIF frame = new ObraIF();
        new ObraC(frame, this.service);
        openFrame(frame);
    }

    private void openFrame(JInternalFrame frame) {
        this.view.desktopPane.add(frame);
        this.view.setMainActionsEnabled(false);
        frame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                view.setMainActionsEnabled(true);
            }
        });
        frame.setVisible(true);
        try {
            frame.setSelected(true);
        } catch (Exception ignored) {
        }
    }
}
