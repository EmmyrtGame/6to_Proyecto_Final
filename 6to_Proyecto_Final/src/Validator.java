
public class Validator {

	public static String validarUsuario(String usuario) {
		
		
		if(!usuario.matches("^[A-ZÁÉÍÓÚÑa-záéíóúñ]+(\s[A-ZÁÉÍÓÚÑa-záéíóúñ]+)*$")) { 
			
			return "Nombre incorrecto (solo debe de contener letras)";
		}
		
		if((usuario.length()) <5) {
			
			return "usuario demasiado corto";
		}
		
		if(usuario.length() > 10) {
			return "usuario demasiado largo";
		}
		
		return "correcto";
		
	
	}
	
	
	public static String validarPassword(String password) { 
		
		//esto es para ver si la caja de texto está completamente vacía
		if(password.trim().isEmpty()) {
			
			return "Contraseña incorrecta (contraseña vacía)"; //se retorna un mensaje de error
		}
		
		//Determina el límite de longitud de la contra. Esto es opcional pero suele ser común en las contraseñas. 
		if((password.length()) <5) {
			
			return "contraseña demasiado corta";
		}
		
		if(password.length() > 10) {
			return "contraseña demasiado larga";
		}
		
		if(password.matches("^[a-z]+")) {
			return "La contraseña no debe de tener solo minúsculas";
		}
		
		if(password.matches("^[A-Z]+")) {
			return "La contraseña no debe de tener solo mayúsculas";
		}
		
		return "correcto";
	}
	
	
	public static String validarNombre(String nombre) {
		        nombre = nombre.trim();
		        
		        if(nombre.isEmpty()) {
		        	return "El nombre está vacío";
		        }

		        if (nombre.length() < 5) {
		            return "El nombre es demasiado corto.";
		        }
		        if (nombre.length() > 60) {
		            return "El nombre es demasiado largo.";
		        }

		        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+")) {
		            return "El nombre solo puede contener letras, números, espacios y acentos.";
		        }
		        return "correcto";
	}
	
	
	public static String validarDescripcion(String descripcion) {
		
		if(descripcion.trim().isEmpty()) {
			
			return "La descripción está vacía";
		}
		
		if(!descripcion.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
			return "Solo se pueden poner letras y espacios";
		}
		
		if (descripcion.length() < 5) {
            return "La descripción es demasiado corta.";
        }
		if(descripcion.length() > 70) {
			
			return "Descripción muy larga";
		}
		
		return "correcto";
	}
	
	
	public static String validarPrecio(String precioStrng) {
		
		if(precioStrng.trim().isEmpty() ) {
			return "El precio está vacío";
		}
		
		if(precioStrng.startsWith(".")) {
			return "precio incorrecto (no puede iniciar con punto)";
		}
		
		try {
			double precio = Double.parseDouble(precioStrng);
				if(precio <= 0) {
				return "Precio incorrecto (debe ser mayor a 0)";
			} 
			
			}catch(NumberFormatException e) {
				return "El precio debe de ser un número";		
		}
		
		return "correcto";
	}
	
	
	public static String validarProveedor(String proveedor) {
		
		if(proveedor.trim().isEmpty()) {
			return "El proveedor está vacío";
		}
		
		if (proveedor.length() < 5) {
            return "El proveedor es demasiado corto.";
        }
		if(proveedor.length() > 70) {
			
			return "Descripción muy larga";
		}
		
		if(!proveedor.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
			return "Solo se pueden poner letras y espacios";
		}
		
		return "correcto";
	}
	
	
	public static String validarCategoria(String categoria) {
		
		if(categoria.trim().isEmpty()) {
			return "La categoría está vacía";
		}
		
		if (categoria.length() < 5) {
            return "La categoría es demasiado corta.";
        }
		
		if(!categoria.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
			return "Solo se pueden poner letras y espacios";
		}
		
		return "correcto";
	}

	
	public static String validarCantidad(String cantidadStrng) {
	
		if(cantidadStrng.trim().isEmpty()) {
			return "La cantidad está vacía";
		}
		
		try {
			int cantidad = Integer.parseInt(cantidadStrng);
			if(cantidad <= 0) {
				return "Precio incorrecto (debe de sser mayoir a 0)";
			}
		}catch(NumberFormatException e) {
			return "La cantidad debe de ser un valo numérico entero";
		}
		
		return "correcto";
	}
	
	
	public static String validarCodigo(String codigo) {
		
		if(codigo.trim().isEmpty()) {
			return "El código está vacío";
		}
		
		if(codigo.length() < 6) {
			return "El código debe de tener 6 o más dfígitos";
		}
		
		if(!codigo.matches("[a-zA-Z0-9]+")) {
			return "código incorrecto (solo debe de tener numeros y letras)";
		}
		
		return "correcto";
	}
}
