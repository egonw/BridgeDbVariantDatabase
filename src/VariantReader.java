// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Read the source file, map the transcript identifier to gene identifier, 
* and create the input .txt file for VariantCreator
* @author J.Melius
*/
public class VariantReader {

	public static Map<String,String> map = new HashMap<String,String>();
	static int count = 0;
	static int transcript = 0;
	static BufferedWriter writer;
	static boolean flag;	
	static List<String> subset_list;
	
	public static void main(String[] args) throws FileNotFoundException, IOException  {

		File in = new File(args[0]);
		File f = new File (args[1]);
		
		SpeciesConfiguration config = new SpeciesConfiguration(f.getAbsolutePath());
		subset_list = config.getSubsetList();
		String term = config.getSubsetTerm();
		
		InputStream is = config.getTranscript();		
		map = transcriptReader(is);
		
		File out = new File(config.getEndpoint());
		writer = new BufferedWriter(new FileWriter(out,true));
		System.out.println("reading :"+out.getAbsolutePath());
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

		    	flag = false;
				
		    	if (term.equals("polyphen"))
		    		tmp = check_polyphen(info, tmp);
		    	else
		    		tmp = check_subset(info, tmp);
		    	
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
	
	
	public static String check_polyphen (String info, String tmp){
		
		int index = info.indexOf("Polyphen=");
		info = info.substring(index); 
		index = info.indexOf(";");
		info = info.substring(0,index);
		for(String w: info.split(",")){    		
			
			if (w.contains("probably_damaging")){
				String [] polyphen = w.split("\\|");
				String transcriptID = "";
				if (w.split("\\|").length>3){
					float score = Float.parseFloat(polyphen[2]);
					if (score >= 0.908){
						transcriptID = polyphen[3];
					}
				}
				else{
					float score = Float.parseFloat(polyphen[1]);
					if (score >= 0.908){
						transcriptID = polyphen[2];	    					
					}
					count++;
				}
				if (map.get(transcriptID)!=null){
					flag = true;
					tmp += "\t" + map.get(transcriptID);
					transcript++;
				}
			}
		}
		return tmp;
	}
	
	public static String check_subset (String info, String tmp){		
		int index = info.indexOf("VE=");
    	info = info.substring(index);
    	for(String w: info.split(",")){
    		if (checkIfExists(w)){
    			String transcriptID = w.split("\\|")[3];
    			if (map.get(transcriptID)!=null){
    				flag = true;
    				tmp += "\t" + map.get(transcriptID);
    				transcript++;
    			}
    		}
    	}
    	return tmp;
	}
	
	public static boolean checkIfExists(String w) {
	    for (String element:subset_list ) {
	    	if (w.contains(element) ) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public static Map<String,String> transcriptReader(InputStream in) throws FileNotFoundException, IOException {
		Map<String,String> map = new HashMap<String,String>();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
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