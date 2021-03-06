
/**
 * zählt, wie oft die Personen in jedem Kapitel erwähnt wird wenn in der vis das
 * jeweilige kapitel ausgewählt wird, wird das programm von neuem starten
 * output:JSON file with data made by Anke Rüb and An Khuong Bui
 */
import java.io.PrintWriter;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CountOccur {

    private HashMap<String, Integer> entities = new HashMap<>();

    private List<String> indexes; // for source and target tags in Json file
    private List<String> targetIndexes;
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
            List<String> chapID;
            List<String> lemmaList = new ArrayList<>();

            for (String[] nnpParts : nnps) {
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
                        entities.put(lemma.substring(0, 1).toUpperCase() + lemma.substring(1).toLowerCase(), oldCount + 1);
                        indexes = new ArrayList<>(entities.keySet()); // <== Parse
                    }
                }
            }

            //print hashmap line by line, for each chapter
            System.out.println("========== nodes in current chapter: " + currentChapter + "==========");
            for (Map.Entry<String, Integer> entry : entities.entrySet()) {
                if (entry.getValue() != 0) {
                    System.out.println(indexes.indexOf(entry.getKey()) + " " + entry.getKey() + " : " + entry.getValue());
                }
            }
        } catch (Exception e) {
            System.out.println("wrong file");
        }
        return entities;
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

    public HashMap<String, HashMap<String, Integer>> getAllEdges() {

        HashMap<String, HashMap<String, Integer>> listOfOccurenceEdges = new HashMap<>();//big hashmap: key is a person and value is a hashmap
        //of all persons cooccuring with the big key person, mapping to how often they cooccur with the big key person

        for (String[] ent : nnps) {

            if (Integer.parseInt(ent[0]) == currentChapter) {
                HashMap<String, Integer> cooccurrenceEdges = new HashMap<>();
                //inner hashmap
                List<Integer> sentenceIds = getSentenceIds(ent[2]);

                for (String[] entity : nnps) { //going through tagged text 2nd time

                    if (Integer.parseInt(entity[0]) == currentChapter) {
                        //we add an edge/weight to the edge from the main person for every person occurring in the same or in adjacent sentences
                        //as the main person. We create no edge from the main person to itself.
                        if (sentenceIds.contains(Integer.parseInt(entity[1])) || sentenceIds.contains(Integer.parseInt(entity[1]) + 1)
                                || (sentenceIds.contains(Integer.parseInt(entity[1]) - 1))) {
                            String person = entity[2];

                            if (person.contentEquals(ent[2])) {
                                cooccurrenceEdges.put(entity[2].substring(0, 1).toUpperCase() + entity[2].substring(1).toLowerCase(), 0);
                            }

                            if (cooccurrenceEdges.containsKey(person)) {
                                int count = cooccurrenceEdges.get(person);
                                count += 1;
                                cooccurrenceEdges.put(entity[2].substring(0, 1).toUpperCase() + entity[2].substring(1).toLowerCase(), count);
                            } else {
                                cooccurrenceEdges.put(person.substring(0, 1).toUpperCase() + person.substring(1).toLowerCase(), 1);
                            }
                        } else {
                            cooccurrenceEdges.put(entity[2].substring(0, 1).toUpperCase() + entity[2].substring(1).toLowerCase(), 0);
                        }
                    }
                }
                listOfOccurenceEdges.put(ent[2].substring(0, 1).toUpperCase() + ent[2].substring(1).toLowerCase(), cooccurrenceEdges);
            }
        }
        return listOfOccurenceEdges;
    }

    public void createJson() {
        JSONObject root = new JSONObject(); //actual json file root
        JSONArray listNodes = new JSONArray(); //array for nodes
        JSONArray listLinks = new JSONArray(); //array for links

        //NODES
        for (Map.Entry<String, Integer> entry : entities.entrySet()) {
            if (entry.getValue() != 0) {
                JSONObject nodesData = new JSONObject(); //data of name,group,size
                nodesData.put("name", entry.getKey());
                nodesData.put("group", 1);
                nodesData.put("size", entry.getValue() * 1000);

                //add the data into the array
                listNodes.add(nodesData);

            }
        }
        root.put("nodes", listNodes);

        //LINKS
        HashMap<String, HashMap<String, Integer>> getEdges = getAllEdges();
        Iterator<Map.Entry<String, HashMap<String, Integer>>> it = getEdges.entrySet().iterator();
        while (it.hasNext()) { //removes empty keys in getEdges
            Map.Entry<String, HashMap<String, Integer>> e = it.next();
            String key = e.getKey();
            HashMap<String, Integer> value = e.getValue();
            if (value.isEmpty()) {
                if (!entities.containsKey(key)) { //leaves the empty keys which are in the nodes
                    it.remove();
                }
            }
        }
        for (Map.Entry<String, HashMap<String, Integer>> entry : getEdges.entrySet()) {
            indexes = new ArrayList<>(getEdges.keySet()); // <== Parse
            HashMap<String, Integer> valueofGetEdges = entry.getValue();
            for (Map.Entry<String, Integer> sth : valueofGetEdges.entrySet()) { //going through smaller hashmap aka value of listHashMap
                targetIndexes = new ArrayList<>(valueofGetEdges.keySet()); // <== Parse
                JSONObject linksData = new JSONObject(); // data of source,target,value,length
                if ((sth.getValue() * 1000) != 0) {
                    if (!entry.getKey().equals(sth.getKey())) {
                        linksData.put("source", indexes.indexOf(entry.getKey()));
                        int targetValue = targetIndexes.indexOf(sth.getKey());
                        linksData.put("target", targetValue);
                        linksData.put("value", 1);
                        linksData.put("length", (sth.getValue() * 1000));
                        listLinks.add(linksData);

                        //for me to control before creating a json file
                        System.out.println(indexes.indexOf(entry.getKey()) + entry.getKey() + " to " + targetValue + sth.getKey() + " length: " + (sth.getValue() * 1000));
                    }
                }
            }

        }
        System.out.println(getEdges);
        root.put("links", listLinks);
        //creates new json file and print solution into it
        try {
            PrintWriter writer = new PrintWriter("web/countOcc.json", "UTF-8");
            writer.print(root);
            writer.close();
        } catch (Exception e) {
            System.out.println("could not output file. Path is incorrect");
        }
    }


public HashMap getOccur() {
        return entities;
    }
}
