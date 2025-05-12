import java.sql.ResultSet;
import java.sql.SQLException;

public class Sesion {
	private int idUsuario;
	private String usuario;
	private String contrasena;
	private String nombre;
	private String rol;
	
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
    
    public String getContrasena() {
        return contrasena;
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
