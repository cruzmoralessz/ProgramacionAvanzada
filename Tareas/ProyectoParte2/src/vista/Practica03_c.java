package vista;

import controlador.AppS;
import controlador.CategoriaC;

public class Practica03_c extends CategoriaIF {
    public Practica03_c(AppS service) {
        super();
        new CategoriaC(this, service);
    }
}
