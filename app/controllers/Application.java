package controllers;

import play.*;
import play.mvc.*;

//JSON imports
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

//DB
import play.db.ebean.*;
import models.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Hello World"));
    }

    /**
       This method is used to capture the addition
       of a customer to a user's customers.
       Returns json object with success or failure.
       If success returns customer info to add to customer
       list.
     */
    @Transactional
    public static Result addCustomer(){
	//TODO add information to DB and return info if success
	JsonNode json = request().body().asJson();
	Customer c = new Customer();
	c.setFirstName("sam");
	c.setLastName("johnson");
	c.setEmail("sjohnson540@gmail.com");
	Ebean.save(c);
	return ok("SUCCESSFUL JSON CALL");
    }

}
