console.log("loaded");
$(function(){
    console.log("binding click event");
    //perform the add user ajax call when form submitted
    $("#add-customer-button").click(function(event){
	//event.preventDefault();
	console.log("Submited User Data");
	//console.log($().serialize());
    });
});