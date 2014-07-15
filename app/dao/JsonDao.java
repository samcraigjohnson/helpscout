package dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import exceptions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.NullPointerException;

import play.Logger;

/**
   This class is used to serve the JSON by accessing
   the DB for specific API requests
 */
public class JsonDao{

    /**
       This method returns all of the customers, and aggregates
       their data fields into a valid JSON string
     */
    public static String getCustomerJson(){
	JSONArray cArray = new JSONArray();
	for(Customer c: Customer.find.all()){
	    cArray.add(getCustomerObject(c));
	}
	
	return cArray.toJSONString();
    }
    
    /**
       This method is used to add a new customer into the
       database
     */
    public static String addCustomer(String fn, String ln, List<String> emails, List<String> phoneNumbers){
	Customer c = new Customer(fn, ln);
	c.save();
	for(String email : emails){
	    CustomerEmail ce = new CustomerEmail(c, email);
	    ce.save();
	    c.emails.add(ce);
	}
	for(String num : phoneNumbers){
	    CustomerPhone cp = new CustomerPhone(c, num);
	    cp.save();
	    c.phoneNumbers.add(cp);
	}
	return getCustomerObject(c).toJSONString();
    }
    
    /**
       This method is used to return a success message
     */
    public static String successJson(String message){
	JSONObject jo = new JSONObject();
	jo.put("status", "success");
	jo.put("message", message);
	return jo.toJSONString();
    }
    
    /**
       This method returns customer profiles that are considered
       to be duplicates. The duplication is judged on similarities
       in email, phone number, name, company role, usernames for
       social media sites, etc.
     */
    public static String getSimilarCustomers(){
	JSONArray dupArray = new JSONArray();
	ArrayList<Long> dupIds = new ArrayList<Long>();
	List<Customer> customers = Customer.find.all();
	for(int i=0; i<customers.size(); i++){
	    JSONArray customerArray = new JSONArray();
	    for(int j=i+1; j<customers.size(); j++){
		if(checkIfDuplicate(customers.get(i), customers.get(j))){
		    addDuplicate(customers.get(j), customerArray);
		}
	    }    
	    //Check to see if any duplicates have been added
	    if(customerArray.size() > 0){
		addDuplicate(customers.get(i), customerArray);
		dupArray.add(customerArray);
	    }
	}
	return dupArray.toJSONString();
    }
    
    /**
       This method is used to compare two customers to see
       if they could be potential duplicates
     */
    public static boolean checkIfDuplicate(Customer c1, Customer c2){
	/*
	  Check if the username portion of the 
	  email matches, also check if the first customer's
	  name is present in the second customer's email,
	  and vice versa
	*/
	for(CustomerEmail ce: c1.emails){
	    for(CustomerEmail ce2: c2.emails){
		String username = ce.email.split("@")[0].toLowerCase();
		String username2 = ce2.email.split("@")[0].toLowerCase();
		if(username.equalsIgnoreCase(username2)){
		    return true;
		}

		if(username.contains(c2.lastName.toLowerCase()) && username.contains(c2.firstName.toLowerCase())){
		    return true;
		}

		if(username2.contains(c1.firstName.toLowerCase()) && username2.contains(c1.lastName.toLowerCase())){
		    return true;
		}
	    }
	}
	
	/*
	  Check if there are any phone number matches,
	  first 9 digits consitues a match
	 */
	for(CustomerPhone cp: c1.phoneNumbers){
	    for(CustomerPhone cp2: c2.phoneNumbers){
		String num1 = cp.phoneNumber.substring(0, cp.phoneNumber.length()-1);
		String num2 = cp2.phoneNumber.substring(0, cp2.phoneNumber.length()-1);
		
		if(num1.equals(num2)){
		    return true;
		}
	    }
	}

	return false;
    }

    /**
       Method used to remove a specific customer from the database
    */
    public static String removeCustomer(Long id) throws InvalidJsonException{
	Customer c = Customer.find.byId(id);
	if(c != null){
	    c.delete();
	    return successJson("Successfully deleted customer");
	}
	else{
	    throw new InvalidJsonException("Customer does not exist");
	}
    }
    /**
       Method used to update a customer's data
     */
    public static String updateCustomer(JsonNode json) throws InvalidJsonException{
	Long id = json.findValue("customer_id").longValue();
	Customer c = Customer.find.byId(id);
	if(c != null){
	    Iterator<JsonNode> iterator = json.findValue("updates").iterator();
	    while(iterator.hasNext()){
		JsonNode update = iterator.next();
		String action = update.findValue("action").textValue();
		String field = update.findValue("field").textValue();
		String value = update.findValue("value").textValue();
		if(action.equals("add")){
		    addInfo(c, field, value);
		}
		else if(action.equals("delete")){
		    deleteInfo(c, field, value);
		}
		else if(action.equals("change")){
		    String oldValue = update.findValue("oldValue").textValue();
		    changeInfo(c, field, value, oldValue);
		}
		else{
		    throw new InvalidJsonException("Action is not valid");
		}
	    }
	}
	else{
	    throw new InvalidJsonException("Customer does not exist");
	}
	return successJson("Successfully updated customer");
    }

