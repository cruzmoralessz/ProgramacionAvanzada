package dao.interfaces;

import java.util.List;

public interface MetodosDao<T> {

    public List<T> listar();

    public boolean insertar(T obj);

    public boolean actualizar(T obj);

    public int buscaCodigo(int codigo);

    public T getObjeto(int codigo);

}
