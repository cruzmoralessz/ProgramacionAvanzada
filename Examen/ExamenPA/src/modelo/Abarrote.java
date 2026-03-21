package modelo;

public class Abarrote extends Producto {
	public Abarrote(String id, String nombre, String descripcion, double precioCompra, 
			double precioVenta, int stock, boolean activo, String rutaImagen) {
		super(id, nombre, descripcion, "Abarrotes", precioCompra, precioVenta, stock, activo, rutaImagen);
	}
}