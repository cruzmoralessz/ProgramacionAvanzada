package controlador;

import java.util.List;

import javax.swing.JOptionPane;

import modelo.AppModel;
import vista.MainView;

public class AppController {
    private AppModel model;
    private MainView view;
    private Object[][] datosCalculadosGrupos = null;

    public AppController(AppModel model, MainView view) {
        this.model = model;
        this.view = view;
        initController();
        actualizarTablaCatalogo();
    }

    private void initController() {
        view.btnCalcularPromedios.addActionListener(e -> {
            model.cargarCalificaciones();
            datosCalculadosGrupos = model.calcularIndicadoresAcademicos();
            
            view.modeloPromedios.setRowCount(0);
            for (Object[] fila : datosCalculadosGrupos) {
                view.modeloPromedios.addRow(fila);
            }
            JOptionPane.showMessageDialog(view, "Cálculos realizados correctamente.");
        });

        view.btnGenerarCedula.addActionListener(e -> {
            if (datosCalculadosGrupos == null || datosCalculadosGrupos.length == 0) {
                JOptionPane.showMessageDialog(view, "Primero debes calcular los promedios en la primera pestaña.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Object[][] datosCedula = model.generarDatosCedula(datosCalculadosGrupos);
            
            view.modeloCedula.setRowCount(0);
            for (Object[] fila : datosCedula) {
                view.modeloCedula.addRow(fila);
            }
        });

        view.btnAgregarCatalogo.addActionListener(e -> {
            String academia = view.txtAcademia.getText().trim();
            String asignatura = view.txtAsignatura.getText().trim();
            
            if (!academia.isEmpty() && !asignatura.isEmpty()) {
                model.agregarCatalogo(academia, asignatura);
                actualizarTablaCatalogo();
                view.txtAcademia.setText("");
                view.txtAsignatura.setText("");
            } else {
                JOptionPane.showMessageDialog(view, "Llena ambos campos.");
            }
        });

        view.btnEliminarCatalogo.addActionListener(e -> {
            int filaSeleccionada = view.tablaCatalogo.getSelectedRow();
            if (filaSeleccionada >= 0) {
                model.eliminarCatalogo(filaSeleccionada);
                actualizarTablaCatalogo();
            } else {
                JOptionPane.showMessageDialog(view, "Selecciona una fila para eliminar.");
            }
        });
    }

    private void actualizarTablaCatalogo() {
        view.modeloCatalogo.setRowCount(0);
        List<String[]> catalogo = model.getCatalogo();
        for (String[] fila : catalogo) {
            view.modeloCatalogo.addRow(fila);
        }
    }
}