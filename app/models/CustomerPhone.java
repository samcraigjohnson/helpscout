package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class CustomerPhone extends Model{
    
    @Id
    public Long id;

    @Constraints.Required
    @ManyToOne
    public Customer customer;

    @Constraints.Required
    public String phoneNumber;

    public CustomerPhone(Customer c, String phone){
	customer = c;
	phoneNumber = phone;
    }

    public static Finder<Long, CustomerPhone> find =
	new Finder<Long, CustomerPhone>(Long.class, CustomerPhone.class);
}