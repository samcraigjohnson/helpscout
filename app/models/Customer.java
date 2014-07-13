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

    @OneToMany(cascade=CascadeType.ALL)
    public List<CustomerEmail> emails;
    @OneToMany(cascade=CascadeType.ALL)
    public List<CustomerJob> jobs;
    @OneToMany(cascade=CascadeType.ALL)
    public List<CustomerUsername> usernames;
    @OneToMany(cascade=CascadeType.ALL)
    public List<CustomerPhone> phoneNumbers;

    public Customer(String fName, String lName){
	firstName = fName;
	lastName = lName;
    }

    public static Finder<Long, Customer> find = new Finder<Long, Customer>(Long.class, Customer.class);

}
