import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionAccess {

    private static final String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
    private static final String DB_CARPETA = "DB";
    private static final String DB_ARCHIVO = "abarrotes.accdb";
    private String url;
    private Connection conn;

    public ConexionAccess() {
        // Construye la ruta absoluta al archivo .accdb dentro de la carpeta DB
        String basePath = System.getProperty("user.dir");
        String dbPath = basePath + File.separator + DB_CARPETA + File.separator + DB_ARCHIVO;
        this.url = "jdbc:ucanaccess://" + dbPath;
    }

    private void abrirConexion() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        conn = DriverManager.getConnection(url);
    }
    
    public Connection abrirConexionDirecta() throws SQLException {
        try {
            abrirConexion();
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el driver de Access", e);
        }
        return conn;
    }
    
    public Connection obtenerConexion() throws SQLException {
    	try {
    		if (conn == null || conn.isClosed()) {
    			abrirConexion();
    		}
    	}catch(ClassNotFoundException e) {
    		e.printStackTrace();
    		throw new SQLException("No se pudo cargar el driver", e);
    	}
    	return conn;
    }

    public void cerrarConexion() {
        if (conn != null) {
            try { conn.close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * Ejecuta sentencias INSERT, UPDATE o DELETE.
     */
    public boolean ejecutarSentencia(String sql) {
        boolean resultado = false;
        try {
            abrirConexion();
            Statement st = conn.createStatement();
            int filas = st.executeUpdate(sql);
            resultado = (filas >= 0);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }
        return resultado;
    }

    /**
     * Ejecuta una consulta SELECT y devuelve el ResultSet.
     */
    public ResultSet obtenerSentencia(String sql) throws SQLException {
        try {
            abrirConexion();
            Statement st = conn.createStatement();
            return st.executeQuery(sql);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se cargó el driver de Access.", e);
        }
    }
}