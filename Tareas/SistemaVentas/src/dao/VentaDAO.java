/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import dao.interfaces.MetodosDao;
import entidades.Cliente;
import entidades.DetalleVenta;
import entidades.Venta;
import main.Acceso;

public class VentaDAO implements MetodosDao<Venta> {

    private final List<Venta> lista;
    private Metodos<Venta> metodos;
    private final String ruta = "venta.txt";
    private boolean resp;
    private Venta venta;
    private final ClienteDAO DATOS;
    private final DetalleVentaDAO DATOS_DET;

    public VentaDAO() {
        lista = new ArrayList<>();
        metodos = new Metodos<>(lista);
        DATOS = new ClienteDAO();
        DATOS_DET = new DetalleVentaDAO();
        cargarLista();
    }

    private void cargarLista() {
        Venta ven; //1, david
        for (String dato : Acceso.cargarArchivo(ruta)) {
            StringTokenizer st = new StringTokenizer(dato, ",");
            ven = new Venta();

            ven.setId(Integer.parseInt(st.nextToken()));
            ven.setFecha(st.nextToken());
            ven.setDescuento(Double.parseDouble(st.nextToken()));

            int idCliente = Integer.parseInt(st.nextToken());
            Cliente cliente = DATOS.getObjeto(idCliente);
            ven.setClienteId(cliente);

            ven.setTotal(Double.parseDouble(st.nextToken()));
            ven.setEstado(Boolean.parseBoolean(st.nextToken()));

            metodos.agregarRegistro(ven);
        }
    }

    @Override
    public List listar() {
        List<Venta> registros = new ArrayList<>();
        try {
            for (String dato : Acceso.cargarArchivo(ruta)) {
                StringTokenizer st = new StringTokenizer(dato, ",");
                venta = new Venta();

                venta.setId(Integer.parseInt(st.nextToken()));
                venta.setFecha(st.nextToken());
                venta.setDescuento(Double.parseDouble(st.nextToken()));

                int idCliente = Integer.parseInt(st.nextToken());
                Cliente cliente = DATOS.getObjeto(idCliente);
                venta.setClienteId(cliente);

                venta.setTotal(Double.parseDouble(st.nextToken()));
                venta.setEstado(Boolean.parseBoolean(st.nextToken()));

                registros.add(venta);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al listar Ventas: " + e.getMessage());
        }
        return registros;
    }

    @Override
    public boolean insertar(Venta obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new Venta(obj.getId(), obj.getFecha(), obj.getDescuento(), obj.getClienteId(), obj.getTotal(), true, obj.getDetalles());
            metodos.agregarRegistro(obj);
            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                venta = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(venta.getId() + "," + venta.getFecha()+ "," + venta.getDescuento()+ "," + 
                        venta.getClienteId().getRut()+ "," + venta.getTotal()+ "," +venta.isEstado()));
            }
            pw.close();
            
            for(DetalleVenta det : obj.getDetalles()){
                DATOS_DET.insertar(det);
            }
            
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar Ventas: " + e.getMessage());
        }
        return resp;
    }
    
    public boolean anular(Venta obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new Venta(obj.getId(), obj.getFecha(), obj.getDescuento(), obj.getClienteId(), obj.getTotal(), false, obj.getDetalles());
            int codigo = buscaCodigo(obj.getId());
            if (codigo == -1) {
                return false;
            } else {
                metodos.modificar(codigo, obj);
            }

            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                venta = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(venta.getId() + "," + venta.getFecha()+ "," + venta.getDescuento()+ "," + 
                        venta.getClienteId().getRut()+ "," + venta.getTotal()+ "," +venta.isEstado()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al anular Ventas: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public boolean actualizar(Venta obj) {
        return false;
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
    public Venta getObjeto(int codigo) {
        Venta vent = null;
        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
            vent = metodos.obtenerRegistro(i);
            if (vent.getId() == codigo) {
                vent = new Venta(vent.getId(), vent.getFecha(), vent.getDescuento(), vent.getClienteId(), vent.getTotal(), vent.isEstado());
                return vent;
            }
        }
        return vent;
    }

}
