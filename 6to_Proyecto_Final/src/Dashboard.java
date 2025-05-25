import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Dashboard extends JPanel {
    public Dashboard() {
        // BorderLayout con espacios internos de 10px
        super(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
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
