package entidades;

public class DetalleVenta {

    private int id;
    private double precio;
    private int cantidad;
    private int idVenta;
    private Producto productoId;

    public DetalleVenta() {
    }

    public DetalleVenta(int id, double precio, int cantidad, int idVenta, Producto productoId) {
        this.id = id;
        this.precio = precio;
        this.cantidad = cantidad;
        this.idVenta = idVenta;
        this.productoId = productoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Producto getProductoId() {
        return productoId;
    }

    public void setProductoId(Producto productoId) {
        this.productoId = productoId;
    }

}
