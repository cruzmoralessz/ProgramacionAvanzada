package Parte2MVC;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VistaPractica03 extends JFrame {
    
    public JComboBox<Object> ComboCategoria;
    public JTextField Tid, Tinsumo;
    public JButton Bagregar, Beliminar, Bsalir;
    public JTextArea areaProductos;
    private JPanel panelFormulario;

    public VistaPractica03() {
        super("Administración de Productos - Arquitectura MVC");
        setBounds(0, 0, 390, 370);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panelFormulario = new JPanel();
        panelFormulario.setLayout(null);
        panelFormulario.setBackground(Color.WHITE);
        getContentPane().add(panelFormulario, BorderLayout.CENTER); 
        
        JLabel labelCategoria = new JLabel("Categoría:");
        labelCategoria.setBounds(10, 66, 71, 20);
        
        ComboCategoria = new JComboBox<>();
        ComboCategoria.setEnabled(true);
        ComboCategoria.setBounds(91, 66, 160, 20);
        panelFormulario.add(labelCategoria);
        panelFormulario.add(ComboCategoria); 
        
        JLabel labelId = new JLabel("ID:");
        labelId.setBounds(10, 9, 71, 20);
        Tid = new JTextField(10);
        Tid.setEditable(false);
        Tid.setBounds(91, 9, 147, 20);
        panelFormulario.add(labelId);
        panelFormulario.add(Tid);

        JLabel labelInsumo = new JLabel("Insumo:");
        labelInsumo.setBounds(10, 34, 71, 20);
        Tinsumo = new JTextField(20);
        Tinsumo.setEditable(false);
        Tinsumo.setBounds(91, 35, 147, 20);
        panelFormulario.add(labelInsumo);
        panelFormulario.add(Tinsumo);

        Bagregar = new JButton("Agregar");
        Bagregar.setBounds(20, 104, 111, 20);
        panelFormulario.add(Bagregar);

        Beliminar = new JButton("Eliminar");
        Beliminar.setBounds(153, 104, 111, 20);
        panelFormulario.add(Beliminar);

        Bsalir = new JButton("Salir");
        Bsalir.setBounds(274, 104, 79, 20);
        panelFormulario.add(Bsalir); 
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 132, 357, 179);
        panelFormulario.add(scrollPane);
        
        areaProductos = new JTextArea(10, 40);
        scrollPane.setViewportView(areaProductos);
        areaProductos.setEditable(false);
    }
}