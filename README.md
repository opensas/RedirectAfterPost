# Ejemplo de validación de formularios con Play! framework

## Ventajas y desventajas de utilizar REFRESH AFTER POST para validar errores

Las buenas prácticas indican que en cada formulario donde el usuario ingrese datos, debemos validar los mismos y mostrarle una pantalla con los errores al usuario a fin de que pueda corregirlos.

Existen dos técnicas comunmente utilizadas para realizar esto con play! framework.

Este proyecto tiene por objetivo discutir estas dos técnicas, mostrando las ventajas y desventajas de las mismas.

## 1. Volver a hacer el render del template del formulario SIN REDIRECCIONAR

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

Implementamos una acción que simplemente nos retornará un nuevo usuario y nos direccionará al template que nos mostrará el formulario para ingresar la información del nuevo usuario.

```
public class Rendered extends Controller {

    public static void add() {
    	final User user = new User();
    	render(user);
    }

```

Y el formulario:

views/Rendered/add.html

```

#{extends 'main.html' /}
#{set title:'Home' /}

<h1>crear un nuevo usuario</h1>
(SIN redirect after post)
<hr />

#{ifErrors}
    <p>se han encontrado errores</p>
    <ul>
    #{errors}
        <li>${error}</li>
    #{/errors}
    </ul>
#{/ifErrors}

<form method="POST" action="@{save()}">
    <label for="user.name">nombre</label>
    <input type="text" name="user.name" value="${user.name}"><br />

    <label for="user.surname">apellido</label>
    <input type="text" name="user.surname" value="${user.surname}"><br />
    
    <input type="submit" value="grabar">
    o
    #{a @Application.list()}cancelar#{/a}
</form>
```

> Comunmente utilizaríamos el tag ```#{form @save()}``` que directamente se encarga de generar la action url y el método tal como está definido en el archivo routes, pero en este caso preferimos hacerlo a mano para evitar el authentity token, a fin de poder ver bien qué información va posteada en cada request.

La acción Rendered.save nos queda entonces de la siguiente manera:

controllers.Rendered.save

```
    public static void save(@Valid User user) {
    	if (validation.hasErrors()) {
    		render("@add", user);      // si hubo error, hacemos un render de add.html SIN REDIRECT AFTER POST
    	}
    	user.save();                   // grabamos el usuario
        Application.list();            // si todo fue grabado bien, hacemos un REDIRECT AFTER POST
    }
```

> [Redirect after post](http://en.wikipedia.org/wiki/Post/Redirect/Get) es una técnica para evitar posteos duplicados así como el famoso problema del F5 en los exploradores. Al hacer inmediatamente un redirect, con un método GET, luego de cada post, en caso de refrescar la página lo que se repetirá es el request con el GET, que no debería modificar ningún dato.

Es decir, si hay algún error, simplemente hacemos un render de _views/Rendered/add.html, pasándole como parámetro el Usuario que no cumplió con alguna de las validaciones.

Si por el contrario el usuario es grabado correctamente, entonces sí hacemos un redirect after post.


## 2. Guardar los errores en la variable flash y hacer un redirect after post

El segundo enfoque consiste en guardar la información cargada, así como los mensajes de error, y pedir al explorador que haga un redirect, a fin de mostrarle los errores para que los corrija. Recordemos que en play la varaible flash es almacenada en cookies del lado del cliente. Es decir, recibe el mismo tratamiento que la variable session, con la diferencia que lo que guardemos en el flash sólamente durará durante un request. Comúnmente esta variable es utilizada para mostrar mensajes de error o notificaciones acerca de las acciones recién efectuadas por el usuario.

Con este enfoque nuestro controlador quedaría así:

controllers.Redirected

```
public class Redirected extends Controller {

    public static void add() {
    	render();
    }
    
    public static void save(@Valid User user) {
    	if (validation.hasErrors()) {
    		params.flash();			//guardo en el flash los parámetros posteados
    		validation.keep();		//guardo los errores para el próximo request, los guarda en cookies
    		add();                  // hago un redirect after post a la acción add
    	}
    	user.save();
        Application.list();
    }
    
}
```

Y en nuestro template:

views/Redirected/add.html

```
#{extends 'main.html' /}
#{set title:'Home' /}

<h1>crear un nuevo usuario</h1>
(CON redirect after post)
<hr />

#{ifErrors}
    <p>se han encontrado errores</p>
    <ul>
    #{errors}
        <li>${error}</li>
    #{/errors}
    </ul>
#{/ifErrors}

<form method="POST" action="@{save()}">
    <label for="user.name">nombre</label>
    <input type="text" name="user.name" value="${flash['user.name']}"><br />

    <label for="user.surname">apellido</label>
    <input type="text" name="user.surname" value="${flash['user.surname']}"><br />
    
    <input type="submit" value="grabar">
    o
    #{a @Application.list()}cancelar#{/a}

</form>
```

> Note como en el value de cada control ya no accedemos directamente al objeto user, sino que accedemos al valor almacenado en el flash, de la siguiente manera ```value="${flash['user.name']}"```.

