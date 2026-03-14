package controlador;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import dao.ProveedorDAO;
import entidades.Categoria;
import entidades.Producto;
import entidades.Proveedor;

public class ProductoCONTROL {

    private final ProductoDAO DATOS;
    private final CategoriaDAO DATOS_CAT;
    private final ProveedorDAO DATOS_PROV;
    private final Producto obj;
    private DefaultTableModel modeloTabla;

    public ProductoCONTROL() {
        this.DATOS = new ProductoDAO();
        DATOS_CAT = new CategoriaDAO();
        DATOS_PROV = new ProveedorDAO();
        this.obj = new Producto();
    }

    public DefaultTableModel listar() {
        String[] titulos = {"ID", "Nombre", "Precio", "Stock", "Categoria ID", "Categoria", "Proveedor ID", "Proveedor"};
        this.modeloTabla = new DefaultTableModel(null, titulos);

        String[] registro = new String[8];
        List<Producto> lista = DATOS.listar();
        for (Producto item : lista) {
            System.out.println("nombre<: "+ item.getNombre());
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getNombre();
            registro[2] = Double.toString(item.getPrecio());
            registro[3] = Integer.toString(item.getStock());
            registro[4] = Integer.toString(item.getCategoriaId().getId());
            registro[5] = item.getCategoriaId().getNombre();
            registro[6] = Integer.toString(item.getProveedorId().getRut());
            registro[7] = item.getProveedorId().getNombre();
            
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }

    public String insertar(Integer id, String nombre, double precio, int stock, int idCat, int idProv) {
        if (DATOS.buscaCodigo(id) != -1) {
            return "El ID ingresado ya existe";
        } else if (DATOS.buscaNombre(nombre) != -1) {
            return "El nombre ingresado ya existe";

        } else {
            obj.setId(id);
            obj.setNombre(nombre);
            obj.setPrecio(precio);
            obj.setStock(stock);
            obj.setCategoriaId(new Categoria(idCat));
            obj.setProveedorId(new Proveedor(idProv));

            if (DATOS.insertar(obj)) {
                return "OK";
            } else {
                return "Error en el Registro";
            }
        }
    }

    public String actualizar(Integer id, String nombre, String nombreAnterior, double precio, int stock, int idCat, int idProv) {

        if (nombre.equals(nombreAnterior)) {
            obj.setId(id);
            obj.setNombre(nombre);
            obj.setPrecio(precio);
            obj.setStock(stock);
            obj.setCategoriaId(new Categoria(idCat));
            obj.setProveedorId(new Proveedor(idProv));

            if (DATOS.actualizar(obj)) {
                return "OK";
            } else {
                return "Error en el Registro";
            }

        } else {
            if (DATOS.buscaNombre(nombre) != -1) {
                return "El nombre ingresado ya existe";
            } else {
                obj.setId(id);
                obj.setNombre(nombre);
                obj.setPrecio(precio);
                obj.setStock(stock);
                obj.setCategoriaId(new Categoria(idCat));
                obj.setProveedorId(new Proveedor(idProv));

                if (DATOS.actualizar(obj)) {
                    return "OK";
                } else {
                    return "Error en el Registro";
                }
            }
        }
    }

    public DefaultComboBoxModel seleccionarCategoria() {
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        List<Categoria> lista = new ArrayList<>();
        lista = DATOS_CAT.listar();
        for (Categoria item : lista) {
            items.addElement(new Categoria(item.getId(), item.getNombre()));
        }
        return items;
    }

    public DefaultComboBoxModel seleccionarProveedor() {
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        List<Proveedor> lista = new ArrayList<>();
        lista = DATOS_PROV.listar();
        for (Proveedor item : lista) {
            items.addElement(new Proveedor(item.getRut(), item.getNombre()));
        }
        return items;
    }


}
