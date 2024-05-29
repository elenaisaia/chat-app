package com.wsmt.chatapi.mq;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wsmt.chatapi.domain.Message;
import com.wsmt.chatapi.domain.MessageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class Send implements Runnable {

    private final static String QUEUE_NAME = "test";

    @PersistenceContext
    private EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MessageRepository messageRepository;

    public Send(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    //    @Transactional
//    public void sendMessage(Message message, Channel channel) throws InterruptedException {
//        System.out.println(message);
//        try {
//            channel.basicPublish("", QUEUE_NAME, null, objectMapper.writeValueAsBytes(message));
//            channel.waitForConfirmsOrDie(5_000);
//        } catch (IOException | TimeoutException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        boolean isRunning = true;
        while (isRunning) {
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                channel.confirmSelect();
                while (isRunning) {
                    try {
                        //citire din db mesajele ordonate dupa identity crescator
                        //pt fiecare, trimit pe coada
                        //dupa scris pe coada si primit confirm, update status in db
                        System.out.println("Reading from db...");

                        List<Message> pendingMessages = getPendingMessages();
                        for (Message message : pendingMessages) {
                            System.out.println(message);
                            try {
                                channel.basicPublish("", QUEUE_NAME, null, objectMapper.writeValueAsBytes(message));
                                channel.waitForConfirmsOrDie(5_000);
                                message.setStatus("SENT");
                                messageRepository.save(message);

                            } catch (IOException | TimeoutException e) {
                                System.out.println("Message not sent! Cause: ");
                                e.printStackTrace();
                            }
                        }

                        System.out.println("Sleeping...");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        isRunning = false;
                        System.out.println("Stopping...");
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (IOException | TimeoutException e) {
                System.out.println("Could not connect to RabbitMQ! Cause:");
                e.printStackTrace();
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                isRunning = false;
                System.out.println("Stopping...");
                Thread.currentThread().interrupt();
            }
        }
    }

    public List<Message> getPendingMessages() {
        return entityManager.createQuery("select m from Message m where m.status='PENDING' order by m.id asc", Message.class)
                .setMaxResults(50).getResultList();
    }
}