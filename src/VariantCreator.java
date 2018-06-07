import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;

import org.bridgedb.DataSource;
import org.bridgedb.IDMapperException;
import org.bridgedb.Xref;
import org.bridgedb.bio.DataSourceTxt;
import org.bridgedb.creator.BridgeDbCreator;
import org.bridgedb.creator.DbBuilder;
import org.bridgedb.rdb.construct.GdbConstruct;

import script.SnpAttributes;
import script.SpeciesConfiguration;


public class VariantCreator {
	static GdbConstruct newDb;
	static Xref mainXref ;
	
	static SnpAttributes snp ;
	
	static Xref rightXref;
	
	static DataSource snpDs;
	static DataSource geneDs;
	

	private static HashSet<Xref> addedXrefs = new HashSet<Xref>();
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, IDMapperException, SQLException {
		DataSourceTxt.init();
		snpDs= DataSource.getExistingBySystemCode("Sn");
		geneDs= DataSource.getExistingBySystemCode("En");
		merge(args[0]);
	}

	public static void merge(String configPath) throws IOException, ClassNotFoundException, IDMapperException, SQLException{
		Date date = new Date();	
		System.out.println(date);
		
		File f = new File (configPath);
		SpeciesConfiguration config = new SpeciesConfiguration(f.getAbsolutePath());
		System.out.println(config.getFileName());

		String input =  config.getEndpoint();

		String line = "";

		BridgeDbCreator creator = new BridgeDbCreator();
		creator.setOutputFilePath(config.getFileName());
		creator.setDbSourceName("Ensembl");
		creator.setDbVersion("1.0");
		creator.setDbSeries(config.getDBName());
		creator.setDbDataType("SNP");

		DbBuilder dbBuilder = new DbBuilder(creator);
		dbBuilder.createNewDb();
		
		newDb = dbBuilder.getNewDb();

		FileInputStream inputStream = new FileInputStream(input);
		Scanner sc = new Scanner(inputStream, "UTF-8");
		
		date = new Date();	
		System.out.println(date);

		int n=0;
		while (sc.hasNextLine()) {
			
		    line = sc.nextLine();
			line = line.replaceAll("\\s+", "\t");
			String[] split = line.split("\t");
			
			snp = new  SnpAttributes(split[0], "", split[1],
					split[1],  "",  "", split[4],	"", "",  split[3]);
			
			Xref mainXref= new Xref(split[2],snpDs);
			
			if (addedXrefs.add(mainXref)){
				newDb.addGene(mainXref);
				newDb.addLink(mainXref, mainXref);
				newDb.addAttribute(mainXref, "Chromosome position start (bp)", snp.getChrom_start());
				newDb.addAttribute(mainXref, "Chromosome position end (bp)", snp.getChrom_end());
				newDb.addAttribute(mainXref, "Chromosome", snp.getChromosme());
				newDb.addAttribute(mainXref, "Variant Alleles", snp.getAllele());
				newDb.addAttribute(mainXref, "MAF", snp.getMinor_allele_freq());

				if (split.length>6){
					HashSet<Xref> addedRrightXref = new HashSet<Xref>();
					for(int i = 5; i < split.length; i++){
						Xref rightXref = new Xref(split[i],geneDs);
						if (addedXrefs.add(rightXref)){
							newDb.addGene(rightXref);
						}
						if (addedRrightXref.add(rightXref)){
							newDb.addLink(mainXref, rightXref);
						}
					}
				}
				else if (split.length==6){
					Xref rightXref = new Xref(split[5],geneDs);
					if (addedXrefs.add(rightXref)){
						newDb.addGene(rightXref);
					}
					newDb.addLink(mainXref, rightXref);
				}
			}
			
			n++;
			if (n%100==0){
				try {
					newDb.executeBatch();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println(mainXref);
				}
			}
			if (n%1000==0){
				System.out.println(n);
			}

		}
		if (inputStream != null) {
	        inputStream.close();
	    }
	    if (sc != null) {
	        sc.close();
	    }
	    
	    date = new Date();	
	    System.out.println(date);
	
	    System.out.println("Start to the creation of the database, might take some time");
	    newDb.executeBatch();
	    dbBuilder.setNewDb(newDb);
	    dbBuilder.finalizeDb();
	}
}
