import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

public class Transcript2Gene {

	public static void main(String[] args) throws JSONException, Exception {
		// TODO Auto-generated method stub
		
		String geneID = transcript2gene("ENST00000450305");
		System.out.println(geneID);
	}
	public static String readUrl(String urlString) throws Exception {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        
	        URLConnection connection = url.openConnection();
		    HttpURLConnection httpConnection = (HttpURLConnection)connection;
//		    
		    httpConnection.setRequestProperty("Content-Type", "application/json");
	        int responseCode = httpConnection.getResponseCode();
	   	 	
	        
	        if(responseCode == 429){
	        	String retry = httpConnection.getHeaderField("Retry-After");
	        	System.out.println(retry);
	        	TimeUnit.SECONDS.sleep(Integer.valueOf(retry)+10);
	        	readUrl(urlString);
	        	//		   	    	return "null";
	        	//		   	      throw new RuntimeException("Response code was not 200. Detected response was "+responseCode +"\t"+urlString);
	        }
	   	    if(responseCode != 200) {
//	   	    	String retry = httpConnection.getHeaderField("Retry-After");
//	   	    	System.out.println(retry);
//	   	    	TimeUnit.SECONDS.sleep(Integer.valueOf(retry));
//	   	    	readUrl(urlString);
	   	    	return "null";
//	   	      throw new RuntimeException("Response code was not 200. Detected response was "+responseCode +"\t"+urlString);
	   	    }
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 
	        httpConnection.disconnect();
	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	        
	    }
	}
	public static String transcript2gene(String id) throws JSONException, Exception{
//		System.out.println(id);
		String server = "http://rest.ensembl.org";
	    String ext = "/lookup/id/"+id+"?content-type=application/json;format=full";	    
	    JSONObject obj;
		try {
			obj = new JSONObject(readUrl(server + ext));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(server + ext);
			e.printStackTrace();
			return "null";
		}
	    String p = obj.getString("Parent");
		return p;
	}
	/*
	public static String transcript2gene(String id) throws JSONException, Exception{
		String server = "http://rest.ensembl.org";
	    String ext = "/lookup/id/"+id+"?content-type=application/json;format=full";
//	    URL url = new URL(server + ext);
//	 
//	    URLConnection connection = url.openConnection();
//	    HttpURLConnection httpConnection = (HttpURLConnection)connection;
//	    
//	    httpConnection.setRequestProperty("Content-Type", "application/json");
	    
	 
//	    InputStream response = cConnection.getInputStream();
//	    int responseCode = httpConnection.getResponseCode();
//	 
//	    if(responseCode != 200) {
//	      throw new RuntimeException("Response code was not 200. Detected response was "+responseCode);
//	    }
	 
//	    String output;
//	    Reader reader = null;
	    
	    JSONObject obj = new JSONObject(readUrl(server + ext));
//	    String pageName = obj.getJSONObject("pageInfo").getString("pageName");
	    String p = obj.getString("Parent");
//	    httpConnection.disconnect();
		return p;
	}*/
}
