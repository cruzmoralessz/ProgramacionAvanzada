package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaPrincipal extends JFrame {

	public JComboBox<String> cbAsignatura, cbProfesor, cbGrupo;
	public JButton btnCargar, btnNuevo, btnGuardar, btnEliminar;
	public JLabel lblSemaforo;

	public JTextArea txtAtributo, txtCriterio;
	public JTextField txtIndicador;

	public JTable tablaInstrumento, tablaRubrica, tablaAlumnos;
	public DefaultTableModel modInst, modRub, modAlum;

	public JLabel lblSumaInstrumento, lblPromedioAlumnos;

	public VistaPrincipal() {
		setTitle("SAE-AE: Sistema Automatizado de Evaluación");
		setSize(1100, 780);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		panelTop.setBackground(new Color(30, 60, 100));

		cbAsignatura = new JComboBox<>(new String[] { "Seleccione Asignatura..." });
		cbProfesor = new JComboBox<>(new String[] { "Seleccione Profesor..." });
		cbGrupo = new JComboBox<>(new String[] { "Seleccione Grupo..." });

		btnCargar = new JButton("Cargar Datos");
		btnNuevo = new JButton("Limpiar");
		btnGuardar = new JButton("Guardar JSON y Generar Excel");
		btnEliminar = new JButton("Borrar Reg.");

		lblSemaforo = new JLabel("🔴 Sin Iniciar");
		lblSemaforo.setForeground(Color.RED);
		lblSemaforo.setFont(new Font("Arial", Font.BOLD, 14));

		panelTop.add(cbAsignatura);
		panelTop.add(cbProfesor);
		panelTop.add(cbGrupo);
		panelTop.add(btnCargar);
		panelTop.add(btnNuevo);
		panelTop.add(btnGuardar);
		panelTop.add(btnEliminar);
		panelTop.add(lblSemaforo);
		add(panelTop, BorderLayout.NORTH);

		JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
		JPanel panelCompartido = new JPanel(new GridLayout(1, 3, 10, 6));
		panelCompartido.setBorder(BorderFactory.createTitledBorder("Competencias a Evaluar"));

		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(new JLabel("Atributo de Egreso:"), BorderLayout.NORTH);
		txtAtributo = new JTextArea(2, 20);
		txtAtributo.setLineWrap(true);
		p1.add(new JScrollPane(txtAtributo), BorderLayout.CENTER);

		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(new JLabel("Criterio de Desempeño:"), BorderLayout.NORTH);
		txtCriterio = new JTextArea(2, 20);
		txtCriterio.setLineWrap(true);
		p2.add(new JScrollPane(txtCriterio), BorderLayout.CENTER);

		JPanel p3 = new JPanel(new BorderLayout());
		p3.add(new JLabel("Indicador:"), BorderLayout.NORTH);
		txtIndicador = new JTextField();
		p3.add(txtIndicador, BorderLayout.CENTER);

		panelCompartido.add(p1);
		panelCompartido.add(p2);
		panelCompartido.add(p3);
		panelCentral.add(panelCompartido, BorderLayout.NORTH);

		JTabbedPane pestanas = new JTabbedPane();
		pestanas.addTab("1. Instrumento (Criterios y Obs.)", crearPanelInstrumento());
		pestanas.addTab("2. Rúbrica (Niveles de Desempeño)", crearPanelRubrica());
		pestanas.addTab("3. Lista de Cotejo (Alumnos)", crearPanelAlumnos());

		panelCentral.add(pestanas, BorderLayout.CENTER);
		add(panelCentral, BorderLayout.CENTER);
	}

	private JPanel crearPanelInstrumento() {
		JPanel panel = new JPanel(new BorderLayout());
		String[] cols = { "Criterio de Evaluación", "Calificación (Núm.)", "Observaciones" };
		modInst = new DefaultTableModel(cols, 6);
		tablaInstrumento = new JTable(modInst);
		tablaInstrumento.setRowHeight(35);
		panel.add(new JScrollPane(tablaInstrumento), BorderLayout.CENTER);

		JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lblSumaInstrumento = new JLabel("Suma Total: 0");
		lblSumaInstrumento.setFont(new Font("Arial", Font.BOLD, 16));
		sur.add(lblSumaInstrumento);
		panel.add(sur, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel crearPanelRubrica() {
		JPanel panel = new JPanel(new BorderLayout());
		String[] cols = { "Criterio", "Excelente (10)", "Bueno (9)", "Regular (8-7)", "No Alcanza (6-0)" };
		modRub = new DefaultTableModel(cols, 4);
		tablaRubrica = new JTable(modRub);
		tablaRubrica.setRowHeight(35);
		panel.add(new JScrollPane(tablaRubrica), BorderLayout.CENTER);
		return panel;
	}

	private JPanel crearPanelAlumnos() {
		JPanel panel = new JPanel(new BorderLayout());
		String[] cols = { "Nombre del Alumno", "Calificación Final (0-10)" };
		modAlum = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return c != 0;
			}
		};
		tablaAlumnos = new JTable(modAlum);
		tablaAlumnos.setRowHeight(30);

		tablaAlumnos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object val, boolean isSel, boolean focus,
					int row, int col) {
				Component c = super.getTableCellRendererComponent(table, val, isSel, focus, row, col);
				if (col == 1 && val != null && !val.toString().trim().isEmpty()) {
					try {
						if (Double.parseDouble(val.toString()) < 7.0) {
							c.setBackground(new Color(255, 102, 102));
							c.setForeground(Color.WHITE);
						} else {
							c.setBackground(isSel ? table.getSelectionBackground() : Color.WHITE);
							c.setForeground(Color.BLACK);
						}
					} catch (Exception ignored) {
						c.setBackground(Color.WHITE);
					}
				} else {
					c.setBackground(isSel ? table.getSelectionBackground() : Color.WHITE);
					c.setForeground(Color.BLACK);
				}
				return c;
			}
		});
		panel.add(new JScrollPane(tablaAlumnos), BorderLayout.CENTER);

		JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lblPromedioAlumnos = new JLabel("Promedio Grupal: 0.0");
		lblPromedioAlumnos.setFont(new Font("Arial", Font.BOLD, 16));
		sur.add(lblPromedioAlumnos);
		panel.add(sur, BorderLayout.SOUTH);
		return panel;
	}
}