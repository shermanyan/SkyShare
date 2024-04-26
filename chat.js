document.addEventListener('DOMContentLoaded', () => {
    const messageInput = document.querySelector('.message-input input');
    const sendButton = document.querySelector('.message-input button');
  
    sendButton.addEventListener('click', () => {
      const messageText = messageInput.value.trim();
      if (messageText) {
        // Here you could append the message to the chat and clear the input field
        messageInput.value = '';
        // For demonstration, we'll just log it
        console.log('Message sent:', messageText);
      }
    });
  });
  