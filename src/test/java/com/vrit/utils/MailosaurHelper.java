
package com.vrit.utils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.SearchCriteria;

public class MailosaurHelper {

    private static final String SERVER_ID = "uebl2mni";

    private final MailosaurClient mailosaur;

    public MailosaurHelper() {

        String apiKey = System.getenv("MAILOSAUR_API_KEY");

        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new RuntimeException(
                "MAILOSAUR_API_KEY environment variable not found!"
            );
        }

        System.out.println("Mailosaur API key loaded: true");

        mailosaur = new MailosaurClient(apiKey);
    }

    // Generate unique email address
    public String generateEmailAddress()
            throws IOException, MailosaurException {

        return mailosaur.servers()
                .generateEmailAddress(SERVER_ID);
    }

    // Retrieve OTP from email
    public String getOTP(String email)
            throws IOException, MailosaurException {

        System.out.println("Waiting for verification email...");

        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(email);

        Message message = mailosaur.messages()
                .get(SERVER_ID, criteria, 60);

        if (message == null || message.text() == null) {
            throw new RuntimeException(
                "Verification email not received!"
            );
        }

        System.out.println("Verification email received!");
        System.out.println("Email subject: " + message.subject());

        String emailBody = message.text().body();

        Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
        Matcher matcher = pattern.matcher(emailBody);

        if (matcher.find()) {

            System.out.println("OTP retrieved successfully!");

            return matcher.group();
        }

        throw new RuntimeException(
            "Six-digit OTP not found in verification email!"
        );
    }
}
