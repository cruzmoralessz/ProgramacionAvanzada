package controlador;

import modelo.Alumno;
import modelo.Evaluacion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class GestorExcel {

	private final String RUTA_BD = "BaseDeDatos.xlsx";
	private DataFormatter formatter = new DataFormatter();

	public List<String> obtenerAsignaturas() {
		TreeSet<String> set = new TreeSet<>();
		try (FileInputStream fis = new FileInputStream(RUTA_BD); Workbook wb = new XSSFWorkbook(fis)) {
			Sheet s = wb.getSheetAt(0);
			for (Row row : s)
				if (row.getRowNum() > 0)
					set.add(formatter.formatCellValue(row.getCell(2)));
		} catch (Exception ignored) {
		}
		return new ArrayList<>(set);
	}

	public List<String> obtenerProfesores(String asig) {
		TreeSet<String> set = new TreeSet<>();
		try (FileInputStream fis = new FileInputStream(RUTA_BD); Workbook wb = new XSSFWorkbook(fis)) {
			Sheet s = wb.getSheetAt(0);
			for (Row row : s)
				if (row.getRowNum() > 0 && formatter.formatCellValue(row.getCell(2)).equals(asig))
					set.add(formatter.formatCellValue(row.getCell(1)));
		} catch (Exception ignored) {
		}
		return new ArrayList<>(set);
	}

	public List<String> obtenerGrupos(String asig, String prof) {
		TreeSet<String> set = new TreeSet<>();
		try (FileInputStream fis = new FileInputStream(RUTA_BD); Workbook wb = new XSSFWorkbook(fis)) {
			Sheet s = wb.getSheetAt(0);
			for (Row row : s)
				if (row.getRowNum() > 0 && formatter.formatCellValue(row.getCell(2)).equals(asig)
						&& formatter.formatCellValue(row.getCell(1)).equals(prof))
					set.add(formatter.formatCellValue(row.getCell(0)));
		} catch (Exception ignored) {
		}
		return new ArrayList<>(set);
	}

	public List<Alumno> obtenerAlumnos(String asig, String prof, String grp) {
		List<Alumno> lista = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream(RUTA_BD); Workbook wb = new XSSFWorkbook(fis)) {
			Sheet s = wb.getSheetAt(0);
			for (Row row : s) {
				if (row.getRowNum() > 0 && formatter.formatCellValue(row.getCell(2)).equals(asig)
						&& formatter.formatCellValue(row.getCell(1)).equals(prof)
						&& formatter.formatCellValue(row.getCell(0)).equals(grp)) {
					lista.add(new Alumno(formatter.formatCellValue(row.getCell(3)),
							formatter.formatCellValue(row.getCell(4))));
				}
			}
		} catch (Exception ignored) {
		}
		return lista;
	}

	public void generarReporteExcel(Evaluacion eval, String rutaPlantilla, String rutaDestino) {
		try (FileInputStream fis = new FileInputStream(rutaPlantilla); Workbook wb = new XSSFWorkbook(fis)) {

			List<Alumno> alumnos = eval.getEquipos().size() > 6 ? eval.getEquipos().subList(0, 6) : eval.getEquipos();

			// 1er hoja dle excel
			Sheet h0 = wb.getSheetAt(0);
			escribir(h0, 2, 2, eval.getAsignatura());
			escribir(h0, 2, 6, eval.getGrupo().trim());
			escribir(h0, 3, 2, eval.getProfesor());
			escribir(h0, 5, 2, eval.getAtributoEgreso());
			escribir(h0, 6, 2, eval.getCriterioDesempeno());
			escribir(h0, 7, 2, eval.getIndicador());

			int fila = 12;
			double sumaInst = 0;
			for (Evaluacion.CriterioInstrumento c : eval.getCriteriosInstrumento()) {
				escribir(h0, fila, 1, c.getAspecto());
				escribir(h0, fila, 5, String.valueOf(c.getCalificacion()));
				escribir(h0, fila, 6, c.getObservaciones());
				sumaInst += c.getCalificacion();
				fila++;
			}
			escribir(h0, 18, 5, String.valueOf(sumaInst));

			// 2da hoja del excel
			Sheet h1 = wb.getSheetAt(1);

			escribir(h1, 5, 4, eval.getAsignatura());
			escribir(h1, 6, 4, eval.getProfesor());

			escribir(h1, 8, 4, eval.getAtributoEgreso());
			escribir(h1, 9, 4, eval.getCriterioDesempeno());
			escribir(h1, 10, 4, eval.getIndicador());

			fila = 15;
			for (Evaluacion.CriterioRubrica c : eval.getCriteriosRubrica()) {
				escribir(h1, fila, 1, c.getAspecto());
				escribir(h1, fila, 6, c.getExcelente());
				escribir(h1, fila, 8, c.getBueno());
				escribir(h1, fila, 10, c.getRegular());
				escribir(h1, fila, 12, c.getNoAlcanza());
				fila++;
			}

			// 3er hoja del excel
			Sheet h2 = wb.getSheetAt(2);
			escribir(h2, 2, 2, eval.getAsignatura());
			escribir(h2, 2, 6, eval.getGrupo().trim());
			escribir(h2, 3, 2, eval.getProfesor());
			escribir(h2, 6, 2, eval.getAtributoEgreso());
			escribir(h2, 7, 2, eval.getCriterioDesempeno());
			escribir(h2, 8, 2, eval.getIndicador());

			fila = 15;
			double sumaAlum = 0;
			for (Alumno al : alumnos) {
				escribir(h2, fila, 0, al.getNombre());
				escribir(h2, fila, 6, String.valueOf(al.getCalificacionRubrica()));
				sumaAlum += al.getCalificacionRubrica();
				fila++;
			}
			if (!alumnos.isEmpty()) {
				double promedio = sumaAlum / alumnos.size();
				escribir(h2, 21, 6, String.format("%.1f", promedio));
			}

			try (FileOutputStream fos = new FileOutputStream(rutaDestino)) {
				wb.write(fos);
			}
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	private void escribir(Sheet s, int r, int c, String v) {
		if (v == null)
			return;
		Row row = s.getRow(r);
		if (row == null)
			row = s.createRow(r);
		Cell cell = row.getCell(c);
		if (cell == null)
			cell = row.createCell(c);
		cell.setCellValue(v);
	}
}