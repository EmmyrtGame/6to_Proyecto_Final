/**
 * Productos
 * 
 * Clase de tipo objeto para administrar los productos
 */
public class Productos {
	
	private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String proveedor;
    private String categoria;
    private int cantidad;
    private String codigo;
    
    /**
     * Constructor paramatrizado con todos los parámetros para definir un producto
     */
    public Productos(int id, String nombre, String descripcion, double precio, String proveedor, String categoria, int cantidad, String codigo){
    	this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.codigo = codigo;
    }
    
    /**
     * Constructor por defecto que crea un producto vacío.
     */
    public Productos() {
        this.id = -1;
        this.nombre = "";
        this.descripcion = "";
        this.precio = 0.0;
        this.proveedor = "";
        this.categoria = "";
        this.cantidad = 0;
        this.codigo = "";
    }
    
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public String getProveedor() { return proveedor; }
    public String getCategoria() { return categoria; }
    public int getCantidad() { return cantidad; }
    public String getCodigo() { return codigo; }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
	
}
