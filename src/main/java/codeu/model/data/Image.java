package codeu.model.data;

import codeu.model.store.basic.ImageStore;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Image {
	
  private static final String TARGET_URL = "https://vision.googleapis.com/v1/images:annotate?";
  private static final String API_KEY = "key=AIzaSyAmTbdJrzov7ZVwGBzCVHPTM8F1L913yZM";
  private String url; 
  private String response;

  public Image(String url) {
    this.url = url;
  }
    
  public void callToAPI() {
    try {
      URL serverUrl = new URL(TARGET_URL + API_KEY);
      URLConnection urlConnection = serverUrl.openConnection();
      HttpURLConnection httpConnection = (HttpURLConnection)urlConnection;
      httpConnection.setRequestMethod("POST");
      httpConnection.setRequestProperty("Content-Type", "application/json");
      httpConnection.setDoOutput(true);
      BufferedWriter httpRequestBodyWriter = new BufferedWriter(new
            OutputStreamWriter(httpConnection.getOutputStream()));
      httpRequestBodyWriter.write
      		("{\"requests\":  [{ \"features\":  [ {\"type\": \"LABEL_DETECTION\""
			+"}], \"image\": {\"source\": { \"imageUri\":"
			+" \"" + url + "\"}}}]}");
      httpRequestBodyWriter.close();
      String httpMessage = httpConnection.getResponseMessage(); 
      System.out.println(httpMessage);
      if (httpConnection.getInputStream() == null) {
	    System.out.println("No stream");
	    return;
      } 
      Scanner httpResponseScanner = new Scanner (httpConnection.getInputStream());
      while (httpResponseScanner.hasNext()) {
	    String line = httpResponseScanner.nextLine();
	    this.response += line;
	    System.out.println(line);  //  alternatively, print the line of response
      }
      httpResponseScanner.close();
	} catch(Exception e) {
		System.out.println(e.getMessage());
	}
}

  public String getResponse() {
    return response;
  }
  public String getUrl() {
    return url;
  }

  public String getHTML() {
   return "<a href=" + url + "><img style=\"max-width:500px\" src=" + url + "></a> ";
  }
   
  /* gets imagee from image store by this url. If image not in imagestore
   * imagestore will add a image by this url */
  public static Image getImageFromUrl(String url) {
    return ImageStore.getInstance().getImageFromUrl(url);
  }

}
