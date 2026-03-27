package modelo;

import java.util.ArrayList;

public class GestionProveedores {
    private ArrayList<Proveedor> listaProveedores;

    public GestionProveedores() {
        this.listaProveedores = new ArrayList<>();
    }

    public void insertar(Proveedor p) {
        listaProveedores.add(p);
    }

    public void actualizar(Proveedor p) {
        for (int i = 0; i < listaProveedores.size(); i++) {
            if (listaProveedores.get(i).getId().equals(p.getId())) {
                listaProveedores.set(i, p);
                return;
            }
        }
    }

    public void eliminar(String id) {
        listaProveedores.removeIf(p -> p.getId().equals(id));
    }

    public Proveedor buscar(String id) {
        for (Proveedor p : listaProveedores) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public boolean existe(String id) {
        return buscar(id) != null;
    }

    public ArrayList<Proveedor> listar() {
        return listaProveedores;
    }
}