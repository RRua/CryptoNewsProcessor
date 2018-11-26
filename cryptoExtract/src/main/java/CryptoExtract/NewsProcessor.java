package CryptoExtract;

import Utils.Contraction;
import javafx.util.Pair;
import org.apache.jena.base.Sys;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class NewsProcessor implements TextProcessor {

    public String newsPath= "";
    Map<String,LinkedList<Triple<Integer,Integer,String>>> wordsIndexList = new HashMap();
    public Map<String, Set<PoSTriple>> news_pos_info = new HashMap<>(); // tag -> (word - > (word, lemma, tag)
    public List<Triple<String,String, String>> news_relations_triples = new ArrayList<>();





    public void addRelation(String s , String separator){
       String [] x = s.split(separator);
       if (x.length==4){
           Triple<String, String, String> t = new Triple<>(x[1],x[2],x[3]);
           this.news_relations_triples.add(t);
       }
       else{
           System.out.println("BAD RELATION -> " +s);
       }
    }


    public Map<Integer,String> getSentences(){
        Map<Integer,TreeSet<Triple<Integer,Integer,String>>> map = new HashMap<>();
        for (LinkedList<Triple<Integer,Integer,String>> l : this.wordsIndexList.values()){
            for (Triple<Integer,Integer,String> t  : l){
                if (map.containsKey(t.first)){
                    map.get(t.first).add(t);
                }
                else {
                    TreeSet<Triple<Integer,Integer,String>> tt = new TreeSet<>(new TripleComparator());
                    tt.add(t);
                    map.put(t.first,tt);
                }
            }
        }
        Map<Integer,String> ret = new HashMap<>();
        for (TreeSet<Triple<Integer,Integer,String>> ts : map.values()){
            StringBuilder sb = new StringBuilder();
            for (Triple<Integer,Integer,String> triple : ts){
               sb.append(" " +triple.third);
            }
            ret.put(((Integer) ts.first().first), sb.toString());
        }
        return ret;
    }




    public static void fillMapWithTriple(Map m , List<Triple> list)  {

        for( Triple t : list){

            if(m.containsKey(t.third)){
                 Set x = ((Set) m.get(t.third));
                 x.add(t);
            }
            else {
                Set<Triple> s = new HashSet<>();
                s.add(t);
                m.put(t.third, s);
            }
        }
        //TODO

    }

    // receives tuples ( word, lemma ,tag)
    public void wordsToPoS(List<String> listPoS , String tokenSeparator) throws Exception {
        Pair<Integer,Integer> pair = new Pair<>(0,0);
        for(String s : listPoS){
            String [] toks = s.split(tokenSeparator);
            if (s.equals("") || s.equals("\n") || s.contains("<blank>") || s.equals(".")){
                continue;
            }
            if (toks.length<3 ){
                System.out.println("!!!error in  NER " +s);
            }
            String word = toks[0];
            String lemma = toks[1];
            String tag = toks[2];

            if (word.contains("_")) { // if linguakit merged two (or more) words in NER
                String [] xx = word.split("_");
                for (String x : xx){
                    //System.out.println("splitted _ " + x);
                    Triple t = wordsIndexList.get(x).pollFirst();
                    if (t==null) {
                        for (String st : wordsIndexList.keySet()){
                            if (word.matches(st)){
                                if (word.contains("\\.")){
                                    t = wordsIndexList.get(st).pollFirst();
                                }
                            }
                        }
                        if (t==null)
                            throw new Exception(" word not found!! ");
                    }
                    pair = new Pair(t.first,t.second);
                    PoSTriple pt = new PoSTriple(((Integer) t.first), ((Integer) t.second), new Triple<String,String, String>(word,toks[1],toks[2]));
                    if (this.news_pos_info.containsKey(tag)){
                        this.news_pos_info.get(tag).add(pt);
                    }
                    else {
                        Set<PoSTriple> ll = new HashSet<>();
                        ll.add(pt);
                        this.news_pos_info.put(tag,ll);
                    }

                }
            }
            else {
                Triple t = null;
                //System.out.println(" WORD -> " + word);
                try {
                    t= wordsIndexList.get(word).pollFirst();
                }
                catch (NullPointerException e){
                    if (lemma.contains("+"))
                        continue;
                    else{
                        if (word.matches("[0-9]*.*") && word.contains("%")){
                            tag= "PVALUE"; // TODO
                            PoSTriple pt =  new PoSTriple(-1, -1, new Triple<String,String, String>(((String)word),word,tag));
                            if (this.news_pos_info.containsKey(tag)){
                                this.news_pos_info.get(tag).add(pt);
                            }
                            else {
                                Set<PoSTriple> ll = new HashSet<>();
                                ll.add(pt);
                                this.news_pos_info.put(tag,ll);
                            }
                        }
                        else{
                            if (Contraction.known_contractions.containsKey(word)){
                                Pair<String,String> p = Contraction.known_contractions.get(word);
                                String [] x = { p.getKey(), p.getValue()};
                                for(String string : x){
                                    t= wordsIndexList.get(string).pollFirst();
                                    PoSTriple pp = new PoSTriple(((Integer) t.first), ((Integer) t.second), new Triple(t.third,t.third, tag));
                                    if (this.news_pos_info.containsKey(tag)){
                                        this.news_pos_info.get(tag).add(pp);
                                    }
                                    else {
                                        Set<PoSTriple> ll = new HashSet<>();
                                        ll.add(pp);
                                        this.news_pos_info.put(tag,ll);
                                    }
                                }
                            }
                            else{
                                try {
                                    word = word.replaceAll("[^\\x00-\\x7F]", "").replaceAll("@","");
                                    t= wordsIndexList.get(word).pollFirst();
                                    PoSTriple pp = new PoSTriple(((Integer) t.first), ((Integer) t.second), new Triple(t.third,t.third, tag));
                                    if (this.news_pos_info.containsKey(tag)){
                                        this.news_pos_info.get(tag).add(pp);
                                    }
                                    else {
                                        Set<PoSTriple> ll = new HashSet<>();
                                        ll.add(pp);
                                        this.news_pos_info.put(tag,ll);
                                    }

                                }
                                catch (NullPointerException ex){
                                    PoSTriple pt =  new PoSTriple(-1, -1, new Triple<String,String, String>(((String)word),word,tag));
                                    if (this.news_pos_info.containsKey(tag)){
                                        this.news_pos_info.get(tag).add(pt);
                                    }
                                    else {
                                        Set<PoSTriple> ll = new HashSet<>();
                                        ll.add(pt);
                                        this.news_pos_info.put(tag,ll);
                                    }
                                    System.out.println("Error in word ->" + word);
                                }
                                //e.printStackTrace();
                            }
                        }

                    }

                    // try to retrieve the correct match
                    for (String st : wordsIndexList.keySet()){
                        if (st.matches((word.substring(0, (word.length()>2? word.length()-2 : 2))))){
                            // is a candidate
                            t =  wordsIndexList.get(st).pollFirst();
                            if (isNear(((Integer) t.first), ((Integer) t.second),((Integer) pair.getKey()), ((Integer) pair.getValue()))){
                                break;
                            }
                        }
                    }
                    if (t==null){
                        continue;
                    }
                    else {
                        try {
                            word = word.replaceAll("[^\\x00-\\x7F]", "").replaceAll("@","");
                            t= wordsIndexList.get(word).pollFirst();
                            PoSTriple pp = new PoSTriple(((Integer) t.first), ((Integer) t.second), new Triple(t.third,t.third, tag));
                            if (this.news_pos_info.containsKey(tag)){
                                this.news_pos_info.get(tag).add(pp);
                            }
                            else {
                                Set<PoSTriple> ll = new HashSet<>();
                                ll.add(pp);
                                this.news_pos_info.put(tag,ll);
                            }

                        }
                        catch (NullPointerException ex){
                            System.out.println(" Error in word ->" + word + " . ignoring...");
                        }
                    }
                }
                //System.out.println(word);
                PoSTriple pt = null;
               try{
                   pt =  new PoSTriple(((Integer) t.first), ((Integer) t.second), new Triple<String,String, String>(((String) t.third),toks[1],toks[2]));
                   pair = new Pair(t.first,t.second);
                   if (this.news_pos_info.containsKey(tag)){
                       this.news_pos_info.get(tag).add(pt);
                   }
                   else {
                       Set<PoSTriple> ll = new HashSet<>();
                       ll.add(pt);
                       this.news_pos_info.put(tag,ll);
                   }
               }
               catch (Exception e){
                   System.out.println("Error in word -> " + word);
               }

            }
        }

    }



    public void readWordsFromFile(String filepath, String tokenSeparator) throws IOException {
        BufferedReader abc = new BufferedReader(new FileReader(filepath));
        String line = null;
        int old_sentence_index = 0,  sentence_index =0, wordIncrement=0;
        while((line = abc.readLine()) !=null){
            String [] tokens = line.split(tokenSeparator);
            if (tokens.length==3){
                old_sentence_index = sentence_index;
                sentence_index = Integer.parseInt(tokens[0]);
                if (sentence_index>old_sentence_index){
                    wordIncrement=0;
                }

                int word_index = Integer.parseInt(tokens[1]);
                word_index+=wordIncrement;
                String word = tokens[2];
                if (Contraction.linguakit_contractions.containsKey(word.toLowerCase())){
                    Pair x = Contraction.linguakit_contractions.get(word.toLowerCase());
                    Triple<Integer,Integer,String> t1 = new Triple(sentence_index,word_index, ((String) x.getKey()));
                    Triple<Integer,Integer,String> t2 = new Triple(sentence_index,word_index+1, ((String) x.getValue()));
                    wordIncrement++;
                    if (this.wordsIndexList.containsKey(((String) x.getKey()))){
                        this.wordsIndexList.get(x.getKey()).add(t1);
                    }
                    else {
                        LinkedList<Triple<Integer,Integer,String>> ll = new LinkedList<>();
                        ll.add(t1);
                        this.wordsIndexList.put(((String) x.getKey()),ll);
                    }
                    if (this.wordsIndexList.containsKey(((String) x.getValue()))){
                        this.wordsIndexList.get(x.getValue()).add(t2);
                    }
                    else {
                        LinkedList<Triple<Integer,Integer,String>> ll = new LinkedList<>();
                        ll.add(t2);
                        this.wordsIndexList.put(((String) x.getValue()),ll);
                    }
                }
                else {
                    if (word.contains("-")){
                        // split in two
                        for (String s : word.split("-")){
                            Triple<Integer,Integer,String> t = new Triple(sentence_index,word_index+wordIncrement,s);
                            if (this.wordsIndexList.containsKey(s)){
                                this.wordsIndexList.get(s).add(t);
                            }
                            else {
                                LinkedList<Triple<Integer,Integer,String>> ll = new LinkedList<>();
                                ll.add(t);
                                this.wordsIndexList.put(s,ll);
                            }
                            wordIncrement++;
                        }

                    }

                    Triple<Integer,Integer,String> t = new Triple(sentence_index,word_index,word);
                    if (this.wordsIndexList.containsKey(word)){
                        this.wordsIndexList.get(word).add(t);
                    }
                    else {
                        LinkedList<Triple<Integer,Integer,String>> ll = new LinkedList<>();
                        ll.add(t);
                        this.wordsIndexList.put(word,ll);
                    }
                }

            }
        }
        abc.close();
    }


    public boolean isNear( int sentence_index , int word_index, int last_sent_index, int last_word_index){
        if (sentence_index-last_sent_index <=1){
            if (word_index-last_sent_index<=2){
                return true;
            }
        }
        return false;
    }


    public void decontractWordsNews(String nerTool) {

        for(String st : wordsIndexList.keySet()){
            LinkedList l = wordsIndexList.get(st);
            for (Triple pt : wordsIndexList.get(st)){
                if (nerTool.equals("Linguakit")){
                    if (Contraction.linguakit_contractions.containsKey(pt.third)){
                        // deContract
                        Pair x = new Contraction().getDecontraction(((String) pt.third));
                        l.add(l.indexOf(pt),new Triple(pt.first,pt.second,x.getKey()));
                        Triple<Integer,Integer,String> t = new Triple(pt.first, ((Integer) pt.second)+1, x.getValue());
                        l.add(l.indexOf(pt)+1, t);
                        for (int i = l.indexOf(pt)+2; i < l.size() ; i++) {
                            Triple tt = ((Triple<Integer, Integer, String>) l.get(i));
                            l.add(i, (new Triple<Integer,Integer,String>(((Integer) tt.first), ((Integer) tt.second)+1, (String)tt.third)));
                        }
                    }
                }
            }
        }

    }

    public Set<Pair<String,String>> getOtherRelevantEntities(BaseKnowledge base){
        Set<Pair<String,String>> set = new HashSet<>();
        for (String s : news_pos_info.keySet()){
            if (s.matches("NC.*")){
                for (PoSTriple t : news_pos_info.get(s)){
                    String word = t.triple.first;
                    String ent = base.isRelevantEntity(word.toLowerCase());
                    if(ent!=null){
                        set.add(new Pair<>(word,ent));
                    }
                }
            }
        }
        return set;
    }

    public Set<Pair<String,String>> getRelevantEntities (){
        Set<Pair<String,String>> l = new HashSet<>();
        for (String s : news_pos_info.keySet()){
            if (s.matches("NP.*")|| s.matches("W") || s.matches("PVALUE") || s.matches("Z")){
                for (PoSTriple t : news_pos_info.get(s)){
                    if (s.matches("PVALUE")){
                        if(t.triple.first.contains("%")){
                            l.add(new Pair(t.triple.first, "PERCENTAGE"));
                        }
                        else if(t.triple.first.contains(":")){
                            l.add(new Pair(t.triple.first,"HOUR"));
                        }
                        else {
                            l.add(new Pair(t.triple.first, t.triple.third));
                        }
                    }
                    else
                        l.add(new Pair(t.triple.first, t.triple.third));
                }
            }
        }
        return l;
    }

    @Override
    public List<Triple<String, String, String>> getRelations() {
        return news_relations_triples;
    }

    @Override
    public Map<String, Set<PoSTriple>> getPoS() {
        return news_pos_info;
    }

    @Override
    public Pair<String,String> getLemmaAndTagOfProcessedWord(String word) {
        for (Set<PoSTriple> s : this.news_pos_info.values()){
            for (PoSTriple p : s){
                if (word.equals(p.triple.first))
                    return new Pair<>(p.triple.second,p.triple.third);
            }
        }
        return null;
    }

    @Override
    public String getVerbOfProcessedSentence(String sent) {
        String [] ss = sent.split(" ");
        for (int i = 0; i <ss.length ; i++) {
            String s= ss[i];
            if (s.contains("_")){
                continue;
            }
            Pair<String,String> p = getLemmaAndTagOfProcessedWord(s);
            if (p!=null && p.getValue().startsWith("V")){
                // ITS A VERB
                boolean moreVerbs = false;
                String otherVerb= null;
                if (i+1<ss.length){ // check if following word is also a verb // e.g "será feito"
                    for (int j = i+1; j < ss.length; j++) {
                        String s2 = ss[j];
                        if (!s2.contains("_")){
                            Pair<String, String> p2 = getLemmaAndTagOfProcessedWord(s2);
                            if (p2!=null && p2.getValue().startsWith("V")) {
                                moreVerbs=true;
                                otherVerb=p2.getKey();
                            }
                        }
                    }
                    if (moreVerbs){
                        return p.getKey() + " " + otherVerb;
                    }
                    else{
                        return p.getKey();
                    }
                }
                else {
                    return p.getKey();
                }
            }
        }
        return null;
    }

    @Override
    public boolean isSingular(String word) {
        Pair x = getLemmaAndTagOfProcessedWord(word);
        if (x!=null){
            String tag = ((String) x.getValue()).replaceAll("[0-9]","");
            if (tag.endsWith("P")){
                return false;
            }
        }
        else {
            return true;
        }
        return true;

    }
}