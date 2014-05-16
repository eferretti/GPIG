package gpigb.report;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 
public class EmailReporter {
	/**
    Outgoing Mail (SMTP) Server
    requires TLS or SSL: smtp.gmail.com (use authentication)
    Use Authentication: Yes
    Port for TLS/STARTTLS: 587
    */
	public static void sendAttachmentEmail(String toEmail, String subject, String body, String filename){
	    
		final String username = "miromarinov@gmail.com";
		final String password = "hmm";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		
		try{
	         MimeMessage msg = new MimeMessage(session);
	         msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	         msg.addHeader("format", "flowed");
	         msg.addHeader("Content-Transfer-Encoding", "8bit");	           
	         msg.setFrom(new InternetAddress("hums@gmail.com", "NoReply-HUMS"));	 
	         msg.setReplyTo(InternetAddress.parse("hums@gmail.com", false));	 
	         msg.setSubject(subject, "UTF-8");	 
	         msg.setSentDate(new Date());
	         msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	           
	         // Create the message body part
	         BodyPart messageBodyPart = new MimeBodyPart();
	 
	         // Fill the message
	         messageBodyPart.setText(body);
	          
	         // Create a multipart message for attachment
	         Multipart multipart = new MimeMultipart();
	 
	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);
	 
	         // Second part is attachment
	         messageBodyPart = new MimeBodyPart();
	         
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);
	 
	         // Send the complete message parts
	         msg.setContent(multipart);
	 
	         // Send message
	         Transport.send(msg);
	         System.out.println("E-mail Sent Successfully with attachment!!");
	      }catch (MessagingException e) {
	         e.printStackTrace();
	      } catch (UnsupportedEncodingException e) {
	         e.printStackTrace();
	    }
	}
	
	/**
    Outgoing Mail (SMTP) Server
    requires TLS or SSL: smtp.gmail.com (use authentication)
    Use Authentication: Yes
    Port for TLS/STARTTLS: 587
  */
	public static void sendSimpleEmail(String toEmail, String subject, String body) {
		 
		final String username = "miromarinov@gmail.com";
		final String password = "hmm";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("mirobomarinov@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("mirobomarinov@gmail.com"));
			message.setSubject(subject);
			message.setText(body);
 
			Transport.send(message);
 
			System.out.println("E-mail Sent Successfully!!");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
 
	public static void main(String[] args) {
		EmailReporter.sendSimpleEmail("miromarinov@gmail.com", "Sub1", "Test1");
		EmailReporter.sendAttachmentEmail("miromarinov@gmail.com", "Sub2", "Test2", "abc.txt");
	}
}