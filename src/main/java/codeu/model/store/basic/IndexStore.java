package codeu.model.store.basic;

import codeu.model.data.Message;
import codeu.model.data.Conversation;
import codeu.model.data.User;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class IndexStore {

  /** Singleton instance of IndexStore. */
  private static IndexStore instance;

  /**
    * Returns a singleton instance of IndexStore that should be shared between all servlet classes.
    */
  public static IndexStore getInstance () {
    if (instance == null) {
      instance = new IndexStore();
    }
    return instance;
  }

  /**
    * Instance getter function for testing.
    */
  public static IndexStore getTestInstance () {
    return new IndexStore();
  }

  /**
    * Sets the hashtable of indices stored by IndexStore
    */
  public  void setHashTable (Hashtable<String, Set<Message>> wordsHash) {
    this.wordsHash = wordsHash;
  }

  /** the in-memory hashtable of IndexStore */
  private Hashtable<String, Set<Message>> wordsHash = new Hashtable<>();

  /**
    * Splits the given message into words and hash them into the hashtable for future use
    */
  public void splitAndHashMessage(Message message) {
    String[] words = message.getContent().split("-|,|!|\\?|\\.|\\s+");
    for (String word: words){
      addMapping(word, message);
    }
  }

  /**
    * Hashes a single word to the hashtable, if the word exists in the hashtable, add the message to the list. Otherwise, add the word to the hashtable.
    * This function should be called whenever a message is being added to MessageStore
    */
  public void addMapping (String word, Message message) {
    if (wordsHash.containsKey(word)) {
      wordsHash.get(word).add(message);
    } else {
      Set<Message> messageList = new HashSet<>();
      messageList.add(message);
      wordsHash.put(word, messageList);
    }
  }

  /**
    * Search function for a single word, returns a Set of messages from the Hashtable if the word is in it, otherwise return null
    */
  public Set<Message> searchWord (String word) {
    if (wordsHash.containsKey(word)) {
      return wordsHash.get(word);
    }
    return null;
  }

  /**
    * User search with username calls the function in UserStore and returns the User if found
    */
  public User searchUser (String username) {
    User user = UserStore.getInstance().getUser(username);
    return user;
  }

  /**
    * Conversation search with conversation title calls the function in ConversationStore and returns the Conversation if found
    */
  public Conversation searchConversation (String title) {
    Conversation conversation = ConversationStore.getInstance().getConversationWithTitle(title);
    return conversation;
  }
}
