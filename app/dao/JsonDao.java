package dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;

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
	for(Customer c: Customer.find.all()){
	    if(!dupIds.contains(c.id)){
		JSONArray customerArray = new JSONArray();
		//check all of the customers email addresses
		for(CustomerEmail ce: c.emails){
		    //find other customer's emails that match email that arent the customers
		    List<CustomerEmail> dupEmails = CustomerEmail.find.where()
			.eq("email", ce.email).ne("customer_id",c.id).findList();
		    //Add all duplicates to an array
		    for(CustomerEmail dupEmail: dupEmails){
			addDuplicate(dupIds, dupEmail.customer, customerArray);
		    }
		
		}
		//Repeat the process with phone numbers
		for(CustomerPhone cp: c.phoneNumbers){
		    List<CustomerPhone> dupPhones = CustomerPhone.find.where()
			.eq("phoneNumber", cp.phoneNumber).ne("customer_id",c.id).findList();
		    for(CustomerPhone dupPhone: dupPhones){
			addDuplicate(dupIds, dupPhone.customer, customerArray);
		    }
		}
	    
		//Check to see if any duplicates have been added
		if(customerArray.size() > 0){
		    customerArray.add(getCustomerObject(c));
		    dupArray.add(customerArray);
		    dupIds.add(c.id);
		}
	    }
	}
	
	return dupArray.toJSONString();
    }

    /**
       Method used to update a customer's data
     */
    public static String updateCustomer(JsonNode json){
	Long id = json.findValue("customer_id").longValue();
	try{
	    Customer c = Customer.find.byId(id);
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
	    }
	}
	catch(NullPointerException e){
	    return "error json";
	}

	return successJson("Successfully updated customer");
    }

    /**
       Helped method used to add a piece of info
     */
    private static void addInfo(Customer c, String field, String value){
	if(field.equals("email")){
	    CustomerEmail ce = new CustomerEmail(c,value);
	    ce.save();
	}
	else if(field.equals("phoneNumber")){
	    CustomerPhone cp = new CustomerPhone(c, value);
	    cp.save();
	}
    }
    /**
       Helper method to remove a piece of info
     */
    private static void deleteInfo(Customer c, String field, String value){
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
	    }//else throw NoSuchItemException
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
	    }
	}

	if(!found){
	    //throw new NoSuchItemException
	}
    }
    /**
       Helper method to change a piece of info
     */
    private static void changeInfo(Customer c, String field, String value, String oldValue){
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
	    //throw NoSuchItemException
	}
    }

    /**
       Helper method to check duplication and return JSONObject
     */
    private static void addDuplicate(List<Long> added, Customer c, JSONArray duplicates){
	if(!added.contains(c.id)){
	    duplicates.add(getCustomerObject(c));
	    added.add(c.id);
	}
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