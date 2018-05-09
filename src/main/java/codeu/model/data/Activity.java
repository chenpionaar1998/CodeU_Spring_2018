// Activity abstract class
// Created by Naomi
//
// An Activity represents an event such as the creation of a new chat, a new
// user, a message in a chat. The minimum necessary information for these 
// activities are the time they happened and who is the actor. The activity feed
// knows how to display them based on the type of the activity.

package codeu.model.data;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.time.Instant;
import java.util.UUID;

public abstract class Activity {

  private final Instant creationTime;
  private final UUID actorId;
  private final User actor;  //TODO

  //constructor for activity should be called by subclass
  protected Activity(UUID actor, Instant creation) {
    this.actorId = actor;
    this.actor = UserStore.getInstance().getUser(actorId);
    this.creationTime = creation;
  }

  public User getActor() {
    return actor; 
  }

  public UUID getActorId() {
    return actorId;
  }

  public Instant getCreationTime() {
    return creationTime;
  }

}
