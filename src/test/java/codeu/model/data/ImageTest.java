package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class ImageTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    UUID conversation = UUID.randomUUID();
    UUID author = UUID.randomUUID();
    String content = "test content";
    Instant creation = Instant.now();

    Message message = new Message(id, conversation, author, content, creation, true);

    Assert.assertEquals(id, message.getId());
    Assert.assertEquals(conversation, message.getConversationId());
    Assert.assertEquals(author, message.getAuthorId());
    Assert.assertEquals(content, message.getContent());
    Assert.assertEquals(creation, message.getCreationTime());
  }

}
