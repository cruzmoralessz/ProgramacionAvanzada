package modelo;

public class BebidaLiquido extends Producto {
	public BebidaLiquido(String id, String nombre, String descripcion, double precioCompra,
			double porcentajeGanancia, int cantidadAlmacen, String unidadMedida, boolean activo) {
		
		super(id, nombre, descripcion, "Bebidas y Líquidos", precioCompra, porcentajeGanancia, 
			  cantidadAlmacen, unidadMedida, activo, "imagenes/" + id + ".jpg");
	}
}