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

/**
* Parse the vcf file or the subset file to convert it into a turtle file
* @author J.Melius
*/
public class LinksetCreator {

	public static void main(String[] args) throws FileNotFoundException, IOException {		

		File file = new File(args[0]);
		
		File out = new File(args[1]);
		BufferedWriter writer = new BufferedWriter(new FileWriter(out,true));
		
		String voidHeader = "@prefix ensemblterms: <http://rdf.ebi.ac.uk/terms/ensembl/> .\n";
		voidHeader += "@prefix ensembl: <http://rdf.ebi.ac.uk/resource/ensembl/> .\n";
		voidHeader += "@prefix dbsnp: <http://www.ncbi.nlm.nih.gov/projects/SNP/snp_ref.cgi?rs=> .\n";
		voidHeader += "@prefix void:  <http://rdfs.org/ns/void#> .\n";
		voidHeader += "@prefix skos:  <http://www.w3.org/2004/02/skos/core#> .\n\n";
		voidHeader += "<>      void:inDataset  <http://bridgedb.org/data/linksets/release92/Variant/Ensembl_dbSNP.void.ttl#homo_sapiens-ensembl-dbSNP-PTV-linkset> .\n\n";

		writer.append(voidHeader);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = "";
		    while ((line = br.readLine()) != null) {
		    	String rs = "";
		    	String en = "";
		    	line = line.replaceAll("\\s+", "\t");
		    	String[] split = line.split("\t");
		    	for ( String s : split){
		    		if ( s.contains("rs"))
		    			rs = s;
		    		if ( s.contains("ENSG") && !s.equals(en)){
		    			en = s;
		    			writer.append("dbsnp:"+rs+ " ensemblterms:INFERRED_FROM_TRANSCRIPT  ensembl:"+ en+" .\n");
		    		}
		    	}
		    }	
		}
		writer.flush();
		writer.close();
	}
}
