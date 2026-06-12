package com.mycompany.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @BeforeEach
    public void reset() {
        Message.messageCount = 0;
        Message.totalMessagesSent = 0;
        Message.allMessages = "";
        for(int i = 0; i < Message.MAX_MESSAGES; i++) {
            Message.messageIDs[i] = null;
            Message.messageHashes[i] = null;
            Message.recipientNumbers[i] = null;
            Message.storedMessages[i] = null;
            Message.sentMessages[i] = null;
            Message.disregardedMessages[i] = null;
        }
    }

    @Test
    public void testHashGeneration() {
        String messageID = "MSG1";
        String hash1 = Integer.toHexString(messageID.hashCode());
        String hash2 = Integer.toHexString(messageID.hashCode());
        assertEquals(hash1, hash2);
        assertNotNull(hash1);
        assertFalse(hash1.isEmpty());
    }

    @Test
    public void testAddMessageTest() {
        Message.addMessageTest("+27838968976", "Hello", "Sent");
        assertEquals(1, Message.messageCount);
        assertEquals("+27838968976", Message.recipientNumbers[0]);
        assertEquals("Hello", Message.sentMessages[0]);
    }

    @Test
    public void testLoadTestData() {
        Message.loadTestData();
        assertEquals(5, Message.messageCount);
        assertEquals("+27834557896", Message.recipientNumbers[0]);
        assertEquals("Did you get the cake?", Message.sentMessages[0]);
    }

    @Test
    public void testRecipientNumberFormat() {
        String valid = "+27838968976";
        String invalid = "0838968976";
        assertTrue(valid.matches("\\+27[0-9]{9}"));
        assertFalse(invalid.matches("\\+27[0-9]{9}"));
    }
}