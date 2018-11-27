package CryptoExtract;

import CryptoExtract.BaseKnowledge;
import Utils.Contraction;
import Utils.ExecutorTask;
import javafx.util.Pair;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;


public class Main {

    public static String tokenSeparator = " ";
    public static String ner = "Linguakit";


    public static List<String> executeCommand (String command){
        List<String> list = new ArrayList<>();
        String s = null;
        try {
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));
            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                list.add(s);
            }
            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

           // System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }

        return list;
    }


    public static WikiPediaProcessor getWikipediatext(String id) throws Exception {
        executeCommand("python -W ignore /Users/ruirua/Documents/PhD_Classes/AIE/work/wiki.py \""+ id + "\"");
        WikiPediaProcessor wp = new WikiPediaProcessor();
        String newsFile = "/Users/ruirua/Documents/PhD_Classes/AIE/work/wikipediaExtracted/"+id +".txt";
       try{
           List <String> l  = executeCommand("python  -W ignore  /Users/ruirua/Documents/PhD_Classes/AIE/work/nltk_example.py "+ newsFile);
           Contraction c = new Contraction();
           wp.readWordsFromFile("/Users/ruirua/Documents/PhD_Classes/AIE/work/words.txt", tokenSeparator );
       }catch (FileNotFoundException e){
           System.out.println("Entity not found in WIKIPEDIA ->" + id);
       }
        Map<Integer,String> sentences= wp.getSentences();

        System.out.println(" PoS tagging and Lemmatizing ...");
        ExecutorTask et = new ExecutorTask("/Users/ruirua/Documents/PhD_Classes/AIE/work/Linguakit-master/linguakit tagger -nec pt " +"/Users/ruirua/Documents/PhD_Classes/AIE/work/wikipediaExtracted/"+id +".txt" );
        Thread thread = new Thread(et);
        thread.start();
        thread.join();
        try {
            wp.wordsToPoS(et.returnList, tokenSeparator);
        }
        catch (Exception e){
            //e.printStackTrace();
            System.out.println("Error in PoS. Ignoring..");
        }

        System.out.println("Processing sentences");
        et = new ExecutorTask(" cat " + newsFile+ " | /Users/ruirua/Documents/PhD_Classes/AIE/work/Linguakit-master/linguakit rel pt ");
        thread = new Thread(et);
        thread.start();
        thread.join();
        for (String s : et.returnList) {
            wp.addRelation(s, "\t");
        }

        return wp;
    }




    public static String[] arrayFromFile(String file) throws Exception {
        BufferedReader abc = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<String>();
        String line = null;

        while((line = abc.readLine()) !=null){
                lines.add(line);
        }
        abc.close();
        return lines.toArray(new String[]{});
    }



    public static boolean processNews(NewsProcessor np) throws Exception{

        System.out.println(" Tokenizing ...");
        // Sentence split / tokenization ( using nltk)
        List <String> l  = executeCommand("python  -W ignore  /Users/ruirua/Documents/PhD_Classes/AIE/work/nltk_example.py "+ np.newsPath);
        Contraction c = new Contraction();
        np.readWordsFromFile("/Users/ruirua/Documents/PhD_Classes/AIE/work/words.txt", tokenSeparator );
        // np.decontractWordsNews(ner);
        Map<Integer,String> sentences= np.getSentences();
        System.out.println(" PoS tagging and Lemmatizing ...");
        ExecutorTask et = new ExecutorTask("/Users/ruirua/Documents/PhD_Classes/AIE/work/Linguakit-master/linguakit tagger -nec pt " + np.newsPath);
        Thread thread = new Thread(et);
        thread.start();
        thread.join();
        try {
            np.wordsToPoS(et.returnList, tokenSeparator);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Processing sentences");
        et = new ExecutorTask(" cat " +np.newsPath + " | /Users/ruirua/Documents/PhD_Classes/AIE/work/Linguakit-master/linguakit rel pt ");
        thread = new Thread(et);
        thread.start();
        thread.join();
        for (String s : et.returnList) {
            np.addRelation(s, "\t");
        }

        np.sentences= sentences;    // important


        /*
        for (String sentence : sentences.values()){
            i++;
            System.out.println("    Analysing relations in sentence " +i);
            et = new ExecutorTask(" echo \"" + sentence+ "\" | /Users/ruirua/Documents/PhD_Classes/AIE/work/Linguakit-master/linguakit rel pt ");
            thread = new Thread(et);
            thread.start();
            thread.join();
            for (String s : et.returnList){
                np.addRelation(s,"\t" );
            }
            System.out.println("     sentiment analysis off");
           /* System.out.println("    Analysing sentiment in sentence " +i);
            et = new ExecutorTask(" echo \"" + sentence+ "\" | /Users/ruirua/Documents/PhD_Classes/AIE/work/Linguakit-master/linguakit sent pt ");
            thread = new Thread(et);
            thread.start();
            thread.join();
            for (String s : et.returnList){
                System.out.println( "sentiment -> " +s);
            }


        }/*


       /* et = new ExecutorTask("/Users/ruirua/Documents/PhD_Classes/AIE/work/Linguakit-master/linguakit rel pt " + np.newsPath);
        thread = new Thread(et);
        thread.start();
        thread.join();
        np.wordsToPoS(et.returnList, tokenSeparator);*/

        return true;

    }





    public static Set<Triple<String,String,String>> filterRelations ( RelationFilter rel, TextProcessor np, Map<String,String> classifiedEntities){
        OntologyMapper om = new OntologyMapper();

        Set<Triple<String,String,String>> s = rel.filterBySubject(np,classifiedEntities);
        s= rel.filterByVerb(np,s);
        Map< String , Pair<String,String>>  subjects = new HashMap<>();
        Map< String , Set<Pair<String,String>>>  preds = new HashMap<>();
        for(Triple<String,String,String> t : s){
            Pair p = rel.identifySubject( t.first, np);
            if (p!=null){
                subjects.put(t.first, p );
            }
            Set se = rel.identifyPred(np  , t.third);
            if (se.size()>0){
                preds.put(t.third,se );
            }
        }
        Set<Triple<String,String,String>> usefulRelations = new HashSet<>();
        for(Triple<String,String,String> t : s){
             System.out.println("##Possibly Useful triple -> "+ t );
            if (subjects.containsKey(t.first)&& preds.containsKey(t.third)){
                Set<Triple<String,String,String>> temporary = rel.classifyTriple(t, subjects.get(t.first), preds.get(t.third), np);
                for (Triple<String,String,String> te : temporary){
                    usefulRelations.add(te);
                    //System.out.println("Triple " + te);
                    om.prepareSubjectToOntology(new Triple<>(te.first, subjects.get(t.first).getKey(), subjects.get(t.first).getValue()), "de" );
                    om.preparePredToOntology(t, te, preds.get(t.third), rel);
                }
            }
        }
        //System.out.println("RESULT: Extracted " + usefulRelations.size() + " triples from " + s.size() + " possible useful triples \n");

        OntologyBridge ob = new OntologyBridge();
        ob.insertEntityIntoOntology(om.relationsToSend);

        return usefulRelations;
    }



    public static void  classifyWithWikipedia( List<String> entitiesToClassify,Map<String,String> classifiedEntities, RelationFilter rel) throws Exception{
        WikiPediaProcessor[] wa = new WikiPediaProcessor[entitiesToClassify.size()];
        for (int i = 0; i <entitiesToClassify.size() ; i++) {
            String entity = entitiesToClassify.get(i);
            System.out.println("processing " + entity);
            wa[i] = getWikipediatext(entity);
            filterRelations(rel, wa[i], classifiedEntities);
        }
    }


    public static void main(String[] args) throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        boolean WikiOn = false;

        //String news= "/Users/ruirua/Documents/PhD_Classes/AIE/work/noticias/examples/not1-mcafee.txt";
         String news = "/Users/ruirua/Documents/PhD_Classes/AIE/work/noticias/examples/5580516263042677579.txt";
        //String news = "/Users/ruirua/Documents/PhD_Classes/AIE/work/noticias/6623327207871204033#2018-11-26.txt";
     //   String news= "/Users/ruirua/Documents/PhD_Classes/AIE/work/noticias/examples/not4-vulneravel.txt";
        String onto =null;
        BaseKnowledge base = new BaseKnowledge(onto);
        NewsProcessor np = new NewsProcessor();
        np.newsPath = news;
        processNews(np);
        Set<Pair<String,String>> ls = np.getRelevantEntities();
        ls.addAll(np.getOtherRelevantEntities(base));
        Map<String,String> classifiedEntities = new HashMap<>();
        List<String> entitiesToClassify = new ArrayList<>();
        for (Pair<String,String> s :ls){
            String type = base.getType(s.getKey(),s.getValue());
            System.out.println("ENTITY -> " + s.getKey()  + "  " + s.getValue()+ " TYPE -> " + type);
            if (type.equals("Person")|| type.equals("Thing")|| type.equals("Location")){
                entitiesToClassify.add(s.getKey());
                if (type.equals("Person")){
                    classifiedEntities.put(s.getKey(),type);
                }
            }
            else {
                classifiedEntities.put(s.getKey(),type);
            }
        }
        np.wordsIndexList.clear();
        RelationFilter rel = new RelationFilter(base);
        rel.init(classifiedEntities);

       if (WikiOn){
           System.out.println(" WIKI processing");
           classifyWithWikipedia(entitiesToClassify,classifiedEntities,rel);
       }

        Set<Triple<String,String,String>> usefulRelations = filterRelations(rel, np, classifiedEntities);
        for (Triple<String,String,String> tr : usefulRelations){
            System.out.println(" Resultant Triple "+ tr);
        }

        System.out.println(" END " );
    }
}


