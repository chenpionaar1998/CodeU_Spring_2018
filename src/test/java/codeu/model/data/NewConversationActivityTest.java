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
import codeu.model.data.User;
import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class NewConversationActivityTest {

  /* Tests the NewConversationActivity constructor made with a conversation
   * reference, which is used when a new conversation is made */
  @Test
  public void testCreateNewActivity() {
    UUID id = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    String title = "Test_Title"; 
    Instant creation = Instant.now();

    Conversation conversation = new Conversation(id, actorId, title, creation); 
    Activity activity = new NewConversationActivity(actorId, creation, title);

    Assert.assertEquals(conversation.getOwnerId(), activity.getActorId());
    Assert.assertEquals(title, ((NewConversationActivity)activity).getConversationTitle());
    Assert.assertEquals(creation, activity.getCreationTime());
  }

  /* Tests the NewConversationActivity constructor made with the creator's user
   * UUID, creation time, and conversation title, which is used when loading an 
   * activity from memory */
  @Test
  public void testCreateLoadActivtiy() {
    UUID actorId = UUID.randomUUID();
    String title = "Test_Title"; 
    Instant creationConversation = Instant.now();
    Instant creation = Instant.now();

    Activity activity = new NewConversationActivity(actorId, creation, title);

    Assert.assertEquals(actorId, activity.getActorId()); 
    Assert.assertEquals(title, ((NewConversationActivity)activity).getConversationTitle());
    Assert.assertEquals(creation, activity.getCreationTime());
  }
}
