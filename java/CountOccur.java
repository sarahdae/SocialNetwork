
/**
 * zählt, wie oft die Personen in jedem Kapitel erwähnt wird wenn in der vis das
 * jeweilige kapitel ausgewählt wird, wird das programm von neuem starten
 * output:JSON file with data
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class CountOccur {

    HashMap<String, Integer> entities = new HashMap<String, Integer>();
    HashMap<String, Integer> events = new HashMap<String, Integer>();
    List<String> indexes; // for source and target tags in Json file

    public CountOccur(CheckChars nnpList) {
        entities = this.counting(nnpList);
    }

    private HashMap<String, Integer> counting(CheckChars nnpList) {
        try {
            String lemma = "";
            String chapterID = "";
            String sentenceID = "";

            ArrayList nnps = nnpList.getCheckedCharacters();
            List<String> chapID = new ArrayList();
            List<String> lemmaList = new ArrayList();
            String currChap = "2"; //starting from one
            String currSent = "1";

            for (int i = 0; i < nnps.size(); i++) { //LOOP TO GO THROUGH LIST OF NNPS
                String[] nnpParts = (String[]) nnps.get(i);//convert object into String[]
                chapterID = nnpParts[0]; //chapterID
                sentenceID = nnpParts[1];
                lemma = nnpParts[2]; //only lemma from filtered nnps
                //System.out.println(lemma); //lemmas are all there
                lemmaList = Arrays.asList(lemma); // convert lemmaString into lemmaList
                chapID = Arrays.asList(chapterID); //

                //only counting one chapter by one
                if (chapterID.equals(currChap)) {
                    for (String name : lemmaList) { //go through lemmaList
                        Integer oldCount = entities.get(name);
                        if (oldCount == null) {
                            oldCount = 0;
                        }
                        //add lemma and number of occurences into hashmap
                        entities.put(lemma, oldCount + 1);
                        indexes = new ArrayList<String>(entities.keySet()); // <== Parse

                    }

                }
                //EVENT COUNTING STARTS HERE - STILL DOES NOT REALLY WORK
                //if two or more entities exist in same or the next sentence
                //new event is created
                //e.g.: given chapter1: in sentence 
                if (sentenceID.equals(currSent)) { //choose sentence
                    for (String event : chapID) {
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
            System.out.println("========== chapter " + currChap + "==========");
            for (Map.Entry<String, Integer> entry : entities.entrySet()) {
                if (entry.getValue() != 0) {
                    System.out.println(indexes.indexOf(entry.getKey()) + entry.getKey() + " : " + entry.getValue());
                }
            }
            
            //THIS STILL DOES NOT WORK PROPERLY
            System.out.println("========== Events in Sentence " + currSent + "==========");
            for (Map.Entry<String, Integer> sth : events.entrySet()) {
                if (sth.getValue() != 0) {
                    System.out.println(sth.getKey() + " : " + sth.getValue());
                }
            }

            //creates new json file and print solution into it
            try {
                PrintWriter writer = new PrintWriter("countOcc.json", "UTF-8");
                writer.println("{");

                writer.println("\"nodes\":[");
                for (Map.Entry<String, Integer> entry : entities.entrySet()) {
                    if (entry.getValue() != 0) {
                        writer.println("{\"name\":\"" + entry.getKey() + "\", \"group\":1, \"size\": " + (entry.getValue() + "00") + "},");
                    }
                }
                writer.println("],");

                writer.println("\"links\":[");
                for (Map.Entry<String, Integer> entry : entities.entrySet()) {
                    if (entry.getValue() != 0) {
                        writer.println("{\"source\":" + indexes.indexOf(entry.getKey()) + ", \"target\":"+ indexes.indexOf(entry.getKey()) +", \"value\":, \"length\":},");
                    }
                }
                writer.println("]");

                writer.println("}");
                writer.close();
            } catch (Exception e) {
                System.out.println("could not output file");
            }

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
        Filter filteredChars = new Filter("E:\\E_Dokumente\\semester7\\wolskaProject\\WolskaProject\\src\\LotF_Annotated.tsv");
        CheckChars check = new CheckChars(filteredChars, "E:\\E_Dokumente\\semester7\\wolskaProject\\WolskaProject\\src\\LotFChars.txt");
        CountOccur test = new CountOccur(check);
    }

}
