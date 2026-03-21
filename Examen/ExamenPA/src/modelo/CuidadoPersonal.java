package modelo;

public class CuidadoPersonal extends Producto {
	public CuidadoPersonal(String id, String nombre, String descripcion, double precioCompra, 
			double precioVenta, int stock, boolean activo, String rutaImagen) {
		super(id, nombre, descripcion, "Cuidado Personal", precioCompra, precioVenta, stock, activo, rutaImagen);
	}
}