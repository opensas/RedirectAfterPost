package controllers;

import java.util.List;

import models.User;
import play.mvc.*;
import play.test.Fixtures;

public class Application extends Controller {

    public static void list() {
    	final List<User> users = User.findAll();
        render(users);
    }
    
    public static void clear() {
    	Fixtures.deleteAllModels();
    	list();
    }
    
}
