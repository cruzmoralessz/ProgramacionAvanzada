package controlador;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import modelo.Evaluacion;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestorJSON {
	private final String RUTA_JSON = "evaluaciones.json";
	private Gson gson;

	public GestorJSON() {

		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	public void guardarEvaluacion(Evaluacion nuevaEval) {
		List<Evaluacion> lista = leerTodasLasEvaluaciones();

		// si ya existe sobrescribirla
		boolean existe = false;
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getId().equals(nuevaEval.getId())) {
				lista.set(i, nuevaEval);
				existe = true;
				break;
			}
		}

		// si nno existe se agrega
		if (!existe) {
			lista.add(nuevaEval);
		}

		try (Writer writer = new FileWriter(RUTA_JSON)) {
			gson.toJson(lista, writer);
		} catch (IOException e) {
			System.err.println("ERROR al guardar el JSON: " + e.getMessage());
		}
	}

	public List<Evaluacion> leerTodasLasEvaluaciones() {
		try (Reader reader = new FileReader(RUTA_JSON)) {
			Type tipoLista = new TypeToken<ArrayList<Evaluacion>>() {
			}.getType();
			List<Evaluacion> lista = gson.fromJson(reader, tipoLista);
			return lista != null ? lista : new ArrayList<>();
		} catch (FileNotFoundException e) {
			// si no existe
			return new ArrayList<>();
		} catch (IOException e) {
			System.err.println("ERROR al leer el JSON: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	public boolean eliminarEvaluacion(String idBuscado) {
		List<Evaluacion> lista = leerTodasLasEvaluaciones();
		boolean removido = lista.removeIf(e -> e.getId().equals(idBuscado));

		if (removido) {
			try (Writer writer = new FileWriter(RUTA_JSON)) {
				gson.toJson(lista, writer);
			} catch (IOException e) {
				System.err.println("ERROR al actualizar el JSON");
			}
		}
		return removido;
	}
}