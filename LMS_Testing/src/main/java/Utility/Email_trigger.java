package Utility;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.File;
import java.util.List;

public class Email_trigger {

    /**
     * Send an HTML email with optional embedded image and report attachment.
     *
     * @param toRecipients List of recipient email addresses
     * @param subject      Subject of the email
     * @param htmlContent  Body content in HTML format
     * @param imagePath    Optional embedded image path (can be null)
     * @param reportPath   Optional report file path to attach (e.g., .html report)
     */
    public static void sendEmail(List<String> toRecipients, String subject, String htmlContent, String imagePath, String reportPath) {
        try {
            System.setProperty("mail.debug", "true");
            System.out.println("📤 Sending Email...");

            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587); // TLS
            email.setAuthenticator(new DefaultAuthenticator("divyakareti22@gmail.com", "eicr guqc elge aton")); // 🔒 Use app password
            email.setStartTLSRequired(true);
            email.setFrom("divyakareti22@gmail.com");
            email.setSubject(subject);

            // Embed screenshot if provided
            String cid = null;
            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    cid = email.embed(imageFile, "Screenshot");
                } else {
                    System.err.println("❌ Image not found: " + imagePath);
                }
            }

            // Compose HTML body
            String emailBody = htmlContent;
            if (cid != null) {
                emailBody += "<br><img src='cid:" + cid + "'/>";
            }
            email.setHtmlMsg(emailBody);

            // Add recipients
            for (String recipient : toRecipients) {
                email.addTo(recipient);
            }

            // Attach report if available
            if (reportPath != null && !reportPath.isEmpty()) {
                File reportFile = new File(reportPath);
                if (reportFile.exists()) {
                    email.attach(reportFile);
                    System.out.println("📎 Report attached: " + reportPath);
                } else {
                    System.err.println("❌ Report file not found: " + reportPath);
                }
            }

            email.send();
            System.out.println("✅ Email sent successfully.");

        } catch (EmailException e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        // Example usage of the sendEmail method
        List<String> toRecipients = List.of("divya.k@dhruvts.com", "example1@domain.com", "example2@domain.com"); // Add recipient emails here
        String subject = "Test HTML Report";
        String htmlContent = "<html><body>"
                + "<h1>Test Report</h1>"
                + "<p>This is a test email with an embedded screenshot.</p>"
                + "</body></html>";
        String imagePath = "C:\\WorkSpace\\EmployeeAutomationFramework\\screenshots\\screenshot.png";

        // Call the reusable sendEmail function
        sendEmail(toRecipients, subject, htmlContent, imagePath, imagePath);
    }
}
