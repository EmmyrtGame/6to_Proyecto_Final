import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Dashboard extends JPanel {
    public Dashboard() {
        // BorderLayout con espacios internos de 10px
        super(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior: Estadísticas en 1 fila x 3 columnas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.add(createStatPanel("Usuarios", "1,234", Color.GREEN));
        statsPanel.add(createStatPanel("Ventas",   "$56,789", Color.BLUE));
        statsPanel.add(createStatPanel("Visitas", "12,345", Color.ORANGE));

        // Panel central: Gráficos simulados en 1 fila x 2 columnas
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        chartsPanel.setBorder(BorderFactory.createTitledBorder("Gráficos"));
        chartsPanel.add(new JLabel("[Gráfico de barras]", SwingConstants.CENTER));
        chartsPanel.add(new JLabel("[Gráfico de líneas]", SwingConstants.CENTER));

        // Panel inferior: Botones de acción alineados a la derecha
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.add(new JButton("Actualizar"));
        actionsPanel.add(new JButton("Exportar"));

        // Montaje final
        add(statsPanel,   BorderLayout.NORTH);
        add(chartsPanel,  BorderLayout.CENTER);
        add(actionsPanel, BorderLayout.SOUTH);
    }

    // Helper para crear cada tarjeta de estadística
    private JPanel createStatPanel(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.PLAIN, 20f));
        valueLabel.setForeground(color);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(color, 2));
        return panel;
    }
}
