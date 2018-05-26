package codeu.model.data;

import codeu.model.store.basic.ImageStore;
import java.util.HashSet;
import java.util.Set;

public class Image {

    String url;
    String [] labels;
    Set<String> descriptions = new HashSet<>();

    public Image(String url) {
    	this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getHtml() {
        return "<a href=" + url + "><img style=\"max-width:500px\" src=" + url + "></a> ";
    }

    /* gets imagee from image store by this url. If image not in imagestore
     * imagestore will add a image by this url */
    public static Image getImageFromUrl(String url) {
    	return ImageStore.getInstance().getImageFromUrl(url);
    }

    public Set<String> getDescription() {
      return descriptions;
    }

    public void addDiscription(String description){
      descriptions.add(description);
    }
}
