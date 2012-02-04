package com.supinfo.printerapplicastion;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.*;
import javax.naming.InitialContext;

/**
 *
 * @author lukas
 */
public class PrinterApplication {

    public static void main(String[] args) throws Exception {
        
        InitialContext context = new InitialContext();
        
        ConnectionFactory connectionFactory = 
                (ConnectionFactory) context.lookup("jms/suprailsConnectionFactory");
        
        Destination destination = (Destination) context.lookup("jms/suprailsQueue");
        
        Connection connection = connectionFactory.createConnection();
        
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        MessageConsumer consumer = session.createConsumer(destination);
        
        System.out.println("En attente de message...");
        
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                
                TextMessage textMessage;
                
                try {
                    textMessage = (TextMessage) message;
                } catch (ClassCastException e) {
                    System.out.println("Invalid message received : " + e.getMessage());
                    return;
                }
                
                try {
                    System.out.println("Message received : " + textMessage.getText());
                } catch (JMSException ex) {
                    Logger.getLogger(PrinterApplication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        connection.start();
    }
}
