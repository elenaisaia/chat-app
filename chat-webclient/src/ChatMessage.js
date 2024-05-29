export const ChatMessage = ({message}) => {
    const userJoinedEventToComponent = (message) => {
        return (
            <div>
                {message.timestamp}: User {message.sender} joined the chat!<br/>
            </div>
        )
    };

    const userLeftEventToComponent = (message) => {
        return (
            <div>
                {message.timestamp}: User {message.sender} left the chat!<br/>
            </div>
        )
    };

    const messageEventToComponent = (message) => {
        return (
            <div>
                {message.timestamp} {message.sender}: {message.text}<br/>
            </div>
        )
    };

    const messageToComponent = (message) => {
        if (message.type) {
            if (message.type === "USER_JOINED") {
                return userJoinedEventToComponent(message);
            }
            else if (message.type === "USER_LEFT") {
                return userLeftEventToComponent(message);
            }
        }
        return messageEventToComponent(message);
    };

    return messageToComponent(message);
}
