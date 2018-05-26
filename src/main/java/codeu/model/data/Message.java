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
  private final List<codeu.model.data.Image> images;
  // private boolean init;
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
  public Message(UUID id, UUID conversation, UUID author, String content, Instant creation, boolean init) {
    this.id = id;
    this.conversation = conversation;
    this.author = author;
    this.creation = creation;
    this.images = parseImages(content, init);
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
    String contentWithPictures = content;

    for(codeu.model.data.Image image : images) {
      if (image.hasError()){
        contentWithPictures += "\n" + "[CLOUD_VISION_API_ERROR]" + image.getErrorMessage();
        contentWithPictures = contentWithPictures + "\n" + image.getHTML();
      }else {
        contentWithPictures = contentWithPictures + "\n" + image.getHTML();
        String descriptions = String.join(",",image.getDescription());
        contentWithPictures += "\n" + "Descriptions: " + descriptions;
      }
    }

    return contentWithPictures;
  }

  /** Finds picture links that end in jpg and add to the end of the message the
   * html rendered picture (link picture, opens to picture in another window)
   * with maximum picture width at 500 */
  private List<codeu.model.data.Image> parseImages(String message, boolean init) {
	List<Image> images = new ArrayList<codeu.model.data.Image>();
	if (init) {
	    String linkRegex = "http(s)?://(.)*(.jpg|.jpeg|.png)";
	    String [] words = message.split("\\s");
	    for(String word : words) {
	      if(word.matches(linkRegex)) {
	        codeu.model.data.Image newImage = codeu.model.data.Image.getImageFromUrl(word);
          newImage.callToAPI();
	        images.add(newImage);
	      }
	    }
	}
    return images;
  }

  /** Returns the creation time of this Message. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the List of the images connected to this message. */
  public List<Image> getImages() {
    return images;
  }
}
