package modelo;

public class FrutaVerdura extends Producto {
	public FrutaVerdura(String id, String nombre, String descripcion, double precioCompra,
			double porcentajeGanancia, int cantidadAlmacen, String unidadMedida, boolean activo) {
		
		super(id, nombre, descripcion, "Frutas y Verduras", precioCompra, porcentajeGanancia, 
			  cantidadAlmacen, unidadMedida, activo, "imagenes/" + id + ".jpg");
	}
}