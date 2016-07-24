import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasse, die aus dem preprocessed File die Zeilen herausfiltert, die NNPs sind, und sie als Liste aus 
 * Arrays (chapter ID, sentence ID  und lemma) abspeichert 
 * @author anke
 *
 */
public class Filter {
	
	ArrayList<String[]> nnps = new ArrayList();

	public Filter() {
		// TODO Auto-generated constructor stub
	}
	
	public Filter(String filename) {
		try  {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		    String line;
		    while ((line = br.readLine()) != null) {
		       if(line.endsWith("NNP"))
		       {
		    	   System.out.println(line);
		    	   String[] splitline = line.split("\t");
		    	   String chapId = splitline[0];
		    	   String sentId = splitline[1];
		    	   String lemma = splitline[4];
		    	   String[] nnp = new String[3];
		    	   nnp[0] = chapId;
		    	   nnp[1]= sentId;
		    	   nnp[2] = lemma;
		    	   for(String thing : nnp){
		    		   System.out.print(thing + " ");
		    	   }
		    	   System.out.println();
		    	   nnps.add(nnp);
		    	   
		       }
		    }
		} catch (IOException e) {
			System.out.println("File not found.");
		}
	}
	
	public ArrayList getNNPS() {
		return nnps;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Filter test = new Filter("LotF_Annotated.tsv");
		Filter test2 = new Filter("TI_Annotated.tsv");
		

	}

}

