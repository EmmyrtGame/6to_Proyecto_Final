
import java.util.Date;
/**
 * Ventas
 * 
 * Clase de tipo objeto para administrar las ventas
 */
public class Venta {
    
    private int id;
    private int id_usuario;
    private int ventas;
    private Date fecha;
    private double total;
    private String estado;
    
    /**
     * Constructor paramatrizado con todos los parámetros para definir una venta
     */
    public Venta(int id, int id_usuario, int ventas, Date fecha, double total, String estado) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.ventas = ventas;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
    }
    
    /**
     * Constructor por defecto que crea una venta vacía.
     */
    public Venta() {
        this.id = -1;
        this.id_usuario = -1;
        this.ventas = 0;
        this.fecha = null;
        this.total = 0.0;
        this.estado = "";
    }
    
    public int getId() { return id; }
    public int getIdUsuario() { return id_usuario; }
    public int getVentas() { return ventas; }
    public Date getFecha() { return fecha; }
    public double getTotal() { return total; }
    public String getEstado() { return estado; }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setIdUsuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setVentas(int ventas) {
        this.ventas = ventas;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public void setTotal(double total) {
		this.total = total;
	}
    
    public void setEstado(String estado) {
    	this.estado = estado;
    }
}
