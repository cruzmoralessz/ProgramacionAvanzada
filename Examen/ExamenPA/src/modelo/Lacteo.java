package modelo;

public class Lacteo extends Producto {
	public Lacteo(String id, String nombre, String descripcion, double precioCompra, 
			double precioVenta, int stock, boolean activo, String rutaImagen) {
		super(id, nombre, descripcion, "Lácteos y Huevo", precioCompra, precioVenta, stock, activo, rutaImagen);
	}
}