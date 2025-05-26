import java.sql.Connection;
import java.sql.PreparedStatement;
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
	 * Obtiene todos los usuarios de la tabla sesiones.
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
	 * Obtiene los usuarios de la base de datos con filtro de búsqueda.
	 * Se usa PreparedStatement para evitar inyecciones SQL.
	 */
	public List<Sesion> obtenerBusqueda(String busqueda, String filtro, int idUsuarioActual) {
	    List<Sesion> lista = new ArrayList<>();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    Connection conn = null;

	    try {
	        conn = db.abrirConexionDirecta(); // Se usa una conexión directa desde el DAO
	        String sqlBase = "SELECT id, usuario, password, nombre, rol FROM sesiones WHERE 1=1";

	        if (busqueda != null && !busqueda.isEmpty()) {
	            String terminoBusqueda = "%" + busqueda + "%";
	            switch (filtro) {
	                case "Todos":
	                	sqlBase += " AND (usuario LIKE ? OR nombre LIKE ? OR rol LIKE ?)";
	                    pstmt = conn.prepareStatement(sqlBase);
	                    pstmt.setString(1, terminoBusqueda);
	                    pstmt.setString(2, terminoBusqueda);
	                    pstmt.setString(3, terminoBusqueda);
	                    break;
	                case "Usuario":
	                    sqlBase += " AND usuario LIKE ?";
	                    pstmt = conn.prepareStatement(sqlBase);
	                    pstmt.setString(1, terminoBusqueda);
	                    break;
	                case "Nombre":
	                    sqlBase += " AND nombre LIKE ?";
	                    pstmt = conn.prepareStatement(sqlBase);
	                    pstmt.setString(1, terminoBusqueda);
	                    break;
	                case "Rol":
	                    sqlBase += " AND rol LIKE ?";
	                    pstmt = conn.prepareStatement(sqlBase);
	                    pstmt.setString(1, terminoBusqueda);
	                    break;
	                default:
	                    pstmt = conn.prepareStatement(sqlBase);
	            }
	        } else {
	            pstmt = conn.prepareStatement(sqlBase); // Sin filtros si la búsqueda está vacía
	        }

	        rs = pstmt.executeQuery();
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
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return lista;
	}

	/**
	 * Método para autenticar un usuario en la base de datos.
	 * Compara las credenciales ingresadas con las almacenadas.
	 */
	public boolean Autenticar(Sesion u) {
        String consulta = "SELECT * FROM sesiones WHERE usuario = ?";
        try {
            Connection conn = db.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(consulta);
            ps.setString(1, u.getUser());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String usuarioDB = rs.getString("usuario");
                String passwordDB = rs.getString("password");

                if (usuarioDB.equals(u.getUser()) && passwordDB.equals(u.getContrasena())) {
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setUser(usuarioDB);
                    u.setContra(passwordDB);
                    u.setRol(rs.getString("rol"));
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }
        return false;
    }

	/**
	 * Método para insertar un nuevo usuario en la base de datos.
	 * Incluye validación de duplicados para usuario y nombre.
	 */
	public boolean insertarUsuario(Sesion u) {
	    // Validar que no exista el usuario
	    if (existeUsuario(u.getUser())) {
	        throw new IllegalArgumentException("Ya existe un usuario con ese nombre de usuario");
	    }
	    
	    // Validar que no exista el nombre
	    if (existeNombre(u.getNombre())) {
	        throw new IllegalArgumentException("Ya existe un usuario con ese nombre");
	    }
	    
	    String sql = "INSERT INTO sesiones (usuario, password, nombre, rol) VALUES (?, ?, ?, ?)";
	    try {
	        Connection conn = db.obtenerConexion();
	        PreparedStatement ps = conn.prepareStatement(sql);

	        ps.setString(1, u.getUser());
	        ps.setString(2, u.getContrasena());
	        ps.setString(3, u.getNombre());
	        ps.setString(4, u.getRol());

	        int filas = ps.executeUpdate();
	        return filas > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        db.cerrarConexion();
	    }
	    return false;
	}
	
	/**
	 * Método para actualizar un usuario en la base de datos.
	 * Incluye validación de duplicados para usuario y nombre.
	 */
	public boolean actualizarUsuario(Sesion u) {
	    // Validar que no exista otro usuario con el mismo nombre de usuario
	    if (existeUsuario(u.getUser(), u.getIdUsuario())) {
	        throw new IllegalArgumentException("Ya existe otro usuario con ese nombre de usuario");
	    }
	    
	    // Validar que no exista otro usuario con el mismo nombre
	    if (existeNombre(u.getNombre(), u.getIdUsuario())) {
	        throw new IllegalArgumentException("Ya existe otro usuario con ese nombre");
	    }
	    
	    String sql = "UPDATE sesiones SET usuario=?, password=?, nombre=?, rol=? WHERE id=?";
	    try {
	        Connection conn = db.obtenerConexion();
	        PreparedStatement ps = conn.prepareStatement(sql);

	        ps.setString(1, u.getUser());
	        ps.setString(2, u.getContrasena());
	        ps.setString(3, u.getNombre());
	        ps.setString(4, u.getRol());
	        ps.setInt(5, u.getIdUsuario());

	        int filas = ps.executeUpdate();
	        return filas > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        db.cerrarConexion();
	    }
	    return false;
	}

	
	/**
	 * Verifica si ya existe un usuario con el nombre de usuario especificado
	 */
	public boolean existeUsuario(String usuario) {
	    String sql = "SELECT COUNT(*) FROM sesiones WHERE usuario = ?";
	    try {
	        Connection conn = db.obtenerConexion();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, usuario);
	        
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        db.cerrarConexion();
	    }
	    return false;
	}

	/**
	 * Verifica si ya existe un usuario con el nombre especificado
	 */
	public boolean existeNombre(String nombre) {
	    String sql = "SELECT COUNT(*) FROM sesiones WHERE nombre = ?";
	    try {
	        Connection conn = db.obtenerConexion();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, nombre);
	        
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        db.cerrarConexion();
	    }
	    return false;
	}

	/**
	 * Verifica si existe un usuario con el nombre de usuario especificado,
	 * excluyendo un ID específico (para validaciones en actualizaciones)
	 */
	public boolean existeUsuario(String usuario, int idExcluir) {
	    String sql = "SELECT COUNT(*) FROM sesiones WHERE usuario = ? AND id <> ?";
	    try {
	        Connection conn = db.obtenerConexion();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, usuario);
	        ps.setInt(2, idExcluir);
	        
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        db.cerrarConexion();
	    }
	    return false;
	}

	/**
	 * Verifica si existe un usuario con el nombre especificado,
	 * excluyendo un ID específico (para validaciones en actualizaciones)
	 */
	public boolean existeNombre(String nombre, int idExcluir) {
	    String sql = "SELECT COUNT(*) FROM sesiones WHERE nombre = ? AND id <> ?";
	    try {
	        Connection conn = db.obtenerConexion();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, nombre);
	        ps.setInt(2, idExcluir);
	        
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        db.cerrarConexion();
	    }
	    return false;
	}

	/**
	 * Método para eliminar un usuario de la base de datos.
	 */
	public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM sesiones WHERE id=?";
        try {
            Connection conn = db.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }
        return false;
    }
}
