import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        Filter filteredCharacters = new Filter("E:\\E_Dokumente\\semester7\\wolskaProject\\WolskaProject\\src\\LotF_Annotated.tsv");
        CheckChars checkChars = new CheckChars(filteredCharacters, "E:\\E_Dokumente\\semester7\\wolskaProject\\WolskaProject\\src\\LotFChars.txt");
        CountOccur countOccur = new CountOccur(checkChars, 2);
        countOccur.createJson();

        //hashmap in der jeder character einer anzahl zugemappt ist, wie oft er mit der (im Hintergrund ermittelten)
        //"Hauptperson" im selben Satz vorkommt.
        //Die Hauptperson ist die am häufigsten genannte.
        HashMap<String, HashMap<String, Integer>> occurrenceEdges = countOccur.getAllEdges();

    }


}
