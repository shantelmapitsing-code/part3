package com.mycompany.message;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Message {
    private String recipientNumber;
    private String messageContent;

    // ==== MESSAGE CLASS VARIABLES ====
    static int totalMessagesSent = 0;
    static String allMessages = "";

    // Declare Arrays
    static final int MAX_MESSAGES = 100;
    static String[] sentMessages = new String[MAX_MESSAGES];
    static String[] disregardedMessages = new String[MAX_MESSAGES];
    static String[] storedMessages = new String[MAX_MESSAGES];
    static String[] messageHashes = new String[MAX_MESSAGES];
    static String[] messageIDs = new String[MAX_MESSAGES];
    static String[] recipientNumbers = new String[MAX_MESSAGES];
    static int messageCount = 0;
  
// == MAIN MESSAGE SYSTEM == 
    public static void main(String[] args) {
        Message msgHandler = new Message();
        Scanner input = new Scanner(System.in);
        loadTestData();
        System.out.println("Welcome to QuickChat.");
        int option = 0;

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

            // OPTION 1
            if (option == 1) {
                System.out.print("Enter recipient number: ");
                String recipient = input.nextLine();
                System.out.print("Enter message content: ");
                String content = input.nextLine();
                System.out.print("Enter flag [Sent/Stored/Disregard]: ");
                String flag = input.nextLine().trim();

                msgHandler.recipientNumber = recipient;
                msgHandler.messageContent = content;

                String messageID = "MSG" + (totalMessagesSent + 1);
                String messageHash = Integer.toHexString(messageID.hashCode());
                totalMessagesSent++;

                String jsonMessage = "{\n" +
                        " \"messageID\": \"" + messageID + "\",\n" +
                        " \"messageHash\": \"" + messageHash + "\",\n" +
                        " \"recipient\": \"" + recipient + "\",\n" +
                        " \"message\": \"" + content + "\",\n" +
                        " \"flag\": \"" + flag + "\"\n" +
                        "}\n";

                // Use MAX_MESSAGES constant
                if(messageCount < MAX_MESSAGES) {
                    messageIDs[messageCount] = messageID;
                    messageHashes[messageCount] = messageHash;
                    recipientNumbers[messageCount] = recipient;
                    storedMessages[messageCount] = jsonMessage;

                    if(flag.equalsIgnoreCase("Sent")) {
                        sentMessages[messageCount] = content;
                    } else if(flag.equalsIgnoreCase("Disregard")) {
                        disregardedMessages[messageCount] = content;
                    }
                    messageCount++;
                } else {
                    System.out.println("Storage full. Max " + MAX_MESSAGES + " messages reached.");
                }

                allMessages += jsonMessage;

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
            else if (option == 2) {
                System.out.println("Recently sent messages:");
                boolean found = false;
                for(int i = 0; i < messageCount; i++) {
                    if(sentMessages[i]!= null) {
                        System.out.println("To " + recipientNumbers[i] + ": " + sentMessages[i]);
                        found = true;
                    }
                }
                if(!found) System.out.println("No sent messages yet.");
            }
            else if (option == 3) {
                System.out.println("Goodbye!");
            }
            // OPTION 4
            else if (option == 4) {
                int subOption = 0;
                while(subOption!= 7) {
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

                    // a. Display sender and recipient
                    if(subOption == 1) {
                        System.out.println("\nAll stored messages:");
                        if(messageCount == 0) System.out.println("No messages stored yet.");
                        for(int i = 0; i < messageCount; i++) {
                            if(messageIDs[i]!= null) {
                                System.out.println("ID: " + messageIDs[i] + " | Recipient: " + recipientNumbers[i]);
                            }
                        }
                    }
                    else if(subOption == 2) {
                        String longest = "";
                        int index = -1;
                        for(int i = 0; i < messageCount; i++) {
                            if(storedMessages[i]!= null && storedMessages[i].length() > longest.length()) {
                                longest = storedMessages[i];
                                index = i;
                            }
                        }
                        if(index!= -1) System.out.println("Longest message JSON:\n" + longest);
                        else System.out.println("No messages stored yet.");
                    }
                    else if(subOption == 3) {
                        System.out.print("Enter Message ID to search: ");
                        String searchID = input.nextLine();
                        boolean found = false;
                        for(int i = 0; i < messageCount; i++) {
                            if(messageIDs[i]!= null && messageIDs[i].equals(searchID)) {
                                System.out.println("Found message:");
                                System.out.println("Recipient: " + recipientNumbers[i]);
                                System.out.println(storedMessages[i]);
                                found = true;
                                break;
                            }
                        }
                        if(!found) System.out.println("Message ID not found.");
                    }
                    else if(subOption == 4) {
                        System.out.print("Enter recipient number to search: ");
                        String searchRec = input.nextLine();
                        boolean found = false;
                        for(int i = 0; i < messageCount; i++) {
                            if(recipientNumbers[i]!= null && recipientNumbers[i].equals(searchRec)) {
                                System.out.println(storedMessages[i]);
                                found = true;
                            }
                        }
                        if(!found) System.out.println("No messages for that recipient.");
                    }
                    else if(subOption == 5) {
                        System.out.print("Enter Message Hash to delete: ");
                        String delHash = input.nextLine();
                        boolean deleted = false;
                        for(int i = 0; i < messageCount; i++) {
                            if(messageHashes[i]!= null && messageHashes[i].equals(delHash)) {
                                System.out.println("Message: " + messageIDs[i] + " successfully deleted.");
                                storedMessages[i] = null;
                                sentMessages[i] = null;
                                disregardedMessages[i] = null;
                                messageIDs[i] = null;
                                messageHashes[i] = null;
                                recipientNumbers[i] = null;
                                deleted = true;
                                break;
                            }
                        }
                        if(!deleted) System.out.println("Hash not found.");
                    }
                    // f. Display full report
                    else if(subOption == 6) {
                        System.out.println("\n--- Full Report ---");
                        if(messageCount == 0) System.out.println("No messages stored yet.");
                        for(int i = 0; i < messageCount; i++) {
                            if(messageIDs[i]!= null) {
                                System.out.println("Message ID: " + messageIDs[i]);
                                System.out.println("Message Hash: " + messageHashes[i]);
                                System.out.println("Recipient: " + recipientNumbers[i]);
                                System.out.println("Details: " + storedMessages[i]);
                                System.out.println("-------------------");
                            }
                        }
                    }
                    else if(subOption == 7) {
                        break;
                    }
                    else {
                        System.out.println("Invalid option.");
                    }
                }
            }
            else {
                System.out.println("Invalid choice.");
            }
        }
      
    }

    // Method to load test data from brief 
    public static void loadTestData() {
        addMessageTest("+27834557896", "Did you get the cake?", "Sent");
        addMessageTest("+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        addMessageTest("+27834484567", "Yohoooo, I am at your gate.", "Disregard");
        addMessageTest("0838884567", "It is dinner time!", "Sent");
        addMessageTest("+27838884567", "Ok, I am leaving without you.", "Stored");
    }

    public static void addMessageTest(String recipient, String content, String flag) {
        String messageID = "MSG" + (totalMessagesSent + 1);
        String messageHash = Integer.toHexString(messageID.hashCode());
        totalMessagesSent++;
        String jsonMessage = "{ \"messageID\": \"" + messageID + "\", \"messageHash\": \"" + messageHash + "\", \"recipient\": \"" + recipient + "\", \"message\": \"" + content + "\", \"flag\": \"" + flag + "\" }\n";

        // Use MAX_MESSAGES constant here too
        if(messageCount < MAX_MESSAGES) {
            messageIDs[messageCount] = messageID;
            messageHashes[messageCount] = messageHash;
            recipientNumbers[messageCount] = recipient;
            storedMessages[messageCount] = jsonMessage;

            if(flag.equalsIgnoreCase("Sent")) {
                sentMessages[messageCount] = content;
            } else if(flag.equalsIgnoreCase("Disregard")) {
                disregardedMessages[messageCount] = content;
            }
            messageCount++;
        
        
        }
        
    }
}