package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import archivo.GestorCSV;
import modelo.GestionProductos;
import modelo.Producto;
import vista.FormularioProducto;
import vista.FormularioInventario;
import vista.FormularioPuntoVenta;
import vista.VentanaPrincipal;

public class ControladorProducto implements ActionListener {
	private VentanaPrincipal vistaPrincipal;
	private FormularioProducto vistaCRUD;
	private FormularioInventario vistaInventario;
	private FormularioPuntoVenta vistaPV;

	private GestionProductos modelo;
	private GestorCSV persistencia;

	private double subtotalVenta = 0.0;
	private int numeroTicket = 1;

	public ControladorProducto(VentanaPrincipal vistaPrincipal, GestionProductos modelo, GestorCSV persistencia) {
		this.vistaPrincipal = vistaPrincipal;
		this.modelo = modelo;
		this.persistencia = persistencia;

		this.vistaPrincipal.getItemProductos().addActionListener(e -> mostrarVentanaCRUD());
		this.vistaPrincipal.getItemInventario().addActionListener(e -> mostrarVentanaInventario());
		this.vistaPrincipal.getItemPuntoVenta().addActionListener(e -> mostrarVentanaPV());
	}

	private void mostrarVentanaCRUD() {
		if (vistaCRUD == null || vistaCRUD.isClosed()) {
			vistaCRUD = new FormularioProducto();
			vistaCRUD.btnGuardar.addActionListener(this);
			vistaCRUD.btnLimpiar.addActionListener(this);

			vistaCRUD.btnMostrarTodos.addActionListener(e -> actualizarTablaCRUD());
			vistaCRUD.btnBuscarFiltro.addActionListener(e -> {
				String cat = vistaCRUD.cbFiltroCategoria.getSelectedItem().toString();
				filtrarTablaCRUD(cat);
			});

			vistaPrincipal.getDesktopPane().add(vistaCRUD);
			vistaCRUD.setVisible(true);
			actualizarTablaCRUD();
		} else {
			vistaCRUD.toFront();
		}
	}

	private void mostrarVentanaInventario() {
		if (vistaInventario == null || vistaInventario.isClosed()) {
			vistaInventario = new FormularioInventario();
			vistaInventario.btnBuscar.addActionListener(e -> filtrarInventario());
			vistaInventario.btnLimpiarFiltros.addActionListener(e -> limpiarFiltrosInventario());

			vistaInventario.btnCrearNuevo.addActionListener(e -> {
				mostrarVentanaCRUD();
				limpiarFormulario();
			});

			vistaInventario.btnModificar.addActionListener(e -> cargarDesdeInventario());
			vistaInventario.btnEliminar.addActionListener(e -> eliminarDesdeInventario());

			vistaPrincipal.getDesktopPane().add(vistaInventario);
			vistaInventario.setVisible(true);
			cargarTablaInventario(modelo.listar());
		} else {
			vistaInventario.toFront();
		}
	}

