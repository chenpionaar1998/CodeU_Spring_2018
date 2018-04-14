package codeu.model.store.basic;

import codeu.model.data.User;
import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;
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
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final User USER_ONE =
      new User(UUID.randomUUID(), "test_username_one", "password one", Instant.ofEpochMilli(1000), 0);
  private final User USER_TWO =
      new User(UUID.randomUUID(), "test_username_two", "password two", Instant.ofEpochMilli(2000), 1);
  private final User USER_THREE =
      new User(UUID.randomUUID(), "test_username_three", "password three", Instant.ofEpochMilli(3000), 2);

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    userStore = UserStore.getTestInstance(mockPersistentStorageAgent);
    final List<User> userList = new ArrayList<>();
    userList.add(USER_ONE);
    userList.add(USER_TWO);
    userList.add(USER_THREE);
    userStore.setUsers(userList);
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
    User inputUser = new User(UUID.randomUUID(), "test_username", "password", Instant.now(), 0);

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
    User inputUser = new User(UUID.randomUUID(), "test_username", "password", Instant.now(), 0);
    userStore.addUser(inputUser);

    //get userCount again to check if it is calcutlated correctly, expected result = 4
    userCount = userStore.getUserCount();
    Assert.assertEquals(4, userCount);
  }


  @Test
  public void testSortUserList(){
    // in setup USER_ONE is inserted in pos 0
    List<User> users = userStore.getAllUsers();
    Assert.assertEquals(users.get(0), USER_ONE);
    userStore.sortUserList();
    // after sorting pos 0 should be USER_THREE with messageCount = 2;
    Assert.assertEquals(users.get(0), USER_THREE);
  }


  private void assertEquals(User expectedUser, User actualUser) {
    Assert.assertEquals(expectedUser.getId(), actualUser.getId());
    Assert.assertEquals(expectedUser.getName(), actualUser.getName());
    Assert.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    Assert.assertEquals(expectedUser.getCreationTime(), actualUser.getCreationTime());
  }
}
