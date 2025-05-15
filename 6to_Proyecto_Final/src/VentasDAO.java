import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        String sql = "SELECT id, id_usuario, ventas, fecha, total, estado FROM ventas";
        try {
            ResultSet rs = db.obtenerSentencia(sql);
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado")
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = String.format(
            "SELECT id, id_usuario, ventas, fecha, total, estado FROM ventas " +
            "WHERE fecha BETWEEN #%s# AND #%s#",
            sdf.format(fechaInicio), sdf.format(fechaFin)
        );
        
        try {
            ResultSet rs = db.obtenerSentencia(sql);
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado")
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
        String sql = String.format(
            "SELECT id, id_usuario, ventas, fecha, total, estado FROM ventas " +
            "WHERE estado = '%s'", estado
        );
        
        try {
            ResultSet rs = db.obtenerSentencia(sql);
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado")
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
        String sql = String.format(
            "SELECT id, id_usuario, ventas, fecha, total, estado FROM ventas " +
            "WHERE id_usuario = %d", idUsuario
        );
        
        try {
            ResultSet rs = db.obtenerSentencia(sql);
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado")
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
        String sql = String.format(
            "SELECT id, id_usuario, ventas, fecha, total, estado FROM ventas " +
            "WHERE id = %d", idVenta
        );
        
        try {
            ResultSet rs = db.obtenerSentencia(sql);
            if (rs.next()) {
                return new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado")
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        StringBuilder sql = new StringBuilder("SELECT id, id_usuario, ventas, fecha, total, estado FROM ventas WHERE 1=1");
        
        if (fechaInicio != null && fechaFin != null) {
            sql.append(String.format(" AND fecha BETWEEN #%s# AND #%s#", 
                       sdf.format(fechaInicio), sdf.format(fechaFin)));
        }
        
        if (idUsuario != -1) {
            sql.append(String.format(" AND id_usuario = %d", idUsuario));
        }
        
        if (estado != null && !estado.isEmpty()) {
            sql.append(String.format(" AND estado = '%s'", estado));
        }
        
        try {
            ResultSet rs = db.obtenerSentencia(sql.toString());
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado")
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
     * Calcula estadísticas de ventas para un período.
     */
    public double[] calcularEstadisticas(Date fechaInicio, Date fechaFin) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = String.format(
            "SELECT COUNT(id) as total_ventas, SUM(ventas) as cantidad_ventas, " +
            "SUM(total) as importe_total, AVG(total) as promedio_venta " +
            "FROM ventas WHERE fecha BETWEEN #%s# AND #%s#",
            sdf.format(fechaInicio), sdf.format(fechaFin)
        );
        
        try {
            ResultSet rs = db.obtenerSentencia(sql);
            if (rs.next()) {
                double[] estadisticas = new double[4];
                estadisticas[0] = rs.getDouble("total_ventas");     // Número de ventas
                estadisticas[1] = rs.getDouble("cantidad_ventas");  // Cantidad de productos
                estadisticas[2] = rs.getDouble("importe_total");    // Importe total
                estadisticas[3] = rs.getDouble("promedio_venta");   // Promedio por venta
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
        String sql = String.format("SELECT nombre FROM sesiones WHERE id = %d", idUsuario);
        try {
            ResultSet rs = db.obtenerSentencia(sql);
            if (rs.next()) {
                return rs.getString("nombre");
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = String.format(
            "INSERT INTO ventas (id_usuario, ventas, fecha, total, estado) " +
            "VALUES (%d, %d, #%s#, %f, '%s')",
            idUsuario, ventas, sdf.format(fecha), total, estado
        );
        return db.ejecutarSentencia(sql);
    }
    
    /**
     * Método para obtener la ultima venta del usuario
     */
    public Venta obtenerUltimaVentaUsuario(int idUsuario) {
        String sql = String.format(
            "SELECT TOP 1 * FROM ventas WHERE id_usuario = %d ORDER BY id DESC", 
            idUsuario
        );
        
        try {
            ResultSet rs = db.obtenerSentencia(sql);
            if(rs.next()) {
                return new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("ventas"),
                    rs.getDate("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método para actualizar la venta existente
     */
    public boolean actualizarVenta(int idVenta, int nuevasVentas, double nuevoTotal) {
        String sql = String.format(
            "UPDATE ventas SET ventas = %d, total = %f WHERE id = %d",
            nuevasVentas, nuevoTotal, idVenta
        );
        return db.ejecutarSentencia(sql);
    }
    
    /**
     * Actualiza el estado de una venta.
     */
    public boolean actualizarEstado(int idVenta, String nuevoEstado) {
        String sql = String.format(
            "UPDATE ventas SET estado = '%s' WHERE id = %d",
            nuevoEstado, idVenta
        );
        return db.ejecutarSentencia(sql);
    }
    
    /**
     * Elimina una venta por su ID.
     */
    public boolean eliminar(int idVenta) {
        String sql = String.format("DELETE FROM ventas WHERE id = %d", idVenta);
        return db.ejecutarSentencia(sql);
    }
}
