package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainView extends JFrame {
    
    private JTabbedPane tabbedPane;
    
    public JButton btnCalcularPromedios;
    public JTable tablaPromedios;
    public DefaultTableModel modeloPromedios;

    public JButton btnGenerarCedula;
    public JTable tablaCedula;
    public DefaultTableModel modeloCedula;

    public JTextField txtAcademia;
    public JTextField txtAsignatura;
    public JButton btnAgregarCatalogo;
    public JButton btnEliminarCatalogo;
    public JTable tablaCatalogo;
    public DefaultTableModel modeloCatalogo;

    public MainView() {
        setTitle("Sistema de Análisis de Calificaciones y Cédula 3.3.2");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        initTabPromedios();
        initTabCedula();
        initTabCRUD();

        add(tabbedPane);
    }

    private void initTabPromedios() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelTop = new JPanel();
        
        btnCalcularPromedios = new JButton("Calcular Promedios (Leer CSV)");
        panelTop.add(btnCalcularPromedios);
        
        String[] columnas = {"Profesor", "Asignatura", "Grupo", "Promedio", "Num. Alumnos", "Num. Reprobados", "% Aprobados", "% Reprobados", "Prom. Acreditados"};
        modeloPromedios = new DefaultTableModel(columnas, 0);
        tablaPromedios = new JTable(modeloPromedios);
        
        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaPromedios), BorderLayout.CENTER);
        tabbedPane.addTab("Indicadores Académicos", panel);
    }

    private void initTabCedula() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelTop = new JPanel();
        
        btnGenerarCedula = new JButton("Generar Datos Cédula 3.3.2");
        panelTop.add(btnGenerarCedula);
        
        String[] columnas = {"Academia", "Asignatura", "Núm. Grupos", "Promedio Gral", "% Mayor al Promedio", "% Reprobación", "Profesores"};
        modeloCedula = new DefaultTableModel(columnas, 0);
        tablaCedula = new JTable(modeloCedula);
        
        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCedula), BorderLayout.CENTER);
        tabbedPane.addTab("Cédula 3.3.2", panel);
    }

    private void initTabCRUD() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel panelForm = new JPanel(new FlowLayout());
        panelForm.add(new JLabel("Academia:"));
        txtAcademia = new JTextField(15);
        panelForm.add(txtAcademia);
        
        panelForm.add(new JLabel("Asignatura:"));
        txtAsignatura = new JTextField(15);
        panelForm.add(txtAsignatura);
        
        btnAgregarCatalogo = new JButton("Agregar");
        btnEliminarCatalogo = new JButton("Eliminar Seleccionado");
        panelForm.add(btnAgregarCatalogo);
        panelForm.add(btnEliminarCatalogo);
        
        String[] columnas = {"Academia", "Asignatura"};
        modeloCatalogo = new DefaultTableModel(columnas, 0);
        tablaCatalogo = new JTable(modeloCatalogo);
        
        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCatalogo), BorderLayout.CENTER);
        tabbedPane.addTab("Admin. Academias", panel);
    }
}