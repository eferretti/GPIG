package gpigb.report;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 
public class EmailReporter implements Reporter{
	
	private int id;
	private String toEmail;
	private String subject;
	private String fromEmail;
	private String password;

	

	@Override
	public void setID(int newID) {
		this.id =  newID;
	}

	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public int getConfigurationStepNumber() {
		return 1;
	}

	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> configMap = new HashMap<>();
		configMap.put("From (username): ", new ConfigurationValue(ValueType.String, "humsgpigb@gmail.com"));
		configMap.put("Password: ", new ConfigurationValue(ValueType.String, "whatdoitypehere"));
		configMap.put("To (gmail): ", new ConfigurationValue(ValueType.String, "humsgpigb@gmail.com"));
		configMap.put("Subject: ", new ConfigurationValue(ValueType.String, "HUMS Latest Report"));
		return configMap;
	}
	
	@Override
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			fromEmail = (String) newSpec.get("From (username): ").strValue;
			password = (String) newSpec.get("Password: ").strValue;
			toEmail = (String) newSpec.get("To (gmail): ").strValue;
			subject = (String) newSpec.get("Subject: ").strValue;
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	/**
    Outgoing Mail (SMTP) Server
    requires TLS or SSL: smtp.gmail.com
    Use Authentication: Yes (TLS)
    Port for TLS/STARTTLS: 587
    */
	@Override
	public void generateReport(List<RecordSet<?>> data) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		
		/* populate e-mail body text */
		StringBuffer body = new StringBuffer();
		body.append("Dear HUMS user,");
		body.append(System.getProperty("line.separator"));
		body.append(System.getProperty("line.separator"));
		body.append("You have just recieved your latest report!");
		body.append(System.getProperty("line.separator"));
		body.append(System.getProperty("line.separator"));
		for(RecordSet<?> dataItem : data) {
			body.append(dataItem.toString());
			body.append(System.getProperty("line.separator"));
		}
		body.append(System.getProperty("line.separator"));
		body.append(System.getProperty("line.separator"));
		body.append("Kindest Regards,");
		body.append(System.getProperty("line.separator"));
		body.append(System.getProperty("line.separator"));
		body.append("Your awesome HUMS");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
		String dateStr = dateFormat.format(cal.getTime());
		
		String fileName = "HUMS_" + dateStr + ".txt";
		File file = new File(fileName);
		
		Session session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(fromEmail, password);
					}
				  });
		
		 try {
			 
			 if (file.getParentFile() != null && !file.getParentFile().mkdirs()) {
				    System.out.println("handle permission problems here");
				}
		    System.out.println("either no parent directories there the missing directories have been created");
			if (file.createNewFile() || file.isFile()) {
				System.out.println("ready to write content");
				file.createNewFile();
				BufferedWriter output = new BufferedWriter(new FileWriter(file));
				output.write(body.toString());
				output.flush();
				output.close();
			} else {
				System.out.println("handle directory here");
			}	 
			 
		 
         MimeMessage msg = new MimeMessage(session);
         
         // Set message details
         msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
         msg.addHeader("format", "flowed");
         msg.addHeader("Content-Transfer-Encoding", "8bit");	           
         msg.setFrom(new InternetAddress(fromEmail, "NoReply-HUMS"));	 
         msg.setReplyTo(InternetAddress.parse(fromEmail, false));	 
         msg.setSubject(subject, "UTF-8");	 
         msg.setSentDate(new Date());
         msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
           
         // Create the message body part
         BodyPart messageBodyPart = new MimeBodyPart();
 
         // Fill the message
         messageBodyPart.setText(body.toString());
          
         // Create a multipart message for attachment
         Multipart multipart = new MimeMultipart();
 
         // Set text message part
         multipart.addBodyPart(messageBodyPart);
 
         // Second part is attachment
         messageBodyPart = new MimeBodyPart();
         
         DataSource source = new FileDataSource(fileName);
         messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName(fileName);
         multipart.addBodyPart(messageBodyPart);
 
         // Send the complete message parts
         msg.setContent(multipart);
 
         // Send message
         Transport.send(msg);
         file.delete();
         System.out.println("E-mail Sent Successfully with attachment!!");
	      }catch (MessagingException e) {
	         e.printStackTrace();
	      } catch (UnsupportedEncodingException e) {
	         e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}