package controlador;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;

import modelo.Alumno;
import modelo.Evaluacion;
import modelo.Evaluacion.CriterioInstrumento;
import modelo.Evaluacion.CriterioRubrica;
import vista.VistaPrincipal;

public class ControladorPrincipal implements ActionListener {

	private VistaPrincipal v;
	private GestorExcel gXls;
	private GestorJSON gJsn;
	private List<Alumno> alumnos;

	private static final String PLANTILLA = "examenn.xlsx";

	public ControladorPrincipal(VistaPrincipal vista, GestorExcel gExcel, GestorJSON gJson) {
		this.v = vista;
		this.gXls = gExcel;
		this.gJsn = gJson;
		this.alumnos = new ArrayList<>();

		v.btnCargar.addActionListener(this);
		v.btnNuevo.addActionListener(this);
		v.btnGuardar.addActionListener(this);
		v.btnEliminar.addActionListener(this);

		v.modInst.addTableModelListener(e -> {
			if (e.getType() == TableModelEvent.UPDATE)
				calcularSumaInstrumento();
		});
		v.modAlum.addTableModelListener(e -> {
			if (e.getType() == TableModelEvent.UPDATE)
				calcularPromedioAlumnos();
		});
	}

	public void iniciar() {
		v.lblSemaforo.setText("🔴 Sin Iniciar");
		v.lblSemaforo.setForeground(Color.RED);
		v.setLocationRelativeTo(null);

		for (String a : gXls.obtenerAsignaturas())
			v.cbAsignatura.addItem(a);

		v.cbAsignatura.addActionListener(e -> {
			if (v.cbAsignatura.getSelectedIndex() > 0) {
				v.cbProfesor.removeAllItems();
				v.cbProfesor.addItem("Seleccione Profesor...");
				for (String p : gXls.obtenerProfesores(v.cbAsignatura.getSelectedItem().toString()))
					v.cbProfesor.addItem(p);
			}
		});

		v.cbProfesor.addActionListener(e -> {
			if (v.cbProfesor.getSelectedIndex() > 0 && v.cbAsignatura.getSelectedIndex() > 0) {
				v.cbGrupo.removeAllItems();
				v.cbGrupo.addItem("Seleccione Grupo...");
				for (String g : gXls.obtenerGrupos(v.cbAsignatura.getSelectedItem().toString(),
						v.cbProfesor.getSelectedItem().toString()))
					v.cbGrupo.addItem(g);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == v.btnCargar)
			cargar();
		else if (e.getSource() == v.btnNuevo)
			limpiar();
		else if (e.getSource() == v.btnGuardar)
			guardar();
		else if (e.getSource() == v.btnEliminar)
			eliminar();
	}

	private void calcularSumaInstrumento() {
		double suma = 0;
		for (int i = 0; i < v.modInst.getRowCount(); i++) {
			try {
				String val = v.modInst.getValueAt(i, 1).toString();
				if (!val.trim().isEmpty())
					suma += Double.parseDouble(val);
			} catch (Exception ignored) {
			}
		}
		v.lblSumaInstrumento.setText("Suma Total: " + suma);
	}

	private void calcularPromedioAlumnos() {
		double suma = 0;
		int cont = 0;
		for (int i = 0; i < v.modAlum.getRowCount(); i++) {
			try {
				String val = v.modAlum.getValueAt(i, 1).toString();
				if (!val.trim().isEmpty()) {
					suma += Double.parseDouble(val);
					cont++;
				}
			} catch (Exception ignored) {
			}
		}
		double prom = cont > 0 ? (suma / cont) : 0.0;
		v.lblPromedioAlumnos.setText(String.format("Promedio Grupal: %.1f", prom));
	}

	// log princial
	private void cargar() {
		if (v.cbAsignatura.getSelectedIndex() == 0 || v.cbGrupo.getSelectedIndex() == 0)
			return;

		String id = v.cbAsignatura.getSelectedItem().toString().replace(" ", "") + "_"
				+ v.cbGrupo.getSelectedItem().toString().trim();

		Evaluacion ev = gJsn.leerTodasLasEvaluaciones().stream().filter(x -> x.getId().equals(id)).findFirst()
				.orElse(null);

		limpiarMatriz();
		v.modAlum.setRowCount(0);

		if (ev != null) {

			v.txtAtributo.setText(ev.getAtributoEgreso());
			v.txtCriterio.setText(ev.getCriterioDesempeno());
			v.txtIndicador.setText(ev.getIndicador());

			// 1er pestaña
			List<CriterioInstrumento> cInst = ev.getCriteriosInstrumento();
			for (int i = 0; i < 6 && i < cInst.size(); i++) {
				v.modInst.setValueAt(cInst.get(i).getAspecto(), i, 0);
				v.modInst.setValueAt(cInst.get(i).getCalificacion(), i, 1);
				v.modInst.setValueAt(cInst.get(i).getObservaciones(), i, 2);
			}

			// 2da pestaña
			List<CriterioRubrica> cRub = ev.getCriteriosRubrica();
			for (int i = 0; i < 4 && i < cRub.size(); i++) {
				v.modRub.setValueAt(cRub.get(i).getAspecto(), i, 0);
				v.modRub.setValueAt(cRub.get(i).getExcelente(), i, 1);
				v.modRub.setValueAt(cRub.get(i).getBueno(), i, 2);
				v.modRub.setValueAt(cRub.get(i).getRegular(), i, 3);
				v.modRub.setValueAt(cRub.get(i).getNoAlcanza(), i, 4);
			}

			// 3er pestaña
			alumnos = ev.getEquipos();
			for (Alumno a : alumnos)
				v.modAlum.addRow(new Object[] { a.getNombre(), a.getCalificacionRubrica() });

			v.lblSemaforo.setText("🟢 Cargado");
			v.lblSemaforo.setForeground(Color.GREEN);

		} else {
			v.txtAtributo.setText("");
			v.txtCriterio.setText("");
			v.txtIndicador.setText("");

			alumnos = gXls.obtenerAlumnos(v.cbAsignatura.getSelectedItem().toString(),
					v.cbProfesor.getSelectedItem().toString(), v.cbGrupo.getSelectedItem().toString());

			if (alumnos.size() > 6) {
				JOptionPane.showMessageDialog(v,
						"El grupo tiene " + alumnos.size() + " alumnos en total.\n"
								+ "Se cargarán los primeros 6 alumnos para este equipo",
								"AVISO", JOptionPane.INFORMATION_MESSAGE);
			}

			int limit = Math.min(alumnos.size(), 6);
			alumnos = new ArrayList<>(alumnos.subList(0, limit));
			for (Alumno a : alumnos)
				v.modAlum.addRow(new Object[] { a.getNombre(), "" });

			v.lblSemaforo.setText("🟡 Incompleto");
			v.lblSemaforo.setForeground(Color.ORANGE);
		}

		calcularSumaInstrumento();
		calcularPromedioAlumnos();
	}

	private void guardar() {
		if (v.tablaInstrumento.isEditing())
			v.tablaInstrumento.getCellEditor().stopCellEditing();
		if (v.tablaRubrica.isEditing())
			v.tablaRubrica.getCellEditor().stopCellEditing();
		if (v.tablaAlumnos.isEditing())
			v.tablaAlumnos.getCellEditor().stopCellEditing();

		if (alumnos.isEmpty()) {
			JOptionPane.showMessageDialog(v, "Cargue un grupo antes de guardar", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String asig = v.cbAsignatura.getSelectedItem().toString();
		String prof = v.cbProfesor.getSelectedItem().toString();
		String grp = v.cbGrupo.getSelectedItem().toString();

		List<CriterioInstrumento> critInst = new ArrayList<>();
		List<CriterioRubrica> critRub = new ArrayList<>();

		for (int i = 0; i < 6; i++) {
			int calif = 0;
			try {
				calif = Integer.parseInt(str(v.modInst.getValueAt(i, 1)));
			} catch (Exception ignored) {
			}
			critInst.add(
					new CriterioInstrumento(str(v.modInst.getValueAt(i, 0)), calif, str(v.modInst.getValueAt(i, 2))));
		}

		for (int i = 0; i < 4; i++) {
			critRub.add(new CriterioRubrica(str(v.modRub.getValueAt(i, 0)), str(v.modRub.getValueAt(i, 1)),
					str(v.modRub.getValueAt(i, 2)), str(v.modRub.getValueAt(i, 3)), str(v.modRub.getValueAt(i, 4))));
		}

		for (int i = 0; i < v.modAlum.getRowCount(); i++) {
			try {
				alumnos.get(i).setCalificacionRubrica(Integer.parseInt(v.modAlum.getValueAt(i, 1).toString()));
			} catch (Exception ex) {
				alumnos.get(i).setCalificacionRubrica(0);
			}
		}


		Evaluacion eval = new Evaluacion(asig.replace(" ", "") + "_" + grp.trim(), asig, prof, grp, "Ene - Junio 2026", //periodo por default

				v.txtAtributo.getText(), v.txtCriterio.getText(), v.txtIndicador.getText(), critInst, critRub, alumnos);

		gJsn.guardarEvaluacion(eval);

		JFileChooser fc = new JFileChooser();
		fc.setSelectedFile(
				new File(asig.replace(" ", "_") + "_" + prof.replace(" ", "_") + "_" + grp.trim() + ".xlsx"));

		if (fc.showSaveDialog(v) == JFileChooser.APPROVE_OPTION) {
			String ruta = fc.getSelectedFile().getAbsolutePath();
			if (!ruta.endsWith(".xlsx"))
				ruta += ".xlsx";

			gXls.generarReporteExcel(eval, PLANTILLA, ruta);

			v.lblSemaforo.setText("🟢 Guardado");
			v.lblSemaforo.setForeground(Color.GREEN);
			JOptionPane.showMessageDialog(v, "JSON guardado y Excel generado correctamente");
		}
	}

	private void eliminar() {
		if (v.cbAsignatura.getSelectedIndex() > 0) {
			String id = v.cbAsignatura.getSelectedItem().toString().replace(" ", "") + "_"
					+ v.cbGrupo.getSelectedItem().toString().trim();
			if (gJsn.eliminarEvaluacion(id))
				limpiar();
		}
	}

	private void limpiar() {
		if (v.tablaInstrumento.isEditing())
			v.tablaInstrumento.getCellEditor().stopCellEditing();
		if (v.tablaRubrica.isEditing())
			v.tablaRubrica.getCellEditor().stopCellEditing();
		if (v.tablaAlumnos.isEditing())
			v.tablaAlumnos.getCellEditor().stopCellEditing();

		v.cbAsignatura.setSelectedIndex(0);
		v.txtAtributo.setText("");
		v.txtCriterio.setText("");
		v.txtIndicador.setText("");
		v.modAlum.setRowCount(0);
		limpiarMatriz();
		alumnos = new ArrayList<>();
		calcularSumaInstrumento();
		calcularPromedioAlumnos();
		v.lblSemaforo.setText("🔴 Sin Iniciar");
		v.lblSemaforo.setForeground(Color.RED);
	}

	private void limpiarMatriz() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++)
				v.modInst.setValueAt("", i, j);
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++)
				v.modRub.setValueAt("", i, j);
		}
	}

	private String str(Object o) {
		return o == null ? "" : o.toString();
	}
}