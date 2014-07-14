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
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

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

	String fn = json.findValue("firstName").textValue();
	String ln = json.findValue("lastName").textValue();
	JsonNode email = json.findValue("email");
	JsonNode phoneNumber = json.findValue("phoneNumber");
		 
	try{
	    if(validateString(fn) && validateString(ln)){
		Iterator<JsonNode> emails = email.iterator();
		List<String> emailStrings = new ArrayList<String>();
		while(emails.hasNext()){
		    String e = emails.next().textValue();
		    if(validateEmail(e)){
			emailStrings.add(e);
		    }
		}
		Iterator<JsonNode> phoneNumbers = phoneNumber.iterator();
		List<String> phoneStrings = new ArrayList<String>();
		while(phoneNumbers.hasNext()){
		    String num = phoneNumbers.next().textValue();
		    if(validatePhoneNumber(num)){
			phoneStrings.add(num);
		    }
		}

		return ok(JsonDao.addCustomer(fn,ln,emailStrings,phoneStrings));
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
	if(validateId(id)){
	    return ok(JsonDao.removeCustomer(id));
	}
	//TODO return success json and id of deleted customer
	return status(400, "Bad Request");
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
    
    private static boolean validateId(Long id){
	if(id != null){
	    return true;
	}
	return false;
    }

}
