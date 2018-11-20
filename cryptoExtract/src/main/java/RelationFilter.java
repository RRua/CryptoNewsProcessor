
import Utils.Contraction;
import javafx.util.Pair;

import java.util.*;

public class RelationFilter {

    public static Map<String,String> usefulRelations = new HashMap<>(); // lemmatized // relation -> Ontology relation
    public static Map<String,String> usefulCaracteristic = new HashMap<>(); // lemmatized // relation -> Ontology relation
    //public static Set<String> issueCaracteristic = new HashSet<>(); // lemmatized // relation -> Ontology relation
    //public static Set<String> coinCaracteristic = new HashSet<>(); // lemmatized // relation -> Ontology relation
    public  Map<String,String> entitiesMap = new HashMap<>(); // contains all entities and sinonyms

    public RelationFilter(){
       //usefulRelations.add("");
       usefulRelations.put("ser", "instanceOf");
       usefulRelations.put("ser um", "instanceOf");
       usefulRelations.put("fazer", "did");
       usefulRelations.put("dizer", "said");
       usefulRelations.put("declarar", "said");
        usefulRelations.put("afirmar", "said");
        usefulRelations.put("revelar", "said");
        usefulRelations.put("confidenciar", "said");
        usefulRelations.put("comentar", "said");
        usefulRelations.put("falar", "said");
        usefulRelations.put("relatar", "said");
        usefulRelations.put("encontrar", "said");
        usefulRelations.put("anunciar", "said");
        usefulRelations.put("aprovar", "said");
        usefulRelations.put("planear", "said");
        usefulRelations.put("projetar", "said");
        usefulRelations.put("servir", "said");
        usefulRelations.put("publicar", "said");

       usefulCaracteristic.put("programador","Person");
        usefulCaracteristic.put("presidente","Person");
        usefulCaracteristic.put("CEO","Person");
        usefulCaracteristic.put("fundador","Person");
        usefulCaracteristic.put("criador","Person");
        usefulCaracteristic.put("hacker","Person");
        usefulCaracteristic.put("acionista","Person");
        usefulCaracteristic.put("proprietário","Person");
        usefulCaracteristic.put("investigador","Person");
        usefulCaracteristic.put("secretário","Person");
        usefulCaracteristic.put("administrador","Person");
        usefulCaracteristic.put("bilionário","Person");
        usefulCaracteristic.put("milionário","Person");
        usefulCaracteristic.put("famoso","Person");
        usefulCaracteristic.put("governador","Person");
        usefulCaracteristic.put("deputado","Person");
        usefulCaracteristic.put("líder","Person");
        usefulCaracteristic.put("expert","Person");
        usefulCaracteristic.put("ministro","Person");

        usefulCaracteristic.put("vulnerabilidade","Issue");
        usefulCaracteristic.put("problema","Issue");
        usefulCaracteristic.put("atacar","Issue");
        usefulCaracteristic.put("ataque","Issue");
        usefulCaracteristic.put("malicioso","Issue");
        usefulCaracteristic.put("malicia","Issue");
        usefulCaracteristic.put("falhar","Issue");
        usefulCaracteristic.put("roubar","Issue");
        usefulCaracteristic.put("invadir","Issue");
        usefulCaracteristic.put("invasor","Issue");
        usefulCaracteristic.put("problema","Issue");

        usefulCaracteristic.put("declaração","Statement");
        usefulCaracteristic.put("afirmação","Statement");
        usefulCaracteristic.put("entrevista","Statement");
        usefulCaracteristic.put("comentário","Statement");

        usefulCaracteristic.put("sistema","Organization");

       // usefulCaracteristic.put("");


   }


    public void init(Map<String, String> relevantEntities) { // relevantEntities = [(entity name, type)]
       Map<String,String> spa = new HashMap<>();
        for (String p : relevantEntities.keySet()){
            if (relevantEntities.get(p).equals("Person")|| relevantEntities.get(p).equals("Organization")){
                String [] sp = ((String) p).replaceAll("@","_").split("_");
                entitiesMap.put(((String) p), ((String) p));
                for ( String s : sp){
                    if (s.equals("de") || Contraction.linguakit_contractions.containsKey(s)){
                        continue;
                    }
                    entitiesMap.put(s,  p);
                    spa.putIfAbsent(s,  relevantEntities.get(p));
                }
            }
            else {
                entitiesMap.put(((String) p), ((String) p));
                spa.putIfAbsent(p,  relevantEntities.get(p));
            }

        }
        relevantEntities.putAll(spa );
    }


