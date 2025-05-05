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
        
        // Agregar label de filtro para el combo
        GridBagConstraints filterLabelGbc = new GridBagConstraints();
        filterLabelGbc.gridx = 1;
        filterLabelGbc.gridy = 0;
        filterLabelGbc.weightx = 0.0;
        filterLabelGbc.fill = GridBagConstraints.HORIZONTAL;
        filterLabelGbc.anchor = GridBagConstraints.CENTER;
        filterLabelGbc.insets = new Insets(0, 5, 0, 5);
        JLabel filterLabel = new JLabel("Filtro:");
        searchPanel.add(filterLabel, filterLabelGbc);

        // Reubicar el combobox en la fila inferior
        GridBagConstraints comboGbc = new GridBagConstraints();
        comboGbc.gridx = 1;
        comboGbc.gridy = 1;
        comboGbc.weighty = 1.0;
        comboGbc.insets = new Insets(0, 5, 0, 5);
        filterCombo = new JComboBox<>(new String[] { "Todos", "Categoría", "Estado" });
        filterCombo.setPreferredSize(new Dimension(120, 30));
        searchPanel.add(filterCombo, comboGbc);

        // Campo de texto de búsqueda (se expande)
        GridBagConstraints searchFieldGbc = new GridBagConstraints();
        searchFieldGbc.gridx = 0;
        searchFieldGbc.gridy = 1;
        searchFieldGbc.weightx = 1.0;
        searchFieldGbc.fill = GridBagConstraints.HORIZONTAL;
        searchFieldGbc.insets = new Insets(0, 5, 0, 5);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 30));
        searchPanel.add(searchField, searchFieldGbc);

        // Botón de búsqueda
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridx = 2;
        buttonGbc.gridy = 1;
        buttonGbc.insets = new Insets(0, 5, 0, 5);
        JButton searchButton = new JButton("Buscar");
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchPanel.add(searchButton, buttonGbc);

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
