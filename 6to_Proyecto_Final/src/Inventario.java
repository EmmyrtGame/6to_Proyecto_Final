import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Inventario extends JPanel {
    private JTextField txtBusqueda;
    private JComboBox<String> cmbFiltro;
    private JTable tblInventario;
    private ProductosDAO dao = new ProductosDAO();

    /**
     * Método constructor de la clase
     */
    public Inventario() {
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Margenes exteriores
        setLayout(new BorderLayout(10, 10)); // Establece los puntos de agarre de los componentes
        iniciarPanelSuperior(); // Procedimiento que inicializa el panel superior (barra de búsqueda)
        iniciarTabla(); // Procedimiento que inicializa la tabla de datos
        iniciarPanelInferior();
        cargarProductos(); // Procedimiento que carga los datos de la base de datos
    }

    /**
     * Procedimiento que inicializa todos los componentes del panel superior
     */
    private void iniciarPanelSuperior() {
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Inventario");
        title.setFont(new Font("Century Gothic", Font.BOLD, 24));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.add(title);
        top.add(titlePanel);

        JPanel busquedaPanel = new JPanel(new GridBagLayout());
        
        // Agregar label de filtro para el combo
        GridBagConstraints filterLabelGbc = new GridBagConstraints();
        filterLabelGbc.gridx = 1;
        filterLabelGbc.gridy = 0;
        filterLabelGbc.weightx = 0.0;
        filterLabelGbc.fill = GridBagConstraints.HORIZONTAL;
        filterLabelGbc.anchor = GridBagConstraints.CENTER;
        filterLabelGbc.insets = new Insets(0, 5, 0, 5);
        JLabel filterLabel = new JLabel("Filtro:");
        busquedaPanel.add(filterLabel, filterLabelGbc);

        // Reubicar el combobox en la fila inferior
        GridBagConstraints comboGbc = new GridBagConstraints();
        comboGbc.gridx = 1;
        comboGbc.gridy = 1;
        comboGbc.weighty = 1.0;
        comboGbc.insets = new Insets(0, 5, 0, 5);
        cmbFiltro = new JComboBox<>(new String[] { "Todos", "Nombre", "Descripción", "Precio", "Proveedor", "Categoría", "Cantidad", "Código" });
        cmbFiltro.setFont(new Font("Century Gothic", Font.BOLD, 12));
        cmbFiltro.setPreferredSize(new Dimension(120, 30));
        busquedaPanel.add(cmbFiltro, comboGbc);

        // Campo de texto de búsqueda
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
        searchButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String busquedaInput = txtBusqueda.getText();
        		String filtro = cmbFiltro.getSelectedItem().toString();
        		cargarBusqueda(busquedaInput, filtro);
        	}
        });
        searchButton.setPreferredSize(new Dimension(100, 30));
        busquedaPanel.add(searchButton, buttonGbc);

        top.add(busquedaPanel);
        add(top, BorderLayout.NORTH);
    }
    
    /**
     * Procedimiento que inicializa el panel inferior con botones de acción
     */
    private void iniciarPanelInferior() {
        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnEliminar = new JButton("Eliminar Producto");
        btnEliminar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnEliminar.setBackground(new Color(255, 100, 100));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarProductoSeleccionado();
            }
        });
        
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnAgregar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mostrarModalAgregar();
        	}
        });
        
        bottom.add(btnAgregar);
        bottom.add(btnEliminar);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Procedimiento que muestra el modal de agregar
     */
    private void mostrarModalAgregar() {
    	ModalAgregar agregar = new ModalAgregar();
    	cargarProductos();
    }

    /**
     * Procedimiento que inicializa el componente de la tabla
     */
    private void iniciarTabla() {
        String[] cols = {"Nombre","Descripción","Precio","Proveedor","Categoría","Cantidad","Código"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblInventario = new JTable(model);
        tblInventario.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        tblInventario.setFillsViewportHeight(true);
        add(new JScrollPane(tblInventario), BorderLayout.CENTER);
    }
    
    /**
     * Procedimiento que manda a llamar y lee la búsqueda de los productos en la base de datos.
     */
    private void cargarBusqueda(String busquedaInput, String filtroInput) {
    	DefaultTableModel modelo = (DefaultTableModel) tblInventario.getModel();
    	modelo.setRowCount(0);
    	List<Productos> productos = dao.obtenerBusqueda(busquedaInput, filtroInput);
    	for (Productos p : productos) {
            modelo.addRow(new Object[]{
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getProveedor(),
                p.getCategoria(),
                p.getCantidad(),
                p.getCodigo()
            });
        }
    }

    /**
     * Procedimiento que lee todos los productos de la base de datos y los carga en la tabla
     */
    private void cargarProductos() {
        DefaultTableModel modelo = (DefaultTableModel) tblInventario.getModel();
        modelo.setRowCount(0); // limpia filas previas
        List<Productos> productos = dao.obtenerTodos();
        for (Productos p : productos) {
            modelo.addRow(new Object[]{
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getProveedor(),
                p.getCategoria(),
                p.getCantidad(),
                p.getCodigo()
            });
        }
    }
    
    /**
     * Elimina el producto seleccionado en la tabla
     */
    private void eliminarProductoSeleccionado() {
        int filaSeleccionada = tblInventario.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // Obtener el código del producto seleccionado
            String codigo = (String) tblInventario.getValueAt(filaSeleccionada, 6);
            
            // Mostrar confirmación
            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar el producto con código " + codigo + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                // Buscar el producto por su código
                List<Productos> productos = dao.obtenerBusqueda(codigo, "Código");
                if (!productos.isEmpty()) {
                    int id = productos.get(0).getId();
                    boolean eliminado = dao.eliminar(id);
                    
                    if (eliminado) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Producto eliminado con éxito",
                            "Eliminación exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        // Recargar la tabla
                        cargarProductos();
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "No se pudo eliminar el producto",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Por favor, seleccione un producto de la tabla",
                "Aviso",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Panel de Inventario");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setContentPane(new Inventario());
            f.setSize(800,600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
