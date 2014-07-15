var merge_customers = {};

$(function(){
    //perform the add user ajax call when form submitted
    $("#add-customer-button").click(function(event){
	event.preventDefault();
	$formVals = $("#add-user input");
	var custObj = createObjectFromForm($formVals);
	custObj.email = [custObj.email];
	custObj.phoneNumber = [custObj.phoneNumber];
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

function createMergeObject(merge_data){
    var primary_cust = merge_data[0];
    var updates = [];
    var toDelete = [];
    console.log("merge_data length: " +merge_data.length);
    for(var i=1; i < merge_data.length; i++){
	toDelete.push(merge_data[i].id);
	if(merge_data[i].hasOwnProperty('email')){
	    for(var e=0; e<merge_data[i].email.length;e++){
		var update_obj = { 
		    action : "add", 
		    field: "email",
		    value: merge_data[i].email[e]
		};
		updates.push(update_obj);
	    }
	}
	if(merge_data[i].hasOwnProperty('phoneNumber')){
	    for(var p=0; p<merge_data[i].phoneNumber.length;p++){
		var update_obj = { 
		    action : "add", 
		    field: "phoneNumber",
		    value: merge_data[i].phoneNumber[p]
		};
		updates.push(update_obj);
	    }
	}
    }
    var update_obj = { customer_id: primary_cust.id, updates: updates };
    var to_return = { update_data: update_obj, delete_data: toDelete };
    return to_return;
}

function addMergeListener(){
    $('.merge-items').click(function(event){
	event.preventDefault();
	var id = $(this).closest('div').attr('id');
	var merge_data = merge_customers[id];
	var data = createMergeObject(merge_data);
	var updateStringData = JSON.stringify(data.update_data);
	console.log(updateStringData);
	$.ajax({
	    type: "POST",
	    url: "/customers/update",
	    dataType: "text",
	    contentType: "text/json",
	    data: updateStringData,
	    success: function(data){
		console.log("successful update");
		readCustomers();
	    }
	});

	console.log("delete length : " + data.delete_data.length);
	for(var j=0; j<data.delete_data.length; j++){
	    var delete_json = {customer_id: data.delete_data[j]};
	    console.log("delete: " + JSON.stringify(delete_json));
	    $.ajax({
		type: "POST",
		url: "customers/remove",
		dataType: "text",
		contentType: "text/json",
		data: JSON.stringify(delete_json),
		success: function(data){
		    console.log(data);
		    readCustomers();
		}
	    });
	}
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
		text+=createCustomerRow(data[i]);
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
	    merge_customers = {};
	    $("#duplicates-tables").html('');
	    var text = "";
	    for(var i=0; i < data.length; i++){
		merge_customers["table-"+i] = data[i];
		text+="<div id='table-"+i+"'>";
		text+="<table>";
		for(var j=0; j < data[i].length; j++){
		    text+=createCustomerRow(data[i][j]);
		    text+="</tr>";
		}
		text+="</table>";
		text+="<a href='#' class='button small merge-items'>MERGE</a>";
		text+="</div>";
	    }
	    $("#duplicates-tables").append(text);
	    addMergeListener();
	}
    });
}

function createCustomerRow(data){
    var text = "";
    text+="<tr id="+data.id+">";
    text+="<td>"+data.firstName+"</td>";
    text+="<td>"+data.lastName+"</td>";
    text+="<td>";
    for(var i =0; i<data.email.length; i++){
	text+=data.email[i]+"<br>";
    }
    text+="</td>";
    text+="<td>";
    for(var j=0; j<data.phoneNumber.length;j++){
	text+=data.phoneNumber[j]+"<br>";
    }
    text+="</td>";
    return text;
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