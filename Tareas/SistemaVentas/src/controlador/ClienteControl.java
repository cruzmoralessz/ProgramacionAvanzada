package controlador;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

import dao.ClienteDAO;
import entidades.Cliente;

public class ClienteControl {

    private final ClienteDAO DATOS;
    private final Cliente obj;
    private DefaultTableModel modeloTabla;

    public ClienteControl() {
        this.DATOS = new ClienteDAO();
        this.obj = new Cliente();
    }

    public DefaultTableModel listar() {
        String[] titulos = {"RUT", "Nombre"};
        this.modeloTabla = new DefaultTableModel(null, titulos);

        String[] registro = new String[2];
        List<Cliente> lista = DATOS.listar();
        for (Cliente item : lista) {
            registro[0] = Integer.toString(item.getRut());
            registro[1] = item.getNombre();
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }

    public String insertar(Integer rut, String nombre) {
        if (DATOS.buscaCodigo(rut) != -1) {
            return "El rut ingresado ya existe";
        } else if (DATOS.buscaNombre(nombre) != -1) {
            return "El nombre ingresado ya existe";

        } else {
            obj.setRut(rut);
            obj.setNombre(nombre);

            if (DATOS.insertar(obj)) {
                return "OK";
            } else {
                return "Error en el Registro";
            }
        }
    }

    public String actualizar(Integer rut, String nombre, String nombreAnterior) {

        if (nombre.equals(nombreAnterior)) {
            obj.setRut(rut);
            obj.setNombre(nombre);

            if (DATOS.actualizar(obj)) {
                return "OK";
            } else {
                return "Error en el Registro";
            }

        } else {
            if (DATOS.buscaNombre(nombre) != -1) {
                return "El nombre ingresado ya existe";
            } else {
                obj.setRut(rut);
                obj.setNombre(nombre);

                if (DATOS.actualizar(obj)) {
                    return "OK";
                } else {
                    return "Error en el Registro";
                }
            }
        }
    }

    public DefaultComboBoxModel seleccionar() {
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        List<Cliente> lista = new ArrayList<>();
        lista = DATOS.listar();
        for (Cliente item : lista) {
            items.addElement(new Cliente(item.getRut(), item.getNombre()));
        }
        return items;
    }

}
