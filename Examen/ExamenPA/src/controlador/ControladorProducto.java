package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import archivo.GestorJSON;
import modelo.GestionProductos;
import modelo.Producto;
import vista.FormularioInventario;
import vista.FormularioProducto;
import vista.FormularioPuntoVenta;
import vista.VentanaPrincipal;

import java.awt.Image;
import javax.swing.ImageIcon;

public class ControladorProducto implements ActionListener {
	private VentanaPrincipal mainUI;
	private FormularioProducto pantallaProductos;
	private FormularioInventario pantallaInventario;
	private FormularioPuntoVenta pantallaCaja;

	private GestionProductos gestionModelo;
	private GestorJSON managerArchivos;

	private double acumuladoCaja = 0.0;
	private int folioActual;

	public ControladorProducto(VentanaPrincipal vistaPrincipal, GestionProductos modelo, GestorJSON persistencia) {
		this.mainUI = vistaPrincipal;
		this.gestionModelo = modelo;
		this.managerArchivos = persistencia;
		
		this.folioActual = managerArchivos.obtenerSiguienteNumeroTicket();

		this.mainUI.getItemProductos().addActionListener(e -> abrirVistaProductos());
		this.mainUI.getItemInventario().addActionListener(e -> abrirVistaInventario());
		this.mainUI.getItemPuntoVenta().addActionListener(e -> abrirVistaPuntoVenta());
	}

	private void abrirVistaProductos() {
		if (pantallaProductos == null || pantallaProductos.isClosed()) {
			pantallaProductos = new FormularioProducto();
			pantallaProductos.btnGuardar.addActionListener(this);
			pantallaProductos.btnLimpiar.addActionListener(this);

			pantallaProductos.btnMostrarTodos.addActionListener(e -> recargarTablaBD());
			pantallaProductos.btnBuscarFiltro.addActionListener(e -> {
				String categoriaFiltro = pantallaProductos.cbFiltroCategoria.getSelectedItem().toString();
				filtrarArticulos(categoriaFiltro);
			});
			pantallaProductos.cbCategoria.addActionListener(e -> cambiarFotoFormulario());
			cambiarFotoFormulario(); 
			pantallaProductos.btnExportarLista.addActionListener(e -> generarExcel());

			mainUI.getDesktopPane().add(pantallaProductos);
			pantallaProductos.setVisible(true);
			recargarTablaBD();
		} else {
			pantallaProductos.toFront();
		}
	}

	private void abrirVistaInventario() {
		if (pantallaInventario == null || pantallaInventario.isClosed()) {
			pantallaInventario = new FormularioInventario();
			pantallaInventario.btnBuscar.addActionListener(e -> aplicarFiltrosInv());
			pantallaInventario.btnLimpiarFiltros.addActionListener(e -> limpiarBuscadorInv());

			pantallaInventario.btnCrearNuevo.addActionListener(e -> {
				abrirVistaProductos();
				vaciarCampos();
			});

			pantallaInventario.btnModificar.addActionListener(e -> mandarAEdicion());
			pantallaInventario.btnEliminar.addActionListener(e -> borrarDeBaseDatos());

			mainUI.getDesktopPane().add(pantallaInventario);
			pantallaInventario.setVisible(true);
			llenarGridInventario(gestionModelo.listar());
		} else {
			pantallaInventario.toFront();
		}
	}

	private void abrirVistaPuntoVenta() {
        if (pantallaCaja == null || pantallaCaja.isClosed()) {
            pantallaCaja = new FormularioPuntoVenta();
            pantallaCaja.btnAñadir.addActionListener(e -> meterAlCarrito());
            pantallaCaja.btnProcesarPago.addActionListener(e -> cobrarTicket());
            pantallaCaja.btnLimpiarCarrito.addActionListener(e -> borrarCarrito());
            pantallaCaja.btnEliminar.addActionListener(e -> quitarProductoLista());
            pantallaCaja.cbProductos.addActionListener(e -> refrescarFotoCaja());
            
            mainUI.getDesktopPane().add(pantallaCaja);
            pantallaCaja.setVisible(true);
            
            cargarComboProductos();
            resetearCuenta();
            pintarInfoFolio(); 
        } else {
            pantallaCaja.toFront();
            pintarInfoFolio();
        }
    }

