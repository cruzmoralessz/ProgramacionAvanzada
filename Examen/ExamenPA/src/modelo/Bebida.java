package modelo;

public class Bebida extends Producto {
	public Bebida(String id, String nombre, String descripcion, double precioCompra, 
			double precioVenta, int stock, boolean activo, String rutaImagen) {
		super(id, nombre, descripcion, "Bebidas", precioCompra, precioVenta, stock, activo, rutaImagen);
	}
}