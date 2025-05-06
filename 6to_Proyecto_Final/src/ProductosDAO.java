import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductosDAO {
    private ConexionAccess db = new ConexionAccess();

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
    
    public Productos obtenerPorCodigo(String codigo) {
        Productos producto = null;
        String sql = "SELECT id, nombre, descripcion, precio, proveedor, categoria, cantidad, codigo " +
                     "FROM productos WHERE codigo = '" + codigo + "'";
        try {
            ResultSet rs = db.obtenerSentencia(sql);
            if (rs.next()) {
                producto = new Productos(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getString("proveedor"),
                    rs.getString("categoria"),
                    rs.getInt("cantidad"),
                    rs.getString("codigo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }
        return producto; 
    }

    public void actualizarProducto(Productos producto) {
        String sql = "UPDATE productos SET " +
                     "nombre = '" + producto.getNombre() + "', " +
                     "descripcion = '" + producto.getDescripcion() + "', " +
                     "precio = " + producto.getPrecio() + ", " +
                     "proveedor = '" + producto.getProveedor() + "', " +
                     "categoria = '" + producto.getCategoria() + "', " +
                     "cantidad = " + producto.getCantidad() + " " +
                     "WHERE codigo = '" + producto.getCodigo() + "'";
        try {
            db.obtenerSentencia(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }
    }

    public void eliminarProducto(String codigo) {
        String sql = "DELETE FROM productos WHERE codigo = '" + codigo + "'";
        try {
            db.obtenerSentencia(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }
    }
}
