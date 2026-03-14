package controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

import dao.ClienteDAO;
import dao.DetalleVentaDAO;
import dao.ProductoDAO;
import dao.VentaDAO;
import entidades.Cliente;
import entidades.DetalleVenta;
import entidades.Producto;
import entidades.Venta;

public class VentaCONTROL {

    private final VentaDAO DATOS;
    private final ClienteDAO DATOSCLI;
    private final ProductoDAO DATOSPROD;
    private final DetalleVentaDAO DATOS_DET;
    private final Venta obj;
    private DefaultTableModel modeloTabla;

    public VentaCONTROL() {
        this.DATOS = new VentaDAO();
        DATOSCLI = new ClienteDAO();
        DATOSPROD = new ProductoDAO();
        DATOS_DET = new DetalleVentaDAO();
        this.obj = new Venta();
    }

    public DefaultTableModel listar() {
        String[] titulos = {"ID", "FECHA", "DESCUENTO", "ID CLIENTE", "CLIENTE", "TOTAL", "ESTADO"};
        this.modeloTabla = new DefaultTableModel(null, titulos);

        String[] registro = new String[7];
        List<Venta> lista = DATOS.listar();
        for (Venta item : lista) {
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getFecha();
            registro[2] = Double.toString(item.getDescuento());
            registro[3] = Integer.toString(item.getClienteId().getRut());
            registro[4] = item.getClienteId().getNombre();
            registro[5] = Double.toString(item.getTotal());
            if (item.isEstado()) {
                registro[6] = "Activo";
            } else {
                registro[6] = "Anulado";
            }
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }

    public DefaultTableModel listarDetalle(int idVenta) {
        String[] titulos = {"ID PRODUCTO", "PRODUCTO", "STOCK", "CANTIDAD", "PRECIO", "SUBTOTAL"};
        this.modeloTabla = new DefaultTableModel(null, titulos);

        String[] registro = new String[6];
        List<DetalleVenta> lista = DATOS_DET.listarDetalles(idVenta);
        for (DetalleVenta item : lista) {
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getProductoId().getNombre();
            registro[2] = Integer.toString(item.getProductoId().getStock());
            registro[3] = Integer.toString(item.getCantidad());
            registro[4] = Double.toString(item.getPrecio());
            registro[5] = Double.toString(item.getCantidad() * item.getPrecio());

            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }

    public String insertar(Integer id, String fecha, double descuento, int idCliente, double total, DefaultTableModel modeloDetalles) {
        if (DATOS.buscaCodigo(id) != -1) {
            return "El ID ingresado ya existe";
        } else {
            obj.setId(id);
            obj.setFecha(fecha);
            obj.setDescuento(descuento);
            obj.setClienteId(new Cliente(idCliente));
            obj.setTotal(total - descuento);
            obj.setEstado(true);

            List<DetalleVenta> listaDetalles = new ArrayList<>();
            int idProducto;
            int cantidad;
            double precio;

            for (int i = 0; i < modeloDetalles.getRowCount(); i++) {
                idProducto = Integer.parseInt(String.valueOf(modeloDetalles.getValueAt(i, 0)));
                cantidad = Integer.parseInt(String.valueOf(modeloDetalles.getValueAt(i, 3)));
                precio = Double.parseDouble(String.valueOf(modeloDetalles.getValueAt(i, 4)));
                listaDetalles.add(new DetalleVenta(claveGenerar(), precio, cantidad, id, new Producto(idProducto)));
            }

            obj.setDetalles(listaDetalles);

            if (DATOS.insertar(obj)) {
                return "OK";
            } else {
                return "Error en el Registro";
            }
        }
    }

    private int claveGenerar() {
        int min = 1000;
        int max = 9999;
        Random randon = new Random();
        int value = randon.nextInt(max + min) + min;
        return value;
    }

    public String anular(Integer id, String fecha, double descuento, int idCliente, double total) {
        obj.setId(id);
        obj.setFecha(fecha);
        obj.setDescuento(descuento);
        obj.setClienteId(new Cliente(idCliente));
        obj.setTotal(total - descuento);
        obj.setEstado(false);
        if (DATOS.anular(obj)) {
            return "OK";
        } else {
            return "Error al anular Venta";
        }

    }

    public DefaultComboBoxModel seleccionarCliente() {
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        List<Cliente> lista = new ArrayList<>();
        lista = DATOSCLI.seleccionar();
        for (Cliente item : lista) {
            items.addElement(new Cliente(item.getRut(), item.getNombre()));
        }
        return items;
    }

    public DefaultComboBoxModel seleccionarProducto() {
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        List<Producto> lista = new ArrayList<>();
        lista = DATOSPROD.seleccionar();
        for (Producto item : lista) {
            items.addElement(new Producto(item.getId(), item.getNombre(), item.getPrecio(), item.getStock()));
        }
        return items;
    }

}
