package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class CustomerEmail extends Model{
    
    @Id
    public Long id;

    @Constraints.Required
    @ManyToOne
    public Customer customer;

    @Constraints.Required
    public String email;

    public CustomerEmail(Customer c, String emailAddress){
	customer = c;
	email = emailAddress;
    }

    public static Finder<Long, CustomerEmail> find =
	new Finder<Long, CustomerEmail>(Long.class, CustomerEmail.class);
}