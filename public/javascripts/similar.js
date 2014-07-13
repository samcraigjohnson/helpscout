$(function(){
    //perform the add user ajax call when form submitted
    $("#add-customer-button").click(function(event){
	event.preventDefault();
	$formVals = $("#add-user input");
	var custObj = createObjectFromForm($formVals);
	$.post("/customers/add", custObj, function(data){
	    console.log(data);
	});
	clearFormVals($formVals);
    });
});

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