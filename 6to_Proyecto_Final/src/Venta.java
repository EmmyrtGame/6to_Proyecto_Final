/**
 * Ventas
 * 
 * Clase de tipo objeto para administrar las ventas
 */
public class Venta {
    
    private int id;
    private int id_usuario;
    private int ventas;
    private String fecha;
    
    /**
     * Constructor paramatrizado con todos los parámetros para definir una venta
     */
    public Venta(int id, int id_usuario, int ventas, String fecha) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.ventas = ventas;
        this.fecha = fecha;
    }
    
    /**
     * Constructor por defecto que crea una venta vacía.
     */
    public Venta() {
        this.id = -1;
        this.id_usuario = -1;
        this.ventas = 0;
        this.fecha = "";
    }
    
    public int getId() { return id; }
    public int getIdUsuario() { return id_usuario; }
    public int getVentas() { return ventas; }
    public String getFecha() { return fecha; }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setIdUsuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setVentas(int ventas) {
        this.ventas = ventas;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
