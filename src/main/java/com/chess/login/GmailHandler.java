package com.chess.login;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GmailHandler {
    private static final Session session = setupServerProperties();

    private static final String DEFAULT_VERIFICATION_SUBJECT_MESSAGE = "Verification code";
    private static final String DEFAULT_VERIFICATION_BODY_MESSAGE = "Hello! Your Verification code is ";

    private static final String DEFAULT_WELCOME_SUBJECT_MESSAGE = "Welcome to Chess For Fun!!!";
    private static final String DEFAULT_WELCOME_BODY_MESSAGE = "Hello, thank you for joining. " +
            "We are hoping for you to have a great time here. \n" +
            "Your google authentication key is ";

    private static final String EMAIL_HOST = "smtp.gmail.com";

    private static final String SENDER = "chessForFun360@gmail.com";
    private static final String PASSWORD = "ibwklmtdpbxwdjmk";
    private String recipient;
    private MimeMessage mimeMessage;

    public GmailHandler(String recipient){
        this.recipient = recipient;
    }


    public void draftVerificationEmail() throws MessagingException {
        String randomSixDigits = getRandomSixDigits();
        String subject = DEFAULT_VERIFICATION_SUBJECT_MESSAGE;
        String body = DEFAULT_VERIFICATION_BODY_MESSAGE + randomSixDigits;

        setMimeMessageProperties(subject, body);
    }

    public void draftWelcomeEmail(String key) throws MessagingException{
        String subject = DEFAULT_WELCOME_SUBJECT_MESSAGE;
        String body = DEFAULT_WELCOME_BODY_MESSAGE + key;

        setMimeMessageProperties(subject, body);

    }

    public void sendEmail() throws MessagingException {
        Transport transport = session.getTransport("smtp");

        transport.connect(EMAIL_HOST, SENDER, PASSWORD);
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        transport.close();
    }

    private static String getRandomSixDigits(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(random.nextInt(10)); // generates random digit between 0 and 9
        }
        return stringBuilder.toString();
    }

    private static Session setupServerProperties(){
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        return Session.getDefaultInstance(properties);
    }

    private void setMimeMessageProperties(String subject, String body) throws MessagingException {
        mimeMessage = new MimeMessage(session);

        InternetAddress address = new InternetAddress(recipient);
        //address.validate();

        mimeMessage.addRecipient(Message.RecipientType.TO, address);


        mimeMessage.setSubject(subject);
        mimeMessage.setText(body);
    }

    public static boolean isValidEmailAddress(String email){
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
