import java.util.HashMap;

/**
 * Main class to organize other classes, uses the other classes. Program is organized from here
 */
public class Main {

    public static void main(String[] args) {
        CountOccur test = new CountOccur(
                new CheckChars(
                        new Filter("/home/anke/SocialNetwork/TextFiles/TI_Annotated.tsv"),
                        "/home/anke/SocialNetwork/TextFiles/TI_Chars.txt"), 0);//<-- die 0 ist die chapter Id die man hier angeben kann. Wenn 0, wird einfach ganzes Buch genommen,
        //wenn andere Zahl das entsprechende Kapitel!
        HashMap<String, Integer> occurrances = test.getOccur();
        for(HashMap.Entry<String, Integer> entry : occurrances.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

}
