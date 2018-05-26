package codeu.model.data;

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

}
