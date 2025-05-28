/**
 * Proveedor
 * 
 * Clase de tipo objeto para administrar los proveedores del sistema de abarrotes
 */
public class Proveedor {
    
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    
    /**
     * Constructor parametrizado con todos los parámetros para definir un proveedor
     */
    public Proveedor(int id, String nombre, String direccion, String telefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
    }
    
    /**
     * Constructor por defecto que crea un proveedor vacío
     */
    public Proveedor() {
        this.id = -1;
        this.nombre = "";
        this.direccion = "";
        this.telefono = "";
        this.correo = "";
    }
    
    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    
    /**
     * Override del método toString
     */
    @Override
    public String toString() {
        return this.nombre;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
}