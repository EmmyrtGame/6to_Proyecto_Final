import java.sql.ResultSet;
import java.sql.SQLException;

public class Sesion {
	public static int idUsuario;
	public static String usuario;
	public static String contrasena;
	public static String nombre;
	public static String rol;
	
	public static ConexionAccess db;
	
	/**
	 * Método constructor
	 */
	public Sesion(int idUsuario, String usuario, String contrasena, String nombre, String rol)
	{
		this.idUsuario = idUsuario;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.nombre = nombre;
		this.rol = rol;
		
		db = new ConexionAccess();
	}
	
	public boolean Autenticar()
	{
	    String consulta = String.format("SELECT * FROM sesiones WHERE usuario='%s' AND password='%s'", usuario, contrasena);
	    try {
	        ResultSet rs = db.obtenerSentencia(consulta);
	        if (rs.next()) {
	            this.idUsuario = rs.getInt("id");
	            this.nombre = rs.getString("nombre");
	            this.usuario = rs.getString("usuario");
	            this.contrasena = rs.getString("password");
	            this.rol = rs.getString("rol");
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        db.cerrarConexion();
	    }
	    return false;
	}
	
	public void Desautenticar()
	{
		this.idUsuario = -1;
		this.nombre = "";
		this.usuario = "";
		this.contrasena = "";
		this.rol = "";
	}

	
	public void setUser(String usuario)
	{
		this.usuario = usuario;
	}
	
	public void setContra(String contrasena)
	{
		this.contrasena = contrasena;
	}
}
