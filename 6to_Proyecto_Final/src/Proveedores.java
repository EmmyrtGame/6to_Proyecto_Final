import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.text.ParseException;

public class Proveedores extends JPanel {
    private JTextField txtBusqueda;
    private JComboBox<String> cmbFiltro;
    private JTable tblProveedores;
    private ProveedoresDAO dao = new ProveedoresDAO();
    private Sesion sesionActual;
    private String originalNombre = "";
    private String originalDireccion = "";
    private String originalTelefono = "";
    private String originalCorreo = "";
    
    // Componentes para el formulario de edición/creación
    private JTextField txtEditando;
    private JTextField txtNombre;
    private JTextField txtDireccion;
    private JFormattedTextField txtTelefono;
    private JTextField txtCorreo;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnEliminar;
    private JLabel lblEditando;
    private JLabel lblRegistros;
    
    private JLabel lblErrorNombre;
    private JLabel lblErrorDireccion;
    private JLabel lblErrorTelefono;
    private JLabel lblErrorCorreo;
    
    private boolean modoEdicion = false;
    private int idProveedorSeleccionado = -1;

    /**
     * Constructor de la clase
     */
    public Proveedores(Sesion sesionActual) {
        this.sesionActual = sesionActual;
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(10, 10));
        
        iniciarPanelSuperior();
        iniciarTabla();
        iniciarPanelEdicion();
        iniciarPanelInferior();
        cargarProveedores();
    }
    
    private void iniciarPanelSuperior() {
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("Gestión de Proveedores");
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
        cmbFiltro = new JComboBox<>(new String[] { "Todos", "Nombre", "Dirección", "Teléfono", "Correo" });
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
                    cargarProveedores();
                } else {
                    List<Proveedor> proveedores = dao.obtenerBusqueda(busqueda, filtro);
                    DefaultTableModel modelo = (DefaultTableModel) tblProveedores.getModel();
                    modelo.setRowCount(0); // Limpiar filas previas
                    
                    for (Proveedor p : proveedores) {
                        modelo.addRow(new Object[]{
                            p.getId(),
                            p.getNombre(),
                            p.getDireccion(),
                            p.getTelefono(),
                            p.getCorreo()
                        });
                    }
                    lblRegistros.setText(tblProveedores.getRowCount() + " proveedores");
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
        String[] cols = {"ID", "Nombre", "Dirección", "Teléfono", "Correo"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        tblProveedores = new JTable(model);
        tblProveedores.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tblProveedores.setFillsViewportHeight(true);
        tblProveedores.getColumnModel().getColumn(0).setMinWidth(0);
        tblProveedores.getColumnModel().getColumn(0).setMaxWidth(0);
        tblProveedores.getColumnModel().getColumn(0).setWidth(0);
        tblProveedores.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        tblProveedores.setRowHeight(50);

        // Listener para selección en la tabla
        tblProveedores.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostrarDetallesProveedor();
                }
            }
        });

        add(new JScrollPane(tblProveedores), BorderLayout.CENTER);
    }
    
    private void iniciarPanelEdicion() {
        JPanel panelEdicion = new JPanel();
        panelEdicion.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK), 
            "Datos del Proveedor", 
            TitledBorder.DEFAULT_JUSTIFICATION, 
            TitledBorder.DEFAULT_POSITION, 
            new Font("Century Gothic", Font.BOLD, 14)
        ));
        panelEdicion.setPreferredSize(new Dimension(350, 0));
        panelEdicion.setLayout(new GridBagLayout());
        
        // Campo ID (solo lectura)
        lblEditando = new JLabel("Editando a: ");
        lblEditando.setVisible(modoEdicion);
        agregarComponente(panelEdicion, lblEditando, 0, 0, GridBagConstraints.NONE, 0.0);
        
        txtEditando = new JTextField();
        txtEditando.setEditable(false);
        txtEditando.setVisible(modoEdicion);
        agregarComponente(panelEdicion, txtEditando, 1, 0, GridBagConstraints.HORIZONTAL, 1.0);
        
        // Campo Nombre
        agregarComponente(panelEdicion, new JLabel("Nombre:"), 0, 1, GridBagConstraints.NONE, 0.0);
        
        txtNombre = new JTextField();
        txtNombre.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	validarCampo(txtNombre.getText(), lblErrorNombre, "nombre");
            }
        });
        agregarComponente(panelEdicion, txtNombre, 1, 1, GridBagConstraints.HORIZONTAL, 1.0);
        
        lblErrorNombre = new JLabel("");
        lblErrorNombre.setForeground(new Color(128, 0, 0));
        lblErrorNombre.setFont(new Font("Century Gothic", Font.BOLD, 11));
        lblErrorNombre.setVisible(false);
        agregarLabelError(panelEdicion, lblErrorNombre, 1, 2, GridBagConstraints.HORIZONTAL, 1.0);
        
        // Campo Dirección
        agregarComponente(panelEdicion, new JLabel("Dirección:"), 0, 3, GridBagConstraints.NONE, 0.0);
        
        txtDireccion = new JTextField();
        txtDireccion.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
            	validarCampo(txtDireccion.getText(), lblErrorDireccion, "direccion");
            }
        });
        agregarComponente(panelEdicion, txtDireccion, 1, 3, GridBagConstraints.HORIZONTAL, 1.0);
        
        lblErrorDireccion = new JLabel("");
        lblErrorDireccion.setForeground(new Color(128, 0, 0));
        lblErrorDireccion.setFont(new Font("Century Gothic", Font.BOLD, 11));
        lblErrorDireccion.setVisible(false);
        agregarLabelError(panelEdicion, lblErrorDireccion, 1, 4, GridBagConstraints.HORIZONTAL, 1.0);
        
        // Campo Teléfono con formato
        agregarComponente(panelEdicion, new JLabel("Teléfono:"), 0, 5, GridBagConstraints.NONE, 0.0);
        
        try {
            MaskFormatter telefonoFormatter = new MaskFormatter("##-####-####");
            telefonoFormatter.setPlaceholderCharacter('_');
            txtTelefono = new JFormattedTextField(telefonoFormatter);
        } catch (ParseException e) {
            txtTelefono = new JFormattedTextField();
        }
        txtTelefono.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
            	validarCampo(txtTelefono.getText(), lblErrorTelefono, "telefono");
            }
        });
        agregarComponente(panelEdicion, txtTelefono, 1, 5, GridBagConstraints.HORIZONTAL, 1.0);
        
        lblErrorTelefono = new JLabel("");
        lblErrorTelefono.setForeground(new Color(128, 0, 0));
        lblErrorTelefono.setFont(new Font("Century Gothic", Font.BOLD, 11));
        lblErrorTelefono.setVisible(false);
        agregarLabelError(panelEdicion, lblErrorTelefono, 1, 6, GridBagConstraints.HORIZONTAL, 1.0);
        
        // Campo Correo
        agregarComponente(panelEdicion, new JLabel("Correo:"), 0, 7, GridBagConstraints.NONE, 0.0);
        
        txtCorreo = new JTextField();
        txtCorreo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
            	validarCampo(txtCorreo.getText(), lblErrorCorreo, "correo");
            }
        });
        agregarComponente(panelEdicion, txtCorreo, 1, 7, GridBagConstraints.HORIZONTAL, 1.0);
        
        lblErrorCorreo = new JLabel("");
        lblErrorCorreo.setForeground(new Color(128, 0, 0));
        lblErrorCorreo.setFont(new Font("Century Gothic", Font.BOLD, 11));
        lblErrorCorreo.setVisible(false);
        agregarLabelError(panelEdicion, lblErrorCorreo, 1, 8, GridBagConstraints.HORIZONTAL, 1.0);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!hayCambios()) {
                    JOptionPane.showMessageDialog(null, "No se han realizado cambios para guardar", 
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                String nombre = txtNombre.getText();
                String direccion = txtDireccion.getText();
                String telefono = txtTelefono.getText();
                String correo = txtCorreo.getText();
                
                // Validar todos los campos antes de guardar
                boolean[] validaciones = new boolean[4];
                validaciones[0] = validarCampo(nombre, lblErrorNombre, "nombre");
                validaciones[1] = validarCampo(direccion, lblErrorDireccion, "direccion");
                validaciones[2] = validarCampo(telefono, lblErrorTelefono, "telefono");
                validaciones[3] = validarCampo(correo, lblErrorCorreo, "correo");

                boolean valido = true;
                for (boolean validacion : validaciones) {
                    if (!validacion) {
                        valido = false;
                    }
                }
                if (!valido) {
                    return;
                }
                
                // Validaciones básicas
                if (nombre.trim().isEmpty() || direccion.trim().isEmpty() || 
                    telefono.trim().isEmpty() || correo.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", 
                        "Error de Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                Proveedor proveedor = new Proveedor(-1, nombre, direccion, telefono, correo);
                
                boolean correcto = false;
                try {
                    if (modoEdicion) {
                        int idProveedorSeleccionado = Integer.parseInt(tblProveedores.getValueAt(tblProveedores.getSelectedRow(), 0).toString());
                        proveedor.setId(idProveedorSeleccionado);
                        correcto = dao.modificar(proveedor);
                    } else {
                        correcto = dao.registrar(proveedor);
                    }
                    
                    if (correcto) {
                        JOptionPane.showMessageDialog(null, "Proveedor guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarProveedores();
                        limpiarFormulario();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al guardar el proveedor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                int filaSeleccionada = tblProveedores.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int idProveedor = Integer.parseInt(tblProveedores.getValueAt(filaSeleccionada, 0).toString());
                    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este proveedor?", "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirmacion == JOptionPane.YES_OPTION) {
                        boolean correcto = dao.eliminar(idProveedor);
                        if (correcto) {
                            JOptionPane.showMessageDialog(null, "Proveedor eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            modoEdicion = false;
                            campoEditar();
                            cargarProveedores();
                            limpiarFormulario();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al eliminar el proveedor", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un proveedor para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
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
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 9;
        buttonGbc.gridwidth = 2;
        buttonGbc.weightx = 1.0;
        buttonGbc.weighty = 1.0;
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.insets = new Insets(5, 5, 5, 5);
        buttonGbc.anchor = GridBagConstraints.NORTH;
        panelEdicion.add(buttonPanel, buttonGbc);
        
        add(panelEdicion, BorderLayout.EAST);
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
     * Método auxiliar para agregar labels de error con restricciones específicas
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
        lblRegistros = new JLabel("0 proveedores");
        leftPanel.add(lblRegistros);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnNuevo = new JButton("Nuevo Proveedor");
        btnNuevo.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnNuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tblProveedores.clearSelection();
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
    
    /**
     * Método para validar campos de proveedores
     */
    private boolean validarCampo(String campo, JLabel errorLabel, String tipoValidacion) {
        String resultado = "";
        
        switch (tipoValidacion) {
            case "nombre":
                resultado = Validator.validarProveedor(campo);
                break;
            case "direccion":
                resultado = Validator.validarDireccion(campo);
                break;
            case "telefono":
                resultado = Validator.validarTelefono(campo);
                break;
            case "correo":
                resultado = Validator.validarCorreo(campo);
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
    
    private void limpiarFormulario() {
        if (!modoEdicion) {
            txtEditando.setText("");
            tblProveedores.clearSelection();
            modoEdicion = false;
            idProveedorSeleccionado = -1;
        }
        txtNombre.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        
        lblErrorNombre.setVisible(false);
        lblErrorDireccion.setVisible(false);
        lblErrorTelefono.setVisible(false);
        lblErrorCorreo.setVisible(false);
    }
    
    private void mostrarDetallesProveedor() {
        int filaSeleccionada = tblProveedores.getSelectedRow();
        if (filaSeleccionada >= 0) {
            modoEdicion = true;
            campoEditar();
            idProveedorSeleccionado = Integer.parseInt(tblProveedores.getValueAt(filaSeleccionada, 0).toString());
            txtEditando.setText(tblProveedores.getValueAt(filaSeleccionada, 1).toString());
            originalNombre = tblProveedores.getValueAt(filaSeleccionada, 1).toString();
            txtNombre.setText(originalNombre);
            originalDireccion = tblProveedores.getValueAt(filaSeleccionada, 2).toString();
            txtDireccion.setText(originalDireccion);
            originalTelefono = tblProveedores.getValueAt(filaSeleccionada, 3).toString();
            txtTelefono.setText(originalTelefono);
            originalCorreo = tblProveedores.getValueAt(filaSeleccionada, 4).toString();
            txtCorreo.setText(originalCorreo);
            modoEdicion = true;
            
            lblErrorNombre.setVisible(false);
            lblErrorDireccion.setVisible(false);
            lblErrorTelefono.setVisible(false);
            lblErrorCorreo.setVisible(false);
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
    
    private void cargarProveedores() {
        DefaultTableModel modelo = (DefaultTableModel) tblProveedores.getModel();
        modelo.setRowCount(0); // Limpiar filas previas
        
        List<Proveedor> proveedores = dao.leer();
        for (Proveedor p : proveedores) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getDireccion(),
                p.getTelefono(),
                p.getCorreo()
            });
        }
        
        lblRegistros.setText(tblProveedores.getRowCount() + " proveedores");
    }
    
    private boolean hayCambios() {
        if (!modoEdicion) {
            return !txtNombre.getText().isEmpty() || 
                   !txtDireccion.getText().isEmpty() || 
                   !txtTelefono.getText().isEmpty() || 
                   !txtCorreo.getText().isEmpty();
        } else {
            String nombreActual = txtNombre.getText();
            String direccionActual = txtDireccion.getText();
            String telefonoActual = txtTelefono.getText();
            String correoActual = txtCorreo.getText();
            
            return !nombreActual.equals(originalNombre) || 
                   !direccionActual.equals(originalDireccion) || 
                   !telefonoActual.equals(originalTelefono) || 
                   !correoActual.equals(originalCorreo);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Gestión de Proveedores");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Crear sesión de prueba
            Sesion sesionPrueba = new Sesion(1, "admin", "admin123", "Administrador", "Admin");
            
            f.setContentPane(new Proveedores(sesionPrueba));
            f.setSize(1000, 700);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}