package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import dao.interfaces.MetodosDao;
import entidades.Categoria;
import main.Acceso;

public class CategoriaDAO implements MetodosDao<Categoria> {

    private final List<Categoria> lista;
    private Metodos<Categoria> metodos;
    private final String ruta = "categoria.txt";
    private boolean resp;
    private Categoria categ;

    public CategoriaDAO() {
        lista = new ArrayList<>();
        metodos = new Metodos<>(lista);
        cargarLista();
    }

    private void cargarLista() {
        Categoria cat; //1, david
        for (String dato : Acceso.cargarArchivo(ruta)) {
            StringTokenizer st = new StringTokenizer(dato, ",");
            cat = new Categoria(Integer.parseInt(st.nextToken()), st.nextToken(), st.nextToken());
            metodos.agregarRegistro(cat);
        }
    }
    
    public int buscaNombre(String nombre) {
        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
            if (metodos.obtenerRegistro(i).getNombre().equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public List listar() {
        List<Categoria> registros = new ArrayList<>();
        try {
            for (String dato : Acceso.cargarArchivo(ruta)) {
                StringTokenizer st = new StringTokenizer(dato, ",");
                categ = new Categoria(Integer.parseInt(st.nextToken()), st.nextToken(), st.nextToken());
                registros.add(categ);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al listar Categorias: " + e.getMessage());
        }
        return registros;
    }

    @Override
    public boolean insertar(Categoria obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new Categoria(obj.getId(), obj.getNombre(), obj.getDescripcion());
            metodos.agregarRegistro(obj);
            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                categ = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(categ.getId() + "," + categ.getNombre()+ "," + categ.getDescripcion()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar Categorias: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public boolean actualizar(Categoria obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new Categoria(obj.getId(), obj.getNombre(), obj.getDescripcion());
            int codigo = buscaCodigo(obj.getId());
            if (codigo == -1) {
                metodos.agregarRegistro(obj);
            } else {
                metodos.modificar(codigo, obj);
            }

            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                categ = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(categ.getId() + "," + categ.getNombre()+ "," + categ.getDescripcion()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar Categoria: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public int buscaCodigo(int codigo) {
        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
            if (codigo == metodos.obtenerRegistro(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Categoria getObjeto(int codigo) {
        Categoria cat = null;
        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
            cat = metodos.obtenerRegistro(i);
            if (cat.getId() == codigo) {
                return cat;
            }
        }
        return cat;
    }

}
