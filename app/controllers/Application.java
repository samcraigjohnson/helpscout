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
import dao.*;

import views.html.*;
import play.Logger;

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
    @BodyParser.Of(BodyParser.Json.class)
    public static Result addCustomer(){
	JsonNode json = request().body().asJson();
	
	String fn = json.findPath("firstName").textValue();
	String ln = json.findPath("lastName").textValue();
	String email = json.findPath("email").textValue();
	String phoneNumber = json.findPath("phoneNumber").textValue();
	String userName = json.findPath("username").textValue();
	String platform = json.findPath("platform").textValue();
	String position = json.findPath("position").textValue();
	String company = json.findPath("company").textValue();
	
	if(validateString(fn) && validateString(ln)){
	    Customer c = new Customer(fn, ln);
	    c.save();
	}
	else{
	    return status(400, "Bad Request");
	}

	if(validateEmail(email)){
	    CustomerEmail ce = new CustomerEmail(c, email);
	    ce.save();
	}
      
	if(validate(position) && validate(company)){
	    CustomerJob cj = new CustomerJob(c, position, company);
	    cj.save();
	}
	
	if(validate(username) && validate(platform)){
	    CustomerUsername cu = new CustomerUsername(c, username, platform);
	    cu.save();
	}

	return ok(Json.toJson(c));
    }

    /**
       API call used to return a list of all the customers given
       a User's username
     */
    public static Result getAllCustomers(){
	return ok(JsonDao.getCustomerJson());
    }

    private boolean validateString(String s){
	if(s == null){
	    return false;
	}
	return true;
    }

    private boolean validateEmail(String s){
	return validateString(s);
	    //todo
    }

}
