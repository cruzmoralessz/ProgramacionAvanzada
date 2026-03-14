package entidades;

public class DireccionProveedor {

    private Proveedor proveedor;
    private String calle;
    private String numero;
    private String ciudad;
    private String comuna;

    public DireccionProveedor() {
    }

    public DireccionProveedor(Proveedor proveedor, String calle, String numero, String ciudad, String comuna) {
        this.proveedor = proveedor;
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.comuna = comuna;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

}
