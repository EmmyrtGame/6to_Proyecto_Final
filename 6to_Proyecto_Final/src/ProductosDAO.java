import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        String sql = "SELECT id, nombre, descripcion, precio, proveedor, categoria, cantidad, codigo, imagen FROM productos";
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
                    rs.getString("codigo"),
                    rs.getString("imagen")
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
        String sql = "SELECT id, nombre, descripcion, precio, proveedor, categoria, cantidad, codigo, imagen FROM productos";
        
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
            } else if (filtro.equals("Categoría")) {
                sql += "categoria LIKE '" + terminoBusqueda + "'";
            } else if (filtro.equals("Proveedor")) {
                sql += "proveedor LIKE '" + terminoBusqueda + "'";
            } else if (filtro.equals("Nombre")) {
                sql += "nombre LIKE '" + terminoBusqueda + "'";
            } else if (filtro.equals("Descripción")) {
                sql += "descripcion LIKE '" + terminoBusqueda + "'";
            } else if (filtro.equals("Código")) {
                sql += "codigo LIKE '" + terminoBusqueda + "'";
            } else if (filtro.equals("Precio")) {
                try {
                    double precioBuscado = Double.parseDouble(busqueda);
                    sql += "precio = " + precioBuscado;
                } catch (NumberFormatException e) {
                    sql += "precio LIKE '" + terminoBusqueda + "'";
                }
            } else if (filtro.equals("Cantidad")) {
                try {
                    int cantidadBuscada = Integer.parseInt(busqueda);
                    sql += "cantidad = " + cantidadBuscada;
                } catch (NumberFormatException e) {
                    sql += "cantidad LIKE '" + terminoBusqueda + "'";
                }
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
                    rs.getString("codigo"),
                    rs.getString("imagen")
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
     * Inserta un nuevo producto en la base de datos
     */
    public boolean insertar(String nombre, String descripcion, Double precio, int cantidad, 
                           String proveedor, String categoria, String codigo, String rutaImagen) {
        String sql = String.format(
            "INSERT INTO productos (nombre, descripcion, precio, proveedor, categoria, cantidad, codigo, imagen) " +
            "VALUES ('%s', '%s', %f, '%s', '%s', %d, '%s', '%s')", 
            nombre, descripcion, precio, proveedor, categoria, cantidad, codigo, 
            (rutaImagen != null ? rutaImagen : "")
        );
        return db.ejecutarSentencia(sql);
    }
    
    /**
     * Método para actualizar un producto de la base de datos.
     */
    public boolean actualizarProducto(int id, String nombre, String descripcion, double precio, 
                            int cantidad, String proveedor, String categoria, 
                            String codigo, String rutaImagen) {
        String sql = String.format(
        "UPDATE productos SET nombre='%s', descripcion='%s', precio=%f, proveedor='%s', " +
        "categoria='%s', cantidad=%d, codigo='%s', imagen='%s' WHERE id=%d",
        nombre, descripcion, precio, proveedor, categoria, cantidad, codigo, 
        (rutaImagen != null ? rutaImagen : ""), id
        );
        return db.ejecutarSentencia(sql);
    }

    /**
     * Elimina un producto por su ID
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = " + id;
        return db.ejecutarSentencia(sql);
    }
    
    /**
     * Obtiene un producto por su código único
     */
    public Productos obtenerPorCodigo(String codigo) {
        String sql = "SELECT id, nombre, descripcion, precio, proveedor, categoria, cantidad, codigo, imagen FROM productos WHERE codigo = '" + codigo + "'";
        try {
            ResultSet rs = db.obtenerSentencia(sql);
            if (rs.next()) {
                return new Productos(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getString("proveedor"),
                    rs.getString("categoria"),
                    rs.getInt("cantidad"),
                    rs.getString("codigo"),
                    rs.getString("imagen")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }
        return null;
    }
    
    /**
     * Verifica si existe un código en la base de datos (para ModalAgregar)
     */
    public boolean existeCodigo(String codigo) {
        String consulta = "SELECT COUNT(*) FROM productos WHERE codigo = '" + codigo + "'";
        try {
            ResultSet rs = db.obtenerSentencia(consulta);
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
     * Verifica si existe un código en otro producto (para ModalEditar)
     */
    public boolean existeCodigoEnOtroProducto(String codigo, int idProductoActual) {
        String consulta = "SELECT COUNT(*) FROM productos WHERE codigo = '" + codigo + 
                         "' AND id <> " + idProductoActual;
        try {
            ResultSet rs = db.obtenerSentencia(consulta);
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
     * Actualiza un producto en la base de datos usando un objeto Productos
     */
    public boolean actualizarProducto(Productos producto) {
        return actualizarProducto(
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getPrecio(),
            producto.getCantidad(),
            producto.getProveedor(),
            producto.getCategoria(),
            producto.getCodigo(),
            producto.getRutaImagen()
        );
    }
}
