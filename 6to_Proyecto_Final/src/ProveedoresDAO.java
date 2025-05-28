import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ProveedoresDAO
 * 
 * Clase DAO (Data Access Object) que encapsula la lógica de acceso a datos
 * para la gestión de proveedores en la base de datos Access
 */
public class ProveedoresDAO {
    private ConexionAccess db = new ConexionAccess();
    
    /**
     * Registra un nuevo proveedor en la base de datos
     */
    public boolean registrar(String nombre, String direccion, String telefono, String correo) {
        String sql = "INSERT INTO proveedores (nombre, direccion, telefono, correo) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setString(3, telefono);
            ps.setString(4, correo);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Registra un nuevo proveedor usando un objeto Proveedor
     */
    public boolean registrar(Proveedor proveedor) {
        return registrar(proveedor.getNombre(), proveedor.getDireccion(), 
                        proveedor.getTelefono(), proveedor.getCorreo());
    }
    
    /**
     * Obtiene todos los proveedores de la base de datos
     */
    public List<Proveedor> leer() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, direccion, telefono, correo FROM proveedores";
        
        try (Connection conn = db.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("correo")
                );
                lista.add(proveedor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    /**
     * Obtiene un proveedor específico por su ID
     */
    public Proveedor obtenerPorId(int id) {
        String sql = "SELECT id, nombre, direccion, telefono, correo FROM proveedores WHERE id = ?";
        
        try (Connection conn = db.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Proveedor(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("correo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Modifica los datos de un proveedor existente y actualiza productos relacionados
     */
    public boolean modificar(int id, String nombre, String direccion, String telefono, String correo) {
        String sqlProveedor = "UPDATE proveedores SET nombre = ?, direccion = ?, telefono = ?, correo = ? WHERE id = ?";
        String sqlProductos = "UPDATE productos SET proveedor = ? WHERE id_proveedor = ?";
        
        try (Connection conn = db.obtenerConexion()) {
            // Iniciar transacción
            conn.setAutoCommit(false);
            
            try {
                // Actualizar proveedor
                PreparedStatement psProveedor = conn.prepareStatement(sqlProveedor);
                psProveedor.setString(1, nombre);
                psProveedor.setString(2, direccion);
                psProveedor.setString(3, telefono);
                psProveedor.setString(4, correo);
                psProveedor.setInt(5, id);
                
                // Actualizar productos con el nuevo nombre del proveedor
                PreparedStatement psProductos = conn.prepareStatement(sqlProductos);
                psProductos.setString(1, nombre);
                psProductos.setInt(2, id);
                
                boolean proveedorActualizado = psProveedor.executeUpdate() > 0;
                psProductos.executeUpdate(); // Actualizar productos (puede ser 0 si no hay productos)
                
                if (proveedorActualizado) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene todos los proveedores para uso en ComboBox
     */
    public List<Proveedor> obtenerParaComboBox() {
        return leer(); // Reutiliza el método existente
    }
    
    /**
     * Modifica un proveedor usando un objeto Proveedor
     */
    public boolean modificar(Proveedor proveedor) {
        return modificar(proveedor.getId(), proveedor.getNombre(), 
                        proveedor.getDireccion(), proveedor.getTelefono(), 
                        proveedor.getCorreo());
    }
    
    /**
     * Elimina un proveedor de la base de datos
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM proveedores WHERE id = ?";
        
        try (Connection conn = db.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Busca proveedores por nombre (búsqueda parcial)
     */
    public List<Proveedor> buscarPorNombre(String nombre) {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, direccion, telefono, correo FROM proveedores WHERE nombre LIKE ?";
        
        try (Connection conn = db.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("correo")
                );
                lista.add(proveedor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    /**
     * Obtiene los proveedores filtrados según el término de búsqueda y el tipo de filtro.
     * Este método utiliza PreparedStatement para evitar inyecciones SQL.
     */
    public List<Proveedor> obtenerBusqueda(String busqueda, String filtro) {
        List<Proveedor> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, nombre, direccion, telefono, correo FROM proveedores");
        List<String> campos = new ArrayList<>();

        // Verificamos que haya texto de búsqueda
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append(" WHERE ");

            // Definimos el filtro según la selección
            switch (filtro) {
                case "Todos":
                    campos.add("nombre");
                    campos.add("direccion");
                    campos.add("telefono");
                    campos.add("correo");
                    break;
                case "Nombre":      campos.add("nombre"); break;
                case "Dirección":   campos.add("direccion"); break;
                case "Teléfono":    campos.add("telefono"); break;
                case "Correo":      campos.add("correo"); break;
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
                }
            }

            // Ejecutamos la consulta y llenamos la lista
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("correo")
                );
                lista.add(proveedor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    /**
     * Verifica si existe un proveedor con el correo especificado
     */
    public boolean existeCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM proveedores WHERE correo = ?";
        
        try (Connection conn = db.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}