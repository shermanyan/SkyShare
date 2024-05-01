document.getElementById('message-form').addEventListener('submit', function(event) {
    event.preventDefault();
    const messageText = document.getElementById('message-text').value;
    if (messageText.trim() !== '') {
        sendMessage(messageText);
        document.getElementById('message-text').value = ''; // Clear input after sending
    }
});

function sendMessage(text) {
    // Construct the message object to be sent
    const messageData = {
        text: text,
        // Include any other relevant data, like sender ID, chat ID, etc.
    };

    // AJAX call to send the message to the server
    fetch('SendMessageServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(messageData)
    })
    .then(response => {
        if(response.ok) {
            return response.json(); // Or handle other response types if needed
        } else {
            throw new Error('Network response was not ok.');
        }
    })
    .then(data => {
        // The message was successfully sent, handle the response
        console.log('Message sent:', data);
        addMessageToChat(text, 'sent');
    })
    .catch(error => {
        console.error('Error sending message:', error);
    });
}


function receiveMessage(text) {
    // This function would be triggered by your real-time messaging system
    console.log('Receiving message:', text); // Placeholder log

    // Simulate adding received message to chat
    addMessageToChat(text, 'received');
}

function addMessageToChat(text, type) {
    const messagesContainer = document.querySelector('.chat-messages');
    const messageElement = document.createElement('div');
    messageElement.classList.add('message', type);
    messageElement.innerHTML = `<p>${text}</p><span class="time">${new Date().toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true })}</span>`;
    messagesContainer.appendChild(messageElement);

    // Scroll to the latest message
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}


// Fetch user information to populate the sidebar
function fetchUsers(groupID) {
    fetch(`/UsersServlet?groupID=${groupID}`)
        .then(response => response.json())
        .then(users => {
            const usersList = document.querySelector('.users-list');
            usersList.innerHTML = ''; // Clear current list
            users.forEach(user => {
                usersList.innerHTML += `<div class="user-item">${user.firstName} ${user.lastName} - ${user.phoneNumber}</div>`;
            });
        })
        .catch(error => console.error('Failed to fetch users:', error));
}

// Fetch departure time and location to update the chat header
function fetchDepartureInfo() {
    fetch(`/DepartureServlet`)
        .then(response => response.json())
        .then(data => {
            const departureInfo = document.querySelector('#group-departure-time');
            // Using innerHTML to set the content of the div
            departureInfo.innerHTML = `Departure Time: ${data.departureTime}`;
        })
        .catch(error => console.error('Failed to fetch departure info:', error));
}

// Update pickup location
function updateLocation(newLocation) {
    fetch(`/LocationServlet`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ pickupLocation: newLocation })
    })
    .then(response => {
        if(response.ok) {
            console.log('Pickup location updated');
            fetchDepartureInfo(); // Refresh departure info
        } else {
            console.error('Failed to update location');
        }
    })
    .catch(error => console.error('Error updating location:', error));
}

// Leave group action
function leaveGroup(userId) {
    fetch('/LeaveGroupServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userId: userId })
    })
    .then(response => {
        if(response.ok) {
            console.log('User has left the group');
            // Additional client-side handling if needed
        } else {
            console.error('Failed to leave group');
        }
    })
    .catch(error => console.error('Error leaving group:', error));
}

// Example of invoking these functions
fetchUsers();
fetchDepartureInfo();

document.querySelector('.location-change button').addEventListener('click', () => {
    const newLocation = document.querySelector('.location-change input').value;
    updateLocation(newLocation);
});

document.querySelector('.leave-group-btn').addEventListener('click', () => {
    const userId = getUserId(); // Assuming there's a function to fetch the user's ID

    if (userId) {
        leaveGroup(userId);
    } else {
        console.error("Failed to retrieve user ID");
        // Optionally, inform the user that an error occurred
    }
});