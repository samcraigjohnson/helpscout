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
        "numbers":["3105554646", "55554545"],
        "emails":["jdoe@gmail.com", "jdoe@hotmail.com"],
        "firstName":"John"
    }
```

***

    
