package modelo;

public class BotanaDulce extends Producto {
	public BotanaDulce(String id, String nombre, String descripcion, double precioCompra,
			double porcentajeGanancia, int cantidadAlmacen, String unidadMedida, boolean activo) {
		
		super(id, nombre, descripcion, "Botanas y Dulces", precioCompra, porcentajeGanancia, 
			  cantidadAlmacen, unidadMedida, activo, "imagenes/" + id + ".jpg");
	}
}