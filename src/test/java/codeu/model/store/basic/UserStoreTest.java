package codeu.model.store.basic;

import codeu.model.data.User;
import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;
import codeu.model.store.basic.MessageStore;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserStoreTest {

  private UserStore userStore;
  private MessageStore messageStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final User USER_ONE =
      new User(UUID.randomUUID(), "test_username_one", "password one", Instant.ofEpochMilli(1000), 0, false);
  private final User USER_TWO =
      new User(UUID.randomUUID(), "test_username_two", "password two", Instant.ofEpochMilli(2000), 0, false);
  private final User USER_THREE =
      new User(UUID.randomUUID(), "test_username_three", "password three", Instant.ofEpochMilli(3000), 0, false);

  private final UUID CONVERSATION_ID_ONE = UUID.randomUUID();
  private final Message MESSAGE_ONE =
      new Message(
          UUID.randomUUID(),
          CONVERSATION_ID_ONE,
          USER_ONE.getId(),
          "message one",
          Instant.ofEpochMilli(1000),
          true);
  private final Message MESSAGE_TWO =
      new Message(
          UUID.randomUUID(),
          CONVERSATION_ID_ONE,
          USER_TWO.getId(),
          "message two",
          Instant.ofEpochMilli(2000),
          true);
  private final Message MESSAGE_THREE =
      new Message(
          UUID.randomUUID(),
          CONVERSATION_ID_ONE,
          USER_THREE.getId(),
          "message three",
          Instant.ofEpochMilli(3000),
          true);

  final List<User> userList = new ArrayList<>();
  final List<Message> messageList = new ArrayList<>();

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    userStore = UserStore.getTestInstance(mockPersistentStorageAgent);

    userList.add(USER_ONE);
    userList.add(USER_TWO);
    userList.add(USER_THREE);
    userStore.setUsers(userList);
    messageStore = MessageStore.getTestInstance(mockPersistentStorageAgent);
    messageList.add(MESSAGE_ONE);
    messageList.add(MESSAGE_TWO);
    messageList.add(MESSAGE_THREE);
    messageStore.setMessages(messageList);
  }

  @Test
  public void testGetUser_byUsername_found() {
    User resultUser = userStore.getUser(USER_ONE.getName());

    assertEquals(USER_ONE, resultUser);
  }

  @Test
  public void testGetUser_byId_found() {
    User resultUser = userStore.getUser(USER_ONE.getId());

    assertEquals(USER_ONE, resultUser);
  }

  @Test
  public void testGetUser_byUsername_notFound() {
    User resultUser = userStore.getUser("fake username");

    Assert.assertNull(resultUser);
  }

  @Test
  public void testGetUser_byId_notFound() {
    User resultUser = userStore.getUser(UUID.randomUUID());

    Assert.assertNull(resultUser);
  }

  @Test
  public void testAddUser() {
    User inputUser = new User(UUID.randomUUID(), "test_username", "password", Instant.now());

    userStore.addUser(inputUser);
    User resultUser = userStore.getUser("test_username");

    assertEquals(inputUser, resultUser);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputUser);
  }

  @Test
  public void testIsUserRegistered_true() {
    Assert.assertTrue(userStore.isUserRegistered(USER_ONE.getName()));
  }

  @Test
  public void testIsUserRegistered_false() {
    Assert.assertFalse(userStore.isUserRegistered("fake username"));
  }

  // add users and get userCount to see if the calculation is correct
  @Test
  public void testGetUserCount(){
    int userCount = userStore.getUserCount();

    // test before user added, expected result = 3 (3 added from setup)
    Assert.assertEquals(3, userCount);

    // add a mock user
    User inputUser = new User(UUID.randomUUID(), "test_username", "password", Instant.now());
    userStore.addUser(inputUser);

    //get userCount again to check if it is calcutlated correctly, expected result = 4
    userCount = userStore.getUserCount();
    Assert.assertEquals(4, userCount);
  }


  /** Do the same thing as getMessageCount and check if the messageCount attribute is incremented correctly */
  @Test
  public void testGetMessageCountForUser(){
    UUID idONE = USER_THREE.getId();
    int messageCount = 0;
    for (Message message : messageList) {
      UUID id = message.getAuthorId();
      if (idONE == id) {
        messageCount++;
      }
    }

    // after distributing the message count, messageCount should be 1 as the 1 message created have id = USER_THREE
    Assert.assertEquals(1, messageCount);
  }

  @Test
  public void testSortUserList(){
    // in setup USER_ONE is inserted in pos 0
    List<User> users = userStore.getUsers();
    USER_THREE.incrementMessageCount();
    Assert.assertEquals(users.get(0), USER_ONE);
    userStore.sortUserList();
    // after sorting pos 0 should be USER_THREE with messageCount = 1;
    Assert.assertEquals(users.get(0), USER_THREE);
  }

  @Test
  public void testGetTopUser(){
    String topUser = userStore.getTopUser();
    // topUser should return last user when all users have messageCount = 0
    Assert.assertEquals("test_username_three", topUser);

    USER_TWO.incrementMessageCount();

    topUser = userStore.getTopUser();
    // topUser should now be userTwo since userTwo has messageCount = 1
    Assert.assertEquals("test_username_two", topUser);
  }

  private void assertEquals(User expectedUser, User actualUser) {
    Assert.assertEquals(expectedUser.getId(), actualUser.getId());
    Assert.assertEquals(expectedUser.getName(), actualUser.getName());
    Assert.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    Assert.assertEquals(expectedUser.getCreationTime(), actualUser.getCreationTime());
  }
}
