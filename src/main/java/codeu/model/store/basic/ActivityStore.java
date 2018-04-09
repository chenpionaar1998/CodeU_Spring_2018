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
  private final static int maxNumActivities = 5;  //FIXME to 100

  private ActivityStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    this.activities = new LinkedList<Activity>();  
  }

  public void addActivity(Activity newActivity) {
    if(activities.size() > maxNumActivities) {
      activities.removeFirst();  //full queue, remove oldest
    }
    activities.addLast(newActivity);
  }

  //return deep copy of the list
  public LinkedList<Activity> getActivities() {
    return new LinkedList<Activity>(activities);

  }

}