	@Override
	public void actionPerformed(ActionEvent evento) {
		if (pantallaProductos != null) {
			if (evento.getSource() == pantallaProductos.btnGuardar)
				registrarNuevoArticulo();
			if (evento.getSource() == pantallaProductos.btnLimpiar)
				vaciarCampos();
		}
	}

	private void registrarNuevoArticulo() {
		try {
			String clave = pantallaProductos.txtId.getText().trim();
			String nom = pantallaProductos.txtNombre.getText().trim();
			String desc = pantallaProductos.txtDescripcion.getText().trim();
			String cat = pantallaProductos.cbCategoria.getSelectedItem().toString();

			if (clave.isEmpty() || nom.isEmpty()) {
				JOptionPane.showMessageDialog(pantallaProductos, "Falta escribir el ID o el nombre del articulo");
				return;
			}

			double costo = Double.parseDouble(pantallaProductos.txtPrecioCompra.getText().trim());
			double venta = Double.parseDouble(pantallaProductos.txtPrecioVenta.getText().trim());
			int existencias = Integer.parseInt(pantallaProductos.txtStock.getText().trim());
			boolean status = pantallaProductos.rbActivo.isSelected();

			Producto articulo = null; 

			switch (cat) {
				case "Abarrotes": articulo = new modelo.Abarrote(clave, nom, desc, costo, venta, existencias, status, "abarrotes.jpg"); break;
				case "Bebidas": articulo = new modelo.Bebida(clave, nom, desc, costo, venta, existencias, status, "bebidas.jpg"); break;
				case "Lácteos y Huevo": articulo = new modelo.Lacteo(clave, nom, desc, costo, venta, existencias, status, "lacteos.jpg"); break;
				case "Frutas y Verduras": articulo = new modelo.FrutaVerdura(clave, nom, desc, costo, venta, existencias, status, "frutas.jpg"); break;
				case "Carnes y Pescados": articulo = new modelo.CarnePescado(clave, nom, desc, costo, venta, existencias, status, "carnes.jpg"); break;
				case "Salchichonería": articulo = new modelo.Salchichoneria(clave, nom, desc, costo, venta, existencias, status, "salchichoneria.jpg"); break;
				case "Panadería y Tortillería": articulo = new modelo.Panaderia(clave, nom, desc, costo, venta, existencias, status, "panaderia.jpg"); break;
				case "Limpieza del Hogar": articulo = new modelo.Limpieza(clave, nom, desc, costo, venta, existencias, status, "limpieza.jpg"); break;
				case "Cuidado Personal": articulo = new modelo.CuidadoPersonal(clave, nom, desc, costo, venta, existencias, status, "cuidado.jpg"); break;
				case "Snacks y Dulcería": articulo = new modelo.Snack(clave, nom, desc, costo, venta, existencias, status, "snacks.jpg"); break;
				case "Mascotas": articulo = new modelo.Mascota(clave, nom, desc, costo, venta, existencias, status, "mascotas.jpg"); break;
				default:
					JOptionPane.showMessageDialog(pantallaProductos, "ERROR. elige una categoría valida");
					return;
			}

			if (gestionModelo.existe(clave)) {
				gestionModelo.actualizar(articulo);
				JOptionPane.showMessageDialog(pantallaProductos, "Articulo actualizado");
			} else {
				gestionModelo.insertar(articulo);
				JOptionPane.showMessageDialog(pantallaProductos, "Guardado!");
			}

			guardarTodo();
			vaciarCampos();

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(pantallaProductos, "ERROR. precio y stock deben ser solo numeros", "Cuidado",JOptionPane.WARNING_MESSAGE);
		}
	}

