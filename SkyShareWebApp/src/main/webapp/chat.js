document.getElementById('message-form').addEventListener('submit', function(event) {
    event.preventDefault();
    const messageText = document.getElementById('message-text').value;
    if (messageText.trim() !== '') {
        sendMessage(messageText);
        document.getElementById('message-text').value = ''; // Clear input after sending
    }
});

function sendMessage(text) {
    // Here you would normally make an AJAX call to send the message to your server
    console.log('Sending message:', text); // Placeholder log

    // Simulate adding message to chat
    addMessageToChat(text, 'sent');
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
