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
	String username = json.findPath("username").textValue();
	String platform = json.findPath("platform").textValue();
	String position = json.findPath("position").textValue();
	String company = json.findPath("company").textValue();
	 
	try{
	    if(validateString(fn) && validateString(ln)){
	     Customer c = new Customer(fn, ln);
	     c.save();

	     if(validateEmail(email)){
		 CustomerEmail ce = new CustomerEmail(c, email);
		 ce.save();
	     }
	     if(validateString(position) && validateString(company)){
		 CustomerJob cj = new CustomerJob(c, position, company);
		 cj.save();
	     }
	     if(validateString(username) && validateString(platform)){
		 CustomerUsername cu = new CustomerUsername(c, username, platform);
		 cu.save();
	     }
	     if(validatePhoneNumber(phoneNumber)){
		 CustomerPhone cp = new CustomerPhone(c, phoneNumber);
		 cp.save();
	     }

	     return ok(Json.toJson(c));
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
    public static Result removeCustomer(){
	JsonNode json = request().body().asJson();
	Long id = json.findValue("c_id").longValue();
	Logger.debug("CustomerID:"+id);
	Customer c = Customer.find.byId(id);
	c.delete();
	return ok("successfully deleted customer");
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
