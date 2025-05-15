import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Principal extends JFrame {
    private static final double SIDEBAR_RATIO = 0.10;  // 10% del ancho
    private JPanel sidebar;
    private JPanel viewContainer;
    private CardLayout viewLayout;
    private Map<String,JToggleButton> btnMap   = new HashMap<>();
    private Map<String,JPanel>       viewMap  = new HashMap<>();
    private ButtonGroup              btnGroup = new ButtonGroup();
    private Sesion sesion;  // Instancia de Sesion
    private Login loginWindow;  // Referencia a la ventana de Login
    private String rol;

    public Principal(Sesion sesion, Login loginWindow) {
        super("Sistema de Abarrotes");
        this.sesion = sesion;
        this.loginWindow = loginWindow;
        this.rol = sesion.getRol();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Ventana al 80% de la pantalla
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = (int)(screen.width * 0.8),
            H = (int)(screen.height * 0.8);
        setPreferredSize(new Dimension(W, H));

        // Contenedor principal con BorderLayout
        JPanel content = new JPanel(new BorderLayout(5,5));
        setContentPane(content);

        // Sidebar con margen y separación
        sidebar = new JPanel();
        sidebar.setBackground(new Color(255, 255, 255));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        sidebar.setPreferredSize(new Dimension((int)(W * SIDEBAR_RATIO), 1));
        content.add(sidebar, BorderLayout.WEST);
        
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(new Color(255, 255, 255));
        userInfoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 10, 5)
        ));
        userInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nombreUsuarioLabel = new JLabel(sesion.getUser());
        nombreUsuarioLabel.setFont(new Font(nombreUsuarioLabel.getFont().getName(), Font.BOLD, 14));
        nombreUsuarioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregar al panel de información
        userInfoPanel.add(nombreUsuarioLabel);

        // Agregar el panel a la sidebar
        sidebar.add(userInfoPanel);
        sidebar.add(Box.createVerticalStrut(15)); // Espacio después del nombre

        // Contenedor de vistas tipo CardLayout
        viewLayout    = new CardLayout();
        viewContainer = new JPanel(viewLayout);
        viewContainer.setBackground(new Color(234, 180, 180));
        content.add(viewContainer, BorderLayout.CENTER);

        // Botones en la sidebar
        addSidebarButton("Dashboard", Dashboard::new);
        addSidebarButton("Inventario", Inventario::new);
        addSidebarButton("Ventas", () -> new Ventas(sesion));
        addSidebarButton("Corte", () -> new CorteCaja(sesion));
        if (this.rol.equals("Admin")) {
        	addSidebarButton("Histórico", HistorialVentas::new);
        	addSidebarButton("Usuarios", () -> new Usuarios(sesion));
        }
        addLogoutButton();

        // Mostrar Dashboard por defecto
        JToggleButton dashBtn = btnMap.get("Dashboard");
        dashBtn.setSelected(true);
        dashBtn.doClick();

        // Empaquetar y mostrar
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addSidebarButton(String key, Supplier<JPanel> panelSupplier) {
        JToggleButton btn = new JToggleButton(key);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        btnMap.put(key, btn);
        btnGroup.add(btn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btn);
        
        btn.addActionListener(e -> {
            // Si ya existe un panel con esta clave, eliminarlo
            if (viewMap.containsKey(key)) {
                viewContainer.remove(viewMap.get(key));
            }
            // Siempre crear un panel nuevo
            JPanel panel = panelSupplier.get();
            viewContainer.add(panel, key);
            viewMap.put(key, panel);

            viewLayout.show(viewContainer, key);
            viewContainer.revalidate();
            viewContainer.repaint();
        });
    }
    
    private void addLogoutButton() {
        // Agregar un espacio vertical antes del botón de cerrar sesión
        sidebar.add(Box.createVerticalGlue());  // Empuja el botón hacia abajo
        JButton logoutBtn = new JButton("Cerrar Sesión");
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logoutBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED, 1),
            BorderFactory.createEmptyBorder(5,10,5,10)
        ));
        logoutBtn.setBackground(new Color(255, 200, 200));
        logoutBtn.setForeground(Color.RED);
        logoutBtn.setFocusPainted(false);

        logoutBtn.addActionListener(e -> {
            // Llamar al método Desautenticar
            sesion.Desautenticar();
            // Mostrar mensaje de cierre de sesión
            JOptionPane.showMessageDialog(this, "Sesión cerrada con éxito", "Cerrar Sesión", JOptionPane.INFORMATION_MESSAGE);
            // Mostrar la ventana de login nuevamente
            loginWindow.setVisible(true);
            // Cerrar la ventana principal
            dispose();
        });

        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(logoutBtn);
    }

    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {
            Sesion sesionPrueba = new Sesion(1, "OxxoAdmin", "123456", "LUIS EMMYRT AVILA AGUILAR", "Admin");
            new Principal(sesionPrueba, null);
        });
    }
}
