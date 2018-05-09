// ConversationActivity
// Represents a Message sent in a conversation. Keeps track of the message sent,
// the conversation it was sent in, and the user who sent the message and the
// time the message was sent for displaying the activity in the activity feed.

package codeu.model.data;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import java.time.Instant;
import java.util.UUID;

public class ConversationActivity extends Activity {
 
  private String conversationTitle;
  private String messagePreview;

  private final static int messageMaxLen = 30;   //max length for message preview

  // initialize activity based on message and conversation title
  public ConversationActivity(Message newMessage, String conversationTitle) {
    super(newMessage.getAuthorId(), newMessage.getCreationTime());
    this.conversationTitle = conversationTitle;
    
    //get shortened message 
    if(newMessage.getContent().length() < messageMaxLen)
      this.messagePreview = newMessage.getContent();
    else 
      this.messagePreview = newMessage.getContent().substring(0, messageMaxLen) + "...";
  }
  
  // constructor for creating activity based on entity in persistent memory 
  public ConversationActivity(UUID actorId, Instant creationTime, String message, 
      String conversationTitle) {
    super(actorId, creationTime);
    this.messagePreview = message;    //should have correct length already
    this.conversationTitle = conversationTitle;
  }

  public String getConversationTitle() {
    return conversationTitle;
  }

  public String getMessagePreview() {
    return this.messagePreview;
  }

}
