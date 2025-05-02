import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
	private JTextField txtContra;
    public Login() {
    	
    	String usuario = "user";
    	String contrasena = "password";
    	
    	setResizable(false);
        setTitle("Login");
        setSize(506, 374);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel de fondo
        JPanel background = new JPanel();
        background.setBackground(new Color(10, 80, 150));
        background.setLayout(null);
        getContentPane().add(background);

        // Panel central ("tarjeta")
        JPanel card = new JPanel();
        card.setBackground(new Color(255, 255, 255));
        card.setBounds(50, 30, 400, 270);
        card.setLayout(null);
        background.add(card);

        // Ícono de usuario (placeholder)
        JLabel icon = new JLabel();
        icon.setIcon(new ImageIcon("./imagenes/user.png"));
        icon.setBounds(161, 11, 100, 95);
        card.add(icon);

        // Etiqueta y campo de Username
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(new Font("Century Gothic", Font.BOLD, 14));
        lblUser.setForeground(new Color(0, 0, 0));
        lblUser.setBounds(50, 117, 100, 25);
        card.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(150, 117, 200, 25);
        card.add(txtUser);

        // Etiqueta y campo de Password
        JLabel lblContra = new JLabel("Contraseña:");
        lblContra.setFont(new Font("Century Gothic", Font.BOLD, 14));
        lblContra.setForeground(new Color(0, 0, 0));
        lblContra.setBounds(50, 157, 100, 25);
        card.add(lblContra);
        
        txtContra = new JTextField();
        txtContra.setBounds(150, 159, 200, 25);
        card.add(txtContra);

        // Botón de LOGIN
        JButton loginBtn = new JButton("Iniciar Sesión");
        loginBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String userInput = txtUser.getText();
        		String contraInput = txtContra.getText();
        		
        		if ((userInput.equals(usuario)) && (contraInput.equals(contrasena))) {
        			JFrame ventana = new Principal();
        			ventana.setVisible(true);
        		}
        	}
        });
        loginBtn.setBackground(new Color(0, 150, 200));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBounds(150, 207, 200, 30);
        card.add(loginBtn);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
