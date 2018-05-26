package codeu.model.store.basic;

import codeu.model.data.Message;
import codeu.model.data.Conversation;
import codeu.model.data.User;
import java.time.Instant;
import java.util.UUID;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexStoreTest {

  private IndexStore indexStore;
  private Hashtable<String, Set<Message>> wordsHash = new Hashtable<>();

  private final UUID CONVERSATION_ID_ONE = UUID.randomUUID();
  private final Message MESSAGE_ONE =
        new Message(
            UUID.randomUUID(),
            CONVERSATION_ID_ONE,
            UUID.randomUUID(),
            "This is test message one.",
            Instant.ofEpochMilli(1000),
            false);
    private final Message MESSAGE_TWO =
        new Message(
            UUID.randomUUID(),
            CONVERSATION_ID_ONE,
            UUID.randomUUID(),
            "This is test message two.",
            Instant.ofEpochMilli(2000),
            false);
    private final Message MESSAGE_THREE =
        new Message(
            UUID.randomUUID(),
            CONVERSATION_ID_ONE,
            UUID.randomUUID(),
            "This is test message three.",
            Instant.ofEpochMilli(3000),
            false);
    private final Message MESSAGE_FOUR =
        new Message(
            UUID.randomUUID(),
            CONVERSATION_ID_ONE,
            UUID.randomUUID(),
            "is Random test message one four.",
            Instant.ofEpochMilli(4000),
            false);
  private Set<Message> messageList = new HashSet<>();
  private List<Message> inOrderMessageList = new ArrayList<>();

  @Before
  public void setup () {
    indexStore = IndexStore.getTestInstance();
    indexStore.setHashTable(wordsHash,null);
    messageList.add(MESSAGE_ONE);
    inOrderMessageList.add(MESSAGE_ONE);
    inOrderMessageList.add(MESSAGE_TWO);
    inOrderMessageList.add(MESSAGE_THREE);
    inOrderMessageList.add(MESSAGE_FOUR);
    indexStore.splitAndHashMessage(MESSAGE_FOUR);
    indexStore.splitAndHashMessage(MESSAGE_THREE);
    indexStore.splitAndHashMessage(MESSAGE_TWO);
    indexStore.splitAndHashMessage(MESSAGE_ONE);
  }

  @Test
  public void testAddMapping () {
    Assert.assertEquals(true, wordsHash.containsKey("This"));
    Assert.assertEquals(true, wordsHash.containsKey("is"));

    // Testing for false including words not in string, caps, and symbols
    Assert.assertEquals(false, wordsHash.containsKey("this"));
    Assert.assertEquals(false, wordsHash.containsKey("one."));
    Assert.assertEquals(false, wordsHash.containsKey("hello"));
  }

  @Test
  public void testSearch () {
    // Testing for both "message" and "test" should both only return the set with one MESSAGE_ONE
    Assert.assertEquals(inOrderMessageList, indexStore.searchMessage("message"));
    Assert.assertEquals(inOrderMessageList, indexStore.searchMessage("test"));

    // Testing for String with no && but contains 2 words should be parsed into A&&B
    List<Message> expectedList = new ArrayList<>();
    expectedList.add(MESSAGE_FOUR);
    Assert.assertEquals(expectedList, indexStore.searchMessage("one four"));
    Assert.assertEquals(null, indexStore.searchMessage("hello"));
    Assert.assertEquals(null, indexStore.searchMessage("one."));
  }

  @Test
  public void testSplitAndHashMessage () {
    Assert.assertEquals(true, wordsHash.containsKey("This"));
    Assert.assertEquals(true, wordsHash.containsKey("is"));
    Assert.assertEquals(true, wordsHash.containsKey("test"));
    Assert.assertEquals(true, wordsHash.containsKey("message"));
    Assert.assertEquals(true, wordsHash.containsKey("one"));

    Assert.assertEquals(false, wordsHash.containsKey("this"));
    Assert.assertEquals(false, wordsHash.containsKey("one."));
  }

  @Test
  public void testsortMessageList () {
    Assert.assertEquals(inOrderMessageList, indexStore.searchMessage("message"));
  }

  @Test
  public void testSearchMessageIntersection () {
    List<Message> expectedList = new ArrayList<>();
    expectedList.add(MESSAGE_ONE);
    expectedList.add(MESSAGE_FOUR);
    Assert.assertEquals(expectedList, indexStore.searchMessage("test&&one&&message"));
    Assert.assertEquals(expectedList, indexStore.searchMessage("test && one && message"));
    Assert.assertEquals(expectedList, indexStore.searchMessage("test&&one&&"));
    Assert.assertEquals(expectedList, indexStore.searchMessage("&&one&&test"));
    Assert.assertEquals(expectedList, indexStore.searchMessage("test one"));
    expectedList.remove(MESSAGE_ONE);
    expectedList.remove(MESSAGE_FOUR);
    Assert.assertEquals(null, indexStore.searchMessage("five && six"));
    Assert.assertEquals(null, indexStore.searchMessage(" && "));
  }

  @Test
  public void testSearchMessageUnion () {
    List<Message> expectedList = new ArrayList<>();
    expectedList.add(MESSAGE_TWO);
    expectedList.add(MESSAGE_THREE);
    expectedList.add(MESSAGE_FOUR);
    Assert.assertEquals(expectedList, indexStore.searchMessage("two||three||four"));
    expectedList.remove(MESSAGE_THREE);
    Assert.assertEquals(expectedList, indexStore.searchMessage("two||four||"));
    expectedList.remove(MESSAGE_TWO);
    expectedList.remove(MESSAGE_FOUR);
    expectedList.add(MESSAGE_THREE);
    expectedList.add(MESSAGE_FOUR);
    Assert.assertEquals(expectedList, indexStore.searchMessage("||three||four"));
    Assert.assertEquals(expectedList, indexStore.searchMessage("three || four"));
    Assert.assertEquals(expectedList, indexStore.searchMessage("three || || four"));
    expectedList.remove(MESSAGE_THREE);
    expectedList.remove(MESSAGE_FOUR);
    Assert.assertEquals(expectedList, indexStore.searchMessage("five || six"));
    Assert.assertEquals(null, indexStore.searchMessage(" || "));
  }
}
