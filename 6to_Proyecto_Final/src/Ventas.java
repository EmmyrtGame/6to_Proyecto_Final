import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class Ventas extends JPanel {
    private JTable productosTable;
    private JTable carritoTable;
    private DefaultTableModel modelProductos;
    private DefaultTableModel modelCarrito;
    private ProductosDAO dao = new ProductosDAO();
    private JLabel lblTotal;
    private JLabel lblTotalIVA;
    private List<Productos> carrito = new ArrayList<>();
    private JButton btnQuitarCarrito;
    private JTextField searchField;
    private JComboBox<String> filterCombo;

    public Ventas() {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(10, 10));
        
        add(crearPanelBusqueda(), BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(crearPanelSuperior(), BorderLayout.NORTH);
        mainPanel.add(crearTablas(), BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        cargarProductos();
    }

    private JPanel crearPanelBusqueda() {
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("Ventas");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.add(title);
        top.add(titlePanel);

        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Campo de búsqueda
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 30));
        searchPanel.add(searchField, gbc);

        // Combo de filtros
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0;
        filterCombo = new JComboBox<>(new String[] { "Todos", "Categoría", "Proveedor" });
        filterCombo.setPreferredSize(new Dimension(120, 30));
        searchPanel.add(filterCombo, gbc);

        // Botón de búsqueda
        gbc.gridx = 2;
        gbc.gridy = 1;
        JButton searchButton = new JButton("Buscar");
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.addActionListener(e -> buscarProductos());
        searchPanel.add(searchButton, gbc);

        top.add(searchPanel);
        return top;
    }

    private void buscarProductos() {
        String textoBusqueda = searchField.getText().trim().toLowerCase();
        String filtro = (String) filterCombo.getSelectedItem();

        modelProductos.setRowCount(0);
        List<Productos> productos = dao.obtenerTodos();
        boolean resultadosEncontrados = false;

        for (Productos p : productos) {
            boolean coincide = false;
            String valorBusqueda = "";

            switch (filtro) {
                case "Categoría":
                    valorBusqueda = p.getCategoria().toLowerCase();
                    break;
                case "Proveedor":
                    valorBusqueda = p.getProveedor().toLowerCase();
                    break;
                default: // Todos
                    valorBusqueda = p.getNombre().toLowerCase() + " " + 
                                  p.getCategoria().toLowerCase() + " " + 
                                  p.getProveedor().toLowerCase();
                    break;
            }

            if (valorBusqueda.contains(textoBusqueda) && p.getCantidad() > 0) {
                modelProductos.addRow(new Object[]{
                    p.getCodigo(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getCantidad()
                });
                resultadosEncontrados = true;
            }
        }

        if (!resultadosEncontrados) {
            JOptionPane.showMessageDialog(this, "No se encontraron coincidencias");
            cargarProductos();
        }
    }

    private JPanel crearPanelSuperior() {
        JPanel topPanel = new JPanel(new BorderLayout());
        
        String[] columnasProductos = {"Código", "Nombre", "Precio", "Cantidad"};
        modelProductos = new DefaultTableModel(columnasProductos, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        productosTable = new JTable(modelProductos);
        
        JButton btnAgregar = new JButton("Agregar al carrito");
        btnAgregar.addActionListener(this::agregarAlCarrito);
        
        btnQuitarCarrito = new JButton("Quitar del carrito");
        btnQuitarCarrito.setEnabled(false);
        btnQuitarCarrito.addActionListener(this::quitarDelCarrito);
        
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnQuitarCarrito);
        
        topPanel.add(new JScrollPane(productosTable), BorderLayout.CENTER);
        topPanel.add(panelBotones, BorderLayout.SOUTH);
        
        return topPanel;
    }

    private JPanel crearTablas() {
        String[] columnasCarrito = {"Código", "Nombre", "Precio", "Cantidad", "Subtotal"};
        modelCarrito = new DefaultTableModel(columnasCarrito, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        carritoTable = new JTable(modelCarrito);
        
        carritoTable.getSelectionModel().addListSelectionListener(e -> {
            btnQuitarCarrito.setEnabled(carritoTable.getSelectedRow() != -1);
        });

        JPanel carritoPanel = new JPanel(new BorderLayout());
        carritoPanel.add(new JLabel("Carrito de Compras"), BorderLayout.NORTH);
        carritoPanel.add(new JScrollPane(carritoTable), BorderLayout.CENTER);

        return carritoPanel;
    }

    private JPanel crearPanelInferior() {
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalIVA = new JLabel("Total + IVA (16%): $0.00");
        lblTotalIVA.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnComprar = new JButton("Realizar Compra");
        btnComprar.addActionListener(this::procesarCompra);
        
        bottomPanel.add(lblTotal);
        bottomPanel.add(lblTotalIVA);
        bottomPanel.add(btnComprar);
        
        return bottomPanel;
    }

    private void cargarProductos() {
        modelProductos.setRowCount(0);
        dao.obtenerTodos().stream()
           .filter(p -> p.getCantidad() > 0)
           .forEach(p -> modelProductos.addRow(new Object[]{
               p.getCodigo(),
               p.getNombre(),
               p.getPrecio(),
               p.getCantidad()
           }));
    }

    private void agregarAlCarrito(ActionEvent e) {
        int fila = productosTable.getSelectedRow();
        if (fila == -1) return;

        String codigo = (String) productosTable.getValueAt(fila, 0);
        Productos producto = dao.obtenerPorCodigo(codigo);
        
        if (producto != null && producto.getCantidad() > 0) {
            producto.setCantidad(producto.getCantidad() - 1);
            dao.actualizarProducto(producto);
            
            carrito.stream()
                  .filter(p -> p.getCodigo().equals(codigo))
                  .findFirst()
                  .ifPresentOrElse(
                      p -> p.setCantidad(p.getCantidad() + 1),
                      () -> carrito.add(clonarProducto(producto, 1))
                  );
            
            actualizarInterfaz();
        }
    }

    private void quitarDelCarrito(ActionEvent e) {
        int fila = carritoTable.getSelectedRow();
        if (fila == -1) return;

        Productos producto = carrito.get(fila);
        Productos inventario = dao.obtenerPorCodigo(producto.getCodigo());
        
        inventario.setCantidad(inventario.getCantidad() + producto.getCantidad());
        dao.actualizarProducto(inventario);
        
        carrito.remove(fila);
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        cargarProductos();
        actualizarCarrito();
    }

    private void actualizarCarrito() {
        modelCarrito.setRowCount(0);
        double total = carrito.stream()
            .mapToDouble(p -> {
                double subtotal = p.getPrecio() * p.getCantidad();
                modelCarrito.addRow(new Object[]{
                    p.getCodigo(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getCantidad(),
                    subtotal
                });
                return subtotal;
            }).sum();
        
        double iva = total * 0.16;
        lblTotal.setText(String.format("Total: $%.2f", total));
        lblTotalIVA.setText(String.format("Total + IVA (16%%): $%.2f", total + iva));
    }

    private void procesarCompra(ActionEvent e) {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Confirmar compra?\n" + lblTotal.getText() + "\n" + lblTotalIVA.getText(),
            "Confirmar Compra",
            JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            carrito.forEach(p -> dao.eliminarProducto(p.getCodigo()));
            carrito.clear();
            actualizarInterfaz();
            JOptionPane.showMessageDialog(this, "Compra realizada exitosamente!");
        }
    }

    private Productos clonarProducto(Productos original, int cantidad) {
        return new Productos(
            original.getId(),
            original.getNombre(),
            original.getDescripcion(),
            original.getPrecio(),
            original.getProveedor(),
            original.getCategoria(),
            cantidad,
            original.getCodigo()
        );
    }
}
