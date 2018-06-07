import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class LinksetCreator {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		File file = new File("//media/bigcat-jonathan/VERBATIM HD/Variant/export_PTV_89.txt");
		
		File out = new File("/home/bigcat-jonathan/Desktop/PTV.ttl");
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
