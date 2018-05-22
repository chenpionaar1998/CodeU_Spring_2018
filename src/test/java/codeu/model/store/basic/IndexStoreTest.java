package codeu.model.store.basic;

import codeu.model.data.Message;
import codeu.model.data.Conversation;
import codeu.model.data.User;
import java.time.Instant;
import java.util.UUID;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
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
          true);
  private Set<Message> messageList = new HashSet<>();

  @Before
  public void setup () {
    indexStore = IndexStore.getTestInstance();
    indexStore.setHashTable(wordsHash);
    messageList.add(MESSAGE_ONE);
    indexStore.addMapping("This", MESSAGE_ONE);
    indexStore.addMapping("is", MESSAGE_ONE);
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
  public void testSearchWord () {
    // Testing for both "This" and "is" should both only return the set with one MESSAGE_ONE
    Assert.assertEquals(messageList, indexStore.searchWord("This"));
    Assert.assertEquals(messageList, indexStore.searchWord("is"));
    Assert.assertEquals(null, indexStore.searchWord("hello"));
    Assert.assertEquals(null, indexStore.searchWord("one."));
  }

  @Test
  public void testSplitAndHashMessage () {
    indexStore.splitAndHashMessage(MESSAGE_ONE);

    Assert.assertEquals(true, wordsHash.containsKey("This"));
    Assert.assertEquals(true, wordsHash.containsKey("is"));
    Assert.assertEquals(true, wordsHash.containsKey("test"));
    Assert.assertEquals(true, wordsHash.containsKey("message"));
    Assert.assertEquals(true, wordsHash.containsKey("one"));

    Assert.assertEquals(false, wordsHash.containsKey("this"));
    Assert.assertEquals(false, wordsHash.containsKey("one."));
  }

}
