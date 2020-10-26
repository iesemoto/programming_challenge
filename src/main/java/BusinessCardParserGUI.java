package main.java;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BusinessCardParserGUI {
    /****************************************************************
     * FUNCTION: Creates a GUI for parsing business car information
     * 
     * ARGUMENTS: runMe - boolean that determines whether to use or not use the GUI
     * 
     * RETURNS: No returns
     ****************************************************************/
    public void createAndShowGUI(Boolean runMe) {
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
}
