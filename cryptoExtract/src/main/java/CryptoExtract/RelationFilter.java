package CryptoExtract;

import Utils.Contraction;
import javafx.util.Pair;

import java.util.*;

public class RelationFilter {

    public static Map<String,String> usefulRelations = new HashMap<>(); // lemmatized // relation -> Ontology relation
    public static Map<String,String> usefulCaracteristic = new HashMap<>(); // lemmatized // relation -> Ontology relation
    //public static Set<String> issueCaracteristic = new HashSet<>(); // lemmatized // relation -> Ontology relation
    //public static Set<String> coinCaracteristic = new HashSet<>(); // lemmatized // relation -> Ontology relation
    public  Map<String,String> entitiesMap = new HashMap<>(); // contains all entities and sinonyms
    public static BaseKnowledge base = null;
    public String lastEntity = null;

    public RelationFilter( BaseKnowledge b){
       //usefulRelations.add("");

        // relations = verbs or set of words with verb
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
        usefulRelations.put("indicar", "said");
        usefulRelations.put("apontar", "said");
        usefulRelations.put("aprovar", "said");
        usefulRelations.put("planear", "said");
        usefulRelations.put("projetar", "said");
        usefulRelations.put("servir", "said");
        usefulRelations.put("publicar", "said");


        usefulRelations.put("ter", "has");
        usefulRelations.put("possuir", "has");

        usefulRelations.put("usar", "Action");
        usefulRelations.put("adoptar", "Action");

        usefulRelations.put("subir", "State");
        usefulRelations.put("descer", "State");
        usefulRelations.put("cair", "State");
        usefulRelations.put("baixar", "State");
        usefulRelations.put("descender", "State");
        usefulRelations.put("comprar", "Action");
        usefulRelations.put("vender", "Action");
        usefulRelations.put("ascender", "State");
        usefulRelations.put("diminuir", "State");
        usefulRelations.put("aumentar", "State");
        usefulRelations.put("estar", "State");
        usefulRelations.put("manter", "State");
        usefulRelations.put("estabilizar", "State");
        usefulRelations.put("adquirir", "Action");
        usefulRelations.put("queda", "State");
        usefulRelations.put("subida", "State");
        usefulRelations.put("investir", "Action");
        usefulRelations.put("negociar", "Action");
        usefulRelations.put("transacionar", "Action");


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
        usefulCaracteristic.put("técnico","Person");
        usefulCaracteristic.put("analista","Person");
        usefulCaracteristic.put("usuário","Person");


        usefulCaracteristic.put("hashrate","Currency");

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
        usefulCaracteristic.put("queda","Issue");

        usefulCaracteristic.put("declaração","Statement");
        usefulCaracteristic.put("afirmação","Statement");
        usefulCaracteristic.put("entrevista","Statement");
        usefulCaracteristic.put("comentário","Statement");

        usefulCaracteristic.put("sistema","Organization");
        usefulCaracteristic.put("aplicativo","Organization");
        usefulCaracteristic.put("instituição","Organization");
        usefulCaracteristic.put("plataforma","Organization");



        base =  b;

       // usefulCaracteristic.put("");


   }


