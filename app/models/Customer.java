package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Customer extends Model{
    
    @Id
    public Long id;

    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    @Constraints.Required
    public String email;

    public static Finder<Long, Customer> find = new Finder<Long, Customer>(Long.class, Customer.class);

}
