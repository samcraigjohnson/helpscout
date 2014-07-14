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
import java.lang.Exception;

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
		 
	try{
	    if(validateString(fn) && validateString(ln)){
	     Customer c = new Customer(fn, ln);
	     c.save();

	     if(validateEmail(email)){
		 CustomerEmail ce = new CustomerEmail(c, email);
		 ce.save();
	     }
	     if(validatePhoneNumber(phoneNumber)){
		 CustomerPhone cp = new CustomerPhone(c, phoneNumber);
		 cp.save();
	     }

	     return ok(JsonDao.getCustomerObject(c).toJSONString());
	    }
	    else{
		return status(400, "Bad Request");
	    } 
	}
	catch(Exception e){
	    return internalServerError("Error: " + e.getMessage());
	}

    }

    /**
       API call used to return a list of all the customers given
       a User's username
     */
    public static Result getAllCustomers(){
	return ok(JsonDao.getCustomerJson());
    }

    /**
       API call used to delete a customer profile given a user id
     */
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result removeCustomer(){
	JsonNode json = request().body().asJson();
	Long id = json.findValue("customer_id").longValue();
	Customer c = Customer.find.byId(id);
	c.delete();
	//TODO return success json and id of deleted customer
	return ok("successfully deleted customer");
    }

    /**
       API call used to update a customer entry.
       Either by adding info, changing info, or removing
       info
     */
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result updateCustomer(){
	JsonNode json = request().body().asJson();
	return ok(JsonDao.updateCustomer(json));
    }

    /**
       API call used to get similar profiles
     */
    @Transactional
    public static Result getSimilarCustomers(){
	return ok(JsonDao.getSimilarCustomers());
    }

    private static boolean validateString(String s){
	if(s == null || s.equals("")){
	    return false;
	}
	return true;
    }

    private static boolean validateEmail(String s){
	return validateString(s);
	    //todo
    }

    private static boolean validatePhoneNumber(String pNum){
	return true;
	//if len != 10 or not all digits; return false;
    }

}
