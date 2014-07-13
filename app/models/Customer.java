package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;

@Entity
public class Customer extends Model{
    
    @Id
    public Long id;

    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    public Customer(String fName, String lName){
	firstName = fName;
	lastName = lName;
	//CustomerEmail newEmail = new CustomerEmail(this, emailAddress);
	//newEmail.save();
    }

    /*
    public List<String> getEmails(){
	List<String> emails = new ArrayList<String>();

	for(CustomerEmail ce: CustomerEmail.findEmailAddresses(id)){
	    emails.add(ce.email);
	}

	return emails;
	}*/

    public static Finder<Long, Customer> find = new Finder<Long, Customer>(Long.class, Customer.class);

}