	private void vaciarCampos() {
		pantallaProductos.txtId.setText("");
		pantallaProductos.txtNombre.setText("");
		pantallaProductos.txtDescripcion.setText("");
		pantallaProductos.cbCategoria.setSelectedIndex(0);
		pantallaProductos.txtPrecioCompra.setText("");
		pantallaProductos.txtPrecioVenta.setText("");
		pantallaProductos.txtStock.setText("");
		pantallaProductos.rbActivo.setSelected(true);
		pantallaProductos.txtId.requestFocus();
	}

	private void recargarTablaBD() {
		pantallaProductos.modeloTabla.setRowCount(0);
		for (Producto prod : gestionModelo.listar()) {
			pantallaProductos.modeloTabla.addRow(new Object[] { prod.getId(), prod.getId(), prod.getNombre(), prod.getCategoria(),
					prod.getStock(), prod.getPrecioVenta(), prod.isActivo() ? "Activo" : "Desactivado" });
		}
	}

	private void filtrarArticulos(String catFiltro) {
		pantallaProductos.modeloTabla.setRowCount(0);
		for (Producto prod : gestionModelo.listar()) {
			if (prod.getCategoria().equals(catFiltro)) {
				pantallaProductos.modeloTabla.addRow(new Object[] { prod.getId(), prod.getId(), prod.getNombre(), prod.getCategoria(),
						prod.getStock(), prod.getPrecioVenta(), prod.isActivo() ? "Activo" : "Desactivado" });
			}
		}
	}

	private void llenarGridInventario(ArrayList<Producto> arreglo) {
		pantallaInventario.modeloInventario.setRowCount(0);
		for (Producto p : arreglo) {
			pantallaInventario.modeloInventario.addRow(new Object[] { p.getId(), p.getNombre(), p.getCategoria(),
					p.getStock(), p.getPrecioVenta(), p.isActivo() ? "Disponible" : "Agotado" });
		}
	}

	private void aplicarFiltrosInv() {
		String idBusqueda = pantallaInventario.txtFiltroId.getText().toLowerCase().trim();
		String nomBusqueda = pantallaInventario.txtFiltroNombre.getText().toLowerCase().trim();
		String tipoBusqueda = pantallaInventario.cbFiltroTipo.getSelectedItem().toString();

		ArrayList<Producto> coinciden = new ArrayList<>();
		for (Producto p : gestionModelo.listar()) {
			boolean matchId = p.getId().toLowerCase().contains(idBusqueda);
			boolean matchNom = p.getNombre().toLowerCase().contains(nomBusqueda);
			boolean matchTipo = tipoBusqueda.equals("Todos") || p.getCategoria().equals(tipoBusqueda);

			boolean matchStatus = true;
			if (pantallaInventario.rbDisponible.isSelected() && !p.isActivo()) matchStatus = false;
			if (pantallaInventario.rbAgotado.isSelected() && p.isActivo()) matchStatus = false;

			if (matchId && matchNom && matchTipo && matchStatus) {
				coinciden.add(p);
			}
		}
		llenarGridInventario(coinciden);
	}

	private void limpiarBuscadorInv() {
		pantallaInventario.txtFiltroId.setText("");
		pantallaInventario.txtFiltroNombre.setText("");
		pantallaInventario.cbFiltroTipo.setSelectedIndex(0);
		pantallaInventario.rbTodos.setSelected(true);
		llenarGridInventario(gestionModelo.listar());
	}

