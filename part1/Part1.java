package com.mycompany.part1;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Part1 {
    // === LOGIN CLASS VARIABLES ===
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    // ==== MESSAGE CLASS VARIABLES s ===
    private static int totalMessagesSent = 0;
    private static String loggedInUser = "";
    private static int messageCount = 0;

    // Declare Arrays
    final int MAX_MESSAGES = 100;
    String[] messageIDs = new String[MAX_MESSAGES];
    String[] messageHashes = new String[MAX_MESSAGES];
    String[] recipientNumbers = new String[MAX_MESSAGES];
    String[] senders = new String[MAX_MESSAGES];
    String[] storedMessages = new String[MAX_MESSAGES];
    String[] sentMessages = new String[MAX_MESSAGES];
    String[] disregardedMessages = new String[MAX_MESSAGES];

    // == CONSTRUCTOR ==
    public Part1() {}
    public Part1(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // == LOGIN CLASS METHODS ==
    public boolean checkUserName() {
        return username!= null && username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) return false;
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean loginUser(String userName, String password) {
        return this.username.equals(userName) && this.password.equals(password);
    }

    public String getUserLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + " " + lastName;
        } else {
            return "Username or password incorrect";
        }
    }

    // == cell number validation ==
    public static boolean isValidCellNumber(String cellNumber) {
        return cellNumber!= null && cellNumber.matches("\\+27[0-9]{9}");
    }

    // Check if message contains "part3"
    public static boolean containsPart3(String messageContent) {
        if (messageContent == null) return false;
        return messageContent.toLowerCase().contains("part3");
    }

    // == MESSAGE SYSTEM ==
    public static void startQuickChat(Scanner input) {
        System.out.println("Welcome to QuickChat.");
        loadTestData();
        int option = 0;
        Part1 obj = new Part1();
        while (option!= 3) {
            System.out.println("1) Send Message");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");
            try {
                option = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            //  Send Message with Flag
            if (option == 1) {
                System.out.print("Enter recipient number: ");
                String recipient = input.nextLine();
                System.out.print("Enter message content: ");
                String content = input.nextLine();
                if (containsPart3(content)) {
                    System.out.println("Note: Message contains 'part3'");
                }
                System.out.print("Enter flag [Sent/Stored/Disregard]: ");
                String flag = input.nextLine().trim();
                String messageID = "MSG" + (totalMessagesSent + 1);
                String messageHash = Integer.toHexString(messageID.hashCode());
                totalMessagesSent++;
                String jsonMessage = "{\n" +
                        " \"messageID\": \"" + messageID + "\",\n" +
                        " \"messageHash\": \"" + messageHash + "\",\n" +
                        " \"sender\": \"" + loggedInUser + "\",\n" +
                        " \"recipient\": \"" + recipient + "\",\n" +
                        " \"message\": \"" + content + "\",\n" +
                        " \"flag\": \"" + flag + "\"\n" +
                        "}\n";

                if (messageCount < obj.MAX_MESSAGES) {
                    obj.messageIDs[messageCount] = messageID;
                    obj.messageHashes[messageCount] = messageHash;
                    obj.recipientNumbers[messageCount] = recipient;
                    obj.senders[messageCount] = loggedInUser;
                    obj.storedMessages[messageCount] = jsonMessage;

                    if (flag.equalsIgnoreCase("Sent")) {
                        obj.sentMessages[messageCount] = content;
                    } else if (flag.equalsIgnoreCase("Disregard")) {
                        obj.disregardedMessages[messageCount] = content;
                    }
                    messageCount++;
                } else {
                    System.out.println("Storage full. Max " + obj.MAX_MESSAGES + " messages reached.");
                }

                try {
                    FileWriter writer = new FileWriter("messages.json", true);
                    writer.write(jsonMessage);
                    writer.close();
                    System.out.println("Message stored in JSON file successfully.");
                } catch (IOException e) {
                    System.out.println("Error saving message.");
                }
                System.out.println("Message processed with flag: " + flag);
            }
            // OPTION 2
            else if (option == 2) {
                System.out.println("Recently sent messages:");
                boolean found = false;
                for (int i = 0; i < messageCount; i++) {
                    if (obj.sentMessages[i]!= null) {
                        System.out.println("To " + obj.recipientNumbers[i] + ": " + obj.sentMessages[i]);
                        found = true;
                    }
                }
                if (!found) System.out.println("No sent messages yet.");
            }
            // OPTION 3
            else if (option == 3) {
                System.out.println("Goodbye!");
            }
            // OPTION 4 - Stored Messages Menu
            else if (option == 4) {
                int subOption = 0;
                while (subOption!= 7) {
                    System.out.println("\n--- Stored Messages ---");
                    System.out.println("1) Display sender and recipient of all stored messages");
                    System.out.println("2) Display the longest stored message");
                    System.out.println("3) Search for a message ID and display recipient and message");
                    System.out.println("4) Search all messages sent or stored by recipient");
                    System.out.println("5) Delete message using message hash");
                    System.out.println("6) Display report with full details of all stored messages");
                    System.out.println("7) Back to main menu");
                    System.out.print("Choose an option: ");
                    try {
                        subOption = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                        continue;
                    }

                    if (subOption == 1) {
                        System.out.println("\nAll stored messages:");
                        if (messageCount == 0) System.out.println("No messages stored yet.");
                        for (int i = 0; i < messageCount; i++) {
                            if (obj.messageIDs[i]!= null) {
                                System.out.println("ID: " + obj.messageIDs[i] + " | Sender: " + obj.senders[i] + " | Recipient: " + obj.recipientNumbers[i]);
                            }
                        }
                    } else if (subOption == 2) {
                        String longest = "";
                        int index = -1;
                        for (int i = 0; i < messageCount; i++) {
                            if (obj.storedMessages[i]!= null && obj.storedMessages[i].length() > longest.length()) {
                                longest = obj.storedMessages[i];
                                index = i;
                            }
                        }
                        if (index!= -1) System.out.println("Longest message JSON:\n" + longest);
                        else System.out.println("No messages stored yet.");
                    } else if (subOption == 3) {
                        System.out.print("Enter Message ID to search: ");
                        String searchID = input.nextLine();
                        boolean found = false;
                        for (int i = 0; i < messageCount; i++) {
                            if (obj.messageIDs[i]!= null && obj.messageIDs[i].equals(searchID)) {
                                System.out.println("Found message:");
                                System.out.println("Recipient: " + obj.recipientNumbers[i]);
                                System.out.println(obj.storedMessages[i]);
                                found = true;
                                break;
                            }
                        }
                        if (!found) System.out.println("Message ID not found.");
                    } else if (subOption == 4) {
                        System.out.print("Enter recipient number to search: ");
                        String searchRec = input.nextLine();
                        boolean found = false;
                        for (int i = 0; i < messageCount; i++) {
                            if (obj.recipientNumbers[i]!= null && obj.recipientNumbers[i].equals(searchRec)) {
                                System.out.println(obj.storedMessages[i]);
                                found = true;
                            }
                        }
                        if (!found) System.out.println("No messages for that recipient.");
                    } else if (subOption == 5) {
                        System.out.print("Enter Message Hash to delete: ");
                        String delHash = input.nextLine();
                        boolean deleted = false;
                        for (int i = 0; i < messageCount; i++) {
                            if (obj.messageHashes[i]!= null && obj.messageHashes[i].equals(delHash)) {
                                System.out.println("Message: " + obj.messageIDs[i] + " successfully deleted.");
                                obj.storedMessages[i] = null;
                                obj.sentMessages[i] = null;
                                obj.disregardedMessages[i] = null;
                                obj.messageIDs[i] = null;
                                obj.messageHashes[i] = null;
                                obj.recipientNumbers[i] = null;
                                obj.senders[i] = null;
                                deleted = true;
                                break;
                            }
                        }
                        if (!deleted) System.out.println("Hash not found.");
                    } else if (subOption == 6) {
                        System.out.println("\n--- Full Report ---");
                        if (messageCount == 0) System.out.println("No messages stored yet.");
                        for (int i = 0; i < messageCount; i++) {
                            if (obj.messageIDs[i]!= null) {
                                System.out.println("Message ID: " + obj.messageIDs[i]);
                                System.out.println("Message Hash: " + obj.messageHashes[i]);
                                System.out.println("Sender: " + obj.senders[i]);
                                System.out.println("Recipient: " + obj.recipientNumbers[i]);
                                System.out.println("Details: " + obj.storedMessages[i]);
                                System.out.println("------------------------");
                            }
                        }
                    } else if (subOption == 7) {
                        break;
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    // Test data loader
    public static void loadTestData() {
        addMessageTest("user_test", "+27834557896", "Did you get the cake?", "Sent");
        addMessageTest("user_test", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        addMessageTest("user_test", "+27834484567", "Yohoooo, I am at your gate.", "Disregard");
        addMessageTest("user_test", "0838884567", "It is dinner time!", "Sent");
        addMessageTest("user_test", "+27838884567", "Ok, I am leaving without you.", "Stored");
    }

    public static void addMessageTest(String sender, String recipient, String content, String flag) {
        Part1 obj = new Part1();
        String messageID = "MSG" + (totalMessagesSent + 1);
        String messageHash = Integer.toHexString(messageID.hashCode());
        totalMessagesSent++;
        String jsonMessage = "{ \"messageID\": \"" + messageID + "\", \"messageHash\": \"" + messageHash + "\", \"sender\": \"" + sender + "\", \"recipient\": \"" + recipient + "\", \"message\": \"" + content + "\", \"flag\": \"" + flag + "\" }\n";

        if (messageCount < obj.MAX_MESSAGES) {
            obj.messageIDs[messageCount] = messageID;
            obj.messageHashes[messageCount] = messageHash;
            obj.recipientNumbers[messageCount] = recipient;
            obj.senders[messageCount] = sender;
            obj.storedMessages[messageCount] = jsonMessage;
            if (flag.equalsIgnoreCase("Sent")) {
                obj.sentMessages[messageCount] = content;
            } else if (flag.equalsIgnoreCase("Disregard")) {
                obj.disregardedMessages[messageCount] = content;
            }
            messageCount++;
        }
    }

    // === MAIN METHOD ===
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("\n--- REGISTRATION ---");
        System.out.print("Enter first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter last name: ");
        String lastName = input.nextLine();
        System.out.print("Enter username (must contain '_' and max 5 characters): ");
        String username = input.nextLine();
        Part1 tempLogin = new Part1(username, "", firstName, lastName);
        while (!tempLogin.checkUserName()) {
            System.out.println("Username is not correctly formatted.");
            System.out.print("Enter username again: ");
            username = input.nextLine();
            tempLogin = new Part1(username, "", firstName, lastName);
        }
        System.out.println("Username successfully captured!");
        System.out.print("Enter password (min 8 chars, 1 capital, 1 number, 1 special char): ");
        String password = input.nextLine();
        tempLogin = new Part1(username, password, firstName, lastName);
        while (!tempLogin.checkPasswordComplexity()) {
            System.out.println("Password is not correctly formatted.");
            System.out.print("Enter password again: ");
            password = input.nextLine();
            tempLogin = new Part1(username, password, firstName, lastName);
        }
        System.out.println("Password successfully captured!");
        System.out.print("Enter cell phone number (e.g., +27838968976): ");
        String cellNumber = input.nextLine();
        while (!isValidCellNumber(cellNumber)) {
            System.out.println("Cell phone number is incorrectly formatted.");
            System.out.print("Enter again (+27XXXXXXXXX): ");
            cellNumber = input.nextLine();
        }
        System.out.println("Cell number successfully captured!");
        Part1 user = new Part1(username, password, firstName, lastName);
        System.out.println("\n REGISTRATION COMPLETE ");
        System.out.println("\nLOGIN ");
        System.out.print("Enter username: ");
        String loginUsername = input.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = input.nextLine();
        if (user.loginUser(loginUsername, loginPassword)) {
            System.out.println(user.getUserLoginStatus(true));
            loggedInUser = loginUsername;
            startQuickChat(input);
        } else {
            System.out.println(user.getUserLoginStatus(false));
        }
        input.close();
    }
}