package modelo;

import java.util.List;

public class Evaluacion {
	private String id, asignatura, profesor, grupo, periodo, atributoEgreso, criterioDesempeno, indicador;
	private List<CriterioInstrumento> criteriosInstrumento;
	private List<CriterioRubrica> criteriosRubrica;
	private List<Alumno> equipos;

	public Evaluacion() {
	}

	public Evaluacion(String id, String asig, String prof, String grp, String per, String atr, String crit, String ind,
			List<CriterioInstrumento> critInst, List<CriterioRubrica> critRub, List<Alumno> eq) {
		this.id = id;
		this.asignatura = asig;
		this.profesor = prof;
		this.grupo = grp;
		this.periodo = per;
		this.atributoEgreso = atr;
		this.criterioDesempeno = crit;
		this.indicador = ind;
		this.criteriosInstrumento = critInst;
		this.criteriosRubrica = critRub;
		this.equipos = eq;
	}

	public String getIndicador() {
		return indicador;
	}

	public void setIndicador(String i) {
		this.indicador = i;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(String a) {
		this.asignatura = a;
	}

	public String getProfesor() {
		return profesor;
	}

	public void setProfesor(String p) {
		this.profesor = p;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String g) {
		this.grupo = g;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String p) {
		this.periodo = p;
	}

	public String getAtributoEgreso() {
		return atributoEgreso;
	}

	public void setAtributoEgreso(String a) {
		this.atributoEgreso = a;
	}

	public String getCriterioDesempeno() {
		return criterioDesempeno;
	}

	public void setCriterioDesempeno(String c) {
		this.criterioDesempeno = c;
	}

	public List<CriterioInstrumento> getCriteriosInstrumento() {
		return criteriosInstrumento;
	}

	public void setCriteriosInstrumento(List<CriterioInstrumento> c) {
		this.criteriosInstrumento = c;
	}

	public List<CriterioRubrica> getCriteriosRubrica() {
		return criteriosRubrica;
	}

	public void setCriteriosRubrica(List<CriterioRubrica> c) {
		this.criteriosRubrica = c;
	}

	public List<Alumno> getEquipos() {
		return equipos;
	}

	public void setEquipos(List<Alumno> e) {
		this.equipos = e;
	}

	// PESTAÑA 1
	public static class CriterioInstrumento {
		private String aspecto;
		private int calificacion;
		private String observaciones;

		public CriterioInstrumento(String asp, int cal, String obs) {
			this.aspecto = asp;
			this.calificacion = cal;
			this.observaciones = obs;
		}

		public String getAspecto() {
			return aspecto;
		}

		public int getCalificacion() {
			return calificacion;
		}

		public String getObservaciones() {
			return observaciones;
		}
	}

	// PESTAÑA 2
	public static class CriterioRubrica {
		private String aspecto, excelente, bueno, regular, noAlcanza;

		public CriterioRubrica(String asp, String exc, String bno, String reg, String noAlc) {
			this.aspecto = asp;
			this.excelente = exc;
			this.bueno = bno;
			this.regular = reg;
			this.noAlcanza = noAlc;
		}

		public String getAspecto() {
			return aspecto;
		}

		public String getExcelente() {
			return excelente;
		}

		public String getBueno() {
			return bueno;
		}

		public String getRegular() {
			return regular;
		}

		public String getNoAlcanza() {
			return noAlcanza;
		}
	}
}