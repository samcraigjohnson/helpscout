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
    addDeleteListener();
    readCustomers();
});

function addDeleteListener(){
    $('.delete-user').click(function(event){
	console.log("clicked");
	event.preventDefault();
	var id = $(this).closest('tr').attr('id');
	console.log(id);
	var data = { c_id : parseFloat(id) };
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
		text+="<td>"+data[i].usernames[0].username+":"+data[i].usernames[0].platform+"</td>";
		text+="<td>"+data[i].jobs[0].position+":"+data[i].jobs[0].company+"</td>";
		text+="<td><a class='button delete-user alert' href='#'>X</a></td>";
		text+="</tr>";
	    }
	    $custTable.append(text);
	    addDeleteListener();
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