package exceptions;

import java.lang.Exception;


public class InvalidJsonException extends Exception{
    public InvalidJsonException(String message){
	super(message);
    }

}