import java.sql.ResultSet;
import java.sql.SQLException;

public class Sesion {
	private int idUsuario;
	private String usuario;
	private String contrasena;
	private String nombre;
	private String rol;
	
	private ConexionAccess db;
	
	/**
	 * Método constructor paramatrizado
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
	
	/**
	 * Método constructor por defecto
	 */
	public Sesion()
	{
		this.idUsuario = -1;
		this.usuario = "";
		this.contrasena = "";
		this.nombre = "";
		this.rol = "";
		
		this.db = new ConexionAccess();
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
		setId(-1);
		setNombre("");
		setUser("");
		setContra("");
		setRol("");
	}
	
	public int getIdUsuario() {
        return idUsuario;
    }

    public String getUser() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }
	
	public void setRol(String rol)
	{
		this.rol = rol;
	}
	
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
	
	public void setId(int id)
	{
		this.idUsuario = id;
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