	private void mandarAEdicion() {
		int row = pantallaInventario.tablaInventario.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(pantallaInventario, "Escoge un producto primero");
			return;
		}
		String codigoBuscado = pantallaInventario.tablaInventario.getValueAt(row, 0).toString();
		Producto aux = gestionModelo.buscar(codigoBuscado);
		if (aux != null) {
			abrirVistaProductos();
			pantallaProductos.txtId.setText(aux.getId());
			pantallaProductos.txtNombre.setText(aux.getNombre());
			pantallaProductos.txtDescripcion.setText(aux.getDescripcion());
			pantallaProductos.cbCategoria.setSelectedItem(aux.getCategoria());
			pantallaProductos.txtPrecioCompra.setText(String.valueOf(aux.getPrecioCompra()));
			pantallaProductos.txtPrecioVenta.setText(String.valueOf(aux.getPrecioVenta()));
			pantallaProductos.txtStock.setText(String.valueOf(aux.getStock()));
			if (aux.isActivo())
				pantallaProductos.rbActivo.setSelected(true);
			else
				pantallaProductos.rbInactivo.setSelected(true);
		}
	}

	private void borrarDeBaseDatos() {
		int row = pantallaInventario.tablaInventario.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(pantallaInventario, "Elige algo para borrar primero");
			return;
		}
		String targetId = pantallaInventario.tablaInventario.getValueAt(row, 0).toString();
		int btnDialogo = JOptionPane.showConfirmDialog(pantallaInventario,
				"Borrar el articulo? " + targetId + "?", "Aviso", JOptionPane.YES_NO_OPTION);
		if (btnDialogo == JOptionPane.YES_OPTION) {
			gestionModelo.eliminar(targetId);
			guardarTodo();
		}
	}

	private void cargarComboProductos() {
		if (pantallaCaja != null) {
			pantallaCaja.cbProductos.removeAllItems();
			for (Producto p : gestionModelo.listar()) {
				if (p.isActivo() && p.getStock() > 0) {
					pantallaCaja.cbProductos.addItem(p.getId() + " - " + p.getNombre());
				}
			}
		}
	}

	private void meterAlCarrito() {
		if (pantallaCaja.cbProductos.getSelectedItem() == null) return;

		String textoCombo = pantallaCaja.cbProductos.getSelectedItem().toString();
		String clv = textoCombo.split(" - ")[0];
		Producto item = gestionModelo.buscar(clv);

		try {
			int c = Integer.parseInt(pantallaCaja.txtCantidad.getText().trim());
			if (c <= 0 || c > item.getStock()) {
				JOptionPane.showMessageDialog(pantallaCaja,
						"No hay suficiente stock. solo hay: " + item.getStock());
				return;
			}

			double calcTotal = c * item.getPrecioVenta();
			pantallaCaja.modeloTicket.addRow(new Object[] { item.getId(), item.getNombre(), c, item.getPrecioVenta(), calcTotal });

			sumarCaja();
			pantallaCaja.txtCantidad.setText("");

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(pantallaCaja, "Escribe un numero bien en la cantidad");
		}
	}

	private void quitarProductoLista() {
		int filaSeleccionada = pantallaCaja.tablaTicket.getSelectedRow();
		if (filaSeleccionada != -1) {
			pantallaCaja.modeloTicket.removeRow(filaSeleccionada);
			sumarCaja();
		} else {
			JOptionPane.showMessageDialog(pantallaCaja, "Selecciona de la tabla lo que quieras quitar");
		}
	}

	private void sumarCaja() {
		acumuladoCaja = 0.0;
		for (int idx = 0; idx < pantallaCaja.modeloTicket.getRowCount(); idx++) {
			acumuladoCaja += Double.parseDouble(pantallaCaja.modeloTicket.getValueAt(idx, 4).toString());
		}

		double imp = acumuladoCaja * 0.16;
		double tot = acumuladoCaja + imp;

		pantallaCaja.txtSubtotal.setText(String.format("%.2f", acumuladoCaja));
		pantallaCaja.txtIva.setText(String.format("%.2f", imp));
		pantallaCaja.txtTotal.setText(String.format("%.2f", tot));
	}

	private void cobrarTicket() {
        if (pantallaCaja.modeloTicket.getRowCount() == 0) {
            JOptionPane.showMessageDialog(pantallaCaja, "No has agregado nada al carrito.");
            return;
        }

        // descontar del inventario
        for (int i = 0; i < pantallaCaja.modeloTicket.getRowCount(); i++) {
            String idProd = pantallaCaja.modeloTicket.getValueAt(i, 0).toString();
            int cantidadLlevada = Integer.parseInt(pantallaCaja.modeloTicket.getValueAt(i, 2).toString());
            Producto pObj = gestionModelo.buscar(idProd);
            if (pObj != null) {
                pObj.setStock(pObj.getStock() - cantidadLlevada);
                gestionModelo.actualizar(pObj);
            }
        }

        String fechaString = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
        double sub = Double.parseDouble(pantallaCaja.txtSubtotal.getText());
        double impuesto = Double.parseDouble(pantallaCaja.txtIva.getText());
        double finalPago = Double.parseDouble(pantallaCaja.txtTotal.getText());
        
        managerArchivos.guardarTicketJSON(folioActual, fechaString, sub, impuesto, finalPago, pantallaCaja.modeloTicket);

        imprimirTicketTexto();
        
        JOptionPane.showMessageDialog(pantallaCaja, "Venta Exitosa \nTotal a pagar: $" + pantallaCaja.txtTotal.getText() + "\nTu ticket: FOLIO" + String.format("%03d", folioActual));
        guardarTodo();
        borrarCarrito();

        folioActual++; 
        pintarInfoFolio(); 
    }

	private void borrarCarrito() {
		pantallaCaja.modeloTicket.setRowCount(0);
		resetearCuenta();
	}

	private void resetearCuenta() {
		acumuladoCaja = 0.0;
		pantallaCaja.txtSubtotal.setText("0.0");
		pantallaCaja.txtIva.setText("0.0");
		pantallaCaja.txtTotal.setText("0.0");
	}

	private void guardarTodo() {
		managerArchivos.exportarJSON(gestionModelo.listar());

		if (pantallaProductos != null && !pantallaProductos.isClosed())
			recargarTablaBD();
		if (pantallaInventario != null && !pantallaInventario.isClosed())
			llenarGridInventario(gestionModelo.listar());
		if (pantallaCaja != null && !pantallaCaja.isClosed())
			cargarComboProductos();
	}

	private void pintarInfoFolio() {
		if (pantallaCaja != null) {
			String horaActual = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
			String stringFolio = String.format("%03d", folioActual);
			pantallaCaja.lblInfoTicket.setText("  Fecha: " + horaActual + "   |   Cajero Activo   |   Ticket #" + stringFolio);
		}
	}
	
	private void generarExcel() {
		if (gestionModelo.listar().isEmpty()) {
			JOptionPane.showMessageDialog(pantallaProductos, "La bd esta null no hay nada que exportar");
			return;
		}
		
		archivo.GestorExcel generadorExcel = new archivo.GestorExcel();
		generadorExcel.generarReporte(gestionModelo.listar());
		
		JOptionPane.showMessageDialog(pantallaProductos, "Exportado en Reportes_Inventario.xlsx \n");
	}
	
	private void refrescarFotoCaja() {
		if (pantallaCaja == null || pantallaCaja.cbProductos.getSelectedItem() == null) {
			if (pantallaCaja != null) {
				pantallaCaja.lblImagenProducto.setIcon(null);
				pantallaCaja.lblImagenProducto.setText("N/A");
			}
			return;
		}

		String strItem = pantallaCaja.cbProductos.getSelectedItem().toString();
		String idUnico = strItem.split(" - ")[0];
		Producto prod = gestionModelo.buscar(idUnico);

		if (prod != null && prod.getRutaImagen() != null && !prod.getRutaImagen().trim().isEmpty()) {
			try {
				ImageIcon iconoReal = new ImageIcon("imagenes/" + prod.getRutaImagen());
				Image reescalada = iconoReal.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
				pantallaCaja.lblImagenProducto.setIcon(new ImageIcon(reescalada));
				pantallaCaja.lblImagenProducto.setText(""); 
			} catch (Exception ex) {
				pantallaCaja.lblImagenProducto.setIcon(null);
				pantallaCaja.lblImagenProducto.setText("Foto perdida");
			}
		} else {
			pantallaCaja.lblImagenProducto.setIcon(null);
			pantallaCaja.lblImagenProducto.setText("N/A");
		}
	}
	
	private void cambiarFotoFormulario() {
		if (pantallaProductos == null || pantallaProductos.cbCategoria.getSelectedItem() == null) return;
		
		String c = pantallaProductos.cbCategoria.getSelectedItem().toString();
		String img = "";
		switch (c) {
			case "Abarrotes": img = "abarrotes.jpg"; break;
			case "Bebidas": img = "bebidas.jpg"; break;
			case "Lácteos y Huevo": img = "lacteos.jpg"; break;
			case "Frutas y Verduras": img = "frutas.jpg"; break;
			case "Carnes y Pescados": img = "carnes.jpg"; break;
			case "Salchichonería": img = "salchichoneria.jpg"; break;
			case "Panadería y Tortillería": img = "panaderia.jpg"; break;
			case "Limpieza del Hogar": img = "limpieza.jpg"; break;
			case "Cuidado Personal": img = "cuidado.jpg"; break;
			case "Snacks y Dulcería": img = "snacks.jpg"; break;
			case "Mascotas": img = "mascotas.jpg"; break;
			default: img = "sin_imagen.jpg";
		}
		
		try {
			ImageIcon imgOriginal = new ImageIcon("imagenes/" + img);
			Image chica = imgOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			pantallaProductos.lblImagenProducto.setIcon(new ImageIcon(chica));
			pantallaProductos.lblImagenProducto.setText("");
		} catch (Exception ex) {
			pantallaProductos.lblImagenProducto.setIcon(null);
			pantallaProductos.lblImagenProducto.setText("Sin Cargar");
		}
	}
	
	private void imprimirTicketTexto() {
		String fechaActual = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
		String nomArchivo = "Historial_Ventas.txt";

		try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(nomArchivo, true))) {
			bw.write("========================================="); bw.newLine();
			bw.write("          SURTI-TIENDA ABARROTES         "); bw.newLine();
			bw.write("========================================="); bw.newLine();
			bw.write("Ticket #: " + String.format("%03d", folioActual)); bw.newLine();
			bw.write("Fecha y Hora: " + fechaActual); bw.newLine();
			bw.write("Atendido por: Cajero Activo"); bw.newLine();
			bw.write("-----------------------------------------"); bw.newLine();
			bw.write("CANT | PRODUCTO | P.UNIT | SUBTOTAL"); bw.newLine();
			bw.write("-----------------------------------------"); bw.newLine();

			for (int i = 0; i < pantallaCaja.modeloTicket.getRowCount(); i++) {
				String c = pantallaCaja.modeloTicket.getValueAt(i, 2).toString();
				String n = pantallaCaja.modeloTicket.getValueAt(i, 1).toString();
				String pu = pantallaCaja.modeloTicket.getValueAt(i, 3).toString();
				String sub = pantallaCaja.modeloTicket.getValueAt(i, 4).toString();

				bw.write(c + " x " + n + " | $" + pu + " | $" + sub);
				bw.newLine();
			}

			bw.write("-----------------------------------------"); bw.newLine();
			bw.write("SUBTOTAL: $" + pantallaCaja.txtSubtotal.getText()); bw.newLine();
			bw.write("IVA (16%): $" + pantallaCaja.txtIva.getText()); bw.newLine();
			bw.write("TOTAL: $" + pantallaCaja.txtTotal.getText()); bw.newLine();
			bw.write("=========================================\n\n");

		} catch (java.io.IOException e) {
			System.out.println("Error al crear el txt del ticket");
		}
	}
}