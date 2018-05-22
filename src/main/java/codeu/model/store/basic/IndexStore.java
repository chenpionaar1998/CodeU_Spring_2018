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
    String[] words = searchTarget.split("\\|\\|");

    if (words.length > 2) {
      return null;
    }else if (searchTarget.indexOf("|") == 0) {
      return search(words[1]);
    }else if (searchTarget.indexOf("|") == (searchTarget.length()-2)) {
      return search(words[0]);
    }else {
      Set<Message> hashSetOne = new HashSet<Message>(search(words[0]));
      Set<Message> hashSetTwo = new HashSet<Message>(search(words[1]));
      for (Message message : hashSetTwo){
        hashSetOne.add(message);
      }
      List<Message> result = new ArrayList<Message>(hashSetOne);
      result = sortMessageList(result);
      return result;
    }
  }

  /**
    * Pre: the given string contains "&&"
    * returns the intersection of the elements in the given searchTarget
    */
  public List<Message> searchIntersection (String searchTarget) {
    String[] words = searchTarget.split("&&");
    List<Message> wordOneResult = new ArrayList<>();
    List<Message> wordTwoResult = new ArrayList<>();
    if (words.length != 2 || searchTarget.indexOf("&") == 0) {
      return null;
    }else {
      wordOneResult = search(words[0]);
      wordTwoResult = search(words[1]);
      Set<Message> hashSet = new HashSet<Message>(wordOneResult);
      List<Message> result = new ArrayList<>();
      for (Message message : wordTwoResult) {
        if (hashSet.contains(message)){
          result.add(message);
        }
      }
      result = sortMessageList(result);
      return result;
    }
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
