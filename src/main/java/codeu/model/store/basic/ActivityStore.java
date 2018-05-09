// ActivityStore
//
// Holds some (constant) number of most recent activities using a linked list
// that acts as a queue, getting rid of oldest and inserting newest at the end of queue. 
// (front of list is oldest)
package codeu.model.store.basic;

import codeu.model.data.Activity;
import codeu.model.store.persistence.PersistentStorageAgent;

import java.util.LinkedList;
import java.util.List;

public class ActivityStore {

  private static ActivityStore instance;

  // returns singleton instance
  public static ActivityStore getInstance() {
    if(instance == null) {
      instance = new ActivityStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  private PersistentStorageAgent persistentStorageAgent;

  // In memory-queue or recent activities
  private LinkedList<Activity> activities;
  private final static int maxNumActivities = 100;  

  private ActivityStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    this.activities = new LinkedList<Activity>();  
  }

  /* Adds given activity to list of activities and removes oldest (first) if
   * there are more than capacity */
  public void addActivity(Activity newActivity) {
    if(activities.size() >= maxNumActivities) {
      activities.removeFirst();  //full queue, remove oldest
    }
    activities.addLast(newActivity);
  }

  /* return deep copy of the list since there shouldn't be a reason to modify the
   * order of the activities outside this class */ 
  public LinkedList<Activity> getActivitiesCopy() {
    return new LinkedList<Activity>(activities);
  }

  // set the list of activities
  public void setActivities(LinkedList<Activity> activities) {
      this.activities = activities;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static ActivityStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new ActivityStore(persistentStorageAgent);
  }

}
