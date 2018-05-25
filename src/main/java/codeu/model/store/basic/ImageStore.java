/* stores image objects which contain url and labels. Can be used to not get
 * labels for same URL twice from Cloud Vision */
package codeu.model.store.basic;

import codeu.model.data.Image;
import java.util.Hashtable;

public class ImageStore {

    private static ImageStore instance;

    public static ImageStore getInstance () {
    	if(instance == null)
    		instance = new ImageStore();
    	return instance;
    }

    public static ImageStore getTestInstance() {
    	return new ImageStore();
    }

    public void setHashTable(Hashtable<String, Image> imageHash) {
    	this.imageHash = imageHash;
    }

    /** in-memory hashtable of images by URL **/
    private Hashtable<String, Image> imageHash = new Hashtable<>();

    public Image getImageFromUrl(String url) {
    	Image image = imageHash.get(url);
    	if(image == null) {
    		image = new Image(url);
    		imageHash.put(url, image);
    	}
    	return image;
    }
}
