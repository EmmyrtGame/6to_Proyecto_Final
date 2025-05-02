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

    public Principal() {
        super("App con Barra Lateral y Vistas Internas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 1) Ventana al 80% de la pantalla
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = (int)(screen.width * 0.8),
            H = (int)(screen.height * 0.8);
        setPreferredSize(new Dimension(W, H));

        // 2) Contenedor principal con BorderLayout
        JPanel content = new JPanel(new BorderLayout(5,5));
        setContentPane(content);

        // 3) Sidebar con margen y separación
        sidebar = new JPanel();
        sidebar.setBackground(new Color(255, 255, 255));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));                // padding exterior[3]
        sidebar.setPreferredSize(new Dimension((int)(W * SIDEBAR_RATIO), 1));
        content.add(sidebar, BorderLayout.WEST);

        // 4) Contenedor de vistas tipo CardLayout
        viewLayout    = new CardLayout();
        viewContainer = new JPanel(viewLayout);
        viewContainer.setBackground(new Color(234, 180, 180));
        content.add(viewContainer, BorderLayout.CENTER);

        // 5) Botones en la sidebar
        addSidebarButton("Dashboard", Dashboard::new);
        addSidebarButton("Vista B",    VistaB::new);
        

        // 6) Mostrar Dashboard por defecto
        JToggleButton dashBtn = btnMap.get("Dashboard");
        dashBtn.setSelected(true);
        dashBtn.doClick();

        // 7) Empaquetar y mostrar
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
            BorderFactory.createEmptyBorder(5,10,5,10)                                  
        ));
        btnMap.put(key, btn);
        btnGroup.add(btn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btn);

        btn.addActionListener(e -> {
            if (!viewMap.containsKey(key)) {
                JPanel panel = panelSupplier.get();
                viewContainer.add(panel, key);
                viewMap.put(key, panel);
            }
            viewLayout.show(viewContainer, key);
            viewContainer.revalidate();
            viewContainer.repaint();
        });
    }

    public static class VistaB extends JPanel {
        public VistaB() {
            super(new FlowLayout());
            add(new JButton("Botón en Vista B"));
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Principal::new);
    }
}
