import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

public class emailSender {

    public static boolean sendMail(String from, String pwd, String subject, String msg, String to[]){
        String host = "smtp.gmail.com";
        Properties props = System.getProperties();
        props.put("mail.smtp.startttls.enable", true);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.from", from);
        props.put("mail.smtp.password", pwd);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session s = Session.getDefaultInstance(props, null);

        try {
            MimeMessage mimeMessage = new MimeMessage(s);
            mimeMessage.setFrom(new InternetAddress(from));

            InternetAddress[] toAddress = new InternetAddress[to.length];
            for(int i = 0; i < to.length; i++){
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i <toAddress.length; i++){
                mimeMessage.addRecipient(MimeMessage.RecipientType.TO, toAddress[i]);
            }

            mimeMessage.setSubject(subject);
            mimeMessage.setText(msg);


            Transport transport = s.getTransport("smtp");
            transport.connect(host, from, pwd);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
            return true;

        }catch (MessagingException m){
            m.printStackTrace();
        }

        return false;
    }
}
