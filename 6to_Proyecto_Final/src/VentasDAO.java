import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * VentasDAO 
 * 
 * En programación, una clase DAO (Data Access Object)
 * es un patrón de diseño que encapsula la lógica de acceso a datos, 
 * separándola de la lógica de negocio de la aplicación.
 */
public class VentasDAO {
    private ConexionAccess db = new ConexionAccess();

    /**
     * Obtiene todas las ventas de la tabla ventas.
     */
    public List<Venta> obtenerTodas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT id, id_usuario, ventas, fecha, total, estado, diferencia FROM ventas ORDER BY fecha DESC";
        try (Connection conn = db.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado"),
                    rs.getDouble("diferencia")
                );
                lista.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }
        return lista;
    }
    
    /**
     * Obtiene las ventas dentro de un rango de fechas.
     */
    public List<Venta> obtenerPorRangoFechas(Date fechaInicio, Date fechaFin) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT id, id_usuario, ventas, fecha, total, estado, diferencia FROM ventas " +
                     "WHERE fecha >= ? AND fecha <= ? ORDER BY fecha DESC";

        try (Connection conn = db.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Establecer la fecha de inicio al comienzo del día
            Calendar calInicio = Calendar.getInstance();
            calInicio.setTime(fechaInicio);
            calInicio.set(Calendar.HOUR_OF_DAY, 0);
            calInicio.set(Calendar.MINUTE, 0);
            calInicio.set(Calendar.SECOND, 0);
            calInicio.set(Calendar.MILLISECOND, 0);
            
            // Establecer la fecha de fin al final del día
            Calendar calFin = Calendar.getInstance();
            calFin.setTime(fechaFin);
            calFin.set(Calendar.HOUR_OF_DAY, 23);
            calFin.set(Calendar.MINUTE, 59);
            calFin.set(Calendar.SECOND, 59);
            calFin.set(Calendar.MILLISECOND, 999);
            
            stmt.setTimestamp(1, new java.sql.Timestamp(calInicio.getTimeInMillis()));
            stmt.setTimestamp(2, new java.sql.Timestamp(calFin.getTimeInMillis()));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado"),
                    rs.getDouble("diferencia")
                );
                lista.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }

        return lista;
    }
    
    /**
     * Obtiene las ventas por estado (pendiente o cerrado).
     */
    public List<Venta> obtenerPorEstado(String estado) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT id, id_usuario, ventas, fecha, total, estado, diferencia FROM ventas WHERE estado = ?";

        try (Connection conn = db.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado"),
                    rs.getDouble("diferencia")
                );
                lista.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }

        return lista;
    }
    
    /**
     * Obtiene las ventas realizadas por un usuario específico.
     */
    public List<Venta> obtenerPorUsuario(int idUsuario) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT id, id_usuario, ventas, fecha, total, estado, diferencia FROM ventas WHERE id_usuario = ?";

        try (Connection conn = db.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado"),
                    rs.getDouble("diferencia")
                );
                lista.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }

        return lista;
    }
    
    /**
     * Obtiene los detalles de una venta específica por su ID.
     */
    public Venta obtenerPorId(int idVenta) {
        String sql = "SELECT id, id_usuario, ventas, fecha, total, estado, diferencia FROM ventas WHERE id = ?";

        try (Connection conn = db.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenta);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado"),
                    rs.getDouble("diferencia")
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
     * Obtiene las ventas filtradas por múltiples criterios.
     */
    public List<Venta> obtenerVentasFiltradas(Date fechaInicio, Date fechaFin, int idUsuario, String estado) {
        List<Venta> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, id_usuario, ventas, fecha, total, estado, diferencia FROM ventas WHERE 1=1");
        List<Object> parametros = new ArrayList<>();

        if (fechaInicio != null && fechaFin != null) {
            sql.append(" AND fecha BETWEEN ? AND ?");
            parametros.add(new java.sql.Date(fechaInicio.getTime()));
            parametros.add(new java.sql.Date(fechaFin.getTime()));
        }

        if (idUsuario != -1) {
            sql.append(" AND id_usuario = ?");
            parametros.add(idUsuario);
        }

        if (estado != null && !estado.isEmpty()) {
            sql.append(" AND estado = ?");
            parametros.add(estado);
        }

        try {
            PreparedStatement ps = db.obtenerConexion().prepareStatement(sql.toString());

            for (int i = 0; i < parametros.size(); i++) {
                Object valor = parametros.get(i);
                if (valor instanceof Integer) {
                    ps.setInt(i + 1, (Integer) valor);
                } else if (valor instanceof String) {
                    ps.setString(i + 1, (String) valor);
                } else if (valor instanceof java.sql.Date) {
                    ps.setDate(i + 1, (java.sql.Date) valor);
                }
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado"),
                    rs.getDouble("diferencia")
                );
                lista.add(v);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();
        }

        return lista;
    }
    
    /**
     * Calcula estadísticas de ventas para un período.
     */
    
    public double[] calcularEstadisticas(Date fechaInicio, Date fechaFin) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "SELECT COUNT(id) as total_ventas, SUM(ventas) as cantidad_ventas, " +
                     "SUM(total) as importe_total, AVG(total) as promedio_venta " +
                     "FROM ventas WHERE fecha BETWEEN ? AND ?";

        try {
            // Preparamos la consulta y enviamos los valores de las fechas
            PreparedStatement ps = db.obtenerConexion().prepareStatement(sql);
            ps.setString(1, sdf.format(fechaInicio) + " 00:00:00");
            ps.setString(2, sdf.format(fechaFin) + " 23:59:59");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double[] estadisticas = new double[4];
                estadisticas[0] = rs.getDouble("total_ventas");     // Número de ventas
                estadisticas[1] = rs.getDouble("cantidad_ventas");  // Cantidad de productos
                estadisticas[2] = rs.getDouble("importe_total");    // Importe total
                estadisticas[3] = rs.getDouble("promedio_venta");   // Promedio por venta
                rs.close();
                ps.close();
                return estadisticas;
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        } finally {
            db.cerrarConexion();  
        }

        return new double[4];
    }

    
    /**
     * Obtiene el nombre del cajero que realizó una venta.
     */
    
    public String obtenerNombreCajero(int idUsuario) {
        String sql = "SELECT nombre FROM sesiones WHERE id = ?";

        try {
            
            PreparedStatement ps = db.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, idUsuario); 

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                rs.close();
                ps.close();
                return nombre;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();  
        }

        return "";  
    }

    
    /**
     * Inserta una nueva venta en la base de datos.
     */
    public boolean insertar(int idUsuario, int ventas, Date fecha, double total, String estado) {
        String sql = "INSERT INTO ventas (id_usuario, ventas, fecha, total, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = db.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, ventas);
            stmt.setDate(3, new java.sql.Date(fecha.getTime()));
            stmt.setDouble(4, total);
            stmt.setString(5, estado);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.cerrarConexion();
        }
    }

    
    /**
     * Método para obtener la ultima venta del usuario
     */
    public Venta obtenerUltimaVentaUsuario(int idUsuario) {
        String sql = "SELECT TOP 1 id, id_usuario, ventas, fecha, total, estado, diferencia FROM ventas " +
                     "WHERE id_usuario = ? ORDER BY fecha DESC";

        try (Connection conn = db.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado"),
                    rs.getDouble("diferencia")
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
     * Método para actualizar la venta existente
     */
    public boolean actualizarVenta(int idVenta, int nuevasVentas, double nuevoTotal) {
        String sql = "UPDATE ventas SET ventas = ?, total = ? WHERE id = ?";
        try (Connection conn = db.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuevasVentas);
            stmt.setDouble(2, nuevoTotal);
            stmt.setInt(3, idVenta);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.cerrarConexion();
        }
    }
    
    /**
     * Método específico para actualizar venta en proceso de corte de caja
     */
   
    public boolean actualizarVentaCorte(int idVenta, double nuevoTotal, String nuevoEstado, double diferencia) {
        String sql = "UPDATE ventas SET total = ?, estado = ? WHERE id = ?";

        try {
            // Preparamos la consulta segura
            PreparedStatement ps = db.obtenerConexion().prepareStatement(sql);
            ps.setDouble(1, nuevoTotal);    
            ps.setString(2, nuevoEstado);    
            ps.setInt(3, idVenta);          

            int filasAfectadas = ps.executeUpdate();  

            ps.close();  
            return filasAfectadas > 0; 

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.cerrarConexion();  
        }

        return false; 
    }

    
    /**
     * Actualiza el estado de una venta.
     */
    public boolean actualizarEstado(int idVenta, String nuevoEstado) {
        String sql = "UPDATE ventas SET estado = ? WHERE id = ?";
        try (Connection conn = db.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idVenta);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.cerrarConexion();
        }
    }
    
    /**
     * Elimina una venta por su ID.
     */
    public boolean eliminar(int idVenta) {
        String sql = "DELETE FROM ventas WHERE id = ?";
        try (Connection conn = db.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenta);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.cerrarConexion();
        }
    }
}