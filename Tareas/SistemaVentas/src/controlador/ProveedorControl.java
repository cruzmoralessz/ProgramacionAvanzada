package controlador;

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

import dao.ProveedorDAO;
import entidades.Proveedor;

public class ProveedorControl {

    private final ProveedorDAO DATOS;
    private final Proveedor obj;
    private DefaultTableModel modeloTabla;

    public ProveedorControl() {
        this.DATOS = new ProveedorDAO();
        this.obj = new Proveedor();
    }

    public DefaultTableModel listar() {
        String[] titulos = {"RUT", "Nombre", "Telefono", "Pagina Web"};
        this.modeloTabla = new DefaultTableModel(null, titulos);

        String[] registro = new String[4];
        List<Proveedor> lista = DATOS.listar();
        for (Proveedor item : lista) {
            registro[0] = Integer.toString(item.getRut());
            registro[1] = item.getNombre();
            registro[2] = item.getTelefono();
            registro[3] = item.getPaginaWeb();
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }

    public String insertar(Integer rut, String nombre, String telefono, String paginaWeb) {
        if (DATOS.buscaCodigo(rut) != -1) {
            return "El rut ingresado ya existe";
        } else if (DATOS.buscaNombre(nombre) != -1) {
            return "El nombre ingresado ya existe";

        } else {
            obj.setRut(rut);
            obj.setNombre(nombre);
            obj.setTelefono(telefono);
            obj.setPaginaWeb(paginaWeb);

            if (DATOS.insertar(obj)) {
                return "OK";
            } else {
                return "Error en el Registro";
            }
        }
    }

    public String actualizar(Integer rut, String nombre, String nombreAnterior, String telefono, String paginaWeb) {

        if (nombre.equals(nombreAnterior)) {
            obj.setRut(rut);
            obj.setNombre(nombre);
            obj.setTelefono(telefono);
            obj.setPaginaWeb(paginaWeb);

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
                obj.setTelefono(telefono);
                obj.setPaginaWeb(paginaWeb);

                if (DATOS.actualizar(obj)) {
                    return "OK";
                } else {
                    return "Error en el Registro";
                }
            }
        }
    }

	public DefaultComboBoxModel<Object> seleccionar() {
		// TODO Auto-generated method stub
		return null;
	}

}
