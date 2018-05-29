package codeu.model.store.basic;

import codeu.model.data.Message;
import codeu.model.data.Conversation;
import codeu.model.data.User;
import codeu.model.data.Image;
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
  public  void setHashTable (Hashtable<String, Set<Message>> wordsHash, Hashtable<String, Set<Image>> imageHash) {
    this.wordsHash = wordsHash;
    this.imageHash = imageHash;
  }

  /** the in-memory hashtable of IndexStore */
  private Hashtable<String, Set<Message>> wordsHash = new Hashtable<>();
  private Hashtable<String, Set<Image>> imageHash = new Hashtable<>();
  /**
    * Splits the given message into words and hash them into the hashtable for future use
    */
  public void splitAndHashMessage(Message message) {
    String[] words = message.getContent().split("-|,|!|\\?|\\.|\\s+|\\W");
    for (String word: words){
      addMapping(word, message);
    }
    List<Image> images = message.getImages();
    if (images.size() > 0) {
      for (Image image : images) {
        Set<String> descriptions = image.getDescription();
        if (!image.hasError()){
          splitAndHashImage(image,descriptions);
        }
      }
    }
  }

  /**
    * Splits the given discriptions and hash them into the hashtable for future use
    */
  public void splitAndHashImage(Image image, Set<String> descriptions) {
    for (String description : descriptions) {
      addMapping(description, image);
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
  public void addMapping (String description, Image image) {
    if (imageHash.containsKey(description)) {
      imageHash.get(description).add(image);
    } else {
      Set<Image> imageList = new HashSet<>();
      imageList.add(image);
      imageHash.put(description, imageList);
    }
  }

  /**
    * Search function for a single word, returns a Set of messages from the Hashtable if the word is in it, otherwise return null
    */
  public List<Message> search (String searchTarget) {
    if (searchTarget.contains("||")) {
      return searchUnion(searchTarget);
    }else if (searchTarget.contains("&&")) {
      searchTarget = searchTarget.replaceAll(" ","");
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
        if (word != ""){
          return null;
        }
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

}
