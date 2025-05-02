import javax.swing.SwingUtilities;

public class MainApp {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            // Crea y muestra la ventana de login
            Login login = new Login();
            login.setVisible(true);
        });
	}

}
