import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
    private JPasswordField txtContra; 
    private Sesion Sesion;

    public Login() {
        Sesion = new Sesion();
        UsuariosDAO dao = new UsuariosDAO();

        setResizable(false);
        setTitle("Login");
        setSize(505, 376);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel de fondo
        JPanel background = new JPanel();
        background.setBackground(new Color(128, 0, 0));
        background.setLayout(null);
        getContentPane().add(background);

        // Panel central
        JPanel card = new JPanel();
        card.setBackground(new Color(255, 255, 255));
        card.setBounds(45, 30, 400, 270);
        card.setLayout(null);
        background.add(card);

        // Ícono de usuario
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
        txtUser.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        txtUser.setBounds(150, 117, 200, 25);
        card.add(txtUser);

        // Etiqueta y campo de Password
        JLabel lblContra = new JLabel("Contraseña:");
        lblContra.setFont(new Font("Century Gothic", Font.BOLD, 14));
        lblContra.setForeground(new Color(0, 0, 0));
        lblContra.setBounds(50, 157, 100, 25);
        card.add(lblContra);

        txtContra = new JPasswordField(); // Cambiado a JPasswordField
        txtContra.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        txtContra.setBounds(150, 159, 200, 25);
        card.add(txtContra);

        // Botón de LOGIN
        JButton loginBtn = new JButton("Iniciar Sesión");
        loginBtn.setFont(new Font("Century Gothic", Font.BOLD, 12));
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userInput = txtUser.getText();
                String contraInput = new String(txtContra.getPassword()); // Obtiene la contraseña correctamente
                
                // VALIDACIÓN DE USUARIO
                String validacionUsuario = Validator.validarUsuario(userInput);
                if (!validacionUsuario.equals("correcto")) {
                    JOptionPane.showMessageDialog(Login.this, validacionUsuario, "Error de Formato", JOptionPane.ERROR_MESSAGE);
                    return; // Detener ejecución si hay error
                }
                
                // VALIDACIÓN DE CONTRASEÑA
                String validacionPassword = Validator.validarPassword(contraInput);
                if (!validacionPassword.equals("correcto")) {
                    JOptionPane.showMessageDialog(Login.this, validacionPassword, "Error de Formato", JOptionPane.ERROR_MESSAGE);
                    return; // Detener ejecución si hay error
                }

                // Si las validaciones son exitosas, proceder con autenticación
                Sesion.setUser(userInput);
                Sesion.setContra(contraInput);

                Sesion.setUser(userInput);
                Sesion.setContra(contraInput);

                if (dao.Autenticar(Sesion)) {
                    JOptionPane.showMessageDialog(null, "Usuario y contraseña correctos", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    Principal Principal = new Principal(Sesion, Login.this);
                    Principal.setVisible(true);
                    txtUser.setText("");
                    txtContra.setText("");
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario y contraseña incorrectos", "Credenciales incorrectas", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loginBtn.setBackground(new Color(128, 0, 0));
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
