package modelo;

public class Panaderia extends Producto {
	public Panaderia(String id, String nombre, String descripcion, double precioCompra, 
			double precioVenta, int stock, boolean activo, String rutaImagen) {
		super(id, nombre, descripcion, "Panadería y Tortillería", precioCompra, precioVenta, stock, activo, rutaImagen);
	}
}