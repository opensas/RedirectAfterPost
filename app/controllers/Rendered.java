package controllers;

import java.util.List;

import models.User;
import play.data.validation.Valid;
import play.mvc.Controller;

public class Rendered extends Controller {

    public static void add() {
    	final User user = new User();
    	render(user);
    }
    
    public static void save(@Valid User user) {
    	if (validation.hasErrors()) {
    		render("@add", user);
    	}
    	user.save();
        Application.list();
    }
    
}