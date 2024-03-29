package models;

import models.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;
import play.db.ebean.*;

public class ModelsTest extends WithApplication {
    
    @Test
    @Transactional
    public void testAddGetCustomer(){
	running(fakeApplication(inMemoryDatabase()), new Runnable(){
		public void run(){
		    new Customer("test3", "name").save();
		    Customer t = Customer.find.where().eq("lastName", "name").findUnique();
		    assertNotNull(t);
		    assertEquals("test3", t.firstName);

		    Customer c = new Customer("testEmail", "test");
		    c.save();
		    CustomerEmail ce = new CustomerEmail(c, "test@test.com");
		    ce.save();

		    assertEquals(ce.find.where().eq("customer", c).findUnique().email, "test@test.com");
		}
	});
    }
}