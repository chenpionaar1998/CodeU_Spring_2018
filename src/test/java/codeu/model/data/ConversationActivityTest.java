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

import codeu.model.store.basic.UserStore;
import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class ConversationActivityTest {

  @Test
  public void testCreateNewActivity() {
    UUID actorId = UUID.randomUUID();
    String title = "Test_Title"; 
    String message = "message_text";  //less than preview length
    Instant creation = Instant.now();

    ConversationActivity activity = new ConversationActivity(actorId, creation, message, title);

    Assert.assertEquals(actorId, activity.getActorId()); 
    Assert.assertEquals(creation, activity.getCreationTime());
    Assert.assertEquals(title, ((ConversationActivity) activity).getConversationTitle());
    Assert.assertEquals(message, ((ConversationActivity) activity).getMessagePreview());
  }

  @Test
  public void testCreateLoadActivtiy() {
    Instant conversationCreation = Instant.now();
    UUID conversationId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    String title = "Test_Title"; 
    String content = "message_text";


    Conversation conversation = new Conversation(conversationId, actorId, title, conversationCreation); 
    Instant messageCreation = Instant.now();
    Message message = new Message(messageId, conversationId, actorId, content, messageCreation);
    Activity activity = new ConversationActivity(message, title);

    Assert.assertEquals(conversation.getOwnerId(), activity.getActorId());
    Assert.assertEquals(messageCreation, activity.getCreationTime());
    Assert.assertEquals(title, ((ConversationActivity)activity).getConversationTitle());
    Assert.assertEquals(content, ((ConversationActivity)activity).getMessagePreview());
  }

}
