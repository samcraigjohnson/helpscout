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

function readCustomers(){
    $.ajax({
	type: "GET",
	url: "/customers",
	dataType: "json",
	success: function(data){
	    console.log(data);
	    $custTable = $("#customer-body");
	    $custTable.html('');
	    for(var i=0; i < data.length; i++){
		$custTable.append("<tr id="+data[i].id+">");
		$custTable.append("<td>"+data[i].firstName+"</td>");
		$custTable.append("<td>"+data[i].lastName+"</td>");
		$custTable.append("<td>"+data[i].emails[0]+"</td>");
		$custTable.append("</tr>");
	    }
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