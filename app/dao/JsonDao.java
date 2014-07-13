package dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import models.*;

import java.util.List;

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
	    JSONObject jo = new JSONObject();
	    jo.put("firstName", c.firstName);
	    jo.put("lastName", c.lastName);
	    jo.put("emails", getEmails(c));
	    jo.put("numbers", getNumbers(c));
	    jo.put("usernames", getUsernames(c));
	    jo.put("jobs", getJobs(c));
	    jo.put("id", c.id);
	    cArray.add(jo);
	}
	
	return cArray.toJSONString();
    }
    
    /**
       This method returns customer profiles that are considered
       to be duplicates. The duplication is judged on similarities
       in email, phone number, name, company role, usernames for
       social media sites, etc.
     */
    public static String getSimilarCustomers(){
	String query = "select..."//todo select statement, groupby
	List<CustomerEmail> simEmails = CustomerEmail.find.where()
	    .eq("");
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

    /**
       Helper method used to return JSONArray of social media
       usernames given a specific customer
     */
    private static JSONArray getUsernames(Customer c){
	List<CustomerUsername> uNames = CustomerUsername.find.where()
		.eq("customer_id", c.id).findList();

	JSONArray uNameArray = new JSONArray();
        for(CustomerUsername uName: uNames){
	    JSONObject jo = new JSONObject();
	    jo.put("platform", uName.platform);
	    jo.put("username", uName.username);
	    uNameArray.add(jo);
	}

	return uNameArray;
    }

    /**
       Helper method used to return JSONArray of jobs
       given a specific customer
     */
    private static JSONArray getJobs(Customer c){
	List<CustomerJob> jobs = CustomerJob.find.where()
		.eq("customer_id", c.id).findList();

	JSONArray jobsArray = new JSONArray();
        for(CustomerJob cj: jobs){
	    JSONObject jo = new JSONObject();
	    jo.put("company", cj.company);
	    jo.put("position", cj.position);
	    jobsArray.add(jo);
	}

	return jobsArray;
    }


}