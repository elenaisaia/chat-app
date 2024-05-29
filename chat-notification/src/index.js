import Koa from 'koa';
import WebSocket from 'ws';
import http from 'http';
import cors from 'koa-cors';
import { v4 as uuid4} from 'uuid';
const amqp = require('amqplib/callback_api');


const app = new Koa();
const server = http.createServer(app.callback());
const webSocketServer = new WebSocket.Server({ server });

app.use(cors());
server.listen(9090);

const broadcastMessage = (senderSession, message) => {
    message = {...message,
        sendByCurrentUser: false,
        timestamp: new Date()};
    const jsonMessage = JSON.stringify(message);
    webSocketServer.clients.forEach(receiverSession => {
        if (receiverSession.sessionId !== senderSession) {
            receiverSession.send(jsonMessage);
        }
    });
}

webSocketServer.on('connection', session => {
    session.sessionId = uuid4();
    console.log("User connected! Session id = " + session.sessionId);
    session.send(JSON.stringify({session: session.sessionId, type: "CURRENT_SESSION"}));
    session.on("message", event => {
        const message = JSON.parse(event);
        if (message) {
            if (message.type && message.type === "JOIN") {
                session.sender = message.sender;
                console.log("User joined! Session id = " + session.sessionId + " Name = " + session.sender);
                broadcastMessage(session.sessionId, {type: "USER_JOINED"});
            } else {
                const senderReply = {
                    sender: session.sender,
                    sendByCurrentUser: true,
                    timestamp: new Date(),
                    text: message.text
                };
                session.send(JSON.stringify(senderReply));
                broadcastMessage(session.sessionId, message);
            }
        }
    });
    session.on("close", () => {
        console.log("User disconnected! Session id = " + session.sessionId);
        broadcastMessage(session.sessionId, {type: "USER_LEFT"})
    });
});

amqp.connect('amqp://localhost', function(error0, connection) {
    if (error0) {
        throw error0;
    }
    connection.createChannel(function(error1, channel) {
        if (error1) {
            throw error1;
        }

        const queue = 'test';

        channel.assertQueue(queue, {
            durable: true
        });

        console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", queue);

        channel.consume(queue, function(msg) {
            console.log(" [x] Received %s", msg.content);
            broadcastMessage(msg.session, JSON.parse(msg.content));
        }, {
            noAck: true
        });
    });
});
