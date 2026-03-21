package modelo;

public class Mascota extends Producto {
	public Mascota(String id, String nombre, String descripcion, double precioCompra, 
			double precioVenta, int stock, boolean activo, String rutaImagen) {
		super(id, nombre, descripcion, "Mascotas", precioCompra, precioVenta, stock, activo, rutaImagen);
	}
}