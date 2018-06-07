import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class VariantReader {

	public static Map<String,String> map = new HashMap<String,String>();
	static int count = 0;
	static int transcript = 0;
	
	public static void main(String[] args) throws FileNotFoundException, IOException  {

		File in = new File(args[0]);

		map = TranscriptReader.read();
		
		File out = new File(args[1]);

		BufferedWriter writer = new BufferedWriter(new FileWriter(out,true));

		try (BufferedReader br = new BufferedReader(new FileReader(in))) {
		    String line;

		    while ((line = br.readLine()) != null) {

		       // process the line.
		    	line = line.replaceAll("\\s+", "\t");
		    	String[] split = line.split("\t");
		    	
		    	String chr = split[0];
		    	String pos = split[1];
		    	String rsID = split[2];
		    	String ref = split[3];
		    	String alt = split[4];
		    	
		    	String info="";
				try {
					info = split[7];
				} catch (Exception e) {
					System.out.println(split[6]);
					info = split[6];
				}

		    	String tmp = chr+ "\t" +
		    			pos+ "\t" +
		    			rsID+ "\t" +
		    			ref+ "\t" +
		    			alt;

		    	boolean flag = false;
				
		    	int index = info.indexOf("Polyphen=");
		    	info = info.substring(index); 
		    	index = info.indexOf(";");
		    	info = info.substring(0,index);
				for(String w: info.split(",")){    			
		    		if (w.contains("probably_damaging")){
		    			String [] polyphen = w.split("\\|");
		    			String transcriptID = "";
		    			if (w.split("\\|").length>3){
		    				transcriptID = polyphen[3];
		    			}
		    			else{
		    				transcriptID = polyphen[2];
			    			count++;
		    			}
		    			if (map.get(transcriptID)!=null){
		    				flag = true;
			    			tmp += "\t" + map.get(transcriptID);
			    			transcript++;
		    			}
		    		}
		    	}
		    	
		    	if (flag)
		    		writer.append(tmp+"\n");
		    }
		}
		System.out.println("Parse done");
		System.out.println("count:"+count);
		System.out.println("transcript:"+transcript);
		map.clear();
		writer.flush();
		writer.close();
	}
}