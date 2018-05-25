package codeu.model.data;

import codeu.model.store.basic.ImageStore;

public class Image {
    
    String url; 
    String [] labels;

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

}
