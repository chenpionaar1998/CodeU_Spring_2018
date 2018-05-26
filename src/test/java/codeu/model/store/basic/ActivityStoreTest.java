package codeu.model.store.basic;

import codeu.model.data.Activity;
import codeu.model.data.Conversation;
import codeu.model.data.ConversationActivity;
import codeu.model.data.NewConversationActivity;
import codeu.model.data.NewConversationActivity;
import codeu.model.data.NewUserActivity;
import codeu.model.data.User;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ActivityStoreTest {

  private ActivityStore activityStore;
  private PersistentStorageAgent mockPersistentStorageAgent;


  private final Activity ACTIVITY_NEWUSER = new NewUserActivity(
      UUID.randomUUID(), Instant.ofEpochMilli(1000));

  private final Conversation CONVERSATION_ONE =
      new Conversation(
          UUID.randomUUID(), UUID.randomUUID(), "conversation_one", Instant.ofEpochMilli(1000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    activityStore = ActivityStore.getTestInstance(mockPersistentStorageAgent);

    final LinkedList<Activity> activityList = new LinkedList<>();
    activityList.add(ACTIVITY_NEWUSER);
    activityStore.setActivities(activityList);
  }

  @Test
  public void testAddNewUserActivity() {
      NewUserActivity inputActivity = new NewUserActivity(UUID.randomUUID(), Instant.now());

      activityStore.addActivity(inputActivity);
      List<Activity> activities = activityStore.getActivitiesCopy();
      Activity resultActivity = activities.get(activities.size() - 1);

      Assert.assertTrue(resultActivity instanceof NewUserActivity);
      NewUserActivity testActivity = (NewUserActivity) resultActivity;

      Assert.assertEquals(inputActivity.getActorId(), resultActivity.getActorId());
      Assert.assertEquals(inputActivity.getCreationTime(), resultActivity.getCreationTime());
  }

  @Test
  public void testAddNewConversationActivity() {
      NewConversationActivity inputActivity = new NewConversationActivity(
          UUID.randomUUID(), Instant.now(), "Conversation-Title");

      activityStore.addActivity(inputActivity);
      LinkedList<Activity> activities = activityStore.getActivitiesCopy();
      Activity resultActivity = activities.get(activities.size() - 1);

      Assert.assertTrue(resultActivity instanceof NewConversationActivity);
      NewConversationActivity testActivity = (NewConversationActivity) resultActivity;

      Assert.assertEquals(inputActivity.getActorId(), testActivity.getActorId());
      Assert.assertEquals(inputActivity.getCreationTime(), testActivity.getCreationTime());
      Assert.assertEquals(inputActivity.getConversationTitle(), testActivity.getConversationTitle());
  }

  @Test
  public void testAddConversationActivity() {
      ConversationActivity inputActivity = new ConversationActivity(
          UUID.randomUUID(), Instant.now(), "Conversation-Title", "Message");

      activityStore.addActivity(inputActivity);
      LinkedList<Activity> activities = activityStore.getActivitiesCopy();
      Activity resultActivity = activities.get(activities.size() - 1);

      Assert.assertTrue(resultActivity instanceof ConversationActivity);
      ConversationActivity testActivity = (ConversationActivity) resultActivity;

      Assert.assertEquals(inputActivity.getActorId(), testActivity.getActorId());
      Assert.assertEquals(inputActivity.getCreationTime(), testActivity.getCreationTime());
      Assert.assertEquals(inputActivity.getConversationTitle(), testActivity.getConversationTitle());
      Assert.assertEquals(inputActivity.getMessagePreview(), testActivity.getMessagePreview());
  }
}
