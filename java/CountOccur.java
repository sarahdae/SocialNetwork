
/**
 * zählt, wie oft die Person im Buch in jedem Kapitel erwähnt wird
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CountOccur {

    HashMap<String, Integer> counter = new HashMap<String, Integer>();

    public CountOccur(Filter nnpList) {
        counter = this.counting(nnpList);
    }

    private HashMap<String, Integer> counting(Filter nnpList) {
        try {
            String lemma = "";
            String chapterID = "";
            int count = 0;
            int currentChap = 1;
            ArrayList nnps = nnpList.getNNPS();
            List<String> chapID = new ArrayList();
            List<String> lemmaList = new ArrayList();

            for (int i = 0; i < nnps.size(); i++)//go through list of filtered nnps
            {
                String[] nnpParts = (String[]) nnps.get(i);//convert object into String[]
                chapterID = nnpParts[0]; //chapterID
                lemma = nnpParts[2]; //lemma from filtered nnps
                //System.out.println(lemma); //lemmas are all there
                lemmaList = Arrays.asList(lemma); // convert lemmaString into list
                chapID = Arrays.asList(chapterID);
                while (count <= 34) {
                    for (String name : lemmaList) { //go through entities
                        Integer oldCount = counter.get(name);
                        if (oldCount == null) {
                            oldCount = 0;
                        }
                        counter.put(lemma, oldCount + 1);
                    }
                    count++;
                }
            }
            int chapSize = Integer.parseInt(chapID.get(chapID.size() - 1));
            System.out.println(chapSize);
            //update values on each chapter
            //print hashmap line by line, for each chapter
            while ((count <= chapSize) && (currentChap <= chapSize)) {
                System.out.println("========== Chapter " + currentChap + " ==========");
                for (Map.Entry<String, Integer> entry : counter.entrySet()) {
                    if (entry.getValue() != 0) {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    }
                    counter.put(entry.getKey(), 0); //reset values to 0
                }
                count++;
                currentChap++;
            }
            //if lemma equals
            //reset value to 0 when new chapter begins
        } catch (Exception e) {
            System.out.println("wrong file");
        }
        return counter;
    }

    public HashMap getOccur() {
        return counter;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Filter filteredChars = new Filter("TI_Annotated.tsv");
        CountOccur test = new CountOccur(filteredChars);
    }

}
