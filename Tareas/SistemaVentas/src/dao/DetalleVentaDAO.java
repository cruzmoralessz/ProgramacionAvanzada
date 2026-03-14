package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import entidades.DetalleVenta;
import entidades.Producto;
import main.Acceso;

public class DetalleVentaDAO {

    private final List<DetalleVenta> listaDetalles;
    private Metodos<DetalleVenta> metodoDetalle;
    private final String ruta = "detalleventa.txt";
    private boolean resp;
    private DetalleVenta detalles;
    private final ProductoDAO DATOS_PROD;

    public DetalleVentaDAO() {
        listaDetalles = new ArrayList<>();
        metodoDetalle = new Metodos<>(listaDetalles);
        DATOS_PROD = new ProductoDAO();
        detalles = new DetalleVenta();
        cargarLista();
    }

    private void cargarLista() {
        DetalleVenta detalleVenta; //1, david
        for (String dato : Acceso.cargarArchivo(ruta)) {
            StringTokenizer st = new StringTokenizer(dato, ",");
            detalleVenta = new DetalleVenta();

            detalleVenta.setId(Integer.parseInt(st.nextToken()));
            detalleVenta.setPrecio(Double.parseDouble(st.nextToken()));
            detalleVenta.setCantidad(Integer.parseInt(st.nextToken()));
            detalleVenta.setIdVenta(Integer.parseInt(st.nextToken()));

            int idProducto = Integer.parseInt(st.nextToken());
            Producto producto = DATOS_PROD.getObjeto(idProducto);
            detalleVenta.setProductoId(producto);

            metodoDetalle.agregarRegistro(detalleVenta);
        }
    }

    public List listarDetalles(int idVenta) {
        List<DetalleVenta> registros = new ArrayList<>();
        DetalleVenta detalleVenta;
        try {
            for (String dato : Acceso.cargarArchivo(ruta)) {
                StringTokenizer st = new StringTokenizer(dato, ",");

                detalleVenta = new DetalleVenta();

                detalleVenta.setId(Integer.parseInt(st.nextToken()));
                detalleVenta.setPrecio(Double.parseDouble(st.nextToken()));
                detalleVenta.setCantidad(Integer.parseInt(st.nextToken()));

                int idV = Integer.parseInt(st.nextToken());
                detalleVenta.setIdVenta(idV);;

                int idProducto = Integer.parseInt(st.nextToken());
                Producto producto = DATOS_PROD.getObjeto(idProducto);
                detalleVenta.setProductoId(producto);

                if (idV == idVenta) {
                    registros.add(detalleVenta);
                }

            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al listar DetalleVentas: " + e.getMessage());
        }
        return registros;
    }

    public boolean insertar(DetalleVenta obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new DetalleVenta(obj.getId(), obj.getPrecio(), obj.getCantidad(), obj.getIdVenta(), obj.getProductoId());
            metodoDetalle.agregarRegistro(obj);
            for (int i = 0; i < metodoDetalle.cantidadRegistro(); i++) {
                detalles = metodoDetalle.obtenerRegistro(i);
                pw.println(String.valueOf(detalles.getId() + "," + detalles.getPrecio() + "," + detalles.getCantidad() + "," + detalles.getIdVenta()
                        + "," + detalles.getProductoId().getId()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar DetalleVentas: " + e.getMessage());
        }
        return resp;
    }

}
