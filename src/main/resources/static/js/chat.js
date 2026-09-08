
function sendMessage() {
    const input = document.getElementById('chat-input');
    if (!input || !input.value.trim()) return;

    const msgText = input.value.trim();
    const container = document.getElementById('chat-messages');

    // Add outgoing message
    const msgEl = document.createElement('div');
    msgEl.className = 'message-bubble outgoing';
    msgEl.innerHTML = `${msgText} <div style="font-size:0.7rem; opacity:0.7; text-align:right; margin-top:4px;">Just now</div>`;
    container.appendChild(msgEl);

    input.value = '';
    container.scrollTop = container.scrollHeight;

    // Auto response demo
    setTimeout(() => {
        const replyEl = document.createElement('div');
        replyEl.className = 'message-bubble incoming';
        replyEl.innerHTML = `Thank you for your message regarding the property! Our representative will respond shortly. <div style="font-size:0.7rem; color:var(--muted); text-align:right; margin-top:4px;">Just now</div>`;
        container.appendChild(replyEl);
        container.scrollTop = container.scrollHeight;
    }, 1000);
}

document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('chat-input');
    if (input) {
        input.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') sendMessage();
        });
    }
});
