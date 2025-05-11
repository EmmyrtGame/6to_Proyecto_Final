import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
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
    private JTextField txtId;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JTextField txtNombre;
    private JComboBox<String> cmbRol;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnEliminar;
    
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
        cargarUsuarios();
        iniciarPanelInferior();
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
        cmbFiltro = new JComboBox<>(new String[] { "Todos", "ID", "Usuario", "Nombre", "Rol" });
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
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        // Campo ID (solo lectura)
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelEdicion.add(new JLabel("ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtId = new JTextField();
        txtId.setEditable(false);
        panelEdicion.add(txtId, gbc);
        
        // Campo Usuario
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panelEdicion.add(new JLabel("Usuario:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtUsuario = new JTextField();
        panelEdicion.add(txtUsuario, gbc);
        
        // Campo Contraseña
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        panelEdicion.add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtContrasena = new JPasswordField();
        panelEdicion.add(txtContrasena, gbc);
        
        // Campo Nombre
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panelEdicion.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNombre = new JTextField();
        panelEdicion.add(txtNombre, gbc);
        
        // Selector de Rol
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        panelEdicion.add(new JLabel("Rol:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbRol = new JComboBox<>(new String[]{"Admin", "Usuario"});
        panelEdicion.add(cmbRol, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnEliminar.setBackground(new Color(255, 100, 100));
        btnEliminar.setForeground(Color.WHITE);
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnLimpiar);
        buttonPanel.add(btnEliminar);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        
        panelEdicion.add(buttonPanel, gbc);
        
        add(panelEdicion, BorderLayout.EAST);
    }
    
    private void iniciarPanelInferior() {
        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblRegistros = new JLabel((tblUsuarios.getRowCount()) + " usuarios");
        leftPanel.add(lblRegistros);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnNuevo = new JButton("Nuevo Usuario");
        btnNuevo.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnNuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
                modoEdicion = false;
            }
        });
        
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnRefrescar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarUsuarios();
            }
        });
        
        rightPanel.add(btnRefrescar);
        rightPanel.add(btnNuevo);
        
        bottom.add(leftPanel, BorderLayout.WEST);
        bottom.add(rightPanel, BorderLayout.EAST);
        
        add(bottom, BorderLayout.SOUTH);
    }
    
    private void limpiarFormulario() {
        txtId.setText("");
        txtUsuario.setText("");
        txtContrasena.setText("");
        txtNombre.setText("");
        cmbRol.setSelectedIndex(0);
        modoEdicion = false;
        idUsuarioSeleccionado = -1;
        tblUsuarios.clearSelection();
    }
    
    private void mostrarDetallesUsuario() {
        int filaSeleccionada = tblUsuarios.getSelectedRow();
        if (filaSeleccionada >= 0) {
            idUsuarioSeleccionado = Integer.parseInt(tblUsuarios.getValueAt(filaSeleccionada, 0).toString());
            txtId.setText(tblUsuarios.getValueAt(filaSeleccionada, 0).toString());
            txtUsuario.setText(tblUsuarios.getValueAt(filaSeleccionada, 1).toString());
            txtContrasena.setText(""); // No mostrar la contraseña por seguridad
            txtNombre.setText(tblUsuarios.getValueAt(filaSeleccionada, 2).toString());
            String rol = tblUsuarios.getValueAt(filaSeleccionada, 3).toString();
            cmbRol.setSelectedItem(rol);
            modoEdicion = true;
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
