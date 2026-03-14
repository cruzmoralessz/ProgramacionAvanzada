package Parte2MVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import Parte2.Categoria;
import Parte2.Insumo;
import Parte2.ListaInsumos;

public class ControladorPractica03 implements ActionListener {

    private VistaPractica03 vista;
    private ListaInsumos modeloInsumos;
    private ListaCategorias modeloCategorias;

    public ControladorPractica03(VistaPractica03 vista, ListaInsumos modeloInsumos, ListaCategorias modeloCategorias) {
        this.vista = vista;
        this.modeloInsumos = modeloInsumos;
        this.modeloCategorias = modeloCategorias;
        
        this.vista.Bagregar.addActionListener(this);
        this.vista.Beliminar.addActionListener(this);
        this.vista.Bsalir.addActionListener(this);
    }

    public void iniciar() {
        cargarDatosDeArchivos();
        
        vista.ComboCategoria.setModel(new DefaultComboBoxModel<>(modeloCategorias.CategoriasArreglo()));
        
        vista.areaProductos.setText(modeloInsumos.toString());
        
        vista.setVisible(true);
    }

    private ArrayList<String[]> leerArchivoCSV(String nombreArchivo) {
        ArrayList<String[]> lineasSeparadas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineasSeparadas.add(linea.split(","));
            }
        } catch (IOException e) {
            System.err.println("No se encontró el archivo: " + nombreArchivo);
        }
        return lineasSeparadas;
    }

    private void cargarDatosDeArchivos() {
        ArrayList<String[]> datosCategorias = leerArchivoCSV("categorias.txt");
        if (!datosCategorias.isEmpty()) {
            modeloCategorias.cargarCategorias(datosCategorias);
        } else {
            modeloCategorias.agregarCategoria(new Categoria("01", "Materiales"));
            modeloCategorias.agregarCategoria(new Categoria("02", "Mano de Obra"));
        }

        ArrayList<String[]> datosInsumos = leerArchivoCSV("insumos.txt");
        if (!datosInsumos.isEmpty()) {
            modeloInsumos.cargarInsumo(datosInsumos);
        }
    }

    private void guardarDatosEnArchivo() {
        String datosCsv = modeloInsumos.toString().replace("\t\t", ",");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("insumos.txt"))) {
            writer.write(datosCsv);
        } catch (IOException e) {
            System.err.println("Error al guardar insumos.txt");
        }
    }

    public void Volveralinicio() {
        vista.Bagregar.setText("Agregar");
        vista.Bsalir.setText("Salir");
        vista.Beliminar.setEnabled(true);
        vista.Tid.setEditable(false);
        vista.Tinsumo.setEditable(false);
        vista.ComboCategoria.setEnabled(false);
        vista.Tid.setText("");
        vista.Tinsumo.setText("");
        if(vista.ComboCategoria.getItemCount() > 0) {
            vista.ComboCategoria.setSelectedIndex(0);
        }
    }

    private boolean esdatoscompletos() {
        return !vista.Tid.getText().trim().isEmpty() && !vista.Tinsumo.getText().trim().isEmpty();
    }

    public void Altas() {
        if (vista.Bagregar.getText().compareTo("Agregar") == 0) {
            vista.Bagregar.setText("Salvar");
            vista.Bsalir.setText("Cancelar");
            vista.Beliminar.setEnabled(false);
            vista.Tid.setEditable(true);
            vista.Tinsumo.setEditable(true);
            vista.ComboCategoria.setEnabled(true);
            vista.ComboCategoria.setFocusable(true);
            
            if(vista.ComboCategoria.getItemCount() > 0) {
                vista.ComboCategoria.setSelectedIndex(0);
            }
        } else {
            if (esdatoscompletos()) {
                String id = vista.Tid.getText().trim();
                String insumo = vista.Tinsumo.getText().trim();
                String idcategoria = ((Categoria) vista.ComboCategoria.getSelectedItem()).getIdcategoria().trim();
                
                Insumo nodo = new Insumo(id, insumo, idcategoria);
                
                if (!modeloInsumos.agregarInsumo(nodo)) {
                    JOptionPane.showMessageDialog(vista, "Lo siento, el id " + id + " ya existe.");
                } else {
                    vista.areaProductos.setText(modeloInsumos.toString());
                    guardarDatosEnArchivo(); 
                }
                Volveralinicio();
            } else {
                JOptionPane.showMessageDialog(vista, "Por favor, llene todos los campos.");
            }
        }
    }

    public void Eliminar() {
        Object[] opciones = modeloInsumos.idinsumos();
        
        if (opciones == null || opciones.length == 0) {
            JOptionPane.showMessageDialog(vista, "No hay insumos registrados para eliminar.");
            return;
        }

        String id = (String) JOptionPane.showInputDialog(vista, "Seleccione un ID:", "Eliminacion de Insumos", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        
        if (id != null && !id.isEmpty()) { 
            if (!modeloInsumos.eliminarInsumoPorId(id)) {
                JOptionPane.showMessageDialog(vista, "No existe este id");
            } else {
                vista.areaProductos.setText(modeloInsumos.toString());
                guardarDatosEnArchivo();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.Bagregar) {
            Altas();
        } else if (e.getSource() == vista.Beliminar) {
            Eliminar();
        } else if (e.getSource() == vista.Bsalir) {
            if (vista.Bsalir.getText().compareTo("Cancelar") == 0) {
                Volveralinicio();
            } else {
                vista.dispose(); 
                System.exit(0); 
            }
        }
    }
}