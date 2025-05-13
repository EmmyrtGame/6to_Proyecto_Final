import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HistorialVentas extends JPanel {
    private JTable tblVentas;
    private JComboBox<String> cmbPeriodo;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JPanel panelResumen;
    private JLabel lblTotalVentas;
    private JLabel lblPromedioDiario;
    private JLabel lblDetalleVenta;

    /**
     * Método constructor de la clase
     */
    public HistorialVentas() {
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Margenes exteriores
        setLayout(new BorderLayout(10, 10)); // Establece los puntos de agarre de los componentes
        
        iniciarPanelSuperior(); // Procedimiento que inicializa el panel superior (filtros)
        iniciarTabla(); // Procedimiento que inicializa la tabla de datos
        iniciarPanelResumen(); // Panel lateral con resumen de ventas
        cargarDatosReales(); // Cargar datos de ejemplo
        iniciarPanelInferior(); // Procedimiento que inicializa el panel inferior (botones)
    }

    /**
     * Procedimiento que inicializa todos los componentes del panel superior
     */
    private void iniciarPanelSuperior() {
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        
        // Título
        JLabel title = new JLabel("Historial de Ventas");
        title.setFont(new Font("Century Gothic", Font.BOLD, 24));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.add(title);
        top.add(titlePanel);

        // Panel de filtros con GridBagLayout
        JPanel filtrosPanel = new JPanel(new GridBagLayout());
        
        // Selector de periodo
        GridBagConstraints gbcPeriodoLabel = new GridBagConstraints();
        gbcPeriodoLabel.gridx = 0;
        gbcPeriodoLabel.gridy = 0;
        gbcPeriodoLabel.anchor = GridBagConstraints.WEST;
        gbcPeriodoLabel.insets = new Insets(0, 5, 5, 5);
        JLabel lblPeriodo = new JLabel("Periodo:");
        lblPeriodo.setFont(new Font("Century Gothic", Font.BOLD, 12));
        filtrosPanel.add(lblPeriodo, gbcPeriodoLabel);
        
        GridBagConstraints gbcPeriodoCombo = new GridBagConstraints();
        gbcPeriodoCombo.gridx = 0;
        gbcPeriodoCombo.gridy = 1;
        gbcPeriodoCombo.insets = new Insets(0, 5, 10, 5);
        cmbPeriodo = new JComboBox<>(new String[] {
            "Personalizado", "Hoy", "Ayer", "Esta semana", "Semana pasada", 
            "Este mes", "Mes pasado", "Este año", "Año pasado"
        });
        cmbPeriodo.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        cmbPeriodo.setPreferredSize(new Dimension(150, 30));
        filtrosPanel.add(cmbPeriodo, gbcPeriodoCombo);
        
        // Fecha de inicio
        GridBagConstraints gbcFechaInicioLabel = new GridBagConstraints();
        gbcFechaInicioLabel.gridx = 1;
        gbcFechaInicioLabel.gridy = 0;
        gbcFechaInicioLabel.insets = new Insets(0, 15, 5, 5);
        JLabel lblFechaInicio = new JLabel("Fecha inicio:");
        lblFechaInicio.setFont(new Font("Century Gothic", Font.BOLD, 12));
        filtrosPanel.add(lblFechaInicio, gbcFechaInicioLabel);
        
        GridBagConstraints gbcFechaInicioText = new GridBagConstraints();
        gbcFechaInicioText.gridx = 1;
        gbcFechaInicioText.gridy = 1;
        gbcFechaInicioText.insets = new Insets(0, 15, 10, 5);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calInicio = Calendar.getInstance();
        calInicio.add(Calendar.YEAR, -2); // Resta 2 años a la fecha actual
        txtFechaInicio = new JTextField(sdf.format(calInicio.getTime()));
        txtFechaInicio.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		cmbPeriodo.setSelectedIndex(0); // Cambia a "Personalizado" al escribir
        	}
        });
        txtFechaInicio.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        txtFechaInicio.setPreferredSize(new Dimension(120, 30));
        filtrosPanel.add(txtFechaInicio, gbcFechaInicioText);
        
        // Fecha de fin
        GridBagConstraints gbcFechaFinLabel = new GridBagConstraints();
        gbcFechaFinLabel.gridx = 2;
        gbcFechaFinLabel.gridy = 0;
        gbcFechaFinLabel.insets = new Insets(0, 15, 5, 5);
        JLabel lblFechaFin = new JLabel("Fecha fin:");
        lblFechaFin.setFont(new Font("Century Gothic", Font.BOLD, 12));
        filtrosPanel.add(lblFechaFin, gbcFechaFinLabel);
        
        GridBagConstraints gbcFechaFinText = new GridBagConstraints();
        gbcFechaFinText.gridx = 2;
        gbcFechaFinText.gridy = 1;
        gbcFechaFinText.insets = new Insets(0, 15, 10, 5);
        txtFechaFin = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        txtFechaFin.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		cmbPeriodo.setSelectedIndex(0); // Cambia a "Personalizado" al escribir
        	}
        });
        txtFechaFin.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        txtFechaFin.setPreferredSize(new Dimension(120, 30));
        filtrosPanel.add(txtFechaFin, gbcFechaFinText);
        
        // Botón de filtrado
        GridBagConstraints gbcFiltrar = new GridBagConstraints();
        gbcFiltrar.gridx = 3;
        gbcFiltrar.gridy = 1;
        gbcFiltrar.insets = new Insets(0, 15, 10, 5);
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setFont(new Font("Century Gothic", Font.BOLD, 12));
        btnFiltrar.setPreferredSize(new Dimension(100, 30));
        btnFiltrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	cargarDatosReales();
                JOptionPane.showMessageDialog(
                    HistorialVentas.this,
                    "Filtro aplicado: de " + txtFechaInicio.getText() + " hasta " + txtFechaFin.getText(),
                    "Filtro aplicado",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        filtrosPanel.add(btnFiltrar, gbcFiltrar);
        
        cmbPeriodo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarFechasSegunPeriodo();
            }
        });
        
        top.add(filtrosPanel);
        add(top, BorderLayout.NORTH);
    }

    
    /**
     * Ajusta las fechas de inicio y fin según el periodo seleccionado
     */
    private void actualizarFechasSegunPeriodo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        
        switch(cmbPeriodo.getSelectedIndex()) {
            case 0: // Personalizado - No hacer nada
                break;
            case 1: // Hoy
                txtFechaInicio.setText(sdf.format(cal.getTime()));
                txtFechaFin.setText(sdf.format(cal.getTime()));
                break;
            case 2: // Ayer
                cal.add(Calendar.DAY_OF_MONTH, -1);
                String ayer = sdf.format(cal.getTime());
                txtFechaInicio.setText(ayer);
                txtFechaFin.setText(ayer);
                break;
            case 3: // Esta semana
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                txtFechaInicio.setText(sdf.format(cal.getTime()));
                cal.add(Calendar.DAY_OF_WEEK, 6);
                txtFechaFin.setText(sdf.format(cal.getTime()));
                break;
            case 4: // Semana pasada
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                txtFechaInicio.setText(sdf.format(cal.getTime()));
                cal.add(Calendar.DAY_OF_WEEK, 6);
                txtFechaFin.setText(sdf.format(cal.getTime()));
                break;
            case 5: // Este mes
                cal.set(Calendar.DAY_OF_MONTH, 1);
                txtFechaInicio.setText(sdf.format(cal.getTime()));
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                txtFechaFin.setText(sdf.format(cal.getTime()));
                break;
            case 6: // Mes pasado
                cal.add(Calendar.MONTH, -1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                txtFechaInicio.setText(sdf.format(cal.getTime()));
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                txtFechaFin.setText(sdf.format(cal.getTime()));
                break;
            case 7: // Este año
                cal.set(Calendar.DAY_OF_YEAR, 1);
                txtFechaInicio.setText(sdf.format(cal.getTime()));
                cal.set(Calendar.MONTH, 11);
                cal.set(Calendar.DAY_OF_MONTH, 31);
                txtFechaFin.setText(sdf.format(cal.getTime()));
                break;
            case 8: // Año pasado
                cal.add(Calendar.YEAR, -1);
                cal.set(Calendar.DAY_OF_YEAR, 1);
                txtFechaInicio.setText(sdf.format(cal.getTime()));
                cal.set(Calendar.MONTH, 11);
                cal.set(Calendar.DAY_OF_MONTH, 31);
                txtFechaFin.setText(sdf.format(cal.getTime()));
                break;
        }
    }

    /**
     * Procedimiento que inicializa el componente de la tabla
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
        
        tblVentas = new JTable(model);
        tblVentas.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tblVentas.setFillsViewportHeight(true);
        tblVentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblVentas.setRowHeight(25);
        
        // Ocultar las columnas de ID de venta y ID de usuario
        tblVentas.getColumnModel().getColumn(0).setMinWidth(0);
        tblVentas.getColumnModel().getColumn(0).setMaxWidth(0);
        tblVentas.getColumnModel().getColumn(0).setWidth(0);
        
        tblVentas.getColumnModel().getColumn(2).setMinWidth(0);
        tblVentas.getColumnModel().getColumn(2).setMaxWidth(0);
        tblVentas.getColumnModel().getColumn(2).setWidth(0);
        
        // Listener para selección de fila
        tblVentas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostrarDetallesVenta();
                }
            }
        });
        
        add(new JScrollPane(tblVentas), BorderLayout.CENTER);
    }
    
    /**
     * Procedimiento que inicializa el panel de resumen
     */
    private void iniciarPanelResumen() {
        panelResumen = new JPanel();
        panelResumen.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK), 
            "Resumen del período", 
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
        
        lblPromedioDiario = new JLabel("Promedio diario: $0.00");
        lblPromedioDiario.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        lblPromedioDiario.setAlignmentX(Component.LEFT_ALIGNMENT);
        
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
        contenidoResumen.add(lblPromedioDiario);
        contenidoResumen.add(Box.createVerticalStrut(30));
        contenidoResumen.add(lblVentaSeleccionada);
        contenidoResumen.add(Box.createVerticalStrut(10));
        contenidoResumen.add(lblDetalleVenta);
        
        panelResumen.add(contenidoResumen, BorderLayout.NORTH);
        add(panelResumen, BorderLayout.EAST);
    }
    
    /**
     * Procedimiento que inicializa el panel inferior con botones de acción
     */
    private void iniciarPanelInferior() {
        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblRegistros = new JLabel(tblVentas.getRowCount() + " registros encontrados");
        leftPanel.add(lblRegistros);
        
        bottom.add(leftPanel, BorderLayout.WEST);
        bottom.add(rightPanel, BorderLayout.EAST);
        
        add(bottom, BorderLayout.SOUTH);
    }
    
    /**
     * Muestra los detalles de la venta seleccionada
     */
    private void mostrarDetallesVenta() {
        int filaSeleccionada = tblVentas.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // Simulación de obtención de detalles
            String id = tblVentas.getValueAt(filaSeleccionada, 0).toString();
            String fecha = tblVentas.getValueAt(filaSeleccionada, 1).toString();
            String idUsuario = tblVentas.getValueAt(filaSeleccionada, 2).toString();
            String cajero = tblVentas.getValueAt(filaSeleccionada, 3).toString();
            int cantidadVentas = (Integer)tblVentas.getValueAt(filaSeleccionada, 4);
            Double total = (Double)tblVentas.getValueAt(filaSeleccionada, 5);
            
            StringBuilder sb = new StringBuilder("<html>");
            sb.append("<b>Fecha:</b> ").append(fecha).append("<br>");
            sb.append("<b>Cajero:</b> ").append(cajero).append("<br>");
            sb.append("<b>Cant. Ventas:</b> ").append(cantidadVentas).append("<br>");
            sb.append("<b>Total:</b> $").append(String.format("%.2f", total)).append("<br>");
            sb.append("<b>Estado:</b> ").append(tblVentas.getValueAt(filaSeleccionada, 6)).append("<br>");
            sb.append("</html>");
            
            lblDetalleVenta.setText(sb.toString());
        } else {
            lblDetalleVenta.setText("Seleccione una venta para ver detalles");
        }
    }
    
    /**
     * Carga datos reales desde la base de datos para la tabla
     */
    private void cargarDatosReales() {
    	DefaultTableModel model = (DefaultTableModel) tblVentas.getModel();
        model.setRowCount(0); // limpia filas previas
        
        // Obtener fechas del filtro
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaInicio = null;
        Date fechaFin = null;
        
        try {
            fechaInicio = sdf.parse(txtFechaInicio.getText());
            fechaFin = sdf.parse(txtFechaFin.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error en formato de fechas. Usando valores predeterminados.",
                "Error de formato", JOptionPane.ERROR_MESSAGE);
            fechaInicio = new Date();
            fechaFin = new Date();
        }
        
        // Crear instancia del DAO y obtener las ventas
        VentasDAO ventasDAO = new VentasDAO();
        List<Venta> ventas = ventasDAO.obtenerPorRangoFechas(fechaInicio, fechaFin);
        
        double totalPeriodo = 0.0;
        
        // Llenar la tabla con datos reales
        for (Venta venta : ventas) {
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
        }
        
        // Calcular días entre fechas para el promedio diario
        long diff = fechaFin.getTime() - fechaInicio.getTime();
        int diasPeriodo = (int) Math.max(1, diff / (24 * 60 * 60 * 1000)) + 1;
        
        // Actualizar el resumen
        lblTotalVentas.setText("Total de ventas: $" + String.format("%.2f", totalPeriodo));
        lblPromedioDiario.setText("Promedio diario: $" + String.format("%.2f", totalPeriodo / diasPeriodo));
    }

    
    /**
     * Método main para pruebas
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Historial de Ventas");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setContentPane(new HistorialVentas());
            f.setSize(1000, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
    
 // Para obtener el ID de la venta de una fila seleccionada
    public String getIdVentaSeleccionada() {
        int fila = tblVentas.getSelectedRow();
        if (fila >= 0) {
            return tblVentas.getValueAt(fila, 0).toString();
        }
        return null;
    }

    // Para obtener el ID de usuario de una fila seleccionada
    public String getIdUsuarioSeleccionado() {
        int fila = tblVentas.getSelectedRow();
        if (fila >= 0) {
            return tblVentas.getValueAt(fila, 2).toString();
        }
        return null;
    }
}
