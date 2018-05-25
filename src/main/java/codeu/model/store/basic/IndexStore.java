package codeu.model.store.basic;

import codeu.model.data.Message;
import codeu.model.data.Conversation;
import codeu.model.data.User;
import java.time.Instant;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    String[] words = message.getContent().split("-|,|!|\\?|\\.|\\s+|\\W");
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
  public List<Message> search (String searchTarget) {
    if (searchTarget.contains("||")) {
      return searchUnion(searchTarget);
    }else if (searchTarget.contains("&&")) {
      searchTarget = searchTarget.replaceAll(" ","&&");
      return searchIntersection(searchTarget);
    } else if (searchTarget.contains(" ")) {
      searchTarget = searchTarget.replaceAll("\\s+", "&&");
      return searchIntersection(searchTarget);
    }else if (wordsHash.containsKey(searchTarget)){
      List<Message> messageList = new ArrayList<Message>(wordsHash.get(searchTarget));
      return sortMessageList(messageList);
    }
    return null;
  }

  /**
    * Pre: the given string contains "||"
    * returns the union of the elements in the given searchTarget
    */
  public List<Message> searchUnion (String searchTarget) {
    String[] words = searchTarget.split("\\|\\||\\s+");

    if (words.length == 0) {
      return null;
    }
    Set<Message> hashSetResult = new HashSet<Message>();
    for (String word : words){
      if (search(word) != null) {
        Set<Message> hashSetTemp = new HashSet<Message>(search(word));
        for (Message message : hashSetTemp){
          hashSetResult.add(message);
        }
      }
    }
    List<Message> unionResult = new ArrayList<Message>(hashSetResult);
    unionResult = sortMessageList(unionResult);
    return unionResult;
  }

  /**
    * Pre: the given string contains "&&"
    * returns the intersection of the elements in the given searchTarget
    */
  public List<Message> searchIntersection (String searchTarget) {
    if (searchTarget.indexOf("&&") == 0) {
      searchTarget = searchTarget.substring(2);
    }
    String[] words = searchTarget.split("&&");
    if (words.length == 0 || search(words[0]) == null) {
      return null;
    }
    List<Message> intersectionResult = new ArrayList<Message>(search(words[0]));
    for (int i = 1 ; i < words.length ; i++) {
      String word = words[i];
      if (search(word) != null) {
        List<Message> intersectionTemp = new ArrayList<Message>(search(word));
        intersectionResult.retainAll(intersectionTemp);
      }else {
        // if any of the search targets have null result, the intersection should also be null
        return null;
      }
    }
    intersectionResult = sortMessageList(intersectionResult);
    return intersectionResult;

  }

  /**
    * Sorts the Set<Message> so that the output is in order with the creation time of the message
    */
  public List<Message> sortMessageList (List<Message> messageSet) {
    Collections.sort(messageSet, new Comparator<Message> () {
      public int compare (Message a, Message b){
        Instant creationTimeA = a.getCreationTime();
        Instant creationTimeB = b.getCreationTime();
        return (creationTimeA.compareTo(creationTimeB));
      }
    });
    return messageSet;
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
