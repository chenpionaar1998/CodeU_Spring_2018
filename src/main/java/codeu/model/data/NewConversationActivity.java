// NewConversationActivity
// Represents a new conversation has been created. Keeps track of who created
// the conversation and when (super class) and the title of the conversation.

package codeu.model.data;

public class NewConversationActivity extends Activity {
 
  private String conversationTitle;

  // Initialize newConversationActivity for the given new conversation
  public NewConversationActivity(Conversation newConversation) {
    super(newConversation.getOwnerId(), newConversation.getCreationTime());
    this.conversationTitle = newConversation.getTitle();
  }
  
  public String getConversationTitle() {
    return conversationTitle;
  }
}
