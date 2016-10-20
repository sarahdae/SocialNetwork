
/**
 * zählt, wie oft die Personen in jedem Kapitel erwähnt wird wenn in der vis das
 * jeweilige kapitel ausgewählt wird, wird das programm von neuem starten
 * output:JSON file with data
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CountOccur {

    HashMap<String, Integer> entities = new HashMap<String, Integer>();
    HashMap<String, Integer> events = new HashMap<String, Integer>();
    ArrayList chaps = new ArrayList();

    public CountOccur(CheckChars nnpList) {
        entities = this.counting(nnpList);
    }

    private HashMap<String, Integer> counting(CheckChars nnpList) {
        try {
            String lemma = "";
            String chapterID = "";
            String sentenceID = "";
            String chapNum = ""; //only digits as String
            String newKey ="";
            ArrayList nnps = nnpList.getCheckedCharacters();
            List<String> chapID = new ArrayList();
            List<String> lemmaList = new ArrayList();
            
            for (int i = 0; i < nnps.size(); i++) { //LOOP TO GO THROUGH LIST OF NNPS
                String[] nnpParts = (String[]) nnps.get(i);//convert object into String[]
                chapterID = nnpParts[0]; //chapterID
                sentenceID = nnpParts[1];
                lemma = nnpParts[2]; //only lemma from filtered nnps
                //System.out.println(lemma); //lemmas are all there
                lemmaList = Arrays.asList(lemma); // convert lemmaString into lemmaList
                chapID = Arrays.asList(chapterID); //
                
                String currChap= "1"; //starting from one
                String currSent="1";
                
                //I NEED INPUT NUMBER (as String) OF VIS SLIDER (chapNum)
                //only counting one chapter by one
                if (chapterID.equals(currChap)) {
                    for (String name : lemmaList) { //go through lemmaList
                        Integer oldCount = entities.get(name);
                        if (oldCount == null) {
                            oldCount = 0;
                        }
                        //add lemma and number of occurences into hashmap
                        entities.put(lemma, oldCount + 1);
                    }
                    
                }
                    //EVENT COUNTING STARTS HERE
                    //if two or more entities exist in same or the next sentence
                    //new event is created
                //e.g.: given chapter1: in sentence 
                if (sentenceID.equals(currSent)){ //choose sentence
                    for(String event : chapID){
                        Integer evCount = events.get(event);
                           if (evCount == null) {
                            evCount = 0;
                        }
                        events.put(chapterID, evCount + 1);
                        chaps.add(chapterID);
                    }
                }
            }

            //print hashmap line by line, for each chapter
            System.out.println("========== chapter "+chapterID+"==========");
            for (Map.Entry<String, Integer> entry : entities.entrySet()) {
                if (entry.getValue() != 0) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
            }
                        System.out.println("========== sentences "+chaps.toString()+"==========");
                        for (Map.Entry<String, Integer> sth : events.entrySet()) {
                if (sth.getValue() != 0) {
                    System.out.println(sth.getKey() + " : " + sth.getValue());
                }
            }
            //if lemma equals
            //reset value to 0 when new chapter begins
        } catch (Exception e) {
            System.out.println("wrong file");
        }
        return entities;
    }

    public HashMap getOccur() {
        return entities;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Filter filteredChars = new Filter("LotF_Annotated.tsv");
        CheckChars check = new CheckChars(filteredChars, "LotFChars.txt");
        CountOccur test = new CountOccur(check);
    }

}
