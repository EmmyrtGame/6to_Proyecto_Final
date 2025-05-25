import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ventas extends JPanel {
    private JTable productosTable;
    private JTable carritoTable;
    private DefaultTableModel modelProductos;
    private DefaultTableModel modelCarrito;
    private ProductosDAO dao = new ProductosDAO();
    private JLabel lblTotal;
    private JLabel lblTotalIVA;
    private JLabel lblTotalReal;
    private List<Productos> carrito = new ArrayList<>();
    private JButton btnQuitarCarrito;
    private JButton btnAgregarCarrito;
    private JButton btnEliminarUnoCarrito;
    private JButton btnLimpiarCarrito;
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JLabel lblImagenProducto;
    private JPanel panelImagenInferior;
    private Sesion sesion;
    private VentasDAO ventasDAO = new VentasDAO();

    // Tamaño base para la imagen (usado para relación de aspecto)
    private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 320;

    public Ventas(Sesion sesion) {
    	this.sesion = sesion;
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(1100, 700)); // Resolución recomendada

        add(crearPanelBusqueda(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(crearPanelTablas());
        mainPanel.add(crearPanelBotones());
        mainPanel.add(crearPanelInferior());

        add(mainPanel, BorderLayout.CENTER);

        // Listener para responsividad de imagen
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                actualizarTamañoImagen();
            }
        });

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
        
        // GridBagConstraints para searchField
        GridBagConstraints gbcSearchField = new GridBagConstraints();
        gbcSearchField.gridx = 0;
        gbcSearchField.gridy = 1;
        gbcSearchField.weightx = 1.0;
        gbcSearchField.fill = GridBagConstraints.BOTH;
        gbcSearchField.insets = new Insets(0, 5, 0, 5);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        searchPanel.add(searchField, gbcSearchField);

        // GridBagConstraints para filterCombo
        GridBagConstraints gbcFilterCombo = new GridBagConstraints();
        gbcFilterCombo.gridx = 1;
        gbcFilterCombo.gridy = 1;
        gbcFilterCombo.weightx = 0;
        gbcFilterCombo.fill = GridBagConstraints.BOTH;
        gbcFilterCombo.insets = new Insets(0, 5, 0, 5);
        filterCombo = new JComboBox<>(new String[] { "Todos", "Nombre", "Código" });
        filterCombo.setPreferredSize(new Dimension(120, 30));
        searchPanel.add(filterCombo, gbcFilterCombo);

        // GridBagConstraints para searchButton
        GridBagConstraints gbcSearchButton = new GridBagConstraints();
        gbcSearchButton.gridx = 2;
        gbcSearchButton.gridy = 1;
        gbcSearchButton.weightx = 0;
        gbcSearchButton.fill = GridBagConstraints.BOTH;
        gbcSearchButton.insets = new Insets(0, 5, 0, 5);
        JButton searchButton = new JButton("Buscar");
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.addActionListener(e -> buscarProductos());
        searchPanel.add(searchButton, gbcSearchButton);

        top.add(searchPanel);
        return top;
    }

    private void buscarProductos() {
        String textoBusqueda = searchField.getText().trim().toLowerCase();
        String filtro = (String) filterCombo.getSelectedItem();

        modelProductos.setRowCount(0);
        List<Productos> productos = dao.obtenerBusqueda(textoBusqueda, filtro);
        boolean resultadosEncontrados = false;

        for (Productos p : productos) {
            if (p.getCantidad() > 0) {
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

    private JPanel crearPanelTablas() {
        String[] columnasProductos = {"Código", "Nombre", "Precio", "Cantidad"};
        modelProductos = new DefaultTableModel(columnasProductos, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        productosTable = new JTable(modelProductos);
        productosTable.setRowHeight(22);
        productosTable.setFont(new Font("Arial", Font.PLAIN, 13));
        productosTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JTableHeader tableHeader = productosTable.getTableHeader();
        tableHeader.setBackground(new Color(120, 0, 0));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setOpaque(true);

        // Ajustar anchos de columnas
        productosTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
        productosTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre

        productosTable.getSelectionModel().addListSelectionListener(e -> mostrarImagenProductoSeleccionado());

        String[] columnasCarrito = {"Código", "Nombre", "Precio", "Cantidad", "Subtotal"};
        modelCarrito = new DefaultTableModel(columnasCarrito, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        carritoTable = new JTable(modelCarrito);
        carritoTable.setRowHeight(22);
        carritoTable.setFont(new Font("Arial", Font.PLAIN, 13));
        carritoTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JTableHeader tableHeader2 = carritoTable.getTableHeader();
        tableHeader2.setBackground(new Color(200, 230, 200));
        tableHeader2.setForeground(Color.BLACK);
        tableHeader2.setOpaque(true);

        carritoTable.getSelectionModel().addListSelectionListener(e -> {
            boolean seleccionado = carritoTable.getSelectedRow() != -1;
            btnQuitarCarrito.setEnabled(seleccionado);
            btnEliminarUnoCarrito.setEnabled(seleccionado);
        });

        JPanel productosPanel = new JPanel(new BorderLayout());
        productosPanel.add(new JLabel("Productos"), BorderLayout.NORTH);
        JScrollPane scrollProductos = new JScrollPane(productosTable);
        scrollProductos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        productosPanel.add(scrollProductos, BorderLayout.CENTER);

        // PANEL DERECHO: Carrito (arriba, grande) + Imagen (abajo, grande)
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BorderLayout());

        // Carrito arriba (más grande)
        JPanel panelCarritoSuperior = new JPanel(new BorderLayout());
        panelCarritoSuperior.setBorder(BorderFactory.createTitledBorder("Carrito de Compras"));
        JScrollPane scrollCarrito = new JScrollPane(carritoTable);
        scrollCarrito.setPreferredSize(new Dimension(400, 280));
        scrollCarrito.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelCarritoSuperior.add(scrollCarrito, BorderLayout.CENTER);
        panelCarritoSuperior.setPreferredSize(new Dimension(400, 280));

        // Imagen abajo (más grande)
        panelImagenInferior = new JPanel(new BorderLayout());
        lblImagenProducto = new JLabel();
        lblImagenProducto.setHorizontalAlignment(JLabel.CENTER);
        lblImagenProducto.setVerticalAlignment(JLabel.CENTER);
        lblImagenProducto.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        lblImagenProducto.setBorder(BorderFactory.createTitledBorder("Imagen del producto seleccionado"));
        lblImagenProducto.setText("Sin selección");
        panelImagenInferior.add(lblImagenProducto, BorderLayout.CENTER);
        panelImagenInferior.setPreferredSize(new Dimension(400, IMG_HEIGHT + 40));

        // Añadir al panel derecho
        panelDerecho.add(panelCarritoSuperior, BorderLayout.NORTH);
        panelDerecho.add(panelImagenInferior, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, productosPanel, panelDerecho);
        splitPane.setResizeWeight(0.6); // 60% productos, 40% carrito+imagen
        splitPane.setOneTouchExpandable(true);
        splitPane.setMinimumSize(new Dimension(800, 400));
        splitPane.setPreferredSize(new Dimension(1100, 450));
        splitPane.setDividerLocation(0.6);

        JPanel tablasPanel = new JPanel(new BorderLayout());
        tablasPanel.add(splitPane, BorderLayout.CENTER);
        tablasPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        return tablasPanel;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 15, 0));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnAgregarCarrito = new JButton("Agregar al carrito");
        btnAgregarCarrito.addActionListener(this::agregarAlCarrito);

        btnQuitarCarrito = new JButton("Quitar del carrito");
        btnQuitarCarrito.setEnabled(false);
        btnQuitarCarrito.addActionListener(this::quitarDelCarrito);

        btnEliminarUnoCarrito = new JButton("Eliminar un producto");
        btnEliminarUnoCarrito.setEnabled(false);
        btnEliminarUnoCarrito.addActionListener(this::eliminarUnProductoCarrito);

        btnLimpiarCarrito = new JButton("Limpiar carrito");
        btnLimpiarCarrito.addActionListener(this::limpiarCarrito);

        panelBotones.add(btnAgregarCarrito);
        panelBotones.add(btnQuitarCarrito);
        panelBotones.add(btnEliminarUnoCarrito);
        panelBotones.add(btnLimpiarCarrito);

        panelBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return panelBotones;
    }

    private JPanel crearPanelInferior() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        lblTotal = new JLabel("Total sin IVA: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblTotalIVA = new JLabel("IVA (16%): $0.00");
        lblTotalIVA.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblTotalReal = new JLabel("Total con IVA: $0.00");
        lblTotalReal.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnComprar = new JButton("Realizar Compra");
        btnComprar.addActionListener(this::procesarCompra);

        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotalIVA.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotalReal.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnComprar.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(lblTotal);
        bottomPanel.add(lblTotalIVA);
        bottomPanel.add(lblTotalReal);
        bottomPanel.add(Box.createVerticalStrut(5));
        bottomPanel.add(btnComprar);

        bottomPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
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
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para agregar al carrito.");
            return;
        }
        
        String codigo = (String) productosTable.getValueAt(fila, 0);
        Productos producto = dao.obtenerPorCodigo(codigo);
        
        if (producto == null || producto.getCantidad() <= 0) {
            JOptionPane.showMessageDialog(this, "Producto no disponible o sin stock.");
            return;
        }
        
        // Mostrar información del producto y pedir cantidad
        String mensaje = String.format(
            "Producto: %s\n" +
            "Precio: $%.2f\n" +
            "Disponible: %d unidades\n\n" +
            "Ingrese la cantidad a agregar:",
            producto.getNombre(),
            producto.getPrecio(),
            producto.getCantidad()
        );
        
        boolean cantidadValida = false;
        int cantidadSeleccionada = 0;
        
        while (!cantidadValida) {
            String input = JOptionPane.showInputDialog(
                this,
                mensaje,
                "Seleccionar Cantidad",
                JOptionPane.QUESTION_MESSAGE
            );
            
            // Si el usuario cancela
            if (input == null) {
                return;
            }
            
            // Validar que no esté vacío
            if (input.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor ingrese una cantidad válida.", 
                    "Error de Validación", 
                    JOptionPane.ERROR_MESSAGE);
                continue;
            }
            
            try {
                cantidadSeleccionada = Integer.parseInt(input.trim());
                
                // Validar que sea positivo
                if (cantidadSeleccionada <= 0) {
                    JOptionPane.showMessageDialog(this, 
                        "La cantidad debe ser mayor a 0.", 
                        "Error de Validación", 
                        JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                
                // Validar que no exceda el stock disponible
                if (cantidadSeleccionada > producto.getCantidad()) {
                    JOptionPane.showMessageDialog(this, 
                        String.format("La cantidad no puede ser mayor a %d unidades disponibles.", 
                                    producto.getCantidad()), 
                        "Error de Validación", 
                        JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                
                cantidadValida = true;
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor ingrese solo números enteros.", 
                    "Error de Formato", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // Procesar la adición al carrito
        procesarAgregarAlCarrito(producto, cantidadSeleccionada);
    }
    
    private void procesarAgregarAlCarrito(Productos producto, int cantidadSeleccionada) {
        // Actualizar inventario
        producto.setCantidad(producto.getCantidad() - cantidadSeleccionada);
        boolean actualizado = dao.actualizarProducto(producto);
        
        if (actualizado) {
            // Buscar si el producto ya existe en el carrito
            boolean productoEncontrado = false;
            
            for (Productos p : carrito) {
                if (p.getCodigo().equals(producto.getCodigo())) {
                    p.setCantidad(p.getCantidad() + cantidadSeleccionada);
                    productoEncontrado = true;
                    break;
                }
            }
            
            // Si no existe en el carrito, agregarlo
            if (!productoEncontrado) {
                carrito.add(clonarProducto(producto, cantidadSeleccionada));
            }
            
            // Actualizar interfaz
            actualizarInterfaz();
            
            // Mostrar confirmación
            JOptionPane.showMessageDialog(this, 
                String.format("Se agregaron %d unidades de '%s' al carrito.", 
                            cantidadSeleccionada, producto.getNombre()),
                "Producto Agregado",
                JOptionPane.INFORMATION_MESSAGE);
            
        } else {
            // Si falló la actualización, restaurar cantidad original
            producto.setCantidad(producto.getCantidad() + cantidadSeleccionada);
            JOptionPane.showMessageDialog(this, 
                "Error al actualizar el inventario.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void quitarDelCarrito(ActionEvent e) {
        int fila = carritoTable.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para quitar del carrito.");
            return;
        }

        Productos productoCarrito = carrito.get(fila);
        Productos inventario = dao.obtenerPorCodigo(productoCarrito.getCodigo());
        
        if (inventario != null) {
            inventario.setCantidad(inventario.getCantidad() + productoCarrito.getCantidad());
            boolean actualizado = dao.actualizarProducto(inventario);
            
            if (actualizado) {
                carrito.remove(fila);
                actualizarInterfaz();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el inventario.");
            }
        }
    }

    private void eliminarUnProductoCarrito(ActionEvent e) {
        int fila = carritoTable.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
            return;
        }

        Productos productoCarrito = carrito.get(fila);
        Productos inventario = dao.obtenerPorCodigo(productoCarrito.getCodigo());

        if (inventario != null) {
            productoCarrito.setCantidad(productoCarrito.getCantidad() - 1);
            inventario.setCantidad(inventario.getCantidad() + 1);

            boolean actualizadoInventario = dao.actualizarProducto(inventario);

            if (productoCarrito.getCantidad() <= 0) {
                carrito.remove(fila);
            }

            if (actualizadoInventario) {
                actualizarInterfaz();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el inventario.");
            }
        }
    }

    private void limpiarCarrito(ActionEvent e) {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito ya está vacío.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar todos los productos del carrito?",
                "Confirmar limpieza",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            for (Productos productoCarrito : carrito) {
                Productos inventario = dao.obtenerPorCodigo(productoCarrito.getCodigo());
                if (inventario != null) {
                    inventario.setCantidad(inventario.getCantidad() + productoCarrito.getCantidad());
                    dao.actualizarProducto(inventario);
                }
            }
            carrito.clear();
            productosTable.clearSelection();
            actualizarInterfaz();
            JOptionPane.showMessageDialog(this, "Carrito limpiado exitosamente.");
        }
    }

    private void mostrarImagenProductoSeleccionado() {
        int fila = productosTable.getSelectedRow();
        if (fila == -1) {
            lblImagenProducto.setIcon(null);
            lblImagenProducto.setText("Sin selección");
            return;
        }
        String codigo = (String) productosTable.getValueAt(fila, 0);
        Productos producto = dao.obtenerPorCodigo(codigo);
        if (producto != null && producto.getRutaImagen() != null && !producto.getRutaImagen().isEmpty()) {
            ImageIcon icon = new ImageIcon(producto.getRutaImagen());
            Image img = icon.getImage();

            // Escalar imagen manteniendo la relación de aspecto y el tamaño del panel
            Dimension panelSize = panelImagenInferior.getSize();
            int availableWidth = panelSize.width > 0 ? panelSize.width - 20 : IMG_WIDTH;
            int availableHeight = panelSize.height > 0 ? panelSize.height - 40 : IMG_HEIGHT;

            int imgW = icon.getIconWidth();
            int imgH = icon.getIconHeight();
            int newW = availableWidth;
            int newH = availableHeight;

            if (imgW > 0 && imgH > 0) {
                double widthRatio = (double) availableWidth / imgW;
                double heightRatio = (double) availableHeight / imgH;
                double scale = Math.min(widthRatio, heightRatio);
                newW = (int) (imgW * scale);
                newH = (int) (imgH * scale);
            }

            Image scaledImg = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            lblImagenProducto.setIcon(new ImageIcon(scaledImg));
            lblImagenProducto.setText("");
        } else {
            lblImagenProducto.setIcon(null);
            lblImagenProducto.setText("Sin imagen");
        }
    }

    // Método para actualizar la imagen cuando cambia el tamaño del panel
    private void actualizarTamañoImagen() {
        mostrarImagenProductoSeleccionado();
    }

    private void actualizarInterfaz() {
        int selectedRowProductos = productosTable.getSelectedRow();
        String selectedCodigoProducto = null;
        if (selectedRowProductos != -1) {
            selectedCodigoProducto = (String) productosTable.getValueAt(selectedRowProductos, 0);
        }

        int selectedRowCarrito = carritoTable.getSelectedRow();
        String selectedCodigoCarrito = null;
        if (selectedRowCarrito != -1) {
            selectedCodigoCarrito = (String) carritoTable.getValueAt(selectedRowCarrito, 0);
        }

        cargarProductos();
        actualizarCarrito();

        if (selectedCodigoProducto != null) {
            for (int i = 0; i < productosTable.getRowCount(); i++) {
                if (productosTable.getValueAt(i, 0).equals(selectedCodigoProducto)) {
                    productosTable.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }

        if (selectedCodigoCarrito != null) {
            for (int i = 0; i < carritoTable.getRowCount(); i++) {
                if (carritoTable.getValueAt(i, 0).equals(selectedCodigoCarrito)) {
                    carritoTable.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }

        mostrarImagenProductoSeleccionado();
    }

    private void actualizarCarrito() {
        modelCarrito.setRowCount(0);
        double totalConIva = carrito.stream()
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
        
        // Calcular el IVA como parte del precio total (16/116 del total)
        double iva = (totalConIva * 16) / 116;
        double totalSinIva = totalConIva - iva;
        lblTotal.setText(String.format("Total sin IVA: $%.2f", totalSinIva));
        lblTotalIVA.setText(String.format("IVA (16%%): $%.2f", iva));
        lblTotalReal.setText(String.format("Total con IVA: $%.2f", totalConIva));
    }

    private void procesarCompra(ActionEvent e) {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.");
            return;
        }

        // Obtener último registro del usuario
        Venta ultimaVenta = ventasDAO.obtenerUltimaVentaUsuario(sesion.getIdUsuario());
        
        boolean necesitaNuevaVenta = true;
        if (ultimaVenta != null && ultimaVenta.getEstado().equals("Pendiente")) {
            necesitaNuevaVenta = false;
        }

        try {
            double totalConIva = calcularTotal();
            double iva = (totalConIva * 16) / 116;
            double totalSinIva = totalConIva - iva;
            
            // Mostrar diálogo de confirmación con el total y el IVA desglosado
            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                String.format("Total sin IVA: $%.2f\nIVA (16%%): $%.2f\nTotal con IVA: $%.2f\n¿Desea confirmar la venta?", totalSinIva, iva, totalConIva),
                "Confirmar Venta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            // Si el usuario selecciona "No", se cancela la operación
            if (confirmacion == JOptionPane.NO_OPTION || confirmacion == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(this, "Venta cancelada.");
                return;
            }
            
            if (necesitaNuevaVenta) {
                ventasDAO.insertar(
                    sesion.getIdUsuario(),
                    carrito.size(),
                    new Date(),
                    totalConIva, // Registrar el total con IVA, que es el precio original de los productos
                    "Pendiente"
                );
            } else {
                ventasDAO.actualizarVenta(
                    ultimaVenta.getId(),
                    ultimaVenta.getVentas() + carrito.size(),
                    ultimaVenta.getTotal() + totalConIva // Sumar el total con IVA al registro existente
                );
            }
            
            carrito.clear();
            productosTable.clearSelection();
            actualizarInterfaz();
            JOptionPane.showMessageDialog(this, "Venta registrada exitosamente!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar venta: " + ex.getMessage());
        }
    }
    
    private double calcularTotal() {
        return carrito.stream()
            .mapToDouble(p -> p.getPrecio() * p.getCantidad())
            .sum();
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
            original.getCodigo(),
            original.getRutaImagen()
        );
    }
}
