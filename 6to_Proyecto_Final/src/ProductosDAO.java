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
    
    /**
     * Obtiene los productos de la base de datos con filtro de búsqueda
     */
    public List<Productos> obtenerBusqueda(String busqueda, String filtro) {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, precio, proveedor, categoria, cantidad, codigo FROM productos";
        
        // Agregar cláusula WHERE si la cadena de búsqueda no está vacía
        if (busqueda != null && !busqueda.isEmpty()) {
            String terminoBusqueda = "%" + busqueda + "%";
            
            sql += " WHERE ";
            
            if (filtro.equals("Todos")) {
                sql += "(nombre LIKE '" + terminoBusqueda + 
                       "' OR descripcion LIKE '" + terminoBusqueda + 
                       "' OR proveedor LIKE '" + terminoBusqueda + 
                       "' OR categoria LIKE '" + terminoBusqueda + 
                       "' OR codigo LIKE '" + terminoBusqueda + "')";
            } else if (filtro.equals("Nombre")) {
                sql += "nombre LIKE '" + terminoBusqueda + "'";
            } else if (filtro.equals("Descripción")) {
                sql += "descripcion LIKE '" + terminoBusqueda + "'";
            } else if (filtro.equals("Precio")) {
                try {
                    double precioBuscado = Double.parseDouble(busqueda);
                    sql += "precio = " + precioBuscado;
                } catch (NumberFormatException e) {
                    sql += "precio LIKE '" + terminoBusqueda + "'";
                }
            } else if (filtro.equals("Proveedor")) {
                sql += "proveedor LIKE '" + terminoBusqueda + "'";
            } else if (filtro.equals("Categoría")) {
                sql += "categoria LIKE '" + terminoBusqueda + "'";
            } else if (filtro.equals("Cantidad")) {
                try {
                    int cantidadBuscada = Integer.parseInt(busqueda);
                    sql += "cantidad = " + cantidadBuscada;
                } catch (NumberFormatException e) {
                    sql += "cantidad LIKE '" + terminoBusqueda + "'";
                }
            } else if (filtro.equals("Código")) {
                sql += "codigo LIKE '" + terminoBusqueda + "'";
            }
        }
        
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
