// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.data;

import codeu.model.store.basic.UserStore;
import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class NewUserActivityTest {

  @Test
  public void testCreateNewActivity() {
    UUID id = UUID.randomUUID();
    String username = "Test_Username"; 
    String password = "Test_Password"; 
    Instant creation = Instant.now();

    User user = new User(id, username, password, creation);
    NewUserActivity activity = new NewUserActivity(user);
    //UserStore.getInstance().addUser(user);

    //Assert.assertEquals(user, activity.getActor()); 
    Assert.assertEquals(id, activity.getActorId());
    Assert.assertEquals(creation, activity.getCreationTime());
  }

  @Test
  public void testCreateLoad() {
    UUID id = UUID.randomUUID();
    String username = "Test_Username"; 
    String password = "Test_Password"; 
    Instant creation = Instant.now();

    User user = new User(id, username, password, creation);
    NewUserActivity activity = new NewUserActivity(user);
    //UserStore.getInstance().addUser(user);

   // Assert.assertEquals(user, activity.getActor()); 
    Assert.assertEquals(id, activity.getActorId());
    Assert.assertEquals(creation, activity.getCreationTime());
  }
}
