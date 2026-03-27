package modelo;

import java.util.ArrayList;
import java.util.Iterator;

public class GestionProductos {
    private ArrayList<Producto> listaProductos;

    public GestionProductos() {
        this.listaProductos = new ArrayList<>();
    }

    public void setListaProductos(ArrayList<Producto> lista) {
        this.listaProductos = lista;
    }

    public ArrayList<Producto> listar() {
        return listaProductos;
    }

    public boolean existe(String id) {
        for (Producto p : listaProductos) {
            if (p.getId().equals(id)) return true;
        }
        return false;
    }

    public void insertar(Producto p) {
        if (!existe(p.getId())) {
            listaProductos.add(p);
        }
    }

    public Producto buscar(String id) {
        for (Producto p : listaProductos) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public void actualizar(Producto pActualizado) {
        for (int i = 0; i < listaProductos.size(); i++) {
            if (listaProductos.get(i).getId().equals(pActualizado.getId())) {
                listaProductos.set(i, pActualizado);
                break;
            }
        }
    }

    public void eliminar(String id) {
        Iterator<Producto> iter = listaProductos.iterator();
        while (iter.hasNext()) {
            Producto p = iter.next();
            if (p.getId().equals(id)) {
                iter.remove();
                break;
            }
        }
    }
}