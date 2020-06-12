import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MailUtil {

    final static String resourceName = "mail.properties";
    final static String host = "smtp.gmail.com";
    final static String username = "*****@gmail.com";
    final static String password = "*****";

    public static void sendMessage(
            String title,
            String removedURL,
            String addedURL,
            String reusedURL,
            String sendTo
    ) {
        final Properties prop = new Properties();
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName));
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(username));
                    InternetAddress.parse(sendTo));

            message.setSubject(title);
//            message.setText("Здравствуйте, Иван Иванов.\n" +
            message.setText("Здравствуйте, " + sendTo + ".\n" +
                    "За последние сутки во вверенных Вам сайтах произошли следующие изменения:\n\n" +
                    "Исчезли следующие страницы: " + removedURL + "\n" +
                    "Появились следующие новые страницы: " + addedURL + "\n" +
                    "Изменились следующие страницы: " + reusedURL + "\n" +
                    "\nС уважением," +
                    "\n" + "автоматизированная система мониторинга");
            Transport.send(message);
            System.out.println("Done");
        } catch (
                MessagingException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> read() throws MessagingException, IOException {
        final Properties prop = new Properties();
        Map<String, String> mapMail = new HashMap<>();
        String mesHead = null;
        String mesBody = null;
        String mesData = null;

        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        Session session = Session.getDefaultInstance(prop, null);
        Store store = session.getStore("imaps");

        store.connect(host, username, password);

        Folder inbox = store.getFolder("inbox");
        inbox.open(Folder.READ_ONLY);

        int messageCount = inbox.getMessageCount();
//        System.out.println("Total Messages: " + messageCount);

        Message[] messages = null;
        int count = 0;

        if (messageCount == 0) {
            mesBody = null;
        } else if (messageCount < 10) {
            messages = new Message[messageCount];
            messages = inbox.getMessages();
            count = messageCount;
        } else {
            messages = new Message[messageCount];
            messages = inbox.getMessages();
            count = 10;
        }
        System.out.println("------------------------------");

        for (int i = 0; i < count; i++) {
            if (messages[i].getSubject().equals("testApl")) {
                mesHead = Arrays.toString(messages[i].getFrom());
                mapMail.put("mesHead", mesHead);

                mesData = messages[i].getSentDate().toString();
                mapMail.put("mesData", mesData);

                if (messages[i].isMimeType("text/plain")) {
                    mesBody = messages[i].getContent().toString();
                    mapMail.put("mesBody", mesBody);
                } else if (messages[i].isMimeType("multipart/*")) {
                    MimeMultipart mimeMultipart = (MimeMultipart) messages[i].getContent();
                    mesBody = getTextFromMimeMultipart(mimeMultipart);
                    mapMail.put("mesBody", mesBody);
                }
            }
        }
        inbox.close(true);
        store.close();
        if (mesBody == null) {
            return null;
        } else {
            return mapMail;
        }
    }


    private static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }


    /*public static void sendMessage2(
            String title,
            String removedURL,
            String addedURL,
            String reusedURL,
            String sendTo
    ) {
        final Properties prop = new Properties();
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName));
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(username));
                    InternetAddress.parse(sendTo));

            message.setSubject(title);
//            message.setText("Здравствуйте, Иван Иванов.\n" +
            message.setText("Здравствуйте, " + sendTo + ".\n" +
                    "За последние сутки во вверенных Вам сайтах произошли следующие изменения:\n\n" +
                    "Исчезли следующие страницы: " + removedURL + "\n" +
                    "Появились следующие новые страницы: " + addedURL + "\n" +
                    "Изменились следующие страницы: " + reusedURL + "\n" +
                    "\nС уважением," +
                    "\n" + "автоматизированная система мониторинга");

            message.setReplyTo(message.getReplyTo());

            Transport.send(message);
            System.out.println("Done");
        } catch (
                MessagingException e) {
            e.printStackTrace();
        }
    }*/


}


/*public static String read2() throws MessagingException, IOException {
        final Properties prop = new Properties();
        String result = null;

        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        Session session = Session.getDefaultInstance(prop, null);
        Store store = session.getStore("imaps");

        store.connect(host, username, password);

        Folder inbox = store.getFolder("inbox");
        inbox.open(Folder.READ_ONLY);

        int messageCount = inbox.getMessageCount();
//        System.out.println("Total Messages: " + messageCount);

        Message[] messages;
        int count;

        if (messageCount == 0) {
            return result = null;
        } else if (messageCount < 10) {
            messages = new Message[messageCount];
            messages = inbox.getMessages();
            count = messageCount;
        } else {
            messages = new Message[messageCount];
            messages = inbox.getMessages();
            count = 10;
        }
        System.out.println("------------------------------");

        for (int i = 0; i < count; i++) {
            if (messages[i].getSubject().equals("testApl")) {
                System.out.println(Arrays.toString(messages[i].getFrom()));
                if (messages[i].isMimeType("text/plain")) {
                    result = messages[i].getContent().toString();
                } else if (messages[i].isMimeType("multipart/*")) {
                    MimeMultipart mimeMultipart = (MimeMultipart) messages[i].getContent();
                    result = getTextFromMimeMultipart(mimeMultipart);
                }
            }
        }
        inbox.close(true);
        store.close();
        if (result.equals(null)) {
            return null;
        } else {
            return result;
        }
    }*/