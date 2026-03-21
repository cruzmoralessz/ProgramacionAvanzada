package modelo;

public class CarnePescado extends Producto {
	public CarnePescado(String id, String nombre, String descripcion, double precioCompra, 
			double precioVenta, int stock, boolean activo, String rutaImagen) {
		super(id, nombre, descripcion, "Carnes y Pescados", precioCompra, precioVenta, stock, activo, rutaImagen);
	}
}