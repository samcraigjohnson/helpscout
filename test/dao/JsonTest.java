package models;

import models.*;
import dao.JsonDao;

import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;
import play.db.ebean.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class JsonTest extends WithApplication {
 
    @Test
    @Transactional
    public void testAddCustomer(){
	running(fakeApplication(inMemoryDatabase()), new Runnable(){
		public void run(){
		    List<String> emails = new ArrayList<String>();
		    List<String> phoneNums = new ArrayList<String>();
		    emails.add("test@test.com");
		    emails.add("test2@test.com");
		    phoneNums.add("1234441232");
		    phoneNums.add("2223334444");
		    String json = JsonDao.addCustomer("fake", "name", emails, phoneNums);

		    Customer t = Customer.find.where().eq("firstName", "fake").findUnique();
		    assertNotNull(t);
		    assertEquals("name", t.lastName);
		    assertEquals(t.emails.size(), 2);
		    assertEquals(t.phoneNumbers.size(), 2);
		}
	});
    }
    @Test
    @Transactional
    public void testRemoveCustomer(){
	running(fakeApplication(inMemoryDatabase()), new Runnable(){
		public void run(){
		    List<String> emails = new ArrayList<String>();
		    List<String> phoneNums = new ArrayList<String>();
		    emails.add("test@test.com");
		    emails.add("test2@test.com");
		    phoneNums.add("1234441232");
		    phoneNums.add("2223334444");
		    String json = JsonDao.addCustomer("fake", "name", emails, phoneNums);
		    Customer t = Customer.find.where().eq("firstName", "fake").findUnique();
		    assertNotNull(t);
		    JsonDao.removeCustomer(t.id);
		    List<Customer> customers = Customer.find.all();
		    assertEquals(0, customers.size());
		}
	});
    }
    @Test
    @Transactional
    public void testSimilarCustomer(){
	running(fakeApplication(inMemoryDatabase()), new Runnable(){
		public void run(){
		    List<String> emails = new ArrayList<String>();
		    List<String> phoneNums = new ArrayList<String>();
		    emails.add("test@test.com");
		    emails.add("test2@test.com");
		    phoneNums.add("1234441232");
		    phoneNums.add("2223334444");
		    
		    JsonDao.addCustomer("fake", "name", emails, phoneNums);
		    Customer t = Customer.find.where().eq("firstName", "fake").findUnique();
		    JsonDao.addCustomer("fake2", "nam2", emails, phoneNums);
		    Customer t2 = Customer.find.where().eq("firstName", "fake2").findUnique();
		    
		    assertTrue(JsonDao.checkIfDuplicate(t,t2));

		    JsonDao.deleteInfo(t, "email", "test@test.com");
		    JsonDao.deleteInfo(t, "email", "test2@test.com");
		    
		    assertTrue(JsonDao.checkIfDuplicate(t, t2));
		    
		    JsonDao.addInfo(t, "email", "fake2nam2540@hotmail.com");
		    t.refresh();

		    assertTrue(JsonDao.checkIfDuplicate(t,t2));

		}
	});
    }

    @Test
    @Transactional
    public void testUpdateCustomer(){
	running(fakeApplication(inMemoryDatabase()), new Runnable(){
		public void run(){
		    List<String> emails = new ArrayList<String>();
		    List<String> phoneNums = new ArrayList<String>();
		    emails.add("test@test.com");
		    emails.add("test2@test.com");
		    phoneNums.add("1234441232");
		    phoneNums.add("2223334444");
		    String json = JsonDao.addCustomer("fake", "name", emails, phoneNums);	     
		    Customer t = Customer.find.where().eq("firstName", "fake").findUnique();
		    assertNotNull(t);
		    JsonDao.deleteInfo(t, "email", "test@test.com");
		    assertEquals(t.emails.size(), 1);
		    
		    JsonDao.deleteInfo(t, "phoneNumber", "2223334444");
		    assertEquals(t.phoneNumbers.size(), 1);
		    
		    JsonDao.addInfo(t, "email", "t@t.com");
		    assertEquals(t.emails.size(), 2);

		    CustomerEmail ce = CustomerEmail.find.where().eq("email", "t@t.com").findUnique();
		    JsonDao.changeInfo(t, "email", "test2@test.com", "t@t.com");
		    ce.refresh();
		    assertEquals(ce.email, "test2@test.com");
		}
	});
    }

}