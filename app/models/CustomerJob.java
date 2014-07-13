package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class CustomerJob extends Model{
    
    @Id
    public Long id;

    @Constraints.Required
    @ManyToOne
    public Customer customer;

    @Constraints.Required
    public String position;
    public String company;

    public CustomerJob(Customer c, String position, String company){
	customer = c;
	this.position = position;
	this.company = company;
    }

    public static Finder<Long, CustomerJob> find =
	new Finder<Long, CustomerJob>(Long.class, CustomerJob.class);
}