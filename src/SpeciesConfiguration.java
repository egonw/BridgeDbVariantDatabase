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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import script.BioMartAttributes;

/**
* Parse the configuration file and read the properties
* @author J.Melius
*/
public class SpeciesConfiguration {
	private Properties prop;
	private InputStream input;

	public SpeciesConfiguration(String filename){
		prop = new Properties();
		try {
			input = new FileInputStream(filename);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.out.println("Sorry, unable to find " + filename);
		}
		//load a properties file from class path, inside static method
		try {
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getEndpoint(){
		return prop.getProperty("endpoint");
	}
	public String getSchema(){
		return prop.getProperty("schema");
	}
	public String getDBName(){
		return prop.getProperty("database_name");
	}
	public String getFileName(){
		return prop.getProperty("file_name");
	}
	public InputStream getTranscript(){
		return SpeciesConfiguration.class.getClassLoader().getResourceAsStream("gene_transcript");
	}	
	public String getSubsetTerm(){
		return prop.getProperty("subset");
	}
	public List<String> getSubsetList(){
		String subset = prop.getProperty("subset");		
		List<String> term = null;
		switch (subset) {
		case "PTV":			
			term = Arrays.asList(prop.getProperty("PTV").split(","));
			break;
		case "PTVmissense":
			term = Arrays.asList(prop.getProperty("PTVmissense").split(","));
				break;
		case "missense":
			term = Arrays.asList(prop.getProperty("missense").split(","));
				break;
		case "exonic":
			term = Arrays.asList(prop.getProperty("exonic").split(","));
				break;	
		}
		return term;
	}
}
