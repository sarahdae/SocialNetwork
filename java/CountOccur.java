
/**
 * zählt, wie oft die Person im Buch in jedem Kapitel erwähnt wird
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CountOccur {

    ArrayList<String> allChars = new ArrayList();
    HashMap<String, Integer> counter = new HashMap<String, Integer>();

    public CountOccur() {
        //stub
    }

    public CountOccur(Filter nnpList, CheckChars entities) {
        try {

            String lemma = "";
            ArrayList nnps = nnpList.getNNPS();
            ArrayList realCharList = entities.getCheckedCharacters();
            //System.out.println("chapID\tlemma");
            for (int i = 0; i < nnps.size(); i++)//go through list of filtered nnps
            {
                String[] nnpParts = (String[]) nnps.get(i);//lemma from filtered nnps
                String chapterID = nnpParts[0]; //chapterID
                lemma = nnpParts[2];
                //System.out.println(chapterID + " " + lemma);
                //while reading each line --> +1 whenever same name occurs
                //http://stackoverflow.com/questions/4820716/finding-repeated-words-on-a-string-and-counting-the-repetitions
                counter.put(lemma, 0); // put all lemmas as keys with value initial 0
            }
            for (Map.Entry<String, Integer> entry : counter.entrySet()) { //go through entities

                    Integer oldCount = counter.get(entry);
                    if (oldCount == null) {
                        oldCount = 0;
                    }
                    counter.put(lemma, oldCount + 1);
                
                //update values on each chapter
                if (entry.getValue() != 0) { //only print occurrences
                    System.out.println(entry.getKey() + "/" + entry.getValue());
                }
            }
            // go through chapters
            //if lemma equals 
            //reset value to 0 when new chapter begins
        } catch (Exception e) {
            System.out.println("wrong file");
        }
    }

    public HashMap getOccur() {
        return counter;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Filter filteredChars = new Filter("LotF_Annotated.tsv");
        CheckChars entities = new CheckChars(filteredChars, "LotFChars.txt");
        CountOccur test = new CountOccur(filteredChars, entities);

    }

}
