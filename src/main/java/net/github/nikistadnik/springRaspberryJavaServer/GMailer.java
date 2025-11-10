package net.github.nikistadnik.springRaspberryJavaServer;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static javax.mail.Message.RecipientType.TO;

//@Component
public class GMailer {

    private static final String SEND_EMAIL = "nlynxtad.home@gmail.com";
    private static final String REC_EMAIL = "niki.stadnik@gmail.com";
    private final Gmail service;

    public GMailer() throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("Test Mailer")
                .build();
    }

    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(GMailer.class.getResourceAsStream("/client_secret_143683117441-3qgea2fddlahgpfb5umdim6edag0sinh.apps.googleusercontent.com.json")));

        //used to open browser from console for authentication
        // used only the first time
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void sendMail(String subject, String message) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(SEND_EMAIL));
        email.addRecipient(TO, new InternetAddress(REC_EMAIL));
        email.setSubject(subject);
        email.setText(message);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
            msg = service.users().messages().send("me", msg).execute();
            System.out.println("Message id: " + msg.getId());
            System.out.println(msg.toPrettyString());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
    }

    public void test() throws Exception {
        new GMailer().sendMail("A new message", """
                Dear reader,
                                
                Hello world.
                                
                Best regards,
                myself
                """);
    }

    /*
    //project is on Jakarta EE, switch javax.mail imports to jakarta.mail (and jakarta.activation)
    //If you have a method like doorCam.captureJpeg() that returns byte[], just pass its result as jpegBytes.

    public void emailDoorcamSnapshot(GMailer gMailer, byte[] jpegBytes) throws Exception {
        String html = """
        <p>Motion detected at the door.</p>
        <p><img src="cid:doorcam-snap"></p>
        """;

        gMailer.sendMailWithInlineImage(
                "DoorCam Snapshot",
                html,
                jpegBytes,          // image bytes from your DoorCam
                "image/jpeg",       // or "image/png" if appropriate
                "doorcam-snap"      // contentId without angle brackets; must match the cid above
        );
    }



    // NEW: Embed image inline (visible inside HTML body)
    public void sendMailWithInlineImage(String subject,
                                        String htmlBody,
                                        byte[] imageBytes,
                                        String contentType,
                                        String contentId) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(SEND_EMAIL));
        email.addRecipient(TO, new InternetAddress(REC_EMAIL));
        email.setSubject(subject);

        MimeMultipart related = new MimeMultipart("related");

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html; charset=UTF-8");
        related.addBodyPart(htmlPart);

        MimeBodyPart imagePart = new MimeBodyPart();
        DataSource ds = new ByteArrayDataSource(imageBytes, contentType);
        imagePart.setDataHandler(new DataHandler(ds));
        imagePart.setHeader("Content-ID", "<" + contentId + ">");
        imagePart.setDisposition(Part.INLINE);
        related.addBodyPart(imagePart);

        email.setContent(related);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
            Message sent = service.users().messages().send("me", msg).execute();
            System.out.println("Message id: " + sent.getId());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }

    }

     */


}