
/**
 * zählt, wie oft die Person im Buch in jedem Kapitel erwähnt wird
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CountOccur {

    private HashMap<String, Integer> counter = new HashMap<String, Integer>();

    public CountOccur(CheckChars entities, int chapter) {

        if(chapter != 0) {
            int chap = chapter;
        }

        try {
            String lemma = "";
            ArrayList realCharList = entities.getCheckedCharacters();
            //System.out.println("chapID\tlemma");
            for (int i = 0; i < realCharList.size(); i++)//go through list of filtered nnps
            {
                String[] nnpParts = (String[]) realCharList.get(i);//lemma from filtered nnps
                String chapterID = nnpParts[0]; //chapterID
                lemma = nnpParts[2].toLowerCase();
                //System.out.println(chapterID + " " + lemma);
                //while reading each line --> +1 whenever same name occurs
                //http://stackoverflow.com/questions/4820716/finding-repeated-words-on-a-string-and-counting-the-repetitions

                if((chapter == 0) || (chapter == Integer.valueOf(chapterID))) {
                    if(counter.containsKey(lemma)) {
                        int newVal = counter.get(lemma) + 1;
                        counter.put(lemma, newVal);
                    } else {
                        counter.put(lemma, 0);
                    }
                }
            }

            // go through chapters
            //if lemma equals 
            //reset value to 0 when new chapter begins
        } catch (Exception e) {
            System.out.println("wrong file");
        }
    }

    public HashMap<String, Integer> getOccur() {
        return counter;
    }

    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) {
        Filter filteredChars = new Filter("/home/anke/SocialNetwork/TextFiles/LotF_Annotated.tsv");
        CheckChars entities = new CheckChars(filteredChars, "/home/anke/SocialNetwork/TextFiles/LotFChars.txt");
        CountOccur test = new CountOccur(entities);

    }*/

}
