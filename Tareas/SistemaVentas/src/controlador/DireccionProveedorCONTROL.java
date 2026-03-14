package controlador;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

import dao.DirecionProveedorDAO;
import dao.ProveedorDAO;
import entidades.DireccionProveedor;
import entidades.Proveedor;

public class DireccionProveedorCONTROL {

    private final DirecionProveedorDAO DATOS;
    private final ProveedorDAO DATOSPROV;
    private final DireccionProveedor obj;
    private DefaultTableModel modeloTabla;

    public DireccionProveedorCONTROL() {
        this.DATOS = new DirecionProveedorDAO();
        DATOSPROV = new ProveedorDAO();
        this.obj = new DireccionProveedor();
    }

    public DefaultTableModel listar() {
        String[] titulos = {"RUT", "Proveedor", "Calle", "Numero", "Ciudad", "Comuna"};
        this.modeloTabla = new DefaultTableModel(null, titulos);

        String[] registro = new String[6];
        List<DireccionProveedor> lista = DATOS.listar();
        for (DireccionProveedor item : lista) {
            registro[0] = Integer.toString(item.getProveedor().getRut());
            registro[1] = item.getProveedor().getNombre();
            registro[2] = item.getCalle();
            registro[3] = item.getNumero();
            registro[4] = item.getCiudad();
            registro[5] = item.getComuna();
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }

    public String insertar(Integer rut, String calle, String numero, String ciudad, String comuna) {
        if (DATOS.buscaCodigo(rut) != -1) {
            return "Este proveedor ya tiene una direccion agregado, maximo una direccion por cada proveedor";
        } else {
            obj.setProveedor(new Proveedor(rut));
            obj.setCalle(calle);
            obj.setNumero(numero);
            obj.setCiudad(ciudad);
            obj.setComuna(comuna);

            if (DATOS.insertar(obj)) {
                return "OK";
            } else {
                return "Error en el Registro";
            }
        }
    }

    public String actualizar(Integer rut, int rutAnterior, String calle, String numero, String ciudad, String comuna) {

        if (rut.equals(rutAnterior)) {
            obj.setProveedor(new Proveedor(rut));
            obj.setCalle(calle);
            obj.setNumero(numero);
            obj.setCiudad(ciudad);
            obj.setComuna(comuna);

            if (DATOS.actualizar(obj)) {
                return "OK";
            } else {
                return "Error en la actualizacion";
            }
        } else {
            if (DATOS.buscaCodigo(rut) != -1) {
                obj.setProveedor(new Proveedor(rut));
                obj.setCalle(calle);
                obj.setNumero(numero);
                obj.setCiudad(ciudad);
                obj.setComuna(comuna);

                if (DATOS.actualizar(obj)) {
                    return "OK";
                } else {
                    return "Error en la actualizacion";
                }

            } else {
                return "Cliente a actualizar no existe!";
            }
        }

    }

    public DefaultComboBoxModel seleccionar() {
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        List<Proveedor> lista = new ArrayList<>();
        lista = DATOSPROV.listar();
        for (Proveedor item : lista) {
            items.addElement(new Proveedor(item.getRut(), item.getNombre()));
        }
        return items;
    }

}
