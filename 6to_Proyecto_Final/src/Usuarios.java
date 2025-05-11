import javax.swing.JPanel;

public class Usuarios extends JPanel {

	private static final long serialVersionUID = 1L;
	private UsuariosDAO usuario;

	/**
	 * Create the panel.
	 */
	public Usuarios() {
		usuario = new UsuariosDAO();
	}

}
