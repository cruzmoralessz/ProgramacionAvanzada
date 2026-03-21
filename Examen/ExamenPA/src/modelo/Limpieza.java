package modelo;

public class Limpieza extends Producto {
	public Limpieza(String id, String nombre, String descripcion, double precioCompra, 
			double precioVenta, int stock, boolean activo, String rutaImagen) {
		super(id, nombre, descripcion, "Limpieza del Hogar", precioCompra, precioVenta, stock, activo, rutaImagen);
	}
}