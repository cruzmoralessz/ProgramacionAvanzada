package modelo;

public class Salchichoneria extends Producto {
	public Salchichoneria(String id, String nombre, String descripcion, double precioCompra, 
			double precioVenta, int stock, boolean activo, String rutaImagen) {
		super(id, nombre, descripcion, "Salchichonería", precioCompra, precioVenta, stock, activo, rutaImagen);
	}
}