package entidades;

import java.util.Objects;

public class Proveedor {

    private int rut;
    private String nombre;
    private String telefono;
    private String paginaWeb;

    public Proveedor() {
    }

    public Proveedor(int rut) {
        this.rut = rut;
    }

    public Proveedor(int rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
    }

    public Proveedor(int rut, String nombre, String telefono, String paginaWeb) {
        this.rut = rut;
        this.nombre = nombre;
        this.telefono = telefono;
        this.paginaWeb = paginaWeb;
    }

    public int getRut() {
        return rut;
    }

    public void setRut(int rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.rut;
        hash = 47 * hash + Objects.hashCode(this.nombre);
        hash = 47 * hash + Objects.hashCode(this.telefono);
        hash = 47 * hash + Objects.hashCode(this.paginaWeb);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Proveedor other = (Proveedor) obj;
        if (this.rut != other.rut) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.telefono, other.telefono)) {
            return false;
        }
        if (!Objects.equals(this.paginaWeb, other.paginaWeb)) {
            return false;
        }
        return true;
    }

}