    /**
       Helped method used to add a piece of info
     */
    public static void addInfo(Customer c, String field, String value) throws InvalidJsonException{
	if(field.equals("email")){
	    boolean dup = false;
	    for(CustomerEmail ce : c.emails){
		if(ce.email.equalsIgnoreCase(value)){
		    dup = true;
		}
	    }
	    if(!dup){
		CustomerEmail ce = new CustomerEmail(c,value);
		ce.save();
		c.emails.add(ce);
	    }
	}
	else if(field.equals("phoneNumber")){
	    boolean dup = false;
	    for(CustomerPhone cp : c.phoneNumbers){
		if(cp.phoneNumber.equalsIgnoreCase(value)){
		    dup = true;
		}
	    }
	    if(!dup){
		CustomerPhone cp = new CustomerPhone(c, value);
		cp.save();
		c.phoneNumbers.add(cp);
	    }
	}
	else{
	    throw new InvalidJsonException("Field is not valid");
	}
    }
    /**
       Helper method to remove a piece of info
     */
    public static void deleteInfo(Customer c, String field, String value) throws InvalidJsonException{
	boolean found = false;
	if(field.equals("email")){
	    CustomerEmail toRemove = null;
	    for(CustomerEmail ce: c.emails){
		if(ce.email.equals(value)){
		    toRemove = ce;
		    found = true;
		}
	    }
	    if(toRemove != null){
		toRemove.delete();
		c.emails.remove(toRemove);
	    }
	}
	else if(field.equals("phoneNumber")){
	    CustomerPhone toRemove = null;
	    for(CustomerPhone cp: c.phoneNumbers){
		if(cp.phoneNumber.equals(value)){
		    toRemove = cp;
		    found = true;
		}
	    }
	    if(toRemove != null){
		toRemove.delete();
		c.phoneNumbers.remove(toRemove);
	    }
	}

	if(!found){
	    throw new InvalidJsonException("Value does not exist for given customer");
	}
    }
    /**
       Helper method to change a piece of info
     */
    public static void changeInfo(Customer c, String field, String value, String oldValue) throws InvalidJsonException{
	boolean found = false;
	if(field.equals("email")){
	    for(CustomerEmail ce: c.emails){
		if(ce.email.equals(oldValue)){
		    ce.email = value;
		    ce.save();
		    found = true;
		}
	    }
	}
	else if(field.equals("phoneNumber")){
	    for(CustomerPhone cp: c.phoneNumbers){
		if(cp.phoneNumber.equals(oldValue)){
		    cp.phoneNumber = value;
		    cp.save();
		    found = true;
		}
	    }
	}
	if(!found){
	    throw new InvalidJsonException("Value does not exist for given customer");
	}
    }

    /**
       Helper method to check duplication and return JSONObject
     */
    private static void addDuplicate(Customer c, JSONArray duplicates){
	duplicates.add(getCustomerObject(c));
    }

    /**
       Helper method used to create one single customer object
       to be returned
     */
    public static JSONObject getCustomerObject(Customer c){
	JSONObject jo = new JSONObject();
	jo.put("firstName", c.firstName);
	jo.put("lastName", c.lastName);
	jo.put("email", getEmails(c));
	jo.put("phoneNumber", getNumbers(c));
	jo.put("id", c.id);
	return jo;
    }

    /**
       Helper method used to return JSONArray of emails
       given a specific customer
     */
    private static JSONArray getEmails(Customer c){
	List<CustomerEmail> emails = CustomerEmail.find.where()
		.eq("customer_id", c.id).findList();

	JSONArray emailArray = new JSONArray();
        for(CustomerEmail ce: emails){
	    emailArray.add(ce.email);
	}

	return emailArray;
    }

    /**
       Helper method used to return JSONArray of phone numbers
       given a specific customer
     */
    private static JSONArray getNumbers(Customer c){
	List<CustomerPhone> numbers = CustomerPhone.find.where()
		.eq("customer_id", c.id).findList();

	JSONArray numberArray = new JSONArray();
        for(CustomerPhone cp: numbers){
	    numberArray.add(cp.phoneNumber);
	}

	return numberArray;
    }
    
}