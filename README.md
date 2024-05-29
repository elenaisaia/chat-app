A chat application that consists of:
  1. chat-api (java, spring):
    a. a REST api that receives incoming messages and stores them into db with PENDING status;
    b. a thread that reads pending messages from db and sends them to a message queue (RabbitMQ) => Producer, then chnges the status into SENT in db after confirmation;
  2.  chat-notifications (node js): an app that reads messages from the message queue (RabbitMQ) => Consumer, then sends them to all chat participants via web socket
  3.  chat-webclient (react js): a minimalist web client used for login, sending and receiving messages
