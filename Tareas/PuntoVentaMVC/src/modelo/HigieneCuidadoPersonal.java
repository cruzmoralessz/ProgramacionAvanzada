package modelo;

public class HigieneCuidadoPersonal extends Producto {
	public HigieneCuidadoPersonal(String id, String nombre, String descripcion, double precioCompra,
			double porcentajeGanancia, int cantidadAlmacen, String unidadMedida, boolean activo) {
		
		super(id, nombre, descripcion, "Higiene y Cuidado Personal", precioCompra, porcentajeGanancia, 
			  cantidadAlmacen, unidadMedida, activo, "imagenes/" + id + ".jpg");
	}
}