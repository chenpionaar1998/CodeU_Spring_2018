package codeu.model.data;

import codeu.model.store.basic.ImageStore;

public class Image {
    
    String url; 
    String [] labels;

    public Image(String url) {
    	this.url = url;
    }
   
    /* gets imagee from image store by this url. If image not in imagestore
     * imagestore will add a image by this url */
    public static Image getImageFromUrl(String url) {
    	return ImageStore.getInstance().getImageFromUrl(url);
    }

}