	private void mostrarVentanaPV() {
        if (vistaPV == null || vistaPV.isClosed()) {
            vistaPV = new FormularioPuntoVenta();
            vistaPV.btnAñadir.addActionListener(e -> agregarAlCarrito());
            vistaPV.btnProcesarPago.addActionListener(e -> procesarPago());
            vistaPV.btnLimpiarCarrito.addActionListener(e -> limpiarCarrito());
            vistaPV.btnEliminar.addActionListener(e -> eliminarDelCarrito());
            
            vistaPrincipal.getDesktopPane().add(vistaPV);
            vistaPV.setVisible(true);
            
            actualizarComboBoxPuntoVenta();
            reiniciarTotales();
            
            actualizarInfoTicket(); 
        } else {
            vistaPV.toFront();
            actualizarInfoTicket();
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (vistaCRUD != null) {
			if (e.getSource() == vistaCRUD.btnGuardar)
				guardarProducto();
			if (e.getSource() == vistaCRUD.btnLimpiar)
				limpiarFormulario();
		}
	}

	private void guardarProducto() {
		try {
			String id = vistaCRUD.txtId.getText().trim();
			String nombre = vistaCRUD.txtNombre.getText().trim();
			String descripcion = vistaCRUD.txtDescripcion.getText().trim();
			String categoria = vistaCRUD.cbCategoria.getSelectedItem().toString();

			// CHECAR SI ID O NOMBRE ESTAN VACIOS
			if (id.isEmpty() || nombre.isEmpty()) {
				JOptionPane.showMessageDialog(vistaCRUD, "ID y nombre son obligatorios");
				return;
			}

			double pCompra = Double.parseDouble(vistaCRUD.txtPrecioCompra.getText().trim());
			double pVenta = Double.parseDouble(vistaCRUD.txtPrecioVenta.getText().trim());
			int stock = Integer.parseInt(vistaCRUD.txtStock.getText().trim());
			boolean activo = vistaCRUD.rbActivo.isSelected();

			Producto p = new Producto(id, nombre, descripcion, categoria, pCompra, pVenta, stock, activo);

			if (modelo.existe(id)) {
				modelo.actualizar(p);
				JOptionPane.showMessageDialog(vistaCRUD, "Producto actualizado");
			} else {
				modelo.insertar(p);
				JOptionPane.showMessageDialog(vistaCRUD, "Producto registrado");
			}

			sincronizarDatos();
			limpiarFormulario();

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(vistaCRUD, "ERROR: los precios y stock deben ser numeros", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void limpiarFormulario() {
		vistaCRUD.txtId.setText("");
		vistaCRUD.txtNombre.setText("");
		vistaCRUD.txtDescripcion.setText("");
		vistaCRUD.cbCategoria.setSelectedIndex(0);
		vistaCRUD.txtPrecioCompra.setText("");
		vistaCRUD.txtPrecioVenta.setText("");
		vistaCRUD.txtStock.setText("");
		vistaCRUD.rbActivo.setSelected(true);
		vistaCRUD.txtId.requestFocus();
	}

	private void actualizarTablaCRUD() {
		vistaCRUD.modeloTabla.setRowCount(0);
		for (Producto p : modelo.listar()) {
			vistaCRUD.modeloTabla.addRow(new Object[] { p.getId(), p.getId(), p.getNombre(), p.getCategoria(),
					p.getStock(), p.getPrecioVenta(), p.isActivo() ? "Activo" : "Desactivado" });
		}
	}

	private void filtrarTablaCRUD(String categoria) {
		vistaCRUD.modeloTabla.setRowCount(0);
		for (Producto p : modelo.listar()) {
			if (p.getCategoria().equals(categoria)) {
				vistaCRUD.modeloTabla.addRow(new Object[] { p.getId(), p.getId(), p.getNombre(), p.getCategoria(),
						p.getStock(), p.getPrecioVenta(), p.isActivo() ? "Activo" : "Desactivado" });
			}
		}
	}

	private void cargarTablaInventario(ArrayList<Producto> lista) {
		vistaInventario.modeloInventario.setRowCount(0);
		for (Producto p : lista) {
			vistaInventario.modeloInventario.addRow(new Object[] { p.getId(), p.getNombre(), p.getCategoria(),
					p.getStock(), p.getPrecioVenta(), p.isActivo() ? "Disponible" : "Agotado" });
		}
	}

	private void filtrarInventario() {
		String fId = vistaInventario.txtFiltroId.getText().toLowerCase().trim();
		String fNom = vistaInventario.txtFiltroNombre.getText().toLowerCase().trim();
		String fTipo = vistaInventario.cbFiltroTipo.getSelectedItem().toString();

		ArrayList<Producto> filtrados = new ArrayList<>();
		for (Producto p : modelo.listar()) {
			boolean coincideId = p.getId().toLowerCase().contains(fId);
			boolean coincideNom = p.getNombre().toLowerCase().contains(fNom);
			boolean coincideTipo = fTipo.equals("Todos") || p.getCategoria().equals(fTipo);

			boolean coincideEstado = true;
			if (vistaInventario.rbDisponible.isSelected() && !p.isActivo())
				coincideEstado = false;
			if (vistaInventario.rbAgotado.isSelected() && p.isActivo())
				coincideEstado = false;

			if (coincideId && coincideNom && coincideTipo && coincideEstado) {
				filtrados.add(p);
			}
		}
		cargarTablaInventario(filtrados);
	}

	private void limpiarFiltrosInventario() {
		vistaInventario.txtFiltroId.setText("");
		vistaInventario.txtFiltroNombre.setText("");
		vistaInventario.cbFiltroTipo.setSelectedIndex(0);
		vistaInventario.rbTodos.setSelected(true);
		cargarTablaInventario(modelo.listar());
	}

	private void cargarDesdeInventario() {
		int fila = vistaInventario.tablaInventario.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(vistaInventario, "Seleccione un producto de la tabla para modificar");
			return;
		}
		String id = vistaInventario.tablaInventario.getValueAt(fila, 0).toString();
		Producto p = modelo.buscar(id);
		if (p != null) {
			mostrarVentanaCRUD();
			vistaCRUD.txtId.setText(p.getId());
			vistaCRUD.txtNombre.setText(p.getNombre());
			vistaCRUD.txtDescripcion.setText(p.getDescripcion());
			vistaCRUD.cbCategoria.setSelectedItem(p.getCategoria());
			vistaCRUD.txtPrecioCompra.setText(String.valueOf(p.getPrecioCompra()));
			vistaCRUD.txtPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
			vistaCRUD.txtStock.setText(String.valueOf(p.getStock()));
			if (p.isActivo())
				vistaCRUD.rbActivo.setSelected(true);
			else
				vistaCRUD.rbInactivo.setSelected(true);
		}
	}

	private void eliminarDesdeInventario() {
		int fila = vistaInventario.tablaInventario.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(vistaInventario, "Seleccione un producto para eliminar");
			return;
		}
		String id = vistaInventario.tablaInventario.getValueAt(fila, 0).toString();
		int confirm = JOptionPane.showConfirmDialog(vistaInventario,
				"¿Seguro que quieres eliminar el producto " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			modelo.eliminar(id);
			sincronizarDatos();
		}
	}

	private void actualizarComboBoxPuntoVenta() {
		if (vistaPV != null) {
			vistaPV.cbProductos.removeAllItems();
			for (Producto p : modelo.listar()) {
				if (p.isActivo() && p.getStock() > 0) {
					vistaPV.cbProductos.addItem(p.getId() + " - " + p.getNombre());
				}
			}
		}
	}

	private void agregarAlCarrito() {
		if (vistaPV.cbProductos.getSelectedItem() == null)
			return;

		String seleccion = vistaPV.cbProductos.getSelectedItem().toString();
		String id = seleccion.split(" - ")[0];
		Producto p = modelo.buscar(id);

		try {
			int cant = Integer.parseInt(vistaPV.txtCantidad.getText().trim());
			if (cant <= 0 || cant > p.getStock()) {
				JOptionPane.showMessageDialog(vistaPV,
						"Cantidad invalida o stock insuficiente (" + p.getStock() + " disponibles).");
				return;
			}

			double totalProd = cant * p.getPrecioVenta();
			vistaPV.modeloTicket.addRow(new Object[] { p.getId(), p.getNombre(), cant, p.getPrecioVenta(), totalProd });

			calcularTotalesTicket();
			vistaPV.txtCantidad.setText("");

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(vistaPV, "Ingresa una cantidad valida");
		}
	}

	private void eliminarDelCarrito() {
		int fila = vistaPV.tablaTicket.getSelectedRow();
		if (fila != -1) {
			vistaPV.modeloTicket.removeRow(fila);
			calcularTotalesTicket();
		} else {
			JOptionPane.showMessageDialog(vistaPV, "Selecciona un producto del ticket para eliminar");
		}
	}

	private void calcularTotalesTicket() {
		subtotalVenta = 0.0;
		for (int i = 0; i < vistaPV.modeloTicket.getRowCount(); i++) {
			subtotalVenta += Double.parseDouble(vistaPV.modeloTicket.getValueAt(i, 4).toString());
		}

		double iva = subtotalVenta * 0.16;
		double totalFinal = subtotalVenta + iva;

		vistaPV.txtSubtotal.setText(String.format("%.2f", subtotalVenta));
		vistaPV.txtIva.setText(String.format("%.2f", iva));
		vistaPV.txtTotal.setText(String.format("%.2f", totalFinal));
	}

	private void procesarPago() {
        if (vistaPV.modeloTicket.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vistaPV, "El carrito está vacío.");
            return;
        }

        for (int i = 0; i < vistaPV.modeloTicket.getRowCount(); i++) {
            String id = vistaPV.modeloTicket.getValueAt(i, 0).toString();
            int cant = Integer.parseInt(vistaPV.modeloTicket.getValueAt(i, 2).toString());
            Producto p = modelo.buscar(id);
            if (p != null) {
                p.setStock(p.getStock() - cant);
                modelo.actualizar(p);
            }
        }

        guardarTicketTxt();

        JOptionPane.showMessageDialog(vistaPV, "Pago procesado \nTotal cobrado: $" + vistaPV.txtTotal.getText());
        sincronizarDatos();
        limpiarCarrito();

        numeroTicket++; 
        actualizarInfoTicket(); 
    }

	private void limpiarCarrito() {
		vistaPV.modeloTicket.setRowCount(0);
		reiniciarTotales();
	}

	private void reiniciarTotales() {
		subtotalVenta = 0.0;
		vistaPV.txtSubtotal.setText("0.00");
		vistaPV.txtIva.setText("0.00");
		vistaPV.txtTotal.setText("0.00");
	}

	private void sincronizarDatos() {
		persistencia.exportarCSV(modelo.listar());

		if (vistaCRUD != null && !vistaCRUD.isClosed())
			actualizarTablaCRUD();
		if (vistaInventario != null && !vistaInventario.isClosed())
			cargarTablaInventario(modelo.listar());
		if (vistaPV != null && !vistaPV.isClosed())
			actualizarComboBoxPuntoVenta();
	}


	private void guardarTicketTxt() {
		String fechaHora = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
		String nombreArchivo = "Historial_Ventas.txt";

		try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(nombreArchivo, true))) {
			bw.write("========================================="); bw.newLine();
			bw.write("                 ABARROTES               "); bw.newLine();
			bw.write("========================================="); bw.newLine();
			bw.write("Ticket #: " + String.format("%03d", numeroTicket)); bw.newLine();
			bw.write("Fecha y Hora: " + fechaHora); bw.newLine();
			bw.write("Cajero"); bw.newLine();
			bw.write("-----------------------------------------"); bw.newLine();
			bw.write("CANT | PRODUCTO | P.UNIT | SUBTOTAL"); bw.newLine();
			bw.write("-----------------------------------------"); bw.newLine();

			for (int i = 0; i < vistaPV.modeloTicket.getRowCount(); i++) {
				String cant = vistaPV.modeloTicket.getValueAt(i, 2).toString();
				String nombre = vistaPV.modeloTicket.getValueAt(i, 1).toString();
				String pUnit = vistaPV.modeloTicket.getValueAt(i, 3).toString();
				String subtotal = vistaPV.modeloTicket.getValueAt(i, 4).toString();

				bw.write(cant + " x " + nombre + " | $" + pUnit + " | $" + subtotal);
				bw.newLine();
			}

			bw.write("-----------------------------------------"); bw.newLine();
			bw.write("SUBTOTAL: $" + vistaPV.txtSubtotal.getText()); bw.newLine();
			bw.write("IVA (16%): $" + vistaPV.txtIva.getText()); bw.newLine();
			bw.write("TOTAL: $" + vistaPV.txtTotal.getText()); bw.newLine();
			bw.write("=========================================\n\n");

		} catch (java.io.IOException e) {
			JOptionPane.showMessageDialog(vistaPV, "Error al guardar el ticket TXT: " + e.getMessage());
		}
	}

	private void actualizarInfoTicket() {
		if (vistaPV != null) {
			String fechaHoraActual = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
			String numTicketStr = String.format("%03d", numeroTicket);

			vistaPV.lblInfoTicket.setText("  Fecha: " + fechaHoraActual + "   |   Cajero   |   Ticket #" + numTicketStr);
		}
	}
}