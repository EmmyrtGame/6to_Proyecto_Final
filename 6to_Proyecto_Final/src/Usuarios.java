import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class Usuarios extends JPanel {
    private JTextField txtBusqueda;
    private JComboBox<String> cmbFiltro;
    private JTable tblUsuarios;
    private UsuariosDAO dao = new UsuariosDAO();
    private Sesion sesionActual;
    
    // Componentes para el formulario de edición/creación
    private JTextField txtEditando;
    private JTextField txtUsuario;
    private JTextField txtContrasena;
    private JTextField txtNombre;
    private JComboBox<String> cmbRol;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnEliminar;
    private JLabel lblEditando;
    private JLabel lblRegistros;
    
    private JLabel lblErrorUsuario;
    private JLabel lblErrorContrasena;
    private JLabel lblErrorNombre;
    
    private boolean modoEdicion = false;
    private int idUsuarioSeleccionado = -1;

    /**
     * Constructor de la clase
     */
    public Usuarios(Sesion sesionActual) {
        this.sesionActual = sesionActual;
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(10, 10));
        
        iniciarPanelSuperior();
        iniciarTabla();
        iniciarPanelEdicion();
        iniciarPanelInferior();
        cargarUsuarios();
    }
    
    private void iniciarPanelSuperior() {
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("Gestión de Usuarios");
        title.setFont(new Font("Century Gothic", Font.BOLD, 24));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.add(title);
        top.add(titlePanel);

        JPanel busquedaPanel = new JPanel(new GridBagLayout());
        
        // Configuración del filtro
        GridBagConstraints filterLabelGbc = new GridBagConstraints();
        filterLabelGbc.gridx = 1;
        filterLabelGbc.gridy = 0;
        filterLabelGbc.weightx = 0.0;
        filterLabelGbc.fill = GridBagConstraints.HORIZONTAL;
        filterLabelGbc.anchor = GridBagConstraints.CENTER;
        filterLabelGbc.insets = new Insets(0, 5, 0, 5);
        JLabel filterLabel = new JLabel("Filtro:");
        busquedaPanel.add(filterLabel, filterLabelGbc);

        // ComboBox para filtros
        GridBagConstraints comboGbc = new GridBagConstraints();
        comboGbc.gridx = 1;
        comboGbc.gridy = 1;
        comboGbc.weighty = 1.0;
        comboGbc.insets = new Insets(0, 5, 0, 5);
        cmbFiltro = new JComboBox<>(new String[] { "Todos", "Usuario", "Nombre", "Rol" });
        cmbFiltro.setFont(new Font("Century Gothic", Font.BOLD, 12));
        cmbFiltro.setPreferredSize(new Dimension(120, 30));
        busquedaPanel.add(cmbFiltro, comboGbc);

        // Campo de búsqueda
        GridBagConstraints txtBusquedaGbc = new GridBagConstraints();
        txtBusquedaGbc.gridx = 0;
        txtBusquedaGbc.gridy = 1;
        txtBusquedaGbc.weightx = 1.0;
        txtBusquedaGbc.fill = GridBagConstraints.HORIZONTAL;
        txtBusquedaGbc.insets = new Insets(0, 5, 0, 5);
        txtBusqueda = new JTextField();
        txtBusqueda.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        txtBusqueda.setPreferredSize(new Dimension(300, 30));
        busquedaPanel.add(txtBusqueda, txtBusquedaGbc);

        // Botón de búsqueda
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridx = 2;
        buttonGbc.gridy = 1;
        buttonGbc.insets = new Insets(0, 5, 0, 5);
        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String busqueda = txtBusqueda.getText();
				String filtro = (String) cmbFiltro.getSelectedItem();
				
				if (busqueda.isEmpty()) {
					cargarUsuarios();
				} else {
					List<Sesion> usuarios = dao.obtenerBusqueda(busqueda, filtro, sesionActual.getIdUsuario());
					DefaultTableModel modelo = (DefaultTableModel) tblUsuarios.getModel();
					modelo.setRowCount(0); // Limpiar filas previas
					
					for (Sesion u : usuarios) {
						modelo.addRow(new Object[]{
							u.getIdUsuario(),
							u.getUser(),
							u.getNombre(),
							u.getRol()
						});
					}
				}
        	}
        });
        searchButton.setFont(new Font("Century Gothic", Font.BOLD, 12));
        searchButton.setPreferredSize(new Dimension(100, 30));
        busquedaPanel.add(searchButton, buttonGbc);

        top.add(busquedaPanel);
        add(top, BorderLayout.NORTH);
    }
    
    private void iniciarTabla() {
        String[] cols = {"ID", "Usuario", "Nombre", "Rol"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        tblUsuarios = new JTable(model);
        tblUsuarios.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tblUsuarios.setFillsViewportHeight(true);
        tblUsuarios.getColumnModel().getColumn(0).setMinWidth(0);
        tblUsuarios.getColumnModel().getColumn(0).setMaxWidth(0);
        tblUsuarios.getColumnModel().getColumn(0).setWidth(0);
        tblUsuarios.getColumnModel().getColumn(0).setPreferredWidth(0);

        // Listener para selección en la tabla
        tblUsuarios.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostrarDetallesUsuario();
                }
            }
        });

        add(new JScrollPane(tblUsuarios), BorderLayout.CENTER);
    }
    
    private void iniciarPanelEdicion() {
        JPanel panelEdicion = new JPanel();
        panelEdicion.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK), 
            "Datos del Usuario", 
            TitledBorder.DEFAULT_JUSTIFICATION, 
            TitledBorder.DEFAULT_POSITION, 
            new Font("Century Gothic", Font.BOLD, 14)
        ));
        panelEdicion.setPreferredSize(new Dimension(300, 0));
        panelEdicion.setLayout(new GridBagLayout());
        
        // Campo ID (solo lectura)
        lblEditando = new JLabel("Editando a: ");
        lblEditando.setVisible(modoEdicion);
        agregarComponente(panelEdicion, lblEditando, 0, 0, GridBagConstraints.NONE, 0.0);
        
        txtEditando = new JTextField();
        txtEditando.setEditable(false);
        txtEditando.setVisible(modoEdicion);
        agregarComponente(panelEdicion, txtEditando, 1, 0, GridBagConstraints.HORIZONTAL, 1.0);
        
        // Campo Usuario
        agregarComponente(panelEdicion, new JLabel("Usuario:"), 0, 1, GridBagConstraints.NONE, 0.0);
        
        txtUsuario = new JTextField();
        txtUsuario.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				validarCampo(txtUsuario.getText(), lblErrorUsuario, "usuario");
			}
		});
        agregarComponente(panelEdicion, txtUsuario, 1, 1, GridBagConstraints.HORIZONTAL, 1.0);
        
        lblErrorUsuario = new JLabel("");
        lblErrorUsuario.setForeground(new Color(128, 0, 0));
        lblErrorUsuario.setFont(new Font("Century Gothic", Font.BOLD, 11));
        lblErrorUsuario.setVisible(false);
        agregarLabelError(panelEdicion, lblErrorUsuario, 1, 2, GridBagConstraints.HORIZONTAL, 1.0);
        
        // Campo Contraseña
        agregarComponente(panelEdicion, new JLabel("Contraseña:"), 0, 3, GridBagConstraints.NONE, 0.0);
        
        txtContrasena = new JTextField();
        txtContrasena.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarCampo(txtContrasena.getText(), lblErrorContrasena, "password");
            }
        });
        agregarComponente(panelEdicion, txtContrasena, 1, 3, GridBagConstraints.HORIZONTAL, 1.0);
        
        lblErrorContrasena = new JLabel("");
        lblErrorContrasena.setForeground(new Color(128, 0, 0));
        lblErrorContrasena.setFont(new Font("Century Gothic", Font.BOLD, 11));
        lblErrorContrasena.setVisible(false);
        agregarLabelError(panelEdicion, lblErrorContrasena, 1, 4, GridBagConstraints.HORIZONTAL, 1.0);
        
        // Campo Nombre
        agregarComponente(panelEdicion, new JLabel("Nombre:"), 0, 5, GridBagConstraints.NONE, 0.0);
        
        txtNombre = new JTextField();
        txtNombre.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarCampo(txtNombre.getText(), lblErrorNombre, "nombre");
            }
        });
        agregarComponente(panelEdicion, txtNombre, 1, 5, GridBagConstraints.HORIZONTAL, 1.0);
        
        lblErrorNombre = new JLabel("");
        lblErrorNombre.setForeground(new Color(128, 0, 0));
        lblErrorNombre.setFont(new Font("Century Gothic", Font.BOLD, 11));
        lblErrorNombre.setVisible(false);
        agregarLabelError(panelEdicion, lblErrorNombre, 1, 6, GridBagConstraints.HORIZONTAL, 1.0);
        
        // Selector de Rol
        agregarComponente(panelEdicion, new JLabel("Rol:"), 0, 7, GridBagConstraints.NONE, 0.0);
        
        cmbRol = new JComboBox<>(new String[]{"Admin", "Usuario"});
        agregarComponente(panelEdicion, cmbRol, 1, 7, GridBagConstraints.HORIZONTAL, 1.0);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String usuario = txtUsuario.getText();
				String contrasena = new String(txtContrasena.getText());
				String nombre = txtNombre.getText();
				String rol = (String) cmbRol.getSelectedItem();
				
				// Validar todos los campos antes de guardar
	            boolean[] validaciones = new boolean[3];
	            validaciones[0] = validarCampo(usuario, lblErrorUsuario, "usuario");
	            validaciones[1] = validarCampo(contrasena, lblErrorContrasena, "password");
	            validaciones[2] = validarCampo(nombre, lblErrorNombre, "nombre");
	            
	            boolean valido = true;
	            for (boolean validacion : validaciones) {
	                if (!validacion) {
	                    valido = false;
	                }
	            }
	            
	            if (!valido) {
	                return;
	            }

				Sesion sesion = new Sesion(-1, usuario, contrasena, nombre, rol);				
				
				boolean correcto = false;
				if (modoEdicion) {
					int IdUsuarioSeleccionado = Integer.parseInt(tblUsuarios.getValueAt(tblUsuarios.getSelectedRow(), 0).toString());
					sesion.setId(IdUsuarioSeleccionado);
					correcto = dao.actualizarUsuario(sesion);
				} else {
					correcto = dao.insertarUsuario(sesion);
				}
				
				if (correcto) {
				    JOptionPane.showMessageDialog(null, "Usuario guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				    cargarUsuarios();
				    limpiarFormulario();
				} else {
				    JOptionPane.showMessageDialog(null, "Error al guardar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = tblUsuarios.getSelectedRow();
				if (filaSeleccionada >= 0) {
					int idUsuario = Integer.parseInt(tblUsuarios.getValueAt(filaSeleccionada, 0).toString());
					int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este usuario?", "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (confirmacion == JOptionPane.YES_OPTION) {
						boolean correcto = dao.eliminarUsuario(idUsuario);
						if (correcto) {
							JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
							modoEdicion = false;
							campoEditar();
							cargarUsuarios();
							limpiarFormulario();
						} else {
							JOptionPane.showMessageDialog(null, "Error al eliminar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Seleccione un usuario para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
        btnEliminar.setBackground(new Color(255, 100, 100));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setEnabled(modoEdicion);
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnLimpiar);
        buttonPanel.add(btnEliminar);
        
        // El panel de botones ocupa dos columnas y tiene peso vertical
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTH;
        panelEdicion.add(buttonPanel, gbc);
        
        add(panelEdicion, BorderLayout.EAST);
    }
    
    /**
     * Método para validar campos
     */
    private boolean validarCampo(String campo, JLabel errorLabel, String tipoValidacion) {
        String resultado = "";
        
        switch (tipoValidacion) {
            case "usuario":
                resultado = Validator.validarUsuario(campo);
                break;
            case "password":
                resultado = Validator.validarPassword(campo);
                break;
            case "nombre":
                resultado = Validator.validarNombre(campo);
                break;
        }
        
        if (!resultado.equals("correcto")) {
            errorLabel.setText(resultado);
            errorLabel.setVisible(true);
            return false;
        } else {
            errorLabel.setVisible(false);
            return true;
        }
    }

    /**
     * Método auxiliar para agregar componentes con restricciones específicas
     */
    private void agregarComponente(JPanel panel, Component comp, int gridx, int gridy, int fill, double weightx) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(comp, gbc);
    }
    
    /**
     * Método auxiliar para agregar labels con restricciones específicas
     */
    private void agregarLabelError(JPanel panel, Component comp, int gridx, int gridy, int fill, double weightx) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(comp, gbc);
    }

    
    private void iniciarPanelInferior() {
        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblRegistros = new JLabel((tblUsuarios.getRowCount()) + " usuarios");
        leftPanel.add(lblRegistros);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnNuevo = new JButton("Nuevo Usuario");
        btnNuevo.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnNuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	tblUsuarios.clearSelection();
                limpiarFormulario();
                modoEdicion = false;
                campoEditar();
            }
        });
        rightPanel.add(btnNuevo);
        
        bottom.add(leftPanel, BorderLayout.WEST);
        bottom.add(rightPanel, BorderLayout.EAST);
        
        add(bottom, BorderLayout.SOUTH);
    }
    
    private void limpiarFormulario() {
    	if (!modoEdicion) {
    		txtEditando.setText("");
    		tblUsuarios.clearSelection();
    		modoEdicion = false;
            idUsuarioSeleccionado = -1;
    	}
        txtUsuario.setText("");
        txtContrasena.setText("");
        txtNombre.setText("");
        cmbRol.setSelectedIndex(0);
        
        lblErrorUsuario.setVisible(false);
        lblErrorContrasena.setVisible(false);
        lblErrorNombre.setVisible(false);
    }
    
    private void mostrarDetallesUsuario() {
        int filaSeleccionada = tblUsuarios.getSelectedRow();
        if (filaSeleccionada >= 0) {
        	modoEdicion = true;
        	campoEditar();
            idUsuarioSeleccionado = Integer.parseInt(tblUsuarios.getValueAt(filaSeleccionada, 0).toString());
            txtEditando.setText(tblUsuarios.getValueAt(filaSeleccionada, 1).toString());
            txtUsuario.setText(tblUsuarios.getValueAt(filaSeleccionada, 1).toString());
            txtContrasena.setText(""); // No mostrar la contraseña por seguridad
            txtNombre.setText(tblUsuarios.getValueAt(filaSeleccionada, 2).toString());
            String rol = tblUsuarios.getValueAt(filaSeleccionada, 3).toString();
            cmbRol.setSelectedItem(rol);
            modoEdicion = true;
            
            lblErrorUsuario.setVisible(false);
            lblErrorContrasena.setVisible(false);
            lblErrorNombre.setVisible(false);
        }
    }
    
    private void campoEditar() {
		if (modoEdicion) {
			btnGuardar.setText("Actualizar");
			btnEliminar.setEnabled(true);
			txtEditando.setVisible(modoEdicion);
			lblEditando.setVisible(modoEdicion);
		} else {
			txtEditando.setVisible(modoEdicion);
			lblEditando.setVisible(modoEdicion);
			btnGuardar.setText("Guardar");
			btnEliminar.setEnabled(false);
		}
	}
    
    private void cargarUsuarios() {
        DefaultTableModel modelo = (DefaultTableModel) tblUsuarios.getModel();
        modelo.setRowCount(0); // Limpiar filas previas
        
        List<Sesion> usuarios = dao.obtenerTodos(sesionActual.getIdUsuario());
        for (Sesion u : usuarios) {
            modelo.addRow(new Object[]{
                u.getIdUsuario(),
                u.getUser(),
                u.getNombre(),
                u.getRol()
            });
        }
        
        lblRegistros.setText(tblUsuarios.getRowCount() + " usuarios");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Gestión de Usuarios");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Crear sesión de prueba
            Sesion sesionPrueba = new Sesion(1, "admin", "admin123", "Administrador", "Admin");
            
            f.setContentPane(new Usuarios(sesionPrueba));
            f.setSize(900, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
