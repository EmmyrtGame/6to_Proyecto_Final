import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductosDAO 
 * 
 * En programación, una clase DAO (Data Access Object)
 * es un patrón de diseño que encapsula la lógica de acceso a datos, 
 * separándola de la lógica de negocio de la aplicación.
 */
public class ProductosDAO {
    private ConexionAccess db = new ConexionAccess();

    /**
     * Obtiene todos los productos de la tabla productos.
     */
    public List<Productos> obtenerTodos() {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, precio, proveedor, categoria, cantidad, codigo FROM productos";
        try {
            ResultSet rs = db.obtenerSentencia(sql);
            while (rs.next()) {
                Productos p = new Productos(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getString("proveedor"),
                    rs.getString("categoria"),
                    rs.getInt("cantidad"),
                    rs.getString("codigo")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }
        return lista;
    }
}
