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

package codeu.model.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Class representing a message. Messages are sent by a User in a Conversation. */
public class Message {

  private final UUID id;
  private final UUID conversation;
  private final UUID author;
  private final String content;
  private final Instant creation;

  /**
   * Constructs a new Message.
   *
   * @param id the ID of this Message
   * @param conversation the ID of the Conversation this Message belongs to
   * @param author the ID of the User who sent this Message
   * @param content the text content of this Message
   * @param creation the creation time of this Message
   * @param parseImages whether or not links in the message should be found and
   *          added as loaded pictures at end. Based on whether new message or loaded.
   */
  public Message(UUID id, UUID conversation, UUID author, String content, Instant creation, 
                 boolean parseImages) {
    this.id = id;
    this.conversation = conversation;
    this.author = author;
    this.creation = creation;
    if(parseImages) 
        this.content = parseImages(content);
    else
        this.content = content;
  }

  /** Returns the ID of this Message. */
  public UUID getId() {
    return id;
  }

  /** Returns the ID of the Conversation this Message belongs to. */
  public UUID getConversationId() {
    return conversation;
  }

  /** Returns the ID of the User who sent this Message. */
  public UUID getAuthorId() {
    return author;
  }

  /** Returns the text content of this Message. */
  public String getContent() {
    return content;
  }

  /** Finds picture links that end in jpg and add to the end of the message the
   * html rendered picture (link picture, opens to picture in another window)
   * with maximum picture width at 500 */
  public String parseImages(String message) { 
    int startIndex = 0;
    int linkStartIndex;
    int spaceIndex;
    int newlineIndex;
    int endWordIndex;
    List<String> links = new ArrayList<String>(); 

    while (startIndex < message.length()) {
        linkStartIndex = message.indexOf("http", startIndex);
        if(linkStartIndex == -1)
            return message;   //indicated http not in string
 
        spaceIndex = message.indexOf(' ', linkStartIndex);
        newlineIndex = message.indexOf('\n', linkStartIndex);
        if(spaceIndex == -1)     //-1 if no space in the rest of the string
            spaceIndex = message.length();
        if(newlineIndex == -1)
            newlineIndex = message.length();
        endWordIndex = Math.min(newlineIndex, spaceIndex);
        System.out.println("start index is " + startIndex + " end is " + endWordIndex);

        if(message.substring(endWordIndex - 4, endWordIndex).equals(".jpg")) {
            links.add("\n<a href =" + message.substring(
                      linkStartIndex, endWordIndex) + "><img style=\"max-width:500px\" src=" + 
                      message.substring(linkStartIndex, endWordIndex) + "></a>");
        }
        startIndex = endWordIndex;
    }

    if(links.size() > 0)
        message = message + "\n";
   
    for(String link : links) {
        message = message + link; 
    }
    return message;
  }

  /** Returns the creation time of this Message. */
  public Instant getCreationTime() {
    return creation;
  }
}
