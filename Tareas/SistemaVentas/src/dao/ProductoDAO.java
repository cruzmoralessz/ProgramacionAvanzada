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
import entidades.Producto;
import entidades.Proveedor;
import main.Acceso;

public class ProductoDAO implements MetodosDao<Producto> {

    private final List<Producto> lista;
    private Metodos<Producto> metodos;
    private final String ruta = "producto.txt";
    private boolean resp;
    private Producto producto;
    private CategoriaDAO DATOS;
    private ProveedorDAO DATOS_PROV;

    public ProductoDAO() {
        lista = new ArrayList<>();
        metodos = new Metodos<>(lista);
        DATOS = new CategoriaDAO();
        DATOS_PROV = new ProveedorDAO();
        cargarLista();
    }

    private void cargarLista() {
        Producto producto; //1, david
        for (String dato : Acceso.cargarArchivo(ruta)) {
            StringTokenizer st = new StringTokenizer(dato, ",");
            producto = new Producto();

            producto.setId(Integer.parseInt(st.nextToken()));
            producto.setNombre(st.nextToken());
            producto.setPrecio(Double.parseDouble(st.nextToken()));
            producto.setStock(Integer.parseInt(st.nextToken()));

            int idCategoria = Integer.parseInt(st.nextToken());
            int idProveedor = Integer.parseInt(st.nextToken());
            Categoria cat = DATOS.getObjeto(idCategoria);
            Proveedor prov = DATOS_PROV.getObjeto(idProveedor);

            producto.setCategoriaId(cat);
            producto.setProveedorId(prov);

            metodos.agregarRegistro(producto);
        }
    }

    public List seleccionar() {
        List<Producto> registros = new ArrayList<>();
        try {
            for (String dato : Acceso.cargarArchivo(ruta)) {
                StringTokenizer st = new StringTokenizer(dato, ",");
                registros.add(new Producto(Integer.parseInt(st.nextToken()), st.nextToken(), Double.parseDouble(st.nextToken()),
                Integer.parseInt(st.nextToken())));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar Producto!");
        }
        return registros;
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
        List<Producto> registros = new ArrayList<>();
        try {
            for (String dato : Acceso.cargarArchivo(ruta)) {
                StringTokenizer st = new StringTokenizer(dato, ",");
                Producto producto = new Producto();

                producto.setId(Integer.parseInt(st.nextToken()));
                producto.setNombre(st.nextToken());
                producto.setPrecio(Double.parseDouble(st.nextToken()));
                producto.setStock(Integer.parseInt(st.nextToken()));

                int idCategoria = Integer.parseInt(st.nextToken());
                int idProveedor = Integer.parseInt(st.nextToken());
                Categoria cat = DATOS.getObjeto(idCategoria);
                Proveedor prov = DATOS_PROV.getObjeto(idProveedor);

                producto.setCategoriaId(cat);
                producto.setProveedorId(prov);

                registros.add(producto);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al listar Productos: " + e.getMessage());
        }
        return registros;
    }

    @Override
    public boolean insertar(Producto obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new Producto(obj.getId(), obj.getNombre(), obj.getPrecio(), obj.getStock(), obj.getCategoriaId(), obj.getProveedorId());
            metodos.agregarRegistro(obj);
            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                producto = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(producto.getId() + "," + producto.getNombre() + "," + producto.getPrecio() + "," + producto.getStock()
                        + "," + producto.getCategoriaId().getId() + "," + producto.getProveedorId().getRut()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar Productos: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public boolean actualizar(Producto obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new Producto(obj.getId(), obj.getNombre(), obj.getPrecio(), obj.getStock(), obj.getCategoriaId(), obj.getProveedorId());
            int codigo = buscaCodigo(obj.getId());
            if (codigo == -1) {
                metodos.agregarRegistro(obj);
            } else {
                metodos.modificar(codigo, obj);
            }

            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                producto = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(producto.getId() + "," + producto.getNombre() + "," + producto.getPrecio() + "," + producto.getStock()
                        + "," + producto.getCategoriaId().getId() + "," + producto.getProveedorId().getRut()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar Productos: " + e.getMessage());
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
    public Producto getObjeto(int codigo) {
        Producto producto = null;
        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
            producto = metodos.obtenerRegistro(i);
            if (producto.getId() == codigo) {
                return producto;
            }
        }
        return producto;
    }

}
