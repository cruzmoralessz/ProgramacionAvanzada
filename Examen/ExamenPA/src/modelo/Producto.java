package modelo;

public class Producto {
	private String id;
	private String nombre;
	private String descripcion;
	private String categoria;
	private double precioCompra;
	private double precioVenta;
	private int stock;
	private boolean activo;

	public Producto(String id, String nombre, String descripcion, String categoria, double precioCompra,
			double precioVenta, int stock, boolean activo) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.precioCompra = precioCompra;
		this.precioVenta = precioVenta;
		this.stock = stock;
		this.activo = activo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	@Override
    public String toString() {
		//por si algun smart pone comas en la descripcion o nombre
        String nomLimpio = nombre != null ? nombre.replace(",", " ") : "";
        String descLimpia = descripcion != null ? descripcion.replace(",", " ") : "";
        
        return id + "," + nomLimpio + "," + descLimpia + "," + categoria + "," + precioCompra + "," + precioVenta + "," + stock + "," + activo;
    }
}