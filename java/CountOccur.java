
/**
 * zählt, wie oft die Personen in jedem Kapitel erwähnt wird wenn in der vis das
 * jeweilige kapitel ausgewählt wird, wird das programm von neuem starten
 * output:JSON file with data
 * made by Anke Rüb
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class CountOccur {

    private HashMap<String, Integer> entities = new HashMap<>();
    private HashMap<String, Integer> cooccurrenceEdges = new HashMap<>();

    private List<String> indexes; // for source and target tags in Json file
    private ArrayList<String[]> nnps = new ArrayList<>();
    private Integer currentChapter;

    public CountOccur(CheckChars nnpList, int currChap) {
        nnps = nnpList.getCheckedCharacters();
        currentChapter = currChap;
        entities = this.counting();
    }

    private HashMap<String, Integer> counting() {
        try {
            String lemma = "";
            String chapterID = "";
            String sentenceID = "";
            //ArrayList nnps = nnpList.getCheckedCharacters();
            List<String> chapID;
            List<String> lemmaList = new ArrayList<>();

            for (int i = 0; i < nnps.size(); i++) { //LOOP TO GO THROUGH LIST OF NNPS
                String[] nnpParts = nnps.get(i);
                chapterID = nnpParts[0]; //chapterID
                sentenceID = nnpParts[1];
                lemma = nnpParts[2]; //only lemma from filtered nnps
                //System.out.println(lemma); //lemmas are all there
                lemmaList = Arrays.asList(lemma); // convert lemmaString into lemmaList

                //only counting one chapter by one
                if (Integer.parseInt(chapterID) == (currentChapter)) {
                    for (String name : lemmaList) { //go through lemmaList
                        Integer oldCount = entities.get(name);
                        if (oldCount == null) {
                            oldCount = 0;
                        }
                        //add lemma and number of occurences into hashmap
                        entities.put(lemma, oldCount + 1);
                        indexes = new ArrayList<>(entities.keySet()); // <== Parse
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("wrong file");
        }
        return entities;
    }

    private String getMaxOccuring(HashMap<String, Integer> entities) {

        String mainPerson = "";
        Integer maxValueInMap = (Collections.max(entities.values()));
        for (Map.Entry<String, Integer> entry : entities.entrySet()) {
            if (entry.getValue() == maxValueInMap) {
                mainPerson = (entry.getKey());
            }
        }
        return mainPerson;
    }

    private List<Integer> getSentenceIds(String person) {
        List<Integer> sentenceIds = new ArrayList<>();
        for (String[] entity : nnps) {
            if (Integer.parseInt(entity[0]) == currentChapter) {
                if (entity[2].equals(person)) {
                    sentenceIds.add(Integer.parseInt(entity[1]));
                }
            }
        }
        return sentenceIds;
    }

    public HashMap<String, Integer> getCooccurrenceEdges() {

        String mainPerson = getMaxOccuring(entities);
        //A Hashmap mapping from every person to a number indicating how often she cooccurs with the main person!
        List<Integer> sentenceIds = getSentenceIds(mainPerson);

        for (String[] entity : nnps) {
            if (Integer.parseInt(entity[0]) == currentChapter) {
                //we add an edge/weight to the edge from the main person for every person occuring in the same or in adjacent sentences
                if (sentenceIds.contains(Integer.parseInt(entity[1])) || sentenceIds.contains(Integer.parseInt(entity[1]) + 1)
                        || (sentenceIds.contains(Integer.parseInt(entity[1]) - 1))) {
                    String person = entity[2];

                    //as the main person. We create no edge from the main person to itself.
                    if (person.contentEquals(mainPerson)) {
                        continue;
                    }

                    if (cooccurrenceEdges.containsKey(person)) {
                        int count = cooccurrenceEdges.get(person);
                        count += 1;
                        cooccurrenceEdges.put(entity[2], count);
                    } else {
                        cooccurrenceEdges.put(person, 1);
                    }

                }
            }
        }

        return cooccurrenceEdges;
    }

    public void createJson() {
        //creates new json file and print solution into it
        String sourcePerson = getMaxOccuring(entities);
        Map<String, Integer> hash = new HashMap<>();
        hash.put(sourcePerson, 0);
        HashMap<String, Integer> getEdges = getCooccurrenceEdges();
        try {
            PrintWriter writer = new PrintWriter("countOcc.json", "UTF-8");
            writer.println("{");

            //NODES
            writer.println("\"nodes\":[");
            for (Map.Entry<String, Integer> entry : entities.entrySet()) {
                if (entry.getValue() != 0) {
                    writer.println("{\"name\":\"" + entry.getKey() + "\""
                            + ", \"group\":1"
                            + ", \"size\": " + (entry.getValue() * 1000) + "},");
                }
            }
            writer.println("],");

            //LINKS
            //entities = getCooccurrenceEdges(); //HIER WIRD INHALT VON ENTITIES GEÄNDERT
            writer.println("\"links\":[");
            for (Map.Entry<String, Integer> entry : cooccurrenceEdges.entrySet()) {
                if (entry.getValue() != 0) {
                    writer.println("{\"source\":" + hash.get(sourcePerson)
                            + ", \"target\":" + indexes.indexOf(entry.getKey())
                            + ", \"value\":1" //still needs to be programed
                            + ", \"length\": " + (entry.getValue() * 1000) + "},");

                }

            }
            writer.println("]");

            writer.println("}");
            writer.close();
        } catch (Exception e) {
            System.out.println("could not output file");
        }
    }
    
        public HashMap getOccur() {
        return entities;
    }

    public static void main(String[] args) {
    }
}
