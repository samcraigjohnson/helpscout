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
	running(fakeApplication(inMemoryDatabase("test")), new Runnable(){
		public void run(){
		    new Customer("test2", "name", "wh2at3ever@gmail.com").save();
		    Customer t = Customer.find.where().eq("lastName", "name").findUnique();
		    assertNotNull(t);
		    assertEquals("test2", t.firstName);
		}
	});
    }
}