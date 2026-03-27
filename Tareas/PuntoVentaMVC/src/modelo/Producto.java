package modelo;

public abstract class Producto {
	private String id;
	private String nombre;
	private String descripcion;
	private String categoria;
	private double precioCompra;
	private double porcentajeGanancia;
	private double precioVenta;
	private int cantidadAlmacen;
	private String unidadMedida;
	private boolean activo;
	private String rutaImagen; 

	public Producto(String id, String nombre, String descripcion, String categoria, double precioCompra,
			double porcentajeGanancia, int cantidadAlmacen, String unidadMedida, boolean activo, String rutaImagen) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.precioCompra = precioCompra;
		this.porcentajeGanancia = porcentajeGanancia;
		this.cantidadAlmacen = cantidadAlmacen;
		this.unidadMedida = unidadMedida;
		this.activo = activo;
		this.rutaImagen = rutaImagen;
		this.precioVenta = calcularPrecioVenta();
	}

	private double calcularPrecioVenta() {
		return this.precioCompra + (this.precioCompra * (this.porcentajeGanancia / 100));
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
		this.precioVenta = calcularPrecioVenta();
	}

	public double getPorcentajeGanancia() {
		return porcentajeGanancia;
	}

	public void setPorcentajeGanancia(double porcentajeGanancia) {
		this.porcentajeGanancia = porcentajeGanancia;
		this.precioVenta = calcularPrecioVenta();
	}

	public double getPrecioVenta() {
		return precioVenta;
	}

	public int getCantidadAlmacen() {
		return cantidadAlmacen;
	}

	public void setCantidadAlmacen(int cantidadAlmacen) {
		this.cantidadAlmacen = cantidadAlmacen;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}
	
	@Override
	public String toString() {
		String nomLimpio = nombre != null ? nombre.replace(",", " ") : "";
		String descLimpia = descripcion != null ? descripcion.replace(",", " ") : "";
		
		return id + "," + nomLimpio + "," + descLimpia + "," + categoria + "," + 
               precioCompra + "," + porcentajeGanancia + "," + precioVenta + "," + 
               cantidadAlmacen + "," + unidadMedida + "," + activo + "," + rutaImagen;
	}
}