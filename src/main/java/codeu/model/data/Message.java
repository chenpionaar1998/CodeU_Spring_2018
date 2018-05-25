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
import codeu.model.data.*;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

/** Class representing a message. Messages are sent by a User in a Conversation. */
public class Message {

  private final UUID id;
  private final UUID conversation;
  private final UUID author;
  private final String content;
  private final Instant creation;
  private final List<Image> images;

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
    this.images = parseImages(content);
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

    if(images.size() > 0)
        contentWithPictures += '\n';

    for(Image image : images) { 
        contentWithPictures = contentWithPictures + "<a href=" + image.getUrl() + 
                              "><img style=\"max-width:500px\" src=" + image.getUrl() + "></a> ";
    }
    return contentWithPictures;
  }

  /** Finds picture links that end in jpg and add to the end of the message the
   * html rendered picture (link picture, opens to picture in another window)
   * with maximum picture width at 500 */
  public List<Image> parseImages(String message) { 
    List<Image> images = new ArrayList<Image>(); 
    String linkRegex = "http(s)?://(.)*(.jpg|.jpeg|.png)";
    String [] words = message.split("\\s");

    for(String word : words) {
        if(word.matches(linkRegex)) 
            images.add(Image.getImageFromUrl(word));
    }

    //new line separating message and loaded pictures
/*    if(images.size() > 0)
        message = message + "\n";
   
   
    for(String link : images) {
        message = message + "<a href=" + link + "><img style=\"max-width:500px\" src=" +
                  link + "></a> "; 
    }*/
    return images;
  }

  /** Returns the creation time of this Message. */
  public Instant getCreationTime() {
    return creation;
  }
}
