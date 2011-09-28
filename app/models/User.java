package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

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
