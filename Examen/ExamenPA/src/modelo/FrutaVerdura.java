package modelo;

public class FrutaVerdura extends Producto {
	public FrutaVerdura(String id, String nombre, String descripcion, double precioCompra, 
			double precioVenta, int stock, boolean activo, String rutaImagen) {
		super(id, nombre, descripcion, "Frutas y Verduras", precioCompra, precioVenta, stock, activo, rutaImagen);
	}
}