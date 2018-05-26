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
  public  void setHashTable (Hashtable<String, Set<Message>> wordsHash, Hashtable<String, Set<String>> imageHash) {
    this.wordsHash = wordsHash;
    this.imageHash = imageHash;
  }

  /** the in-memory hashtable of IndexStore */
  private Hashtable<String, Set<Message>> wordsHash = new Hashtable<>();
  private Hashtable<String, Set<String>> imageHash = new Hashtable<>();
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
    * Splits the given discriptions and hash them into the hashtable for future use
    */
  public void splitAndHashImage(String url, Set<String> descriptions) {
    for (String description : descriptions) {
      addMapping(description, url);
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
    * Hashes a single description to the hashtable, if the description exists in the hashtable, add the url to the list. Otherwise, add the description to the hashtable.
    * This function should be called whenever a image is being added to ImageStore
    */
  public void addMapping (String description, String url) {
    if (imageHash.containsKey(description)) {
      imageHash.get(description).add(url);
    } else {
      Set<String> imageList = new HashSet<>();
      imageList.add(url);
      imageHash.put(description, imageList);
    }
  }

  /**
    * Search function for a single word, returns a Set of messages from the Hashtable if the word is in it, otherwise return null
    */
  public List<Message> searchMessage (String searchTarget) {
    if (searchTarget.contains("||")) {
      return searchMessageUnion(searchTarget);
    }else if (searchTarget.contains("&&")) {
      searchTarget = searchTarget.replaceAll(" ","");
      return searchMessageIntersection(searchTarget);
    } else if (searchTarget.contains(" ")) {
      searchTarget = searchTarget.replaceAll("\\s+", "&&");
      return searchMessageIntersection(searchTarget);
    }else if (wordsHash.containsKey(searchTarget)){
      List<Message> messageList = new ArrayList<Message>(wordsHash.get(searchTarget));
      return sortMessageList(messageList);
    }
    return null;
  }

  /**
    * Search function for description, returns a List of url from the hashtable if there is a restult, otherwise, return null
    */
  public List<String> searchImage (String descriptions){
    if (descriptions.contains("||")) {
      return searchImageUnion(descriptions);
    }else if (descriptions.contains("&&")) {
      descriptions.replaceAll(" ","");
      return searchImageIntersection(descriptions);
    }else if (descriptions.contains(" ")) {
      descriptions.replaceAll("\\s+","&&");
      return searchImageIntersection(descriptions);
    }else if (imageHash.comtainsKey(descriptions)) {
      List<String> imageList = new ArrayList<String>(imageHash.get(descriptions));
      return imageList;
    }
    return null;
  }

  /**
    * Pre: the given string contains "||"
    * returns the union of the elements in the given searchTarget
    */
  public List<Message> searchMessageUnion (String searchTarget) {
    String[] words = searchTarget.split("\\|\\||\\s+");

    if (words.length == 0) {
      return null;
    }
    Set<Message> hashSetResult = new HashSet<Message>();
    for (String word : words){
      if (searchMessage(word) != null) {
        Set<Message> hashSetTemp = new HashSet<Message>(searchMessage(word));
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
    * Pre: the given string contains "||"
    * returns the union of the elements in the given searchTarget
    */
  public List<String> searchImageUnion (String searchTarget) {
    String[] descriptions = searchTarget.split("\\|\\||\\s+");

    if (descriptions.length == 0) {
      return null;
    }
    Set<String> hashSetResult = new HashSet<String>();
    for (String description : descriptions){
      if (searchImage(description) != null) {
        Set<String> hashSetTemp = new HashSet<String>(searchImage(description));
        for (String url : hashSetTemp){
          hashSetResult.add(url);
        }
      }
    }
    List<String> unionResult = new ArrayList<String>(hashSetResult);
    return unionResult;
  }

  /**
    * Pre: the given string contains "&&"
    * returns the intersection of the elements in the given searchTarget
    */
  public List<Message> searchMessageIntersection (String searchTarget) {
    if (searchTarget.indexOf("&&") == 0) {
      searchTarget = searchTarget.substring(2);
    }
    String[] words = searchTarget.split("&&");
    if (words.length == 0 || searchMessage(words[0]) == null) {
      return null;
    }
    List<Message> intersectionResult = new ArrayList<Message>(searchMessage(words[0]));
    for (int i = 1 ; i < words.length ; i++) {
      String word = words[i];
      if (searchMessage(word) != null) {
        List<Message> intersectionTemp = new ArrayList<Message>(searchMessage(word));
        intersectionResult.retainAll(intersectionTemp);
      }else {
        // if any of the search targets have null result, the intersection should also be null
        if (word != ""){
          return null;
        }
      }
    }
    intersectionResult = sortMessageList(intersectionResult);
    return intersectionResult;

  }

  /**
    * Pre: the given string contains "&&"
    * returns the intersection of the elements in the given searchTarget
    */
  public List<String> searchImageIntersection (String searchTarget) {
    if (searchTarget.indexOf("&&") == 0) {
      searchTarget = searchTarget.substring(2);
    }
    String[] descriptions = searchTarget.split("&&");
    if (descriptions.length == 0 || searchImage(descriptions[0]) == null) {
      return null;
    }
    List<String> intersectionResult = new ArrayList<String>(searchImage(words[0]));
    for (int i = 1 ; i < descriptions.length ; i++) {
      String description = descriptions[i];
      if (searchImage(word) != null) {
        List<String> intersectionTemp = new ArrayList<String>(searchImage(descriptions));
        intersectionResult.retainAll(intersectionTemp);
      }else {
        // if any of the search targets have null result, the intersection should also be null
        if (word != ""){
          return null;
        }
      }
    }
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
