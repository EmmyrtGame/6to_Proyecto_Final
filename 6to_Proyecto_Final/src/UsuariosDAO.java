import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * UsuariosDAO
 * 
 * En programación, una clase DAO (Data Access Object)
 * es un patrón de diseño que encapsula la lógica de acceso a datos,
 * separándola de la lógica de negocio de la aplicación.
 */
public class UsuariosDAO {
	private ConexionAccess db = new ConexionAccess();
	
	/**
	 * Obtiene todos los usuarios de la tabla usuarios.
	 */
	public List<Sesion> obtenerTodos(int id) {
		List<Sesion> lista = new ArrayList<>();
		String sql = "SELECT id, usuario, password, nombre, rol FROM sesiones WHERE id != " + id;
		try {
			ResultSet rs = db.obtenerSentencia(sql);
			while (rs.next()) {
				Sesion u = new Sesion(
					rs.getInt("id"),
					rs.getString("usuario"),
					rs.getString("password"),
					rs.getString("nombre"),
					rs.getString("rol")
				);
				lista.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.cerrarConexion();
		}
		return lista;
	}
}
