// ConversationActivity
// Represents a Message sent in a conversation. Keeps track of the message sent,
// the conversation it was sent in, and the user who sent the message and the
// time the message was sent for displaying the activity in the activity feed.

package codeu.model.data;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import java.util.UUID;

public class ConversationActivity extends Activity {
 
  private String conversationTitle;
  private String messageContent;

  private final int messageMaxLen = 30;   //max length for message preview

  // initialize activity based on message and conversation title
  public ConversationActivity(Message newMessage, String conversationTitle) {
    super(newMessage.getAuthorId(), newMessage.getCreationTime());
    this.conversationTitle = conversationTitle;
    this.messageContent = newMessage.getContent();
  }
    
  /** return shortened preview of message so that the displayed message for 
   * the activity is not too long */
  public String getMessagePreview() {
    String messagePreview;
    if(messageContent.length() < messageMaxLen)
      messagePreview = messageContent;
    else 
      messagePreview = messageContent.substring(0, messageMaxLen) + "...";
    
    return messagePreview;
  }

  public String getConversationTitle() {
    return conversationTitle;
  }

  public String getMessageContent() {
    return this.messageContent;
  }

}
