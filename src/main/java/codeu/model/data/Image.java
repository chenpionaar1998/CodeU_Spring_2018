package codeu.model.data;

import codeu.model.store.basic.ImageStore;
import java.util.HashSet;
import java.util.Set;

import java.net.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

public class Image {

  private static final String TARGET_URL = "https://vision.googleapis.com/v1/images:annotate?";
  public static String API_KEY = "key=AIzaSyAmTbdJrzov7ZVwGBzCVHPTM8F1L913yZM";

  private String url;
  private String response;
  private Set<String> descriptions;
  private boolean error;
  private String errorMessage;

  public Image(String url) {
    this.url = url;
    this.error = false;
    this.errorMessage = "";
  }

  public String callToAPI() {
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
      error = true;
      errorMessage = "ERROR: No Stream";
	    return "ERROR: No Stream";
      }
      Scanner httpResponseScanner = new Scanner (httpConnection.getInputStream());
      while (httpResponseScanner.hasNext()) {
	    String line = httpResponseScanner.nextLine();
	    this.response += line;
	    System.out.println(line);  //  alternatively, print the line of response
      }
      httpResponseScanner.close();
      parseJSON();
	} catch(Exception e) {
		System.out.println(e.getMessage());
    error = true;
    errorMessage = e.getMessage();
		return e.getMessage();
	}
  return null;
  }

  /**
   * Returns a response string produced by the Cloud Vision API
   * The string is in JSON notation
   */
  public String getResponse() {
    return response;
  }

  /**
   * Returns a set of descriptions of the image determined by the API
   */
  public Set<String> getDescription() {
	  return descriptions;
  }

  /**
   * Parses the response string produced by API and returns a set
   * of descriptions after parsing JSON format.
   */
  private void parseJSON() throws org.json.simple.parser.ParseException {
	  descriptions = new HashSet<String>();
	  JSONParser parse = new JSONParser();
	  JSONObject jobj = (JSONObject)parse.parse(response);
	  JSONArray jarr = (JSONArray)jobj.get("responses");
	  for (int i = 0; i < jarr.size(); i++) {
	    JSONObject tempJ = (JSONObject)jarr.get(i);
	    JSONArray tempJarr = (JSONArray) tempJ.get("labelAnnotations");
		  for (int j = 0; j < tempJarr.size(); j++) {
		    JSONObject desJ = (JSONObject) tempJarr.get(j);
		    String description = (String) desJ.get("description");
		    descriptions.add(description);
		  }
	  }
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

  public void addDescription(String description){
    descriptions.add(description);
  }

  public boolean hasError(){
    return error;
  }

  public String getErrorMessage(){
    return errorMessage;
  }
}
