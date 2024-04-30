document.getElementById("signup-form").addEventListener("submit", function (event) {
	event.preventDefault();
	
	var username = document.getElementById("signup-username").value;
	var email = document.getElementById("signup-email").value;
	var password = document.getElementById("signup-password").value;
	var confirm_password = document.getElementById("signup-confirm-password").value;

	// confirm password did not match password
	if (password !== confirm_password) {
		alert("Passwords don't match");
	}
	
	else {
		var xhttp = new XMLHttpRequest();
		xhttp.open("POST", "SignUpServlet", true);
		xhttp.setRequestHeader("Content-Type", "application/json");

		var request = JSON.stringify({
			username: username,
			password: password,
			email: email,
		});

		// Make the call to the servlet
		xhttp.onload = function() {
			if (xhttp.status === 200 && xhttp.readyState === 4) {
				
				var response = JSON.parse(xhttp.responseText);

				// Get the user_id, stored for the current session,
				// and let the program know there is a logged in user
				sessionStorage.setItem("user_id", response.user_id);
				sessionStorage.setItem("userLoggedIn", true);

				// Send the user to set up a ride request
				window.location.href = "form.html";
			}
 			
 			// Several fail cases, username or email taken,
 			// and incomplete form.
 			else if (xhttp.status === 400) {
				alert("Username is already taken.");
			} else if (xhttp.status === 405) {
				alert("Email is already registered.");
			} else {
				alert("Please complete the form.")
			}
		};

		xhttp.send(request);
	}
});