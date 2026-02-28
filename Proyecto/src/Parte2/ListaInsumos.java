package Parte2;

import java.util.ArrayList;

public class ListaInsumos {
    
    private ArrayList<Insumo> insumos;

    public ListaInsumos() {
        this.insumos = new ArrayList<>();
    }

    public boolean agregarInsumo(Insumo insumo) {
        boolean inserto = true;
        if (buscarInsumoPorId(insumo.getIdProducto()) == null) {
            insumos.add(insumo);
        } else {
            inserto = false;
        }
        return inserto;
    }

    public boolean eliminarInsumoPorId(String id) {
        boolean elimino = true;
        Insumo insumo = buscarInsumoPorId(id);
        if (insumo != null) {
            insumos.remove(insumo);
        } else {
            elimino = false;
        }
        return elimino;
    }

    @Override
    public String toString() {
        String resultado = "";
        for (Insumo insumo : insumos) {
            resultado += insumo.toString() + "\n";
        }
        return resultado;
    }

    private Insumo buscarInsumoPorId(String id) {
        for (Insumo insumo : insumos) {
            if (insumo.getIdProducto().equals(id)) {
                return insumo;
            }
        }
        return null;
    }

    public String buscarInsumo(String id) {
        for (Insumo insumo : insumos) {
            if (insumo.getIdProducto().equals(id)) {
                return insumo.getProducto();
            }
        }
        return null;
    }
    
    public void cargarInsumo(ArrayList<String[]> insumosString) {
        for (String[] insumoString : insumosString) {
            String id = insumoString[0];
            String insumo = insumoString[1];
            String idCategoria = insumoString[2];
            Insumo nodo = new Insumo(id, insumo, idCategoria);
            this.agregarInsumo(nodo);
        }
    }
    
    public void cargarInsumos(ArrayList<String[]> insumosString) {
        for (String[] insumoString : insumosString) {
            String idProducto = insumoString[0];
            String producto = insumoString[1];
            String idCategoria = insumoString[2]; 
            
            Insumo insumo = new Insumo(idProducto, producto, idCategoria);
            
            this.agregarInsumo(insumo);
        }
    }

    public String[] idinsumos() {
        int pos = -1;
        String[] datos = new String[this.insumos.size()]; 
        
        for (Insumo nodo : this.insumos) {
            pos++;
            datos[pos] = nodo.getIdProducto();
        }
        
        return datos;
    }
}