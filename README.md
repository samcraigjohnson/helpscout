Help Scout Engineer Test
=================================

API Documentation
-----------------
##Get all customers

`/customers`

Request Type: `GET`

###Example Response:

```javascript
    [{
      "id":1,
      "lastName":"Doe",
      "numbers":["31055554787"],
      "emails":["jdoe@gmail.com"],
      "firstName":"John"
    },
    {
      "id":2,
      "lastName":"Doe",
      "numbers":["45555554787"],
      "emails":["jane.doe@gmail.com"],
      "firstName":"Jane"
    }
    ]
```

***

##Add Customer

`/customers/add`

Request Type: `POST`

###Parameters:

 *firstName*    : `required` String
    
 *lastName*     : `required` String
    
 *email*        :  Array of String. Must be valid email addresses (text@text.com)

 *phoneNumbers* :  Array of String. Must be 10 digit phone numbers &amp; only digits


###Example Request:
```javascript
    { 
        "firstName":"John",
        "lastName":"Doe",
        "email":["jdoe@gmail.com", "jdoe@hotmail.com"],
        "phoneNumber":["3105554646", "55554545"]
    }
```

###Example Response:

```javascript
    { 
        "id":3,
        "lastName":"Doe",
        "phoneNumbers":["3105554646", "55554545"],
        "email":["jdoe@gmail.com", "jdoe@hotmail.com"],
        "firstName":"John"
    }
```

***

##Remove Customer

`/customers/remove`

Request Type: `POST`

###Parameters:

 *customer_id*    : `required` int
    
###Example Request:
```javascript
    { 
        "customer_id":1,
    }
```

###Example Response:

```javascript
    { 
        "message":"Successfully deleted customer",
        "status":"success"
    }
```

***

##Update Customer

`/customers/update`

Request Type: `POST`

###Parameters:

 *customer_id*    : `required` int
 
 *updates*  : `required` Array of update objects.
 
 ... + action : `required` String. Must be `change`,`add`, or `delete`.
 
 ... + field : `required` String. Field to commit action on. Must be `email` or `phoneNumber`.
 
 ... + oldValue : String. `required` if `action` is `change`. Value to look up so that field can be changed.
 
 ...+ value : `required` String. For `delete`, value is the item to be deleted. For `change`, it is the item the field is to be changed to.
     For `add` ,it is the value to be added.
    
###Example Request:
```javascript
    {
        "customer_id":1,
        "updates":[{
                        "action":"change",
                        "field":"email",
                        "oldValue":"jdoe@gmail.com",
                        "value":"john.doe@gmail.com" 
                    },
                    {
                        "action":"add",
                        "field":"phoneNumber",
                        "value":"4105556677" 
                    },
                    {
                        "action":"remove",
                        "field":"email",
                        "value":"jdoe@hotmail.com" 
                    }
                    ]
    }
```

###Example Response:

```javascript
    { 
        "message":"Successfully updated customer",
        "status":"success"
    }
```


    