    public void init(Map<String, String> relevantEntities) { // relevantEntities = [(entity name, type)]
       Map<String,String> spa = new HashMap<>();
        for (String p : relevantEntities.keySet()){
            if (relevantEntities.get(p).equals("Person")|| relevantEntities.get(p).equals("Organization")){
                String [] sp = ((String) p).replaceAll("@","_").split("_");
                entitiesMap.put(((String) p).replace("de_a", "da").replace("de_as", "das").replace("de_o","do").replace("de_os", "dos"), ((String) p));
                for ( String s : sp){
                    if (s.equals("de") || Contraction.linguakit_contractions.containsKey(s)){
                        continue;
                    }
                    if (s.length()>1){
                        entitiesMap.put(s,  p);
                        spa.putIfAbsent(s,  relevantEntities.get(p));
                    }

                }
            }
            else {
                if (p.length()>1){
                    entitiesMap.put(((String) p), ((String) p));
                    spa.putIfAbsent(p,  relevantEntities.get(p));
                }

            }

        }
        relevantEntities.putAll(spa);
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


   public Triple<String,String,String> processRelation (Map<String,String> relevantEntities, String subj, String rel, String pred, TextProcessor tp){
       // infer subject
       Pair<String,String> p = subjectIsEntity(relevantEntities, subj, tp);
       String entitySubject = p.getKey();
       boolean relSubj = subj.toLowerCase().matches("(el(e|a)(s)?)|(est(e|a)(s)?)");
       if (p.getValue().equals("Person")|| relSubj){ // is person
           if(relSubj){
               entitySubject = lastEntity;
           }
           Set<String> set = this.inferRelationship(rel,pred,"Person", entitySubject,tp);
           if (set.isEmpty()){
               System.out.println( (entitySubject==null?subj:entitySubject) + " " + usefulRelations.get(rel) + " " + " cena THINGS (" + pred + " )");
           }
           else {
               for (String s : set){
                   System.out.println( entitySubject + " " + usefulRelations.get(rel) + " " + s);
                   return (new Triple<>(entitySubject , usefulRelations.get(rel), s));
               }
           }
           lastEntity=entitySubject;
       }
       else {
           Set<String> set = this.inferRelationship(rel,pred, p.getValue(), entitySubject,tp);
           if (set.isEmpty()){
               System.out.println( subj + " " + usefulRelations.get(rel) + " " + " UNRELEVANT THINGS (" + pred + " )");
           }
           else {
               for (String s : set){
                   System.out.println( subj + " " + usefulRelations.get(rel) + " " + s);
                    return (new Triple<>(subj , usefulRelations.get(rel), s));
               }
           }
       }
       return null;
   }


  /* public Set<Triple<String,String,String>> filterRelevantRelations ( TextProcessor tp, Map<String,String> relevantEntities){
       Set<Triple<String,String,String>> l2 = new HashSet<>();
       String lastPerson = null;
       if (tp==null|| tp.getRelations().isEmpty()){
           return l2;
       }
       for (Triple<String,String,String> t : tp.getRelations()){
           String relation = t.second.replaceAll("@","_"), subj = t.first.replaceAll("@","_"), pred = t.third.replaceAll("@","_");
           //System.out.println(" Triple -> " + subj + " , " + relation +" , " + pred);
           String verb = tp.getVerbOfProcessedSentence(relation);
           String rel = getContainedRelation(verb);
           if (rel!=null){
               l2.add(processRelation(relevantEntities,subj,rel,pred,tp));
           }
           else {
               // se o verbo da relacao nao pertence ao dominio das relacoes pretendidas, tentar encontrar o verbo no predicado
               verb = tp.getVerbOfProcessedSentence(pred);
               rel = getContainedRelation(verb);
               if (rel!=null){
                   l2.add(processRelation(relevantEntities,subj, rel, pred,tp));
               }
               else { // tentar encontrar eventos

               }
           }
       }
        return l2;
   }*/

    public Set<Triple<String,String,String>> filterByVerb(TextProcessor tp,  Collection<Triple<String,String,String>> triples){
        Set<Triple<String,String,String>> l2 = new HashSet<>();
        for (Triple<String,String,String> triple : triples){

        }


        return l2;
    }




   // subj is an entity if name is in relevant entities or an relevant entity matches the subj
    // return type of entity (Person, organization,...)
    private Pair<String,String> subjectIsEntity(Map<String, String> relevantEntities, String subject, TextProcessor tp) {
       for (String sujeito : subject.split(" ")){
           String subj = "";
           Pair sub = tp.getLemmaAndTagOfProcessedWord(sujeito);
           if (sub==null){
               continue;
           }
           else{
               subj= ((String) sub.getKey());
           }

           for ( String pair : relevantEntities.keySet() ){
               if (subj.toLowerCase().equals(pair.toLowerCase())|| subj.toLowerCase().matches(pair.toLowerCase()) || pair.toLowerCase().matches(subj.toLowerCase())|| subj.replaceAll("_","").contains(pair.replaceAll("_","")) ){
                   return new Pair<>(subj,relevantEntities.get(pair));
               }
           }
           for ( String pair : entitiesMap.keySet() ){
               if (subj.toLowerCase().equals(pair.toLowerCase())|| subj.toLowerCase().matches(pair.toLowerCase()) || pair.toLowerCase().matches(subj.toLowerCase())|| subj.replaceAll("_","").contains(pair.replaceAll("_",""))){
                   return new Pair<>(subj,relevantEntities.get(pair));
               }
           }
           if (usefulCaracteristic.containsKey(subj)){
               return new Pair<>(subj,usefulCaracteristic.get(subj));
           }
           else {
               String s = base.isRelevantEntity(subj);
               if (s!=null){
                   // is base of knowledge
                   return new Pair<>(subj,s);
               }
           }
       }
       return null;
    }


    // sees if predicative contains useful information ( in this work context )
    public Set<String> inferRelationship(String verb, String pred, String type, String subj, TextProcessor tp){
       Set<String> set = new HashSet<>();
       String rel = usefulRelations.get(verb);
       String [] sts = pred.split(" ");
       for (String st : sts){
           String s = tp.getLemmaAndTagOfProcessedWord(st).getKey();
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

    public Set<Triple<String,String,String>> filterBySubject(TextProcessor tp, Map<String, String> relevantEntities ){
        Set<Triple<String,String,String>> l2 = new HashSet<>();
        Set<Triple<String,String,String>> waitSet = new HashSet<>();
        String lastPerson = null;
        if (tp==null|| tp.getRelations().isEmpty()){
            return l2;
        }
        for (Triple<String,String,String> t : tp.getRelations()){
            String subj = t.first;
            Pair<String,String> p = subjectIsEntity(relevantEntities, subj, tp);
            if (p!=null){
                l2.add(t);
            }
            else{ // try to see if predicate contains relation
                Triple<String,String,String> trio = transformInRelationTriple("",t.third,tp);
                if (trio!=null){
                    waitSet.add(trio);
                }
            }
        }
        for (Triple<String,String,String> t : waitSet) {
            String subj = t.first;
            Pair<String,String> p = subjectIsEntity(relevantEntities, subj, tp);
            if (p!=null){
                l2.add(t);
            }
        }
        return l2;
    }


    public Triple<String,String,String> transformInRelationTriple(String prefix, String sentence, TextProcessor tp){
        StringBuilder suj = new StringBuilder();
        StringBuilder pred = new StringBuilder();
        boolean verbFound = false;
        String verb = tp.getVerbOfProcessedSentence(sentence);
        if (verb==null){
         return null;
        }
        for (String word : sentence.split(" ")){
            if(verb.equals(tp.getLemmaAndTagOfProcessedWord(word).getKey())){
                verbFound=true;
            }
            if(!verbFound){
                suj.append(word + " ");
            }
            else {
                pred.append(word + " ");
            }
        }
        return new Triple<String, String, String>(suj.toString(),verb,pred.toString());
    }

    // returns subject of relation ( can be simple or composed ( John ,users of system)
    public Pair<String,String> identifySubject(String subj, TextProcessor tp){
        List<Pair<String,String>> possibleSubjs = new ArrayList<>();
        Pair<String,String> pair = null;
        String [] arrayOfWords = subj.split(" ");
        for (String word : arrayOfWords){
            //String typeOfWord = tp.getLemmaAndTagOfProcessedWord(word).getValue();
            pair = subjectIsEntity(entitiesMap,word,tp);
            if(pair!=null){
                possibleSubjs.add(new Pair<>(word, pair.getValue()));
            }
        }

        if(possibleSubjs.size()==1){
            System.out.println(" So 1 sub " + possibleSubjs.get(0));
            return possibleSubjs.get(0);
        }
        else if (possibleSubjs.size()==2) { // try to understand the composed subject
            if (subj.matches(".*" +possibleSubjs.get(0).getKey() + "\\ (de)\\ (a|o)(s)?\\ " + possibleSubjs.get(1).getKey() + ".*")){
                // e.g usuarios de facebook
                System.out.println("sub 0" +possibleSubjs.get(0));
                System.out.println(" sub 1 " +possibleSubjs.get(1));
            }
        }
        return null;
    }

}
