// NewUserActivity
// Represents new User was created. Keeps track of the user and when the user is
// created (handled by super class). For the purpose of displaying information
// on the activity feed

package codeu.model.data;

public class NewUserActivity extends Activity {
 
  // Only information about activity needed is the user, and user's creation
  // time is the activity's time
  public NewUserActivity(User newUser) {
    super(newUser.getId, newUser.getCreationTime());
  }
}
