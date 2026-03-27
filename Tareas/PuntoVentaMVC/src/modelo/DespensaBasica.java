package modelo;

public class DespensaBasica extends Producto {
	public DespensaBasica(String id, String nombre, String descripcion, double precioCompra,
			double porcentajeGanancia, int cantidadAlmacen, String unidadMedida, boolean activo) {
		
		super(id, nombre, descripcion, "Despensa Básica", precioCompra, porcentajeGanancia, 
			  cantidadAlmacen, unidadMedida, activo, "imagenes/" + id + ".jpg");
	}
}