# Ejemplo de validación de formularios con Play! framework

## Ventajas y desventajas de utilizar REFRESH AFTER POST para validar errores

Las buenas prácticas indican que en cada formulario donde el usuario ingrese datos, debemos validar los mismos y mostrarle una pantalla con los errores al usuario a fin de que pueda corregirlos.

Existen dos técnicas comunmente utilizadas para realizar esto con play! framework.

Este proyecto tiene por objetivo discutir estas dos técnicas, mostrando las ventajas y desventajas de las mismas.

## Volver a hacer el render del template del formulario SIN REDIRECCIONAR

Supongamos que tenemos el siguiente modelo:

models.User

```
@Entity
public class User extends Model {
 
    @Required(message="debe ingresar el nombre del usuario.")
	public String name;
	
	@Required(message="debe ingresar el apellido del usuario.")
	public String surname;

	public User() {
		name = "nuevo usuario";
		surname = "nuevo apellido";
	}
	
}
```