   public String getContainedRelation(String relation){
        if (relation==null|| relation.equals(""))
            return null;
        for (String r : usefulRelations.keySet()){
            if (relation.matches(r)){
                return r;
            }
        }
        return null;
   }


   public Set<Triple<String,String,String>> filterRelevantRelations ( TextProcessor tp, Map<String,String> relevantEntities){
       Set<Triple<String,String,String>> l2 = new HashSet<>();
       String lastPerson = null;
       if (tp==null|| tp.getRelations().isEmpty()){
           return l2;
       }
       for (Triple<String,String,String> t : tp.getRelations()){
           String relation = t.second.replaceAll("@","_"), subj = t.first.replaceAll("@","_"), pred = t.third.replaceAll("@","_");
           String verb = tp.getVerbOfProcessedSentence(relation);
           String rel = getContainedRelation(verb);
           if (rel!=null){
               // infer subject
               Pair<String,String> p = subjectIsEntity(relevantEntities, subj);
               String entitySubject = p.getKey();
               boolean relSubj = subj.toLowerCase().matches("(el(e|a)(s)?)|(est(e|a)(s)?)");
               if (p.getValue().equals("Person")|| relSubj){ // is person
                   if(relSubj){
                        entitySubject = lastPerson;
                   }
                   Set<String> set = this.inferRelationship(rel,pred,"Person", entitySubject);
                   if (set.isEmpty()){
                       System.out.println( entitySubject + " " + usefulRelations.get(rel) + " " + " cena THINGS (" + pred + " )");
                   }
                   else {
                       for (String s : set){
                           System.out.println( entitySubject + " " + usefulRelations.get(rel) + " " + s);
                       }
                   }
                   lastPerson=entitySubject;
               }
               else {
                   Set<String> set = this.inferRelationship(rel,pred, p.getValue(), entitySubject);
                   if (set.isEmpty()){
                       System.out.println( subj + " " + usefulRelations.get(rel) + " " + " UNRELEVANT THINGS (" + pred + " )");
                   }
                   else {
                       for (String s : set){
                           System.out.println( subj + " " + usefulRelations.get(rel) + " " + s);
                       }
                   }
               }

           }


       }



        return l2;
   }

   // subj is an entity if name is in relevant entities or an relevant entity matches the subj
    // return type of entity (Person, organization,...)
    private Pair<String,String> subjectIsEntity(Map<String, String> relevantEntities, String subj) {
       for ( String pair : relevantEntities.keySet() ){
           if (subj.toLowerCase().equals(pair.toLowerCase())|| subj.toLowerCase().matches(pair.toLowerCase()) || pair.toLowerCase().matches(subj.toLowerCase())|| subj.replaceAll("_","").contains(pair.replaceAll("_","")) ){
               return new Pair<>(pair,relevantEntities.get(pair));
           }
       }
       for ( String pair : entitiesMap.keySet() ){
           if (subj.toLowerCase().equals(pair.toLowerCase())|| subj.toLowerCase().matches(pair.toLowerCase()) || pair.toLowerCase().matches(subj.toLowerCase())|| subj.replaceAll("_","").contains(pair.replaceAll("_",""))){
               return new Pair<>(pair,relevantEntities.get(pair));
           }
       }
       return new Pair<>("","");
    }


    // sees if predicative contains useful information ( in this work context )
    public Set<String> inferRelationship(String verb, String pred, String type, String subj){
       Set<String> set = new HashSet<>();
       String rel = usefulRelations.get(verb);
       String [] sts = pred.split(" ");
       for (String s : sts){
           if ( usefulCaracteristic.containsKey(s.toLowerCase()) && ( type.equals(usefulCaracteristic.get(s.toLowerCase())) || type.equals("Thing") )){
               if (!type.equals("Person")){
                   set.add(usefulCaracteristic.get(s.toLowerCase())==null? type : usefulCaracteristic.get(s.toLowerCase()));
               }
               else
                   set.add(s.toLowerCase());
           }
           else if (entitiesMap.containsKey(s.toLowerCase())){
               set.add(s.toLowerCase());
           }
       }
       mapEntities(subj,set,rel);
       return set;
    }

    private void mapEntities(String subj, Set<String> set, String rel) {
        for (String s : set){
            if (rel.equals("instanceOf")){
                entitiesMap.put(s,subj);
            }
        }
    }

}
