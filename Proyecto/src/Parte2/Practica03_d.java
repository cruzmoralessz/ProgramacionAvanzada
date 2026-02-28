package Parte2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Practica03_d extends JFrame implements ActionListener {

    ListaInsumos listainsumo;
    ListaCategorias listacategorias;
    
    private JComboBox ComboCategoria;
    private JTextField Tid, Tinsumo;
    private JButton Bagregar, Beliminar, Bsalir;
    private JTextArea areaProductos;
    private JPanel panelFormulario;

    public Practica03_d() {
        super("Administración de Productos - Actividad 4");
        
        this.listacategorias = new ListaCategorias();
        this.listainsumo = new ListaInsumos();

        cargarDatosDeArchivos();

        setBounds(0, 0, 390, 370);
        panelFormulario = new JPanel();
        panelFormulario.setLayout(null);
        getContentPane().add(panelFormulario, BorderLayout.CENTER); 
        
        JLabel labelCategoria = new JLabel("Categoría:");
        labelCategoria.setBounds(10, 66, 71, 20);
        
        ComboCategoria = new JComboBox(this.listacategorias.CategoriasArreglo());
        
        this.ComboCategoria.setEnabled(true);
        
        ComboCategoria.setBounds(91, 66, 160, 20);
        ComboCategoria.addActionListener(this);
        panelFormulario.add(labelCategoria);
        panelFormulario.add(ComboCategoria); 
        
        JLabel labelId = new JLabel("ID:");
        labelId.setBounds(10, 9, 71, 20);
        this.Tid = new JTextField(10);
        this.Tid.setEditable(false);
        this.Tid.setBounds(91, 9, 147, 20);
        panelFormulario.add(labelId);
        panelFormulario.add(Tid);

        JLabel labelInsumo = new JLabel("Insumo:");
        labelInsumo.setBounds(10, 34, 71, 20);
        this.Tinsumo = new JTextField(20);
        this.Tinsumo.setEditable(false);
        this.Tinsumo.setBounds(91, 35, 147, 20);
        panelFormulario.add(labelInsumo);
        panelFormulario.add(Tinsumo);

        this.Bagregar = new JButton("Agregar");
        this.Bagregar.setBounds(20, 104, 111, 20);
        this.Bagregar.addActionListener(this);
        panelFormulario.add(Bagregar);

        this.Beliminar = new JButton("Eliminar");
        this.Beliminar.setBounds(153, 104, 111, 20);
        this.Beliminar.addActionListener(this);
        panelFormulario.add(Beliminar);

        this.Bsalir = new JButton("Salir");
        this.Bsalir.setBounds(274, 104, 79, 20);
        this.Bsalir.addActionListener(this);
        panelFormulario.add(Bsalir); 
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 132, 357, 179);
        panelFormulario.add(scrollPane);
        
        this.areaProductos = new JTextArea(10, 40);
        scrollPane.setViewportView(areaProductos);
        this.areaProductos.setEditable(false);
        
        this.areaProductos.setText(this.listainsumo.toString());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private ArrayList<String[]> leerArchivoCSV(String nombreArchivo) {
        ArrayList<String[]> lineasSeparadas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Separamos por comas y lo agregamos a la lista
                lineasSeparadas.add(linea.split(","));
            }
        } catch (IOException e) {
            System.err.println("No se encontró o no se pudo leer el archivo: " + nombreArchivo);
        }
        return lineasSeparadas;
    }

    private void cargarDatosDeArchivos() {
        ArrayList<String[]> datosCategorias = leerArchivoCSV("categorias.txt");
        if (!datosCategorias.isEmpty()) {
            this.listacategorias.cargarCategorias(datosCategorias);
        } else {
            this.listacategorias.agregarCategoria(new Categoria("01", "Materiales"));
            this.listacategorias.agregarCategoria(new Categoria("02", "Mano de Obra"));
        }

        ArrayList<String[]> datosInsumos = leerArchivoCSV("insumos.txt");
        if (!datosInsumos.isEmpty()) {
            this.listainsumo.cargarInsumo(datosInsumos);
        }
    }

    private void guardarDatosEnArchivo() {
        String datosCsv = this.listainsumo.toString().replace("\t\t", ",");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("insumos.txt"))) {
            writer.write(datosCsv);
        } catch (IOException e) {
            System.err.println("Error al guardar insumos.txt");
        }
    }

    public void Volveralinicio() {
        this.Bagregar.setText("Agregar");
        this.Bsalir.setText("Salir");
        this.Beliminar.setEnabled(true);
        this.Tid.setEditable(false);
        this.Tinsumo.setEditable(false);
        this.ComboCategoria.setEnabled(false);
        this.Tid.setText("");
        this.Tinsumo.setText("");
        this.ComboCategoria.setSelectedIndex(0);
    }

    private boolean esdatoscompletos() {
        return !this.Tid.getText().trim().isEmpty() && !this.Tinsumo.getText().trim().isEmpty();
    }

    public void Altas() {
        if (this.Bagregar.getText().compareTo("Agregar") == 0) {
            this.Bagregar.setText("Salvar");
            this.Bsalir.setText("Cancelar");
            this.Beliminar.setEnabled(false);
            this.Tid.setEditable(true);
            this.Tinsumo.setEditable(true);
            this.ComboCategoria.setEnabled(true);
            this.ComboCategoria.setFocusable(true);
            
            this.ComboCategoria.setSelectedIndex(0); 
            
        } else {
            if (esdatoscompletos()) {
                String id = this.Tid.getText().trim();
                String insumo = this.Tinsumo.getText().trim();
                String idcategoria = ((Categoria) this.ComboCategoria.getSelectedItem()).getIdcategoria().trim();
                
                Insumo nodo = new Insumo(id, insumo, idcategoria);
                
                if (!this.listainsumo.agregarInsumo(nodo)) {
                    JOptionPane.showMessageDialog(this, "Lo siento, el id " + id + " ya existe.");
                } else {
                    this.areaProductos.setText(this.listainsumo.toString());
                    
                    guardarDatosEnArchivo(); 
                }
                this.Volveralinicio();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos.");
            }
        }
    }

    public void Eliminar() {
        Object[] opciones = this.listainsumo.idinsumos();
        
        if (opciones == null || opciones.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay insumos registrados para eliminar.");
            return;
        }

        String id = (String) JOptionPane.showInputDialog(null, "Seleccione un ID:", "Eliminacion de Insumos", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        
        if (id != null && !id.isEmpty()) { 
            if (!this.listainsumo.eliminarInsumoPorId(id)) {
                JOptionPane.showMessageDialog(this, "No existe este id");
            } else {
                this.areaProductos.setText(this.listainsumo.toString());
                
                guardarDatosEnArchivo();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Bagregar) {
            this.Altas();
        } else if (e.getSource() == this.Beliminar) {
            this.Eliminar();
        } else if (e.getSource() == Bsalir) {
            if (this.Bsalir.getText().compareTo("Cancelar") == 0) {
                this.Volveralinicio();
            } else {
                this.dispose(); 
                System.exit(0); 
            }
        }
    }

    public static void main(String[] args) {
        new Practica03_d();
    }
}