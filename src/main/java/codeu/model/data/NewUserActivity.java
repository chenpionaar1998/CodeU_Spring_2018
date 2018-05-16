// NewUserActivity
// Represents new User was created. Keeps track of the user and when the user is
// created (handled by super class). For the purpose of displaying information
// on the activity feed

package codeu.model.data;

import codeu.model.store.basic.UserStore;
import java.time.Instant;
import java.util.UUID;

public class NewUserActivity extends Activity {
 
  // Only information about activity needed is the user, and user's creation
  // time is the activity's time
  public NewUserActivity(User newUser) {
    super(newUser.getId(), newUser.getCreationTime());
  }

  //constructor to use when loading from persistent data store, needs only the
  //info that was stored in the entity
  public NewUserActivity(UUID userId, Instant creation) {
    super(userId, creation);
  }


  /* returns string for activtity.jsp file to display, including formatting and 
   * html to show links */ 
  public String getFeedDisplay() {
      String actorName = UserStore.getInstance().getUser(getActorId()).getName();
      return actorName + " joined!"; 
  }

}
