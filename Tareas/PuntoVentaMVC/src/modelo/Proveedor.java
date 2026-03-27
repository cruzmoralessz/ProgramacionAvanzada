package modelo;

public class Proveedor {
    private String id;
    private String nombre;
    private String rtn;
    private String telefono;
    private String correo;
    private String direccion;
    private String terminosPago;
    private double limiteCredito;
    private boolean activo;

    public Proveedor(String id, String nombre, String rtn, String telefono, String correo, String direccion, String terminosPago, double limiteCredito, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.rtn = rtn;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.terminosPago = terminosPago;
        this.limiteCredito = limiteCredito;
        this.activo = activo;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRtn() { return rtn; }
    public void setRtn(String rtn) { this.rtn = rtn; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTerminosPago() { return terminosPago; }
    public void setTerminosPago(String terminosPago) { this.terminosPago = terminosPago; }

    public double getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(double limiteCredito) { this.limiteCredito = limiteCredito; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}