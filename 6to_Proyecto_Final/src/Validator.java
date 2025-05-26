public class Validator {
    public static String validarUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return "El usuario no puede estar vacío";
        }
        
        usuario = usuario.trim();
        
        if (!usuario.matches("^[A-ZÁÉÍÓÚÑa-záéíóúñ]+$")) { 
            return "El usuario solo debe contener letras";
        }
        
        if (usuario.length() < 3) {
            return "El usuario es demasiado corto (mínimo 3 caracteres)";
        }
        
        if (usuario.length() > 15) {
            return "El usuario es demasiado largo (máximo 15 caracteres)";
        }
        
        return "correcto";
    }
    
    public static String validarPassword(String password) { 
        if (password == null || password.trim().isEmpty()) {
            return "La contraseña no puede estar vacía";
        }
        
        if (password.length() < 6) {
            return "La contraseña es demasiado corta (mínimo 6 caracteres)";
        }
        
        if (password.length() > 20) {
            return "La contraseña es demasiado larga (máximo 20 caracteres)";
        }
        
        if (password.matches("^[a-z]+$")) {
            return "La contraseña debe contener al menos una mayúscula o número";
        }
        
        if (password.matches("^[A-Z]+$")) {
            return "La contraseña debe contener al menos una minúscula o número";
        }
        
        return "correcto";
    }
    
    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre no puede estar vacío";
        }
        
        nombre = nombre.trim();

        if (nombre.length() < 2) {
            return "El nombre es demasiado corto (mínimo 2 caracteres)";
        }
        
        if (nombre.length() > 100) {
            return "El nombre es demasiado largo (máximo 100 caracteres)";
        }

        // Permite letras, números, espacios, acentos, puntos, guiones y paréntesis
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 .()\\-]+$")) {
            return "El nombre solo puede contener letras, números, espacios, puntos y guiones";
        }
        
        return "correcto";
    }
    
    public static String validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "La descripción no puede estar vacía";
        }
        
        descripcion = descripcion.trim();
        
        if (descripcion.length() < 5) {
            return "La descripción es demasiado corta (mínimo 5 caracteres)";
        }
        
        if (descripcion.length() > 200) {
            return "La descripción es demasiado larga (máximo 200 caracteres)";
        }
        
        // Permite más caracteres especiales útiles para descripciones
        if (!descripcion.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 .,:;()\\-_%/\\\\]+$")) {
            return "La descripción contiene caracteres no permitidos";
        }
        
        return "correcto";
    }
    
    public static String validarPrecio(String precioString) {
        if (precioString == null || precioString.trim().isEmpty()) {
            return "El precio no puede estar vacío";
        }
        
        precioString = precioString.trim();
        
        // Remover comas para formateo de miles
        String precioLimpio = precioString.replace(",", "");
        
        if (precioLimpio.startsWith(".") || precioLimpio.endsWith(".")) {
            return "Formato de precio incorrecto";
        }
        
        // Validar que no tenga múltiples puntos decimales
        if (precioLimpio.split("\\.").length > 2) {
            return "Formato de precio incorrecto";
        }
        
        try {
            double precio = Double.parseDouble(precioLimpio);
            
            if (precio <= 0) {
                return "El precio debe ser mayor a 0";
            }
            
            if (precio > 999999.99) {
                return "El precio es demasiado alto (máximo $999,999.99)";
            }
            
            // Validar máximo 2 decimales
            String[] partes = precioLimpio.split("\\.");
            if (partes.length == 2 && partes[1].length() > 2) {
                return "El precio no puede tener más de 2 decimales";
            }
            
        } catch (NumberFormatException e) {
            return "El precio debe ser un número válido";
        }
        
        return "correcto";
    }
    
    public static String validarProveedor(String proveedor) {
        if (proveedor == null || proveedor.trim().isEmpty()) {
            return "El proveedor no puede estar vacío";
        }
        
        proveedor = proveedor.trim();

        if (proveedor.length() < 2) {
            return "El proveedor es demasiado corto (mínimo 2 caracteres)";
        }
        
        if (proveedor.length() > 100) {
            return "El proveedor es demasiado largo (máximo 100 caracteres)";
        }
        
        // Permite puntos y otros caracteres útiles para nombres de empresas
        if (!proveedor.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 .&()\\-]+$")) {
            return "El proveedor solo puede contener letras, números, espacios y caracteres básicos";
        }
        
        return "correcto";
    }
    
    public static String validarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return "La categoría no puede estar vacía";
        }
        
        categoria = categoria.trim();
        
        if (categoria.length() < 2) {
            return "La categoría es demasiado corta (mínimo 2 caracteres)";
        }
        
        if (categoria.length() > 50) {
            return "La categoría es demasiado larga (máximo 50 caracteres)";
        }
        
        // Permite números y algunos caracteres especiales útiles
        if (!categoria.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 &\\-]+$")) {
            return "La categoría solo puede contener letras, números, espacios y guiones";
        }
        
        return "correcto";
    }

    public static String validarCantidad(String cantidadString) {
        if (cantidadString == null || cantidadString.trim().isEmpty()) {
            return "La cantidad no puede estar vacía";
        }
        
        cantidadString = cantidadString.trim();
        
        // Remover comas para formateo de miles
        String cantidadLimpia = cantidadString.replace(",", "");
        
        try {
            int cantidad = Integer.parseInt(cantidadLimpia);
            
            if (cantidad < 0) {
                return "La cantidad no puede ser negativa";
            }
            
            if (cantidad > 999999) {
                return "La cantidad es demasiado alta (máximo 999,999)";
            }
            
        } catch (NumberFormatException e) {
            return "La cantidad debe ser un número entero válido";
        }
        
        return "correcto";
    }
    
    public static String validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return "El código no puede estar vacío";
        }
        
        codigo = codigo.trim();
        
        // Remover guiones bajos de la máscara si existen
        String codigoLimpio = codigo.replace("_", "");
        
        if (codigoLimpio.length() != 6) {
            return "El código debe tener exactamente 6 caracteres";
        }
        
        if (!codigoLimpio.matches("^[A-Z0-9]+$")) {
            return "El código solo puede contener letras mayúsculas y números";
        }
        
        return "correcto";
    }
}