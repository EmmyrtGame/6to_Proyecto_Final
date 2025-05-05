import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Inventario extends JPanel {
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JTable inventoryTable;
    private ProductosDAO dao = new ProductosDAO();

    /**
     * Método constructor de la clase
     */
    public Inventario() {
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Margenes exteriores
        setLayout(new BorderLayout(10, 10)); // Establece los puntos de agarre de los componentes
        iniciarPanelSuperior(); // Procedimiento que inicializa el panel superior (barra de búsqueda)
        iniciarTabla(); // Procedimiento que inicializa la tabla de datos
        cargarProductos(); // Procedimiento que carga los datos de la base de datos
    }

    /**
     * Procedimiento que inicializa todos los componentes del panel superior
     */
    private void iniciarPanelSuperior() {
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Inventario");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.add(title);
        top.add(titlePanel);

        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.gridy = 0;

        // Agregar label de filtro para el combo
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel filterLabel = new JLabel("Filtro:");
        searchPanel.add(filterLabel, gbc);

        // Reubicar el combobox en la fila inferior
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        filterCombo = new JComboBox<>(new String[] { "Todos", "Categoría", "Estado" });
        filterCombo.setPreferredSize(new Dimension(120, 30));
        searchPanel.add(filterCombo, gbc);

        // Campo de texto de búsqueda (se expande)
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(350, 30));
        searchPanel.add(searchField, gbc);


        // Botón de búsqueda
        gbc.gridx = 2;
        JButton searchButton = new JButton("Buscar");
        searchButton.setPreferredSize(new Dimension(50, 30));
        searchPanel.add(searchButton, gbc);


        top.add(searchPanel);
        add(top, BorderLayout.NORTH);
    }

    /**
     * Procedimiento que inicializa el componente de la tabla
     */
    private void iniciarTabla() {
        String[] cols = {"Nombre","Descripción","Precio","Proveedor","Categoría","Cantidad","Código"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        inventoryTable = new JTable(model);
        inventoryTable.setFillsViewportHeight(true);
        add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
    }

    /**
     * Procedimiento que lee todos los productos de la base de datos y los carga en la tabla
     */
    private void cargarProductos() {
        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        model.setRowCount(0); // limpia filas previas
        List<Productos> productos = dao.obtenerTodos();
        for (Productos p : productos) {
            model.addRow(new Object[]{
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
