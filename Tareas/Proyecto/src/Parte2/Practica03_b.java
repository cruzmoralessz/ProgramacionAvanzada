package Parte2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Practica03_b extends JFrame implements ActionListener {

    ListaCategorias listacategorias;
    
    private JTextField Tid, Tcategoria;
    private JButton Bagregar, Beliminar, Bsalir;
    private JTextArea Tareacategoria;
    private JPanel panelFormulario;

    public Practica03_b() {
        super("Administracion de Categorias");
        
        this.listacategorias = new ListaCategorias();

        setBounds(100, 100, 400, 360);
        panelFormulario = new JPanel();
        panelFormulario.setLayout(null);
        getContentPane().add(panelFormulario, BorderLayout.CENTER); 
        
        JLabel labelId = new JLabel("ID:");
        labelId.setBounds(15, 15, 70, 20);
        this.Tid = new JTextField(10);
        this.Tid.setBounds(95, 15, 160, 20);

        this.Tid.setEditable(false);
        panelFormulario.add(labelId);
        panelFormulario.add(Tid);

        JLabel labelCategoria = new JLabel("Categoría:");
        labelCategoria.setBounds(15, 45, 70, 20);
        this.Tcategoria = new JTextField(20);
        this.Tcategoria.setBounds(95, 45, 160, 20);

        this.Tcategoria.setEditable(false);
        panelFormulario.add(labelCategoria);
        panelFormulario.add(Tcategoria);

        this.Bagregar = new JButton("Agregar");
        this.Bagregar.setBounds(20, 90, 100, 25);
        this.Bagregar.addActionListener(this);
        panelFormulario.add(Bagregar);

        this.Beliminar = new JButton("Eliminar");
        this.Beliminar.setBounds(135, 90, 100, 25);
        this.Beliminar.addActionListener(this);
        panelFormulario.add(Beliminar);

        this.Bsalir = new JButton("Salir");
        this.Bsalir.setBounds(250, 90, 100, 25);
        this.Bsalir.addActionListener(this);
        panelFormulario.add(Bsalir); 
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(15, 135, 350, 160);
        panelFormulario.add(scrollPane);
        
        this.Tareacategoria = new JTextArea();
        scrollPane.setViewportView(Tareacategoria);
        this.Tareacategoria.setEditable(false);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void Volveralinicio() {
        this.Bagregar.setText("Agregar");
        this.Bsalir.setText("Salir");
        this.Beliminar.setEnabled(true);
        this.Tid.setEditable(false);
        this.Tcategoria.setEditable(false);
        this.Tid.setText("");
        this.Tcategoria.setText("");
    }

    private boolean esdatoscompletos() {
        return !this.Tid.getText().trim().isEmpty() && !this.Tcategoria.getText().trim().isEmpty();
    }

    public void Altas() {
        if (this.Bagregar.getText().compareTo("Agregar") == 0) {
            this.Bagregar.setText("Salvar");
            this.Bsalir.setText("Cancelar");
            this.Beliminar.setEnabled(false);
            this.Tid.setEditable(true);
            this.Tcategoria.setEditable(true);
            this.Tid.requestFocus();
        } else {
            if (esdatoscompletos()) {
                String id = this.Tid.getText().trim();
                String nombreCategoria = this.Tcategoria.getText().trim();
                
                Categoria nuevaCategoria = new Categoria(id, nombreCategoria);
                
                if (this.listacategorias.buscarCategoria(id) != null) {
                    JOptionPane.showMessageDialog(this, "Lo siento, el ID " + id + " ya existe.");
                } else {
                    this.listacategorias.agregarCategoria(nuevaCategoria);
                    this.Tareacategoria.setText(this.listacategorias.toLinea());
                }
                this.Volveralinicio();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos (ID y Categoría).");
            }
        }
    }

    public void Eliminar() {
        Object[] objetosCategorias = this.listacategorias.CategoriasArreglo();
        
        if (objetosCategorias == null || objetosCategorias.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay categorías registradas para eliminar.");
            return;
        }

        String[] opcionesId = new String[objetosCategorias.length];
        for (int i = 0; i < objetosCategorias.length; i++) {
            opcionesId[i] = ((Categoria) objetosCategorias[i]).getIdcategoria();
        }

        String id = (String) JOptionPane.showInputDialog(
                null, 
                "Seleccione el ID de la Categoría a eliminar:", 
                "Eliminación de Categorías", 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                opcionesId, 
                opcionesId[0]
        );
        
        if (id != null && !id.isEmpty()) { 
            this.listacategorias.eliminarCategoriaPorId(id);
            this.Tareacategoria.setText(this.listacategorias.toLinea());
            JOptionPane.showMessageDialog(this, "Categoría eliminada con éxito.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Bagregar) {
            this.Altas();
        } else if (e.getSource() == this.Beliminar) {
            this.Eliminar();
        } else if (e.getSource() == this.Bsalir) {
            if (this.Bsalir.getText().compareTo("Cancelar") == 0) {
                this.Volveralinicio();
            } else {
                this.dispose(); 
                System.exit(0); 
            }
        }
    }

    public static void main(String[] args) {
        new Practica03_b();
    }
}