/* stores image objects which contain url and labels. Can be used to not get
 * labels for same URL twice from Cloud Vision */
package codeu.model.store.basic;

import codeu.model.data.Image;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.Hashtable;

public class ImageStore {

  private static ImageStore instance;

  public static ImageStore getInstance () {
    if(instance == null)
      instance = new ImageStore(PersistentStorageAgent.getInstance());
    return instance;
  }

  public static ImageStore getTestInstance() {
    return new ImageStore(PersistentStorageAgent.getInstance());
  }

  public ImageStore(PersistentStorageAgent agent) {
    this.persistentStorageAgent = agent;
  }

  public void setHashTable(Hashtable<String, Image> imageHash) {
    this.imageHash = imageHash;
  }

  /** in-memory hashtable of images by URL **/
  private Hashtable<String, Image> imageHash = new Hashtable<>();
  private PersistentStorageAgent persistentStorageAgent;

  /** returns an Image object with this url. 
   * If the image is already loaded to the hashtable, this image is returned.
   * Otherwise, checks persistent storage for an image by this url. If none is
   * there, a new one is created and added to persistent storage. */
  public Image getImageFromUrl(String url) {
    Image image = imageHash.get(url);
    if(image == null) {
      try {
        image = persistentStorageAgent.loadImage(url);
      }
      catch (Exception e) {
        image = null;  //just create new one
      }
      if(image == null) {
        image = new Image(url);
        persistentStorageAgent.writeThrough(image);
      }

      imageHash.put(url, image);
    }
    return image;
  }
}
