package controllers;

import controllers.*;

import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;
import play.db.ebean.*;
import java.util.*;

public class ValidationTest extends WithApplication {
 
    @Test
    @Transactional
    public void testValidations(){
	running(fakeApplication(inMemoryDatabase()), new Runnable(){
		public void run(){
		    assertTrue(Application.validatePhoneNumber("3334445555"));
		    assertFalse(Application.validatePhoneNumber("13222"));
		    assertFalse(Application.validatePhoneNumber("333444555a"));
		    
		    assertTrue(Application.validateEmail("test2@gmail.com"));
		    assertTrue(Application.validateEmail("test.loco76@fakeurl.co"));
		    assertFalse(Application.validateEmail("douglas"));
		    assertFalse(Application.validateEmail("doglvr@hotmail"));
		}
	});
    }

}