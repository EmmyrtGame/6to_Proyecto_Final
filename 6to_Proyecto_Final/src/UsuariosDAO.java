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
		String sql = "SELECT id, usuario, password, nombre, rol FROM sesiones";
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
	
	/**
	 * Obtiene los usuarios de la base de datos con filtro de búsqueda
	 */
	public List<Sesion> obtenerBusqueda(String busqueda, String filtro, int idUsuarioActual) {
	    List<Sesion> lista = new ArrayList<>();
	    String sql = "SELECT id, usuario, password, nombre, rol FROM sesiones";
	    
	    // Agregar cláusula WHERE adicional si la cadena de búsqueda no está vacía
	    if (busqueda != null && !busqueda.isEmpty()) {
	        String terminoBusqueda = "%" + busqueda + "%";
	        
	        sql += " AND ";
	        
	        if (filtro.equals("Todos")) {
	            sql += "(id LIKE '" + terminoBusqueda + 
	                   "' OR usuario LIKE '" + terminoBusqueda + 
	                   "' OR nombre LIKE '" + terminoBusqueda + 
	                   "' OR rol LIKE '" + terminoBusqueda + "')";
	        } else if (filtro.equals("Usuario")) {
	            sql += "usuario LIKE '" + terminoBusqueda + "'";
	        } else if (filtro.equals("Nombre")) {
	            sql += "nombre LIKE '" + terminoBusqueda + "'";
	        } else if (filtro.equals("Rol")) {
	            sql += "rol LIKE '" + terminoBusqueda + "'";
	        }
	    }
	    
	    try {
	        ResultSet rs = db.obtenerSentencia(sql);
	        while (rs.next()) {
	            Sesion s = new Sesion(
	                rs.getInt("id"),
	                rs.getString("usuario"),
	                rs.getString("password"),
	                rs.getString("nombre"),
	                rs.getString("rol")
	            );
	            lista.add(s);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        db.cerrarConexion();
	    }
	    
	    return lista;
	}

	
	/**
	 * Método para autenticar un usuario en la base de datos.
	 */
	public boolean Autenticar(Sesion u)
	{
	    String consulta = String.format("SELECT * FROM sesiones WHERE usuario='%s' AND password='%s'", u.getUser(), u.getContrasena());
	    try {
	        ResultSet rs = db.obtenerSentencia(consulta);
	        if (rs.next()) {
	            u.setId(rs.getInt("id"));
	            u.setNombre(rs.getString("nombre"));
	            u.setUser(rs.getString("usuario"));
	            u.setContra(rs.getString("password"));
	            u.setRol(rs.getString("rol"));
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        db.cerrarConexion();
	    }
	    return false;
	}
	
	/**
	 * Método para actualizar un usuario en la base de datos.
	 */
	public boolean actualizarUsuario(Sesion u) {
	    String sql = String.format(
	        "UPDATE sesiones SET usuario='%s', password='%s', nombre='%s', rol='%s' WHERE id=%d",
	        u.getUser(), u.getContrasena(), u.getNombre(), u.getRol(), u.getIdUsuario()
	    );
	    return db.ejecutarSentencia(sql);
	}
	
	/**
	 * Método para insertar un nuevo usuario en la base de datos.
	 */
	public boolean insertarUsuario(Sesion u) {
	    String sql = String.format(
	        "INSERT INTO sesiones (usuario, password, nombre, rol) VALUES ('%s', '%s', '%s', '%s')",
	        u.getUser(), u.getContrasena(), u.getNombre(), u.getRol()
	    );
	    return db.ejecutarSentencia(sql);
	}
	
	/**
	 * Método para eliminar un usuario de la base de datos.
	 */
	public boolean eliminarUsuario(int id) {
	    String sql = String.format("DELETE FROM sesiones WHERE id=%d", id);
	    return db.ejecutarSentencia(sql);
	}
}
