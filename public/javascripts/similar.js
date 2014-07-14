$(function(){
    //perform the add user ajax call when form submitted
    $("#add-customer-button").click(function(event){
	event.preventDefault();
	$formVals = $("#add-user input");
	var custObj = createObjectFromForm($formVals);
	$.ajax({
	    type: 'POST',
	    url:"/customers/add", 
	    dataType: 'json',
	    contentType: 'text/json',
	    data:JSON.stringify(custObj), 
	    success: function(data){
		console.log(data);
		readCustomers();
	    }
	});
	clearFormVals($formVals);
    });
    readCustomers();
});

function addMergeListener(){
    $('.merge-items').click(function(event){
	event.preventDefault();
	var id = $(this).siblings('p').first().attr('id');
	var data = { customer_id : 1,
		     updates: [
			 { action: "change",
			   field: "email",
			   oldValue: "sam@sam.com",
			   value: "sjohnson5410@gmail.com"}
		          ]
		   };
	var stringData = JSON.stringify(data);
	console.log(stringData);
	$.ajax({
	    type: "POST",
	    url: "/customers/update",
	    dataType: "text",
	    contentType: "text/json",
	    data: stringData,
	    success: function(data){
		console.log("added email");
		console.log(data);
		readCustomers();
	    }
	});
    });
}

function addDeleteListener(){
    $('.delete-user').click(function(event){
	event.preventDefault();
	var id = $(this).closest('tr').attr('id');
	var data = { customer_id : parseFloat(id) };
	$.ajax({
	    type: "POST",
	    url: "customers/remove",
	    dataType: "text",
	    contentType: "text/json",
	    data: JSON.stringify(data),
	    success: function(data){
		console.log(data);
		readCustomers();
	    }
	});
    });
}

function readCustomers(){
    $.ajax({
	type: "GET",
	url: "/customers",
	dataType: "json",
	success: function(data){
	    console.log(data);
	    $custTable = $("#customer-body");
	    $custTable.html('');
	    var text = "";
	    for(var i=0; i < data.length; i++){
		text+="<tr id="+data[i].id+">";
		text+="<td>"+data[i].firstName+"</td>";
		text+="<td>"+data[i].lastName+"</td>";
		text+="<td>"+data[i].emails[0]+"</td>";
		text+="<td>"+data[i].numbers[0]+"</td>";
		text+="<td><a class='button delete-user alert' href='#'>X</a></td>";
		text+="</tr>";
	    }
	    $custTable.append(text);
	    addDeleteListener();
	}
    });

    $.ajax({
	type: "GET",
	url: "/customers/similar",
	dataType: "json",
	success: function(data){
	    console.log("Similar data:");
	    console.log(data);
	    $("#duplicate-customers").html('');
	    var text = "";
	    for(var i=0; i < data.length; i++){
		text+="<li>";
		for(var j=0; j < data[i].length; j++){
		    text+="<span id="+data[i][j].id+"> "+data[i][j].firstName+"</span>, ";
		}
		text+="<a href='#' class='button small merge-items'>MERGE</a></li>";
	    }
	    $("#duplicate-customers").append(text);
	    addMergeListener();
	}
    });
}

/*
Helper function to turn all form input items into
a js object
*/
function createObjectFromForm(formObj){
    var obj = {}
    formObj.each(function(indx){
	obj[$(this).attr("name")] = $(this).val();
    });
    return obj; 
}

/*
Helper function to clear form vals
*/
function clearFormVals(formObj){
    formObj.each(function(indx){
	$(this).val('');
    });
}