// Copyright 2017 Google Inc.

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

package codeu.model.data;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String password;
  private final Instant creation;
  private static final int NUMBER_OF_MESSAGES = 15;
  private final Queue<Message> messages;
  private String aboutUser;
  private int messageCount;
  private final boolean admin;
  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param password the password of this User
   * @param creation the creation time of this User
   * @param messageCount the number of messages sent by the User
   * @param admin the boolean value for is admin
   */
  public User(UUID id, String name, String password, Instant creation, int messageCount, boolean admin) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.creation = creation;
    this.messageCount = messageCount;
    this.admin = admin;
    this.messages = new LinkedList<Message>();
  }
  
  // adds about me information for the user 
  public void setAbout(String about) {
	  this.aboutUser = about;
  }
  
  // returns the current about me information
  public String getAbout() {
	  return aboutUser;
  }
  
  public void addMessage(Message newMessage) {
	  if (messages.size() == NUMBER_OF_MESSAGES) {
		  messages.remove();
	  }
	  messages.add(newMessage);
  }
  
  public Queue<Message> getMessages() {
	  return messages;
  }

  /**
    * Consturctor for old User
    */
  public User(UUID id, String name, String password, Instant creation) {
    this(id,name,password,creation,0,false);
  }

  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }

  /** Ruturns the password of this User. */
  public String getPassword(){
    return password;
  }

  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the number of messages sent by the User. */
  public int getMessageCount() {
    return messageCount;
  }

  /** Returns the boolean value of admin by the User. */
  public boolean isAdmin() {
    return admin;
  }

  /** Increment messageCount for the particularUser*/
  public void incrementMessageCount(){
    this.messageCount++;
  }
}