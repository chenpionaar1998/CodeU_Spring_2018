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

import codeu.model.store.basic.ImageStore;
import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class ImageTest {

  @Test
  public void testCreate() {
    String url = "https://en.wikipedia.org/wiki/Cumulonimbus_cloud#/media/File:Fly00890_-_Flickr_-_NOAA_Photo_Library.jpg";
    String html = "<a href=" + url + "><img style=\"max-width:500px\" src=" + url + "></a> ";
    codeu.model.data.Image pic = new codeu.model.data.Image(url);

    Assert.assertEquals(url, pic.getUrl());
    Assert.assertEquals(html, pic.getHTML());
  }

  @Test 
    public void testGetHtml() {
      String url = "http://TEST.jpg";
      String correctHtml = "<a href=http://TEST.jpg><img style=\"max-width:500px\" src=http://TEST.jpg></a> ";
      Image testImage = new Image(url);

      Assert.assertEquals(correctHtml, testImage.getHTML());
    }

}
