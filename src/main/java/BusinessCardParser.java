package main.java;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    static int MIN_PHONE_NUM_LEN = 10;
    // Array of job titles
    // Source: https://zety.com/blog/job-titles
    static private HashSet<String> job_keywords = new HashSet<>(Arrays.asList("ltd", "technologies", "co", "corp",
            "asymmetrik", "inc", "service", "llc", "marketing", "web", "president", "nursing", "librarian", "account",
            "product", "brand", "seo", "digital", "ecommerce", "vice", "administrative", "auditing", "bookkeeper",
            "branch", "quality", "chief", "risk", "human", "secretary", "file", "program", "ceo", "coo", "cfo", "cio",
            "cto", "cmo", "chro", "cdo", "cpo", "cco", "team", "controller", "organizer", "supervisor",
            "superintendent", "head", "overseer", "foreman", "principal", "lead", "it", "ux", "sql", "help", "software",
            "devops", "network", "artificial", "cloud", "application", "retail", "store", "real", "cashier", "area",
            "direct", "outside", "b2b", "merchandising", "construction", "taper", "plumber", "vehicle", "carpenter",
            "electrician", "welder", "handyman", "boilermaker", "crane", "building", "pipefitter", "sheet", "iron",
            "mason", "roofer", "solar", "well", "proprietor", "owner", "founder", "managing", "c-level", "shareholders",
            "managers", "supervisors", "front-line", "accounting", "purchasing", "shipping", "virtual", "client",
            "operations", "scrum", "continuous", "credit", "benefits", "accountant", "accounts", "auditor", "budget",
            "finance", "economist", "payroll", "commercial", "mechanical", "civil", "electrical", "chemical", "drafter",
            "engineering", "geological", "biological", "mining", "nuclear", "petroleum", "plant", "production",
            "safety", "vp", "culture", "research", "data", "business", "financial", "biostatistician", "title",
            "market", "medical", "mentor", "tutor/online", "teaching", "substitute", "preschool", "test", "online",
            "professor", "graphic", "interior", "video", "playwright", "musician", "novelist/writer", "computer",
            "photographer", "camera", "sound", "motion", "actor", "music", "travel", "doctor", "caregiver", "cna",
            "physical", "pharmacist", "pharmacy", "massage", "dental", "orderly", "personal", "phlebotomist",
            "telework", "reiki", "housekeeper", "flight", "hotel", "bellhop", "cruise", "entertainment", "concierge",
            "group", "event", "porter", "spa", "wedding", "casino", "reservationist", "events", "meeting", "lodging",
            "valet", "waiter/waitress", "server", "chef", "fast", "barista", "line", "cafeteria", "restaurant", "wait",
            "political", "chemist", "conservation", "sociologist", "biologist", "geologist", "physicist", "astronomer",
            "atmospheric", "molecular", "call", "customer", "telemarketer", "telephone", "dispatcher", "mortgage",
            "mental", "addiction", "school", "speech", "guidance", "social", "life", "couples", "beautician", "hair",
            "nail", "cosmetologist", "salon", "makeup", "esthetician", "skin", "manicurist", "barber", "journalist",
            "copy", "editor/proofreader", "content", "speechwriter", "communications", "screenwriter", "technical",
            "columnist", "public", "proposal", "grant", "translator", "copywriter", "ghostwriter", "warehouse",
            "painter", "heavy", "welding", "landscaping", "mover", "animal", "veterinary", "farm", "zoologist",
            "delivery", "tow", "ups", "mail", "recyclables", "courier", "cab", "office", "hospital", "youth",
            "homeless", "meals", "habitat", "emergency", "red", "community", "women’s", "suicide", "sports", "church",
            "archivist", "actuary", "architect", "entrepreneur", "security", "mechanic", "recruiter", "mathematician",
            "locksmith", "management", "shelf", "caretaker", "library", "hvac", "attorney", "paralegal", "bank",
            "parking", "machinery", "manufacturing", "funeral", "yoga", "buyer", "clerk", "resources", "entry",
            "operating", "information", "technology", "leader", "developer", "programmer", "intelligence", "estate",
            "salesperson", "development", "inspector", "metal", "photovoltaic", "driller", "partner", "employees",
            "and", "master", "improvement", "authorizer", "payable/receivable", "services", "people", "robot",
            "ninjaneer", "overlord", "analyst", "researcher", "tutor", "teacher", "scorer", "esl", "designer", "editor",
            "animator", "engineer", "picture", "nurse", "practitioner", "therapist", "administrator", "laboratory",
            "therapy", "hygienist", "transcriptionist", "nurse/doctor", "agent", "front", "desk", "sales", "planner",
            "coordinator", "ship", "host", "receptionist", "of", "food", "cook", "staff", "person", "chain",
            "scientist", "center", "service", "survey", "for", "support", "the", "loan", "health", "pathologist",
            "coach", "stylist", "technician", "artist", "care", "creator", "relations", "strategist", "game", "critic",
            "media", "equipment", "breeder", "shelter", "walker", "dog", "control", "bus", "truck", "carrier",
            "collector", "kitchen", "relief", "cross", "hotline", "guard", "consultant", "stocker", "teller",
            "assembler", "golf", "desktop", "broker", "inside", "installer", "directors", "c-suite.", "receiving",
            "miscellaneous", "whisperer", "first", "ethical", "bean", "storytelling", "instructor", "film", "director",
            "tech", "assistant", "door", "associate", "manager", "attendant", "maintenance", "executive",
            "representative", "conductor", "trucks", "phone", "specialist", "processor", "counselor", "writer",
            "operator", "trainer", "officer", "board", "volunteer", "wheels", "humanity", "project", "jobs", "house",
            "professional", "ui", "cleaner", "stuff", "impressions", "hacking", "counting", "producer", "greeter", "or",
            "interpreter", "pet", "member", "driver", "builder", "worker", "taxis", "sitter"));

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
            // If the line has numbers or the @, & symbols, assume it's not a name
            int hasDigits = breakDocument[i].replaceAll("\\D", "").length();
            if (breakDocument[i].contains("@") || breakDocument[i].contains("&") || hasDigits > 0
                    || breakDocument[i].isEmpty()) {
                continue;
            }

            boolean foundInvalid = false;
            // Breaks the line into words. To lowercase to compare with lowercase hashset.
            // Removed period to cover abbreviations
            String tempLine = breakDocument[i].toLowerCase().replaceAll("\\.","");
            String[] breakLine = tempLine.split("\\s");
            for (int k = 0; k < breakLine.length; k++) {
                if (job_keywords.contains(breakLine[k])) {
                    foundInvalid = true;
                    break;
                }

            }

            // If it did not contain a job keyword, then assume it's a name.
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
                // If the number of digits is >= MIN_PHONE_NUM_LEN (set to 10), assume it's a
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
                    // If the word contains @ and a dot, assume it's an email
                    if (breakLine[k].contains("@") && breakLine[k].contains("@")) {
                        return breakLine[k];
                    }
                }
            }
        }
        return null;
    }

    /****************************************************************
     * FUNCTION: Creates an instance of the ContactInfo class and populates using
     * the helper methods in this class.
     * 
     * ARGUMENTS: document - the business card text as input
     * 
     * RETURNS: Returns a populated ContactInfo object
     ****************************************************************/
    public ContactInfo getContactInfo(String document) {
        ContactInfo newContact = new ContactInfo();
        newContact.setName(retrieveName(document));
        newContact.setPhoneNumber(retrievePhoneNumber(document));
        newContact.setEmailAddress(retrieveEmailAddress(document));

        return newContact;
    }
}
