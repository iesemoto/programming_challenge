package main.java;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/****************************************************************
 * PROGRAM: Business Card Parser AUTHOR: Iko Esemoto
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
    int MIN_PHONE_NUM_LEN = 10;
    // Some job titles and companies from test example
    private String[] job_keywords = { "engineer", "developer", "analyst", "ltd", "technologies", "co", "corp", "inc",
            "service", "llc" };

    public static void main(String[] args) {
        // If no input test file, open the GUI
        if (args.length == 0) {
            createAndShowGUI(true);
        } else {
            // Get file path
            Path inputTestFile = Paths.get(args[0]);
            try {
                // Read file content
                String content = Files.readString(inputTestFile, StandardCharsets.US_ASCII);
                // Get contact information from file content
                BusinessCardParser bc = new BusinessCardParser();
                ContactInfo test = bc.getContactInfo(content);
                // Print output
                System.out.println(test.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /****************************************************************
     * FUNCTION: Creates a GUI for parsing business car information
     * 
     * ARGUMENTS: runMe - boolean that determines whether to use or not use the GUI
     * 
     * RETURNS: No returns
     ****************************************************************/
    public static void createAndShowGUI(Boolean runMe) {
        JFrame frame = new JFrame("Business Card OCR");

        int width = 800;
        int length = 300;

        int top_width = 300;
        int top_button_width = width - (top_width * 2);
        int top_length = 200;

        // Panel one contains a textarea and a label
        JPanel panel_1 = new JPanel();
        panel_1.setBounds(0, 0, top_width, top_length);

        JLabel label_1 = new JLabel("Enter Business Card Information:", JLabel.LEADING);
        panel_1.add(label_1);

        JTextArea enteredDocument = new JTextArea(10, 27);
        enteredDocument.setMargin(new Insets(5, 5, 5, 5));
        JScrollPane sp = new JScrollPane(enteredDocument);
        panel_1.add(sp);

        // Panel three contains a textarea and a label
        JPanel panel_3 = new JPanel();
        panel_3.setBounds(top_width + top_button_width, 0, top_width, top_length);

        JLabel label_2 = new JLabel("Result:", JLabel.LEADING);
        panel_3.add(label_2);

        JTextArea result = new JTextArea(10, 25);
        result.setMargin(new Insets(5, 5, 5, 5));
        panel_3.add(result);

        // Panel four contains the logger
        JPanel panel_4 = new JPanel();
        panel_4.setBounds(0, top_length, width - 10, 60);
        panel_4.setBorder(BorderFactory.createTitledBorder("Logger"));

        JTextArea logger = new JTextArea("> ", 1, 75);
        panel_4.add(logger);

        // Panel two contains a button
        JPanel panel_2 = new JPanel();
        panel_2.setBounds(top_width, 0, top_button_width, top_length);

        JButton button = new JButton("Parse Card");
        panel_2.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Send error message if empty
                if (enteredDocument.getText().isEmpty()) {
                    logger.setForeground(Color.RED);
                    logger.setText(">  No information was enter! Please enter a business card information.");

                } else {
                    // Parse Business Card Information
                    BusinessCardParser newCard = new BusinessCardParser();
                    ContactInfo output = newCard.getContactInfo(enteredDocument.getText());
                    result.setText(output.toString());
                    logger.setForeground(Color.GREEN);
                    logger.setText(">  Successfully parsed business card information.");
                }
            }
        });

        // Window / Frame Size
        // Increase the window by 10
        frame.setSize(width + 10, length + 10);

        // Adding all panels to frame
        frame.add(panel_1);
        frame.add(panel_2);
        frame.add(panel_3);
        frame.add(panel_4);

        // Has the effect of centering the JFrame
        frame.setLocationRelativeTo(null);

        // Defaults
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(runMe);
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
        // Temp resusable string for debugging
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
