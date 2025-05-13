import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class CorteCaja extends JPanel {
    private JTable tblVentasPendientes;
    private JPanel panelResumen;
    private JLabel lblTotalVentas;
    private JLabel lblCantidadVentas;
    private JLabel lblDetalleVenta;
    private JButton btnCerrarCorte;
    private Sesion sesion; // Para obtener el ID del usuario actual
    private VentasDAO ventasDAO; // Para interactuar con la base de datos

    /**
     * Constructor de la clase CorteCaja
     * @param sesion Sesión del usuario actual para filtrar ventas por ID de usuario
     */
    public CorteCaja(Sesion sesion) {
        this.sesion = sesion;
        this.ventasDAO = new VentasDAO();
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Márgenes exteriores
        setLayout(new BorderLayout(10, 10)); // Layout principal con separación

        iniciarPanelSuperior(); // Panel con título y filtros básicos
        iniciarTabla(); // Tabla para mostrar ventas pendientes
        iniciarPanelResumen(); // Panel lateral con resumen del corte
        cargarVentasPendientes(); // Cargar datos de ventas pendientes
        iniciarPanelInferior(); // Botones de acción como cerrar corte
    }

    /**
     * Inicializa el panel superior con título y opciones de filtro
     */
    private void iniciarPanelSuperior() {
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        // Título
        JLabel title = new JLabel("Corte de Caja");
        title.setFont(new Font("Century Gothic", Font.BOLD, 24));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.add(title);
        top.add(titlePanel);

        // Panel de información del usuario o turno
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 5, 5);
        JLabel lblUsuario = new JLabel("Usuario: " + sesion.getNombre());
        lblUsuario.setFont(new Font("Century Gothic", Font.BOLD, 12));
        infoPanel.add(lblUsuario, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 15, 5, 5);
        JLabel lblRol = new JLabel("Rol: " + sesion.getRol());
        lblRol.setFont(new Font("Century Gothic", Font.BOLD, 12));
        infoPanel.add(lblRol, gbc);

        top.add(infoPanel);
        add(top, BorderLayout.NORTH);
    }

    /**
     * Inicializa la tabla para mostrar las ventas pendientes
     */
    private void iniciarTabla() {
        String[] cols = {"ID", "Fecha", "ID Usuario", "Cajero", "Cantidad Ventas", "Total", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return Double.class; // Para formateo de moneda
                }
                return super.getColumnClass(columnIndex);
            }
        };

        tblVentasPendientes = new JTable(model);
        tblVentasPendientes.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tblVentasPendientes.setFillsViewportHeight(true);
        tblVentasPendientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblVentasPendientes.setRowHeight(25);

        // Ocultar columnas de ID de venta y ID de usuario
        tblVentasPendientes.getColumnModel().getColumn(0).setMinWidth(0);
        tblVentasPendientes.getColumnModel().getColumn(0).setMaxWidth(0);
        tblVentasPendientes.getColumnModel().getColumn(0).setWidth(0);

        tblVentasPendientes.getColumnModel().getColumn(2).setMinWidth(0);
        tblVentasPendientes.getColumnModel().getColumn(2).setMaxWidth(0);
        tblVentasPendientes.getColumnModel().getColumn(2).setWidth(0);

        // Listener para selección de fila
        tblVentasPendientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostrarDetallesVenta();
                }
            }
        });

        add(new JScrollPane(tblVentasPendientes), BorderLayout.CENTER);
    }

    /**
     * Inicializa el panel lateral de resumen del corte de caja
     */
    private void iniciarPanelResumen() {
        panelResumen = new JPanel();
        panelResumen.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            "Resumen del Corte",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Century Gothic", Font.BOLD, 14)
        ));
        panelResumen.setPreferredSize(new Dimension(250, 0)); // Ancho fijo
        panelResumen.setLayout(new BorderLayout());

        JPanel contenidoResumen = new JPanel();
        contenidoResumen.setLayout(new BoxLayout(contenidoResumen, BoxLayout.Y_AXIS));
        contenidoResumen.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Etiquetas para mostrar información de resumen
        lblTotalVentas = new JLabel("Total de ventas: $0.00");
        lblTotalVentas.setFont(new Font("Century Gothic", Font.BOLD, 14));
        lblTotalVentas.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblCantidadVentas = new JLabel("Cantidad de ventas: 0");
        lblCantidadVentas.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        lblCantidadVentas.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblVentaSeleccionada = new JLabel("Detalles de la venta:");
        lblVentaSeleccionada.setFont(new Font("Century Gothic", Font.BOLD, 14));
        lblVentaSeleccionada.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Área para detalles de la venta seleccionada
        lblDetalleVenta = new JLabel("Seleccione una venta");
        lblDetalleVenta.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        lblDetalleVenta.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Espaciadores
        contenidoResumen.add(lblTotalVentas);
        contenidoResumen.add(Box.createVerticalStrut(10));
        contenidoResumen.add(lblCantidadVentas);
        contenidoResumen.add(Box.createVerticalStrut(30));
        contenidoResumen.add(lblVentaSeleccionada);
        contenidoResumen.add(Box.createVerticalStrut(10));
        contenidoResumen.add(lblDetalleVenta);

        panelResumen.add(contenidoResumen, BorderLayout.NORTH);
        add(panelResumen, BorderLayout.EAST);
    }

    /**
     * Inicializa el panel inferior con botones de acción
     */
    private void iniciarPanelInferior() {
        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnCerrarCorte = new JButton("Cerrar Corte");
        btnCerrarCorte.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnCerrarCorte.setBackground(new Color(200, 230, 200));
        btnCerrarCorte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cerrarCorte();
            }
        });

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarVentasPendientes();
                JOptionPane.showMessageDialog(
                    CorteCaja.this,
                    "Lista de ventas pendientes actualizada.",
                    "Actualización",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblRegistros = new JLabel(tblVentasPendientes.getRowCount() + " registros pendientes");
        leftPanel.add(lblRegistros);

        rightPanel.add(btnActualizar);
        rightPanel.add(btnCerrarCorte);

        bottom.add(leftPanel, BorderLayout.WEST);
        bottom.add(rightPanel, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Muestra los detalles de la venta seleccionada en el panel lateral
     */
    private void mostrarDetallesVenta() {
        int filaSeleccionada = tblVentasPendientes.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String fecha = tblVentasPendientes.getValueAt(filaSeleccionada, 1).toString();
            String cajero = tblVentasPendientes.getValueAt(filaSeleccionada, 3).toString();
            int cantidadVentas = (Integer) tblVentasPendientes.getValueAt(filaSeleccionada, 4);
            Double total = (Double) tblVentasPendientes.getValueAt(filaSeleccionada, 5);

            StringBuilder sb = new StringBuilder("<html>");
            sb.append("<b>Fecha:</b> ").append(fecha).append("<br>");
            sb.append("<b>Cajero:</b> ").append(cajero).append("<br>");
            sb.append("<b>Cant. Ventas:</b> ").append(cantidadVentas).append("<br>");
            sb.append("<b>Total:</b> $").append(String.format("%.2f", total)).append("<br>");
            sb.append("<b>Estado:</b> ").append(tblVentasPendientes.getValueAt(filaSeleccionada, 6)).append("<br>");
            sb.append("</html>");

            lblDetalleVenta.setText(sb.toString());
        } else {
            lblDetalleVenta.setText("Seleccione una venta para ver detalles");
        }
    }

    /**
     * Carga las ventas pendientes del usuario actual desde la base de datos
     */
    private void cargarVentasPendientes() {
        DefaultTableModel model = (DefaultTableModel) tblVentasPendientes.getModel();
        model.setRowCount(0); // Limpia filas previas

        List<Venta> ventas = ventasDAO.obtenerPorEstado("Pendiente");
        double totalPeriodo = 0.0;
        int cantidadVentas = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Venta venta : ventas) {
            // Filtrar solo las ventas del usuario actual
            if (venta.getIdUsuario() == sesion.getIdUsuario()) {
                String nombreCajero = ventasDAO.obtenerNombreCajero(venta.getIdUsuario());
                model.addRow(new Object[]{
                    venta.getId(),
                    sdf.format(venta.getFecha()),
                    venta.getIdUsuario(),
                    nombreCajero,
                    venta.getVentas(),
                    venta.getTotal(),
                    venta.getEstado()
                });
                totalPeriodo += venta.getTotal();
                cantidadVentas += venta.getVentas();
            }
        }

        // Actualizar el resumen
        lblTotalVentas.setText("Total de ventas: $" + String.format("%.2f", totalPeriodo));
        lblCantidadVentas.setText("Cantidad de ventas: " + cantidadVentas);
    }

    /**
     * Cierra el corte de caja actualizando el estado de las ventas pendientes a "Cerrado"
     */
    private void cerrarCorte() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de cerrar el corte de caja? Esta acción no se puede deshacer.",
            "Confirmar Cierre",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            List<Venta> ventasPendientes = ventasDAO.obtenerPorEstado("Pendiente");
            boolean exito = true;

            for (Venta venta : ventasPendientes) {
                if (venta.getIdUsuario() == sesion.getIdUsuario()) {
                    boolean actualizado = ventasDAO.actualizarEstado(venta.getId(), "Cerrado");
                    if (!actualizado) {
                        exito = false;
                    }
                }
            }

            if (exito) {
                JOptionPane.showMessageDialog(
                    this,
                    "Corte de caja cerrado con éxito.",
                    "Corte Cerrado",
                    JOptionPane.INFORMATION_MESSAGE
                );
                cargarVentasPendientes(); // Actualizar la tabla
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al cerrar el corte de caja. Algunas ventas no se actualizaron.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * Método main para pruebas
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Corte de Caja");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Sesion sesionPrueba = new Sesion(1, "OxxoAdmin", "123456", "LUIS EMMYRT AVILA AGUILAR", "Admin");
            f.setContentPane(new CorteCaja(sesionPrueba));
            f.setSize(1000, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}