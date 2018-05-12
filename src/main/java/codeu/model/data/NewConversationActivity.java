// NewConversationActivity
// Represents a new conversation has been created. Keeps track of who created
// the conversation and when (super class) and the title of the conversation.

package codeu.model.data;

import codeu.model.store.basic.UserStore;
import java.util.UUID;
import java.time.Instant;

public class NewConversationActivity extends Activity {
 
  private String conversationTitle;

  // Initialize newConversationActivity for the given new conversation
  public NewConversationActivity(Conversation newConversation) {
    super(newConversation.getOwnerId(), newConversation.getCreationTime());
    this.conversationTitle = newConversation.getTitle();
  }
  
  // Initialize activity based on information in entity in persistent memory, 
  // basic activity information (actor id, activity creation time) and converstion
  public NewConversationActivity(UUID actorId, Instant creation, String conversationTitle) {
    super(actorId, creation);
    this.conversationTitle = conversationTitle;
  }

  public String getConversationTitle() {
    return conversationTitle;
  }

  public String getFeedDisplay() {
      String actorName = UserStore.getInstance().getUser(getActorId()).getName();
      return actorName + " created a new conversation <a href=\"/chat/" + conversationTitle 
          + "\">" + conversationTitle + "</a>!"; 
  }
}
