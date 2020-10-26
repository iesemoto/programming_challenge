package main.java;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/****************************************************************
 * PROGRAM: Business Card Parser 
 * AUTHOR: Iko Esemoto
 * 
 * FUNCTION: This program takes a business card text as input and outputs the
 * Name, Phone, and Email Address of the owner of the business card.
 * 
 * INPUT: location of input, i.e. standard input, a file on disk
 * 
 * NOTES: This program will not work properly if, for example, the job or career
 * is not listed in job_keywords
 ****************************************************************/
public class BusinessCardParser {
    // US phone numbers are 10 digits
    static int MIN_PHONE_NUM_LEN = 10;
    // Some job titles and companies from test example
    static private String[] job_keywords = { "engineer", "developer", "analyst", "ltd", "technologies", "co", "corp",
            "inc", "service", "llc" };

    public static void main(String[] args) {
        // If no input test file, open the GUI
        if (args.length == 0) {
            BusinessCardParserGUI gui = new BusinessCardParserGUI();
            gui.createAndShowGUI(true);
        } else {
            // Get file path
            Path inputTestFile = Paths.get(args[0]);
            try {
                // Read file content
                String content = Files.readString(inputTestFile, StandardCharsets.US_ASCII);
                // Get contact information from file content
                BusinessCardParser bcp = new BusinessCardParser();
                ContactInfo test = bcp.getContactInfo(content);
                // Print output
                System.out.println(test.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /****************************************************************
     * FUNCTION: Retrieves the name of the business card holder information
     * 
     * ARGUMENTS: document - the business card text as input
     * 
     * RETURNS: Returns the line of the name found in the business card document
     ****************************************************************/
    public String retrieveName(String document) {
        // Breaks document into lines
        String[] breakDocument = document.split("\n");

        for (int i = 0; i < breakDocument.length; i++) {
            // If the line has numbers or the @, & symbols, maybe it's not a name
            int hasDigits = breakDocument[i].replaceAll("\\D", "").length();
            if (breakDocument[i].contains("@") || breakDocument[i].contains("&") || hasDigits > 0
                    || breakDocument[i].isEmpty()) {
                continue;
            }

            boolean foundInvalid = false;
            for (int k = 0; k < job_keywords.length; k++) {
                // Turned line to lowercase to compare with the lowercase job keywards
                if (breakDocument[i].toLowerCase().contains(job_keywords[k])) {
                    foundInvalid = true;
                    break;
                }
            }
            // If it did not contain a job keyword, then maybe it's a name.
            // Else, it will continue looping
            if (!foundInvalid) {
                return breakDocument[i];
            }
        }

        return null;
    }

    /****************************************************************
     * FUNCTION: Retrieves the phone number of the business card holder information
     * 
     * ARGUMENTS: document - the business card text as input
     * 
     * RETURNS: Returns only the numbers of the phone number found in the business
     * card document
     ****************************************************************/
    public String retrievePhoneNumber(String document) {
        // Breaks document into lines
        String[] breakDocument = document.split("\n");
        // Temp reusable string for debugging
        String isPhoneNumber;

        for (int i = 0; i < breakDocument.length; i++) {

            // If the line contains the word fax or is empty, skip. It's not the phone
            // number
            if (breakDocument[i].toLowerCase().contains("fax") || breakDocument[i].isEmpty()) {
                continue;
            }
            // If the line already contains the words Tel or Phone, extract only the numbers
            // Turned line to lowercase to compare with the lowercase words tel or phone
            else if (breakDocument[i].toLowerCase().contains("tel")
                    || breakDocument[i].toLowerCase().contains("phone")) {
                // Replace all non-digit with empty
                isPhoneNumber = breakDocument[i].replaceAll("\\D", "");
                return isPhoneNumber;
            } else {
                // Remove all non-digit
                int isLikePhoneNumber = breakDocument[i].replaceAll("\\D", "").length();
                // If the number of digits is >= MIN_PHONE_NUM_LEN (set to 10), maybe it's a
                // phone number
                if (isLikePhoneNumber >= MIN_PHONE_NUM_LEN) {
                    isPhoneNumber = breakDocument[i].replaceAll("\\D", "");
                    return isPhoneNumber;
                }
            }

        }
        return null;
    }

    /****************************************************************
     * FUNCTION: Retrieves the email of the business card holder information
     * 
     * ARGUMENTS: document - the business card text as input
     * 
     * RETURNS: Returns the line of the email found in the business card document
     ****************************************************************/
    public String retrieveEmailAddress(String document) {
        // Breaks document into lines
        String[] breakDocument = document.split("\n");

        for (int i = 0; i < breakDocument.length; i++) {
            // If the line is empty, skip. It's not an email
            if (breakDocument[i].isEmpty()) {
                continue;
            }
            // If the line contains @, this line could be an email. If not, keep iterating
            else if (breakDocument[i].contains("@")) {
                // Breaks the line into words. There could be other words in the line
                String[] breakLine = breakDocument[i].trim().split(" ");
                for (int k = 0; k < breakLine.length; k++) {
                    // If the word contains @ and a dot, maybe it's an email
                    if (breakLine[k].contains("@") && breakLine[k].contains("@")) {
                        return breakLine[k];
                    }
                }
            }
        }
        return null;
    }

    /****************************************************************
     * FUNCTION: Creates an instance of the ContactInfo class
     * 
     * ARGUMENTS: document - the business card text as input
     * 
     * RETURNS: Returns a new ContactInfo object
     ****************************************************************/
    public ContactInfo getContactInfo(String document) {
        ContactInfo newContact = new ContactInfo();
        newContact.setName(retrieveName(document));
        newContact.setPhoneNumber(retrievePhoneNumber(document));
        newContact.setEmailAddress(retrieveEmailAddress(document));

        return newContact;
    }
}
