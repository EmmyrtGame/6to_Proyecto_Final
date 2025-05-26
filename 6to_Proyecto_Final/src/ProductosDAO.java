import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductosDAO 
 * 
 * En programación, una clase DAO (Data Access Object)s
 * es un patrón de diseño que encapsula la lógica de acceso a datos, 
 * separándola de la lógica de negocio de la aplicación.
 */
public class ProductosDAO {
    private ConexionAccess db = new ConexionAccess();

    /**
     * Obtiene todos los productos de la tabla productos.
     */
    /**
     * Obtiene todos los productos de la base de datos.
     * Usa PreparedStatement para mayor seguridad y rendimiento.
     */
    public List<Productos> obtenerTodos() {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, precio, proveedor, categoria, cantidad, codigo, imagen FROM productos";
        try (Connection conn = db.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql); // Prepara la consulta
             ResultSet rs = ps.executeQuery()) { // Ejecuta la consulta

            // Recorre los resultados y agrega a la lista
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
        }
        return lista;
    }
    
    /**
     * Obtiene los productos de la base de datos con filtro de búsqueda
     */
    /**
     * Obtiene los productos filtrados según el término de búsqueda y el tipo de filtro.
     * Este método utiliza PreparedStatement para evitar inyecciones SQL.
     */
    public List<Productos> obtenerBusqueda(String busqueda, String filtro) {
        List<Productos> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, nombre, descripcion, precio, proveedor, categoria, cantidad, codigo, imagen FROM productos");
        List<String> campos = new ArrayList<>();
        boolean esNumero = false;

        // Verificamos que haya texto de búsqueda
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append(" WHERE ");

            // Definimos el filtro según la selección
            switch (filtro) {
                case "Todos":
                    campos.add("nombre");
                    campos.add("descripcion");
                    campos.add("proveedor");
                    campos.add("categoria");
                    campos.add("codigo");
                    break;
                case "Nombre":       campos.add("nombre"); break;
                case "Descripción":  campos.add("descripcion"); break;
                case "Proveedor":    campos.add("proveedor"); break;
                case "Categoría":    campos.add("categoria"); break;
                case "Código":       campos.add("codigo"); break;
                case "Precio":
                    try {
                        Double.parseDouble(busqueda);
                        sql.append("precio = ?");
                        esNumero = true;
                    } catch (NumberFormatException e) {
                        sql.append("precio LIKE ?");
                    }
                    break;
                case "Cantidad":
                    try {
                        Integer.parseInt(busqueda);
                        sql.append("cantidad = ?");
                        esNumero = true;
                    } catch (NumberFormatException e) {
                        sql.append("cantidad LIKE ?");
                    }
                    break;
            }

            // Si son campos de texto, se arma la cláusula con LIKE
            if (!campos.isEmpty()) {
                for (int i = 0; i < campos.size(); i++) {
                    sql.append(campos.get(i)).append(" LIKE ?");
                    if (i < campos.size() - 1) {
                        sql.append(" OR ");
                    }
                }
            }
        }

        try (Connection conn = db.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Asignamos los valores dinámicos a los parámetros
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                if (!campos.isEmpty()) {
                    for (int i = 0; i < campos.size(); i++) {
                        ps.setString(i + 1, "%" + busqueda + "%");
                    }
                } else {
                    // Precio o Cantidad con valor exacto
                    if (esNumero) {
                        if (filtro.equals("Precio")) {
                            ps.setDouble(1, Double.parseDouble(busqueda));
                        } else if (filtro.equals("Cantidad")) {
                            ps.setInt(1, Integer.parseInt(busqueda));
                        }
                    } else {
                        ps.setString(1, "%" + busqueda + "%");
                    }
                }
            }

            // Ejecutamos la consulta y llenamos la lista
            ResultSet rs = ps.executeQuery();
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
        }

        return lista;
    }
    
    /**
     * Inserta un nuevo producto en la base de datos.
     * Se utiliza PreparedStatement para prevenir inyecciones SQL
     * y parametrizar los valores de entrada.
     */
    public boolean insertar(String nombre, String descripcion, Double precio, int cantidad,
                            String proveedor, String categoria, String codigo, String rutaImagen) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, proveedor, categoria, cantidad, codigo, imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Asignación de parámetros de manera segura (evita inyecciones SQL)
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, precio);
            ps.setString(4, proveedor);
            ps.setString(5, categoria);
            ps.setInt(6, cantidad);
            ps.setString(7, codigo);
            ps.setString(8, (rutaImagen != null ? rutaImagen : "")); // Aseguraa que no se pase null

            return ps.executeUpdate() > 0; // Ejecuta la sentencia y verifica si se insertó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Método para actualizar un producto de la base de dato
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
     * Elimina un producto de la base de datos usando su ID.
     * Se emplea PreparedStatement para asegurar el tipo de dato y evitar inyección.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection conn = db.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id); // Se pasa el ID del producto a eliminar

            return ps.executeUpdate() > 0; // Si al menos una fila fue eliminada, devuelve true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene un producto por su código único
     * @param codigo El código del producto a buscar
     * @return El producto encontrado o null si no existe
     */
    /**
     * Busca un producto por su código único.
     * Se utiliza PreparedStatement para proteger contra inyección SQL.
     */
    public Productos obtenerPorCodigo(String codigo) {
        String sql = "SELECT id, nombre, descripcion, precio, proveedor, categoria, cantidad, codigo, imagen FROM productos WHERE codigo = ?";
        try (Connection conn = db.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo); // Asigna el valor al parámetro de búsqueda

            ResultSet rs = ps.executeQuery(); // Ejecuta la consulta
            if (rs.next()) {
                // Si encuentra un producto, lo devuelve como objeto
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
        }
        return null; // Si no encuentra el producto, devuelve null
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
     * @param producto El objeto Productos con la información actualizada
     * @return true si la actualización fue exitosa, false en caso contrario
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
