document.getElementById("login-form").addEventListener("submit", function (event) {
	event.preventDefault();
	
	var username = document.getElementById("login-username").value;
	var password = document.getElementById("login-password").value;

	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", "LoginServlet", true);
	xhttp.setRequestHeader("Content-Type", "application/json");

	var request = JSON.stringify({
		username: username,
		password: password
	});

	xhttp.onload = function () {
		if (xhttp.status === 200 && xhttp.readyState === 4) {
			
			var response = JSON.parse(xhttp.responseText);

			alert("Welcome, " + response.username + " " + response.groupID + " " + response.userID);
			// Get the user_id, stored for the current session,
			// and let the program know there is a logged in user
			sessionStorage.setItem("userID", response.userID);
			sessionStorage.setItem("groupID", response.groupID);
			sessionStorage.setItem("username", response.username);
			sessionStorage.setItem("userLoggedIn", true);

			// Send the user to set up a ride request
			window.location.href = "form.html";
		}
		
		// Some fail cases, all parameters not filled or wrong info
		else if (xhttp.status == 400){
            alert("Invalid Log In. Make sure all parameters are filled");
        } else {
			alert("Invalid Log In. Incorrect Password or Username.");
		}
	};

	xhttp.send(request);
});