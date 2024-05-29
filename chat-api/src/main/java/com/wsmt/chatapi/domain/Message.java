package com.wsmt.chatapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String text;
    private String status;
    private String session;

    public Message() {
    }

    public Message(Long id, String sender, String text, String status, String session) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.status = status;
        this.session = session;
    }

    public Message(String sender, String text, String session) {
        this.sender = sender;
        this.text = text;
        this.status = "PENDING";
        this.session = session;
    }

    public static Message createMessage(String sender, String text, String session) throws Exception {
        String errors = "";

        if(sender == null || sender.equals("")) {
            errors += "Seder can not be null! :<\n";
        }
        if(text == null || text.equals("")) {
            errors += "Text can not be null! :<\n";
        }

        if(!errors.equals("")) {
            throw new Exception(errors);
        }

        return new Message(sender, text, session);
    }

    public Long getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public String getStatus() {
        return status;
    }

    public String getSession() {
        return session;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message that = (Message) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    protected void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                ", status='" + status + '\'' +
                ", session='" + session + '\'' +
                "}";
    }
}
