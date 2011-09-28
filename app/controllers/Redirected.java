package controllers;

import java.util.List;

import models.User;
import play.data.validation.Valid;
import play.mvc.Controller;

public class Redirected extends Controller {

    public static void add(User user) {
    	if (user==null) user = new User();
    	flash.put("user.name", user.name);
    	flash.put("user.surname", user.surname);
    	render();
    }
    
    public static void save(@Valid User user) {
    	if (validation.hasErrors()) {
    		params.flash();			// guardo en el flash los parámetros posteados
    		validation.keep();		// guardo los errores para el próximo request, los guarda en cookies
    		add(user);              // hago un redirect a la action form
    	}
    	user.save();
        Application.list();
    }
    
}