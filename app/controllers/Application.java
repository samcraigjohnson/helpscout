package controllers;

import play.*;
import play.mvc.*;
import exceptions.*;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Transactional
    public static Result getAllCustomers(){
	try{
	    return ok(JsonDao.getCustomerJson());
	}
	catch(Exception e){
	    return internalServerError("Error: " + e.getMessage());
	}
    }

    /**
       API call used to delete a customer profile given a user id
     */
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result removeCustomer(){
	JsonNode json = request().body().asJson();
	Long id = json.findValue("customer_id").longValue();
	try{
	    if(validateId(id)){
		return ok(JsonDao.removeCustomer(id));
	    }
	    else{
		return status(400, "Invalid Id");
	    }
	}
	catch(InvalidJsonException e){
	    return status(410, e.getMessage());
	}
	catch(Exception e){
	    return internalServerError("Error: " + e.getMessage());
	}
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
	try{
	    return ok(JsonDao.updateCustomer(json));
	}
	catch(InvalidJsonException e){
	    return status(410, e.getMessage());
	}
	catch(Exception e){
	    return internalServerError("Error: " + e.getMessage());
	}
    }

    /**
       API call used to get similar profiles
     */
    @Transactional
    public static Result getSimilarCustomers(){
	try{
	    return ok(JsonDao.getSimilarCustomers());
	}
	catch(Exception e){
	    return internalServerError("Error: " + e.getMessage());
	}
    }

    private static boolean validateString(String s){
	if(s == null || s.equals("")){
	    return false;
	}
	return true;
    }

    /**
       A valid email address takes the form (foo@bar.com)
     */
    public static boolean validateEmail(String s){
	Pattern p = Pattern.compile(".+@[a-zA-z0-9]+[.][a-zA-Z]{0,3}");
	Matcher m = p.matcher(s);
	return m.matches();
    }

    /**
       A valid phone number has 10 digits and nothing else
     */
    public static boolean validatePhoneNumber(String pNum){
	Pattern p = Pattern.compile("[0-9]{10}");
	Matcher m = p.matcher(pNum);
	return m.matches();
    }
    
    public static boolean validateId(Long id){
	if(id != null){
	    return true;
	}
	return false;
    }

}
