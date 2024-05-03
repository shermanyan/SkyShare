document.getElementById("form-request").addEventListener("submit", function (event) {
	event.preventDefault();

	var flight_number = document.getElementById('flightNumber').value;
	var terminal = document.getElementById('terminalNumber').value;
	var arrival_time = document.getElementById('arrivalTime').value;

	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", "FormRequestServlet", true);
	xhttp.setRequestHeader("Content-Type", "application/json");

	var request = JSON.stringify({
		flight_number: flight_number,
		terminal: terminal,
		arrival_time: arrival_time
	});

	xhttp.onload = function () {
		if (xhttp.status === 200 && xhttp.readyState === 4) {
			
			var response = JSON.parse(xhttp.responseText);

			// Get the user_id, stored for the current session,
			// and let the program know there is a logged in user
			sessionStorage.setItem("user_id", response.user_id);
			sessionStorage.setItem("userLoggedIn", true);

			// Send the user to set up a ride request
			window.location.href = "form.html";
		}
		else {
			alert("Error");
		}
	};

	xhttp.send(request);
});