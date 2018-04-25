// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.store.basic;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import codeu.model.data.User;
import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.json.simple.JSONObject;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class UserStore {

  /** Singleton instance of UserStore. */
  private static UserStore instance;

  /**
   * Returns the singleton instance of UserStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static UserStore getInstance() {
    if (instance == null) {
      instance = new UserStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static UserStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new UserStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Users from and saving Users to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Users. */
  private List<User> users;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private UserStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    users = new ArrayList<>();
  }

  /** Load a set of randomly-generated Message objects. */
  public void loadTestData() {
    users.addAll(DefaultDataStore.getInstance().getAllUsers());
  }

  /**
   * Access the User object with the given name.
   *
   * @return null if username does not match any existing User.
   */
  public User getUser(String username) {
    // This approach will be pretty slow if we have many users.
    for (User user : users) {
      if (user.getName().equals(username)) {
        return user;
      }
    }
    return null;
  }

  /**
   * Access the User object with the given UUID.
   *
   * @return null if the UUID does not match any existing User.
   */
  public User getUser(UUID id) {
    for (User user : users) {
      if (user.getId().equals(id)) {
        return user;
      }
    }
    return null;
  }

  /** Add a new user to the current set of users known to the application. */
  public void addUser(User user) {
    users.add(user);
    persistentStorageAgent.writeThrough(user);
  }

  /** Return true if the given username is known to the application. */
  public boolean isUserRegistered(String username) {
    for (User user : users) {
      if (user.getName().equals(username)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets the List of Users stored by this UserStore. This should only be called once, when the data
   * is loaded from Datastore.
   */
  public void setUsers(List<User> users) {
    this.users = users;
  }

  /**
    * Returns the number of users currently recorded
    */
  public int getUserCount() {
    return users.size();
  }

  /**
    * Returns the users array
    */
  public List<User> getAllUsers(){
    return users;
  }

  /**
    * Returns the user with the most messages sent
    * Pre : the the users list should already have the messageCount calculated
    */
  public String getTopUser(){
    sortUserList();
    return users.get(0).getName();
  }

  /**
    * Iterate through all messages and get the count for the owner of ther messages
    * Should only be called in PersistentDataStore when the messageCount attr does not exist
    */
  public int getMessageCountForUser(UUID uuid){
    List<Message> messages = MessageStore.getInstance().getAllMessages();
    int messageCount = 0;
    for (Message message : messages) {
      UUID id = message.getAuthorId();
      if (id != null) {
        if (uuid == id) {
          messageCount++;
        }
      }
    }
    return messageCount;
  }

  /**
    * Sort the users list by using sort in the library
    */
  public void sortUserList(){
    Collections.sort(users, new Comparator<User>(){
      public int compare(User a, User b){
        return b.getMessageCount() - a.getMessageCount();
      }
    });
  }

  /**
    * make a JSON file that consists JSON objects {user, messageCount}
    * sample JSONFile :
    * [
    *   {messageCount : 20, name : userOne},
    *   {messageCount : 10, name : userTwo}
    * ]
    */
  public void writeJSON(){
    // get the users array sorted with the corresponding messageCount setup
    sortUserList();

    try {
      // check if the directory exists, if not, create it
      File path = new File("./api/stats");
      if (!path.isDirectory()){
        path.mkdirs();
      }
      // make the JSON file in the directory ./api/stats
      File file = new File("./api/stats/", "userData.json");
      FileWriter fileWriter = new FileWriter(file);
      int i = 1;  // fix for no counter in forEach loop, need to know when the loop ends to make it proper JSON file
      fileWriter.write("[");
      fileWriter.write("\n");
      for (User user : users) {
        JSONObject userObj = new JSONObject();
        userObj.put("messageCount", user.getMessageCount());
        userObj.put("name", user.getName());
        fileWriter.write(userObj.toJSONString());
        // adding "," between objects, don't add for the last object
        if (i < users.size()){
          fileWriter.write(",");
        }
        i++;
        fileWriter.write("\n");
      }
      fileWriter.write("]");
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
