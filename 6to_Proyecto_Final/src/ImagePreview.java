import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

// Clase para mostrar vista previa en el JFileChooser
class ImagePreview extends JPanel implements PropertyChangeListener {
    private JLabel label;
    private File file = null;
    
    public ImagePreview(JFileChooser fc) {
        setLayout(new BorderLayout());
        label = new JLabel("Sin imagen", SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(200, 150));
        label.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        add(label, BorderLayout.CENTER);
        fc.addPropertyChangeListener(this);
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(e.getPropertyName())) {
            file = (File) e.getNewValue();
            updatePreview();
        }
    }
    
    private void updatePreview() {
        if (file == null) {
            label.setIcon(null);
            label.setText("Sin imagen");
            return;
        }
        
        try {
            // Verificar si es una imagen
            Image img = ImageIO.read(file);
            if (img == null) {
                label.setIcon(null);
                label.setText("No es una imagen válida");
                return;
            }
            
            // Redimensionar para la vista previa
            int width = 190;
            int height = 140;
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            
            label.setText("");
            label.setIcon(new ImageIcon(scaledImg));
        } catch (IOException ex) {
            label.setIcon(null);
            label.setText("Error al cargar imagen");
        }
    }
}
