import java.util.ArrayList;


public class Entity {

	/**
	 * @param args
	 */
	
	private String chapterId;
	private String sentenceId;
	private String lemma;
	private ArrayList<String> nicknames;
	
	public Entity(String ch, String se, String lem) {
		chapterId = ch;
		sentenceId = se;
		lemma = lem;
		nicknames = new ArrayList();
	}
	
	public void addName(String name) {
		nicknames.add(name);
	}
	
	public String getChapterId() {
		return chapterId;
	}
	
	public String getSentenceId() {
		return sentenceId;
	}
	
	public String getLemma() {
		return lemma;
	}

}
