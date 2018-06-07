import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class TranscriptReader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
	public static Map<String,String> read() throws FileNotFoundException, IOException{
		Map<String,String> map = new HashMap<String,String>();
		
		File in = new File("/home/bigcat-jonathan/Desktop/gene_transcript");
		
		try (BufferedReader br = new BufferedReader(new FileReader(in))) {
		    String line= br.readLine();
		    while ((line = br.readLine()) != null) {
		    	String[] split = line.split("\t");
		    	map.put(split[1], split[0]);
		    }
		    br.close();
		}
		return map;
	}
}
