import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class CheckChars {

	/**
	 * Klasse die die nnp Liste mit den von Hand erstellen Charakterlisten abgleicht, und alle nnps rauswirft, 
	 * die nicht in
	 * der Charakterliste vorkommen.
	 */
	ArrayList<String[]> characters = new ArrayList<>();
	
	public CheckChars(Filter nnpList, String charListFile) {
			characters = this.check(nnpList, charListFile);
		}
	
	private ArrayList<String[]> check(Filter nnpList, String charListFile) {
		try {
			BufferedReader read = new BufferedReader(new FileReader(new File(charListFile)));
			String line;
			ArrayList<?> nnps = nnpList.getNNPS();
			
			for(int i = 0; i < nnps.size(); i++)//go through list of filtered nnps
			{
				String[] nnpParts = (String[]) nnps.get(i);//lemma from filtered nnps
				String lemma = nnpParts[2];
				
				while((line = read.readLine()) != null)//go through file of correct characters
				{
					String[] splitline = line.split(",");
					
					for(String nickname : splitline)
					{
						if(nickname.equals(lemma))//is the lemma of this nnp one of the nicknames in that line? 
						{
						     if(!characters.contains(nnps.get(i)))
						     {
						    	 characters.add((String[])nnps.get(i));
						     }

						}
					}
				}
			}
			read.close();
		} catch (IOException e) {
			System.out.println("File not found CheckChars.");
		}
		
		return characters;
	}
		
	public ArrayList<String[]> getCheckedCharacters() {
		return characters;
	}

	public static void main(String[] args) {
		Filter test = new Filter("C:\\Users\\Sarah Dänel\\Documents\\NetBeansProjects\\JavaApplication3\\src\\TI_Annotated.txt");
		CheckChars chars = new CheckChars(test, "C:\\Users\\Sarah Dänel\\Documents\\NetBeansProjects\\JavaApplication3\\src\\TI_Chars.txt");
		ArrayList<String[]> list = chars.getCheckedCharacters();
		for(int i = 0; i < list.size(); i++)
		{
			String[] nnp = list.get(i);
			for(int j = 0; j < nnp.length; j++)
			{
				System.out.print(nnp[j]);
			}
			System.out.println();
		}
		 
	}
	
}
