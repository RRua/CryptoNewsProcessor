package Utils;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Contraction {
    public  static Map<String, Pair<String, String>> known_contractions = new HashMap<>();
    public  static Map<String, Pair<String, String>> linguakit_contractions = new HashMap<>();


    public Contraction() {
        this.init();
    }


    public boolean hasContraction(String word){
        return known_contractions.containsKey(word);
    }
    public Pair<String,String> getDecontraction(String word){
        return known_contractions.get(word);
    }




    private void init() {

        // from preposition a with def articl
        known_contractions.put("à", new Pair<>("a", "a"));
        known_contractions.put("às", new Pair<>("a", "as"));
        known_contractions.put("ao", new Pair<>("a", "o"));
        known_contractions.put("aos", new Pair<>("a", "os"));
        // from preposition a with dem pron
        known_contractions.put("àquele", new Pair<>("a", "aquele"));
        known_contractions.put("àquela", new Pair<>("a", "aquela"));
        known_contractions.put("àquelas", new Pair<>("a", "aquelas"));
        known_contractions.put("àquilo", new Pair<>("a", "aquilo"));
        known_contractions.put("aonde", new Pair<>("a", "onde"));

        // from preposition com with indef art
        known_contractions.put("cum", new Pair<>("com", "um"));
        // from preposition com with personal pron
        known_contractions.put("comigo", new Pair<>("com", "mim"));
        known_contractions.put("contigo", new Pair<>("com", "ti"));
        known_contractions.put("consigo", new Pair<>("com", "si"));
        known_contractions.put("conosco", new Pair<>("com", "nós"));
        known_contractions.put("convosco", new Pair<>("com", "vós"));

        // from preposition de
        known_contractions.put("do", new Pair<>("de", "o"));
        known_contractions.put("da", new Pair<>("de", "a"));
        known_contractions.put("das", new Pair<>("de", "as"));
        known_contractions.put("dos", new Pair<>("de", "os"));
        //
        known_contractions.put("dum", new Pair<>("de", "um"));
        known_contractions.put("duns", new Pair<>("de", "uns"));
        known_contractions.put("duma", new Pair<>("de", "uma"));
        known_contractions.put("dumas", new Pair<>("de", "umas"));
        //
        known_contractions.put("dele", new Pair<>("de", "ele"));
        known_contractions.put("dela", new Pair<>("de", "ela"));
        known_contractions.put("deles", new Pair<>("de", "eles"));
        known_contractions.put("delas", new Pair<>("de", "elas"));
        //
        known_contractions.put("deste", new Pair<>("de", "este"));
        known_contractions.put("desta", new Pair<>("de", "esta"));
        known_contractions.put("destes", new Pair<>("de", "estes"));
        known_contractions.put("destas", new Pair<>("de", "estas"));
        known_contractions.put("disto", new Pair<>("de", "isto"));
        known_contractions.put("disso", new Pair<>("de", "isso"));
        known_contractions.put("desse", new Pair<>("de", "esse"));
        known_contractions.put("desses", new Pair<>("de", "estes"));
        known_contractions.put("dessa", new Pair<>("de", "essa"));
        known_contractions.put("dessas", new Pair<>("de", "essas"));
        known_contractions.put("daquilo", new Pair<>("de", "aquilo"));
        known_contractions.put("daquele", new Pair<>("de", "aquele"));
        known_contractions.put("daquela", new Pair<>("de", "aquela"));
        //
        known_contractions.put("doutro", new Pair<>("de", "outro"));
        known_contractions.put("doutra", new Pair<>("de", "outra"));
        known_contractions.put("doutros", new Pair<>("de", "outros"));
        known_contractions.put("doutras", new Pair<>("de", "outras"));
        //
        known_contractions.put("daqui", new Pair<>("de", "aqui"));
        known_contractions.put("daí", new Pair<>("de", "aí"));
        known_contractions.put("dali", new Pair<>("de", "ali"));
        known_contractions.put("dáquem", new Pair<>("de", "aquém"));
        known_contractions.put("dálem", new Pair<>("de", "além"));
        known_contractions.put("donde", new Pair<>("de", "onde"));

        // from preposition em
        known_contractions.put("no", new Pair<>("em", "o"));
        known_contractions.put("na", new Pair<>("em", "a"));
        known_contractions.put("nos", new Pair<>("em", "os"));
        known_contractions.put("nas", new Pair<>("em", "as"));
        //
        known_contractions.put("num", new Pair<>("em", "um"));
        known_contractions.put("nuns", new Pair<>("em", "uns"));
        known_contractions.put("numa", new Pair<>("em", "uma"));
        known_contractions.put("numas", new Pair<>("em", "umas"));
        //
        known_contractions.put("nele", new Pair<>("em", "ele"));
        known_contractions.put("nela", new Pair<>("em", "ela"));
        known_contractions.put("neles", new Pair<>("em", "eles"));
        known_contractions.put("nelas", new Pair<>("em", "elas"));
        //
        known_contractions.put("neste", new Pair<>("em", "este"));
        known_contractions.put("nesta", new Pair<>("em", "esta"));
        known_contractions.put("nestes", new Pair<>("em", "estes"));
        known_contractions.put("nestas", new Pair<>("em", "estas"));
        known_contractions.put("nisto", new Pair<>("em", "isto"));
        known_contractions.put("nesse", new Pair<>("em", "esse"));
        known_contractions.put("nessa", new Pair<>("em", "essa"));
        known_contractions.put("nesses", new Pair<>("em", "esses"));
        known_contractions.put("nessas", new Pair<>("em", "essas"));
        known_contractions.put("nisso", new Pair<>("em", "isso"));
        known_contractions.put("naquele", new Pair<>("em", "aquele"));
        known_contractions.put("naquela", new Pair<>("em", "aquela"));
        known_contractions.put("naqueles", new Pair<>("em", "aqueles"));
        known_contractions.put("naquelas", new Pair<>("em", "aquelas"));
        known_contractions.put("naquilo", new Pair<>("em", "aquilo"));

        // from preposition para
        known_contractions.put("pro", new Pair<>("para", "o"));
        known_contractions.put("pra", new Pair<>("para", "a"));
        known_contractions.put("pros", new Pair<>("para", "os"));
        known_contractions.put("pras", new Pair<>("para", "as"));
        //
        known_contractions.put("prum", new Pair<>("para", "um"));
        known_contractions.put("pruns", new Pair<>("para", "uns"));
        known_contractions.put("pruma", new Pair<>("para", "uma"));
        known_contractions.put("prumas", new Pair<>("para", "umas"));
        //
        known_contractions.put("pronde", new Pair<>("para", "onde"));

        // from preposition per
        known_contractions.put("pelo", new Pair<>("por", "o")); // should be "per" "a"
        known_contractions.put("pela", new Pair<>("por", "a"));// should be "per" "a"
        known_contractions.put("pelos", new Pair<>("por", "os"));// should be "per" "a"
        known_contractions.put("pelas", new Pair<>("por", "as"));// should be "per" "a"

/*
        linguakit_contractions.put("à",known_contractions.get("à"));
        linguakit_contractions.put("dele",known_contractions.get("dele"));
        linguakit_contractions.put("nisto",known_contractions.get("nisto"));
        linguakit_contractions.put("às",known_contractions.get("às"));
        linguakit_contractions.put("ao",known_contractions.get("ao"));
        linguakit_contractions.put("aonde",known_contractions.get("aonde"));
*/
        linguakit_contractions.putAll(known_contractions);
        linguakit_contractions.remove("dali");
        linguakit_contractions.remove("pro");
        linguakit_contractions.remove("consigo");
        linguakit_contractions.remove("conosco");
       // linguakit_contractions.remove("disso");
        //linguakit_contractions.remove("pela");
        //linguakit_contractions.put("pela", new Pair<>("por","a"));
        //TODO

    }
}
