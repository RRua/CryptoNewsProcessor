package CryptoExtract;

import Utils.Contraction;
import javafx.util.Pair;
import org.apache.jena.tdb.store.Hash;

import javax.print.attribute.standard.JobStateReasons;
import java.util.*;

public class RelationFilter {

    public static Map<String,String> usefulRelations = new HashMap<>(); // lemmatized // relation -> Ontology relation
    public static Map<String,String> usefulCaracteristic = new HashMap<>(); // lemmatized // relation -> Ontology relation
    public static Map<String,String> truncatedEvents = new HashMap<>();
    public static Map<String,String> usefulNames = new HashMap<>();
    public static Map<String,String> usefulAdjectives = new HashMap<>();
    //public static Set<String> issueCaracteristic = new HashSet<>(); // lemmatized // relation -> Ontology relation
    //public static Set<String> coinCaracteristic = new HashSet<>(); // lemmatized // relation -> Ontology relation
    public  Map<String,String> entitiesMap = new HashMap<>(); // contains all entities and sinonyms
    public static BaseKnowledge base = null;
    public String lastEntity = null;
    public  Map<String,String> classifiedEntities = new HashMap<>();
    public String [] auxVerb = {"ser","estar", "ter", "haver","querer", "dever", "poder","conseguir",
        "pretender", "chegar", "tentar", "continuar", "começar" , "costumar" , "ir", "vir", "voltar",
            "tornar", "andar", "deixar" , "acabar", "poder"};


    public RelationFilter( BaseKnowledge b){
       //usefulRelations.add("");

        usefulNames.put("pre(c|ç).*", "Price");
        usefulNames.put("val(i|o).*", "Value");
        usefulNames.put("hashrate.*", "Value");
        usefulAdjectives.put("baix.*", "Decrease");
        usefulAdjectives.put("subi.*", "Increase");
        usefulAdjectives.put("dimin.*", "Decrease");
        usefulAdjectives.put("aument.*", "Increase");
        usefulAdjectives.put("ca(i|í)*", "Decrease");
        usefulAdjectives.put("levant.*", "Increase");
        usefulAdjectives.put("mant.*", "Neutral");
        usefulAdjectives.put("estabil.*", "Neutral");
        usefulAdjectives.put("ascend.*", "Increase");


        // relations = verbs or set of words with verb
       usefulRelations.put("ser", "instanceOf");
       usefulRelations.put("ser um", "instanceOf");
       usefulRelations.put("fazer", "did");
        truncatedEvents.put("f(a|e)z.*", "Action");
        usefulRelations.put("executar", "did");
        truncatedEvents.put("execut.*", "Action");
        usefulRelations.put("efetuar", "did");
        truncatedEvents.put("efetu.*", "Action");
        usefulRelations.put("reagir", "did");
        truncatedEvents.put("reag.*", "Action");
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

        usefulRelations.put("conter", "has");
        usefulRelations.put("ter", "has");
        usefulRelations.put("possuir", "has");
        usefulRelations.put("ver", "saw");
        truncatedEvents.put("ve.*", "saw");
        usefulRelations.put("usar", "Action");
        truncatedEvents.put("us(a|o).*", "Action");
        usefulRelations.put("adoptar", "Action");
        truncatedEvents.put("adopt(a|o).*", "Action");
        usefulRelations.put("subir", "State");
        truncatedEvents.put("subi.*", "State");
        usefulRelations.put("descer", "State");
        truncatedEvents.put("des(c|ç).*", "State");
        usefulRelations.put("cair", "State");
        truncatedEvents.put("cai.*", "State");
        truncatedEvents.put("caí.*", "State");
        usefulRelations.put("baixar", "State");
        truncatedEvents.put("baix.*", "State");
        usefulRelations.put("descender", "State");
        truncatedEvents.put("descend.*", "State");
        usefulRelations.put("comprar", "Action");
        truncatedEvents.put("compr.*", "Action");
        usefulRelations.put("vender", "Action");
        truncatedEvents.put("vend.*", "Action");
        usefulRelations.put("ascender", "State");
        truncatedEvents.put("ascend.*", "State");
        usefulRelations.put("diminuir", "State");
        truncatedEvents.put("diminu.*", "State");
        usefulRelations.put("aumentar", "State");
        truncatedEvents.put("aument.*", "State");
        usefulRelations.put("estar", "State");
        truncatedEvents.put("est.*", "State");
        usefulRelations.put("manter", "State");
        truncatedEvents.put("mante.*", "State");
        usefulRelations.put("estabilizar", "State");
        truncatedEvents.put("estabiliz(a|o|e).*", "State");
        usefulRelations.put("adquirir", "Action");
        truncatedEvents.put("adquir.*", "Action");
        usefulRelations.put("queda", "State");
        usefulRelations.put("subida", "State");
        usefulRelations.put("subir", "State");
        truncatedEvents.put("sub.*", "State");
        usefulRelations.put("investir", "Action");
        truncatedEvents.put("invest.*", "Action");
        usefulRelations.put("negociar", "Action");
        truncatedEvents.put("negoci.*", "Action");
        usefulRelations.put("transacionar", "Action");
        truncatedEvents.put("transacion.*", "Action");


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
        usefulCaracteristic.put("ataque","Issue");
        usefulCaracteristic.put("atacar","Issue");
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
        for (String type : relevantEntities.keySet()){
            if (relevantEntities.get(type).equals("Person")|| relevantEntities.get(type).equals("Organization")){
                String [] sp = ((String) type).replaceAll("@","_").split("_");
                entitiesMap.put(((String) type).replace("de_a", "da").replace("de_as", "das").replace("de_o","do").replace("de_os", "dos"), ((String) type));
                for ( String s : sp){
                    if (s.equals("de") || Contraction.linguakit_contractions.containsKey(s)){
                        continue;
                    }
                    if (s.length()>1){
                        entitiesMap.put(s,  type);
                        spa.putIfAbsent(s,  relevantEntities.get(type));
                    }

                }
            }
            else {
                if (type.length()>1){
                    entitiesMap.put(((String) type), ((String) type));
                    spa.putIfAbsent(type,  relevantEntities.get(type));
                }

            }
        }
        relevantEntities.putAll(spa);
        this.classifiedEntities =relevantEntities;
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
           if(relSubj&&p.getValue().equals("Person")){
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
           if (p.getValue().equals("Person")){
               lastEntity=entitySubject;
           }
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
            String rel = triple.second;
            String verb = tp.getVerbOfProcessedSentence(rel);
            if (verb==null){ // tentar corrigir
                if (containsAuxVerb(verb)){
                    verb = rel;
                    //System.out.println(" ulha o verbo  de " + rel+ " -> " + verb);
                    HashSet<String> set = new HashSet();
                    Triple<String,String,String> tt = new Triple<>(triple.first, verb, triple.third );
                    l2.add(tt);

                }
                else{
                    continue;
                }
            }
            else{ // OK
                //System.out.println(" ulha o verbo  de " + rel+ " -> " +verb);
                Triple<String,String,String> tt = new Triple<>(triple.first, verb, triple.third );
                l2.add(tt);
            }
        }

        // process l2 to remove unwanted relations
        Set<Triple<String,String,String>> ret = new HashSet<>();
        for (Triple<String,String,String> t : l2){
            String [] split = t.second.split(" ");
            if (split.length>1){ // 2 verbs
                String mainVerb = split[1];
                if (usefulRelations.containsKey(mainVerb)){
                    Triple<String,String,String> tt = new Triple<>(t.first, usefulRelations.get(mainVerb), t.third );
                    ret.add(tt);
                }
            }
            else { // only one verb
                if (usefulRelations.containsKey(t.second)){
                    Triple<String,String,String> tt = new Triple<>(t.first, usefulRelations.get(t.second), t.third );
                    ret.add(tt);
                }
            }
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
                   return new Pair<>(subj,classifiedEntities.get(pair));
               }
           }
           for ( String pair : entitiesMap.keySet() ){
               if (subj.toLowerCase().equals(pair.toLowerCase())|| subj.toLowerCase().matches(pair.toLowerCase()) || pair.toLowerCase().matches(subj.toLowerCase())|| subj.replaceAll("_","").contains(pair.replaceAll("_",""))){
                   return new Pair<>(subj,classifiedEntities.get(pair));
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
            if(verb.startsWith(word)){
                continue;
            }
            else if(verb.endsWith(tp.getLemmaAndTagOfProcessedWord(word).getKey())){
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
    /*
    public Map< Triple<String,String,String> , Triple<String,String,Set<String>>> identifySubject( Collection<Triple<String,String,String>> col, TextProcessor tp){
        Map< Triple<String,String,String> , Triple<String,String,Set<String>>> possibleSubjs = new HashMap<>();
        OntologyMapper om = new OntologyMapper();
        for( Triple<String,String,String> tsubj : col){
            String subj = tsubj.first;
            Pair<String,String> pair = null;
            String [] arrayOfWords = subj.split(" ");
            for (String word : arrayOfWords){
                //String typeOfWord = tp.getLemmaAndTagOfProcessedWord(word).getValue();
                pair = subjectIsEntity(entitiesMap,word,tp);
                if(pair!=null){
                    if (base.isType(pair.getValue())){ // se é um tipo considerado
                        possibleSubjs.put( tsubj ,new Triple<String, String, Set<String>>(pair.getValue(), null,new HashSet<>()));
                    }
                    else { // entao é uma entidade detetada
                        System.out.println(" nao xei o tipo do " + pair.getValue());
                        //possibleSubjs.add(new Triple<>(word,pair.getKey(), entitiesMap.get(pair.getKey())));
                        //possibleSubjs.put( tsubj ,new Triple<String, String, Set<String>>(pair.getValue(), null,new HashSet<>()));
                    }
                }
            }

            if(possibleSubjs.size()==1){
                // System.out.println(" So 1 sub " + possibleSubjs.get(0));
                // om.prepareSubjectToOntology(possibleSubjs,"");
                // TODO uncomment
            }
            else if (possibleSubjs.size()==2) { // try to understand the composed subject
                if (subj.matches(".*" +possibleSubjs.get(0).first + "\\ (de)\\ (a|o)(s)?\\ " + possibleSubjs.get(1).first + ".*")){
                    // e.g usuarios de facebook
                    // System.out.println("sub 0" +possibleSubjs.get(0));
                    // System.out.println(" sub 1 " +possibleSubjs.get(1));
                    // om.prepareSubjectToOntology(possibleSubjs,"de");
                    //TODO uncomment
                }
            }





        }

        return possibleSubjs;
    }*/

    // returns  real subject  and type ( pair ( real subj, type) )  ( SuBJ can be simple or composed ( John ,users of system)
    public Pair<String,String> identifySubject(String subj, TextProcessor tp){
        List<Pair<String,String>> possibleSubjs = new ArrayList<>();
        Pair<String,String> pair = null;
        String [] arrayOfWords = subj.split(" ");
        for (String word : arrayOfWords){
            //String typeOfWord = tp.getLemmaAndTagOfProcessedWord(word).getValue();
            pair = subjectIsEntity(entitiesMap,word,tp);

            if(pair!=null){
                if (base.isType(pair.getValue())){ // se é um tipo considerado
                    possibleSubjs.add(new Pair<>(word, pair.getValue()));
                }
            }
        }

        if(possibleSubjs.size()==1){
            //System.out.println(" So 1 sub " + possibleSubjs.get(0));
            // om.prepareSubjectToOntology(possibleSubjs,"");
            return possibleSubjs.get(0); // (type, real subj)
        }
        else if (possibleSubjs.size()==2) { // try to understand the composed subject
            if (subj.matches(".*" +possibleSubjs.get(0).getKey() + "\\ (de)\\ (a|o)(s)?\\ " + possibleSubjs.get(1).getKey() + ".*")){
                // e.g usuarios de facebook
                //System.out.println("sub 0" +possibleSubjs.get(0));
                //System.out.println(" sub 1 " +possibleSubjs.get(1));
                // om.prepareSubjectToOntology(possibleSubjs,"de");
                return new Pair<>(possibleSubjs.get(0).getKey() + " " + possibleSubjs.get(1).getKey(), possibleSubjs.get(0).getValue() );
            }
        }
        return null;
    }



    // returns pairs (real  pred, type)
    public Set<Pair<String,String>> identifyPred ( TextProcessor tp, String sub){
        Set<Pair<String,String>> set = new HashSet<>();
        for (String word : sub.split(" ")){
            // check if is an useful characteristic or an entity
            Pair p = tp.getLemmaAndTagOfProcessedWord(word);
            if (p==null){
                System.out.println("Error identifying pred in word " + word);
                continue;
            }
            else{
                String s = ((String) p.getKey());
                if ( usefulCaracteristic.containsKey(s.toLowerCase())){
                    set.add(new Pair<>(word, usefulCaracteristic.get(s.toLowerCase())));
                }
                else if (classifiedEntities.containsKey(s.toLowerCase())){
                    if ( ! classifiedEntities.get(s.toLowerCase()).equals("Thing")){
                        set.add(new Pair<>(word, classifiedEntities.get(s.toLowerCase())));
                    }

                }
                else if ( usefulRelations.containsKey(s.toLowerCase())){
                    set.add(new Pair<>(word, usefulRelations.get(s.toLowerCase())));
                }
                else {
                    String smatch = matchEvent(s);
                    if( smatch!=null){
                        set.add(new Pair<>(s, usefulRelations.get(smatch)));
                    }
                }
            }
        }
        return set;
    }


    // classification like (Person ,Said, Statement)
    public Set<Triple<String,String,String>> classifyTriple( Triple<String,String,String> original_Triple , Pair<String,String> subjPair, Set<Pair<String,String>> predPair , TextProcessor tp  ){
        Set<Triple<String,String,String>> returnSet = new HashSet<>( );
        String retFirst = null, retSecond=null, retThird = null;
        String verb = original_Triple.second;
        String relationOfVerb = usefulRelations.get(verb);
        if (containsAuxVerb(verb)){
            verb=original_Triple.second.split(" ")[original_Triple.second.split(" ").length-1];
            relationOfVerb= usefulRelations.get(verb);
        }
        if (relationOfVerb!=null){
            retFirst=subjPair.getValue();
            retSecond=relationOfVerb;
            //System.out.println("Rel " + retFirst + " <-> " + retSecond);
            if (relationSubjVerbMakesSense(retFirst,retSecond)){

                if (retSecond.equals("said")){
                    //System.out.println("##Statement unrolled : "+ subjPair.getKey() + " said " + original_Triple.third);
                    returnSet.add( new Triple<>(subjPair.getKey(),retSecond, "Statement"));
                }
                else if (retSecond.equals("did")){
                    //System.out.println( retFirst +" fez "+ original_Triple.third);
                    for (Pair<String,String> p : predPair){
                        String mat = matchEvent(p.getKey());
                        if (mat!=null){
                            //System.out.println( "inferi que -> " + retFirst +" " + mat + " " );
                            if (usefulRelations.get(mat).equals("Action")){
                                String act = unrollAction(subjPair.getKey(), mat, original_Triple.third, tp );
                                //System.out.println("##Action unrolled : "+ subjPair.getKey() + " " + act);
                                returnSet.add( new Triple<>(subjPair.getKey(),act.split(" ")[0], act.split(" ")[1]));

                            }
                            else if (usefulRelations.get(mat).equals("State")){
                                String what =  unrollState(original_Triple.first, mat, original_Triple.third, tp );
                               // System.out.println("##State unrolled : " +subjPair.getValue() + " " + what);
                                returnSet.add( new Triple<>(subjPair.getKey(),what.split(" ")[0], what.split(" ")[1]));
                            }
                        }
                    }
                }
                else if (retSecond.equals("has")){
                    // tentar ver se nao ha outro verbo no
                   // System.out.println("tem tem");
                    for (Pair<String,String> p : predPair){
                        String mat = matchEvent(p.getKey());
                        if (mat!=null){
                            //System.out.println( "inferi que -> " + retFirst +" " + mat + " " );
                            if (usefulRelations.get(mat).equals("Action")){
                                String act = unrollAction(subjPair.getKey(), mat, original_Triple.third, tp );
                               // System.out.println("##Action unrolled : "+ subjPair.getKey() + " " + act);
                                returnSet.add( new Triple<>(subjPair.getKey(),act.split(" ")[0], act.split(" ")[1]));
                            }
                            else if (usefulRelations.get(mat).equals("State")){
                                String what =  unrollState(original_Triple.first, mat, original_Triple.third, tp );
                                //System.out.println("##State unrolled : " +subjPair.getValue() + " " + what);
                                returnSet.add( new Triple<>(subjPair.getKey(),what.split(" ")[0], what.split(" ")[1]));
                            }
                        }
                    }

                }
                else if (retSecond.equals("State")){
                    //System.out.println( retFirst +" esta "+ original_Triple.third);
                    for (Pair<String,String> p : predPair){
                        String mat = matchEvent(p.getKey());
                        if (mat!=null){
                            //System.out.println( "inferi que -> " + retFirst +"  " + mat + " " );
                            if (usefulRelations.get(mat).equals("Action")){
                                String act = unrollAction(subjPair.getKey(), mat, original_Triple.third, tp );
                               // System.out.println("##Action unrolled : "+ subjPair.getKey() + " " + act);
                                returnSet.add( new Triple<>(subjPair.getKey(),act.split(" ")[0], act.split(" ")[1]));
                            }
                            else if (usefulRelations.get(mat).equals("State")){
                                String what = unrollState(original_Triple.first, mat, original_Triple.third, tp );
                              //  System.out.println("##State unrolled : " +subjPair.getValue() + " " + what);
                                returnSet.add( new Triple<>(subjPair.getKey(),what.split(" ")[0], what.split(" ")[1]));
                            }
                        }
                    }
                }
                else if (retSecond.equals("Action")){
                    //System.out.println( retFirst +" " +retSecond + " "+ original_Triple.third);
                    if (usefulRelations.containsKey(retSecond) && usefulRelations.get(retSecond).equals("Action")){
                        String act = unrollAction(subjPair.getKey(), retSecond, original_Triple.third, tp );
                      //  System.out.println("##Action unrolled : "+ subjPair.getKey() + " " + act);
                        returnSet.add( new Triple<>(subjPair.getKey(),act.split(" ")[0], act.split(" ")[1]));
                    }
                }
                else if (retSecond.equals("saw")){
                    // tem que ter relacao relevante a seguir
                    for (Pair<String,String> p : predPair){
                        String mat = matchEvent(p.getKey());
                        if (mat!=null){
                            //System.out.println( "inferi que -> " + retFirst +"  " + mat + " " );
                            retSecond=mat;
                            if (usefulRelations.get(mat).equals("Action")){
                                String act = unrollAction(subjPair.getKey(), mat, original_Triple.third, tp );
                               // System.out.println("##Action unrolled : "+ subjPair.getKey() + " " + act);
                                returnSet.add( new Triple<>(subjPair.getKey(),act.split(" ")[0], act.split(" ")[1]));
                            }
                            else if (usefulRelations.get(mat).equals("State")){
                                String what = unrollState(original_Triple.first, mat, original_Triple.third, tp );
                               // System.out.println("##State unrolled : " +subjPair.getValue() + " " + what);
                                returnSet.add( new Triple<>(subjPair.getKey(),what.split(" ")[0], what.split(" ")[1]));
                            }
                        }
                    }
                }
            }

        }
        return returnSet;
    }


    // answer  question:  which (Currency, Organization) oneSubj (buyed,traded, ...) ?
    public String unrollAction(String oneSubj, String action, String pred, TextProcessor tp ){

        // first check if predicative contains objective of relation // currency or organization
        for (String word : pred.split(" ")){
            Pair<String, String> p = tp.getLemmaAndTagOfProcessedWord(word);
            if (p==null){
                continue;
            }
            String normalized = p.getKey();
            String tag = p.getValue();
            if (base.getType(word,tag).equals("Currency")|| base.getType(word,tag).equals("Cryptocurrency") || base.getType(word,tag).equals("Organization") ){
                return action + " " + word;
            }
            else if (base.getType(normalized,tag).equals("Currency")|| base.getType(normalized,tag).equals("Cryptocurrency") || base.getType(normalized,tag).equals("Organization") ){
                return action + " " + word;
            }
            else if (classifiedEntities.containsKey(word)|| classifiedEntities.containsKey(normalized)){
                return action + " " + word;
            }
        }

        // answer not found, get sentence and try to find in sentence
        HashSet h = new HashSet();
        h.addAll(Arrays.asList(oneSubj.split(" ")));
        h.addAll(Arrays.asList(pred.split(" ")));
        String sentence = tp.getSentenceOfWords( h); // get corrensponding sentence
        // pruning sentence to find objective of relation ( comprou bitcoin)
        if (sentence!=null){
            boolean found= false;
            for (String word : sentence.split(" ")){
                Pair<String, String> p = tp.getLemmaAndTagOfProcessedWord(word);
                if (p==null ){
                    continue;
                }
                else if (!found){
                    if (p.getValue().equals(action)|| action.equals(word)){
                        found=true;
                    }
                    else {
                        //Pair<String,String> pp = tp.getLemmaAndTagOfProcessedWord(word);
                       // if (pp!=null&&usefulRelations.containsKey(pp.getKey())&& usefulRelations.get(pp.getKey()).equals("Action")){
                        String match = matchEvent(word);
                        if (match!=null&& match.equals(action)){
                            found=true;
                        }
                        else {
                            continue;
                        }
                    }
                }

                String normalized = p.getKey();
                String tag = p.getValue();
                if (base.getType(word,tag).equals("Currency")|| base.getType(word,tag).equals("Cryptocurrency") || base.getType(word,tag).equals("Organization") ){
                    if(classifiedEntities.containsKey(word)){
                        return action + " " + word;
                    }
                }
                else if (base.getType(normalized,tag).equals("Currency")|| base.getType(normalized,tag).equals("Cryptocurrency") || base.getType(normalized,tag).equals("Organization") ){
                    if (classifiedEntities.containsKey(normalized)){
                        return action + " " + word;
                    }
                }
                else if ((classifiedEntities.containsKey(word)|| classifiedEntities.containsKey(normalized))){
                    if (classifiedEntities.get(word)!=null && (classifiedEntities.get(word).equals("Currency")|| classifiedEntities.get(word).equals("Cryptourrency") || classifiedEntities.get(word).equals("Organization") )) {
                        return action + " " + word;
                    }
                    else if (classifiedEntities.get(normalized)!=null && (classifiedEntities.get(normalized).equals("Currency")|| classifiedEntities.get(normalized).equals("Cryptourrency") || classifiedEntities.get(normalized).equals("Organization") )) {
                        return action + " " + normalized;
                    }
                }
            }
        }

        return action + " " + "Currency";
    }

    // answer to question : what is decrasing / increasing ? answer: price, value , rate?
    public String unrollState(String fullSubj , String event, String pred, TextProcessor tp ){
        HashSet h = new HashSet();
        h.addAll(Arrays.asList(fullSubj.split(" ")));
        h.addAll(Arrays.asList(pred.split(" ")));
        String sentence = tp.getSentenceOfWords( h); // get corrensponding sentence
        // get index of splitpoint (verb of relation)
        if (sentence==null){
            return  event + " " + "Nothing";
        }
        String[] wordsOfSentence = sentence.split(" ");
        for (String word : wordsOfSentence){
            for ( String name : usefulNames.keySet()){
                if (word.matches(name)){
                    return event +  " " + word;
                }
            }
        }

        return  event + " " + "Nothing";
    }



    public boolean relationSubjVerbMakesSense(String subjtype, String relType){
        if (subjtype.equals("Person")){
            if (relType.equals("said")){
                return true;
            }
            else if (relType.equals("did")){
                return true;
            }
            else if (relType.equals("has")){
                return true;
            }
            else if (relType.equals("Action")){
                return true;
            }
            else if (relType.equals("State")){
                return true;
            }
            else if (relType.equals("saw")){
                return true;
            }
        }
        else if (subjtype.equals("Organization")){
            if (relType.equals("said")){
                return true;
            }
            else if (relType.equals("did")){
                return true;
            }
            else if (relType.equals("has")){
                return true;
            }
            else if (relType.equals("Action")){
                return true;
            }
            else if (relType.equals("State")){
                return true;
            }
            else if (relType.equals("saw")){
                return true;
            }
        }
        if (subjtype.equals("Currency")){
            if (relType.equals("did")){
                return true;
            }
            else if (relType.equals("has")){
                return true;
            }
            else if (relType.equals("Action")){
                return true;
            }
            else if (relType.equals("State")){
                return true;
            }
            else if (relType.equals("saw")){
                return true;
            }
        }
        if (subjtype.equals("Cryptocurrency")){
            if (relType.equals("did")){
                return true;
            }
            else if (relType.equals("has")){
                return true;
            }
            else if (relType.equals("Action")){
                return true;
            }
            else if (relType.equals("State")){
                return true;
            }
            else if (relType.equals("saw")){
                return true;
            }
        }
        if (subjtype.equals("Statement")){
            if (relType.equals("said")){
                return true;
            }
            else if (relType.equals("did")){
                return true;
            }
            else if (relType.equals("has")){
                return true;
            }
            else if (relType.equals("Action")){
                return true;
            }
            else if (relType.equals("State")){
                return true;
            }
            else if (relType.equals("saw")){
                return true;
            }
        }
        if (subjtype.equals("Person")){
            if (relType.equals("said")){
                return true;
            }
            else if (relType.equals("did")){
                return true;
            }
            else if (relType.equals("has")){
                return true;
            }
            else if (relType.equals("Action")){
                return true;
            }
            else if (relType.equals("State")){
                return true;
            }
            else if (relType.equals("saw")){
                return true;
            }
        }
        return false;
    }


    public String matchEvent(String possibleEv){
        for (String st :  truncatedEvents.keySet()){
            if (possibleEv.matches(st)){
                for (String s : usefulRelations.keySet()){
                    if(s.matches(st.replace("í","i").replace("õ","o"))){
                        return s;
                    }
                }
            }
        }
        return null;
    }


    public boolean containsAuxVerb(String normalizedVerb){
        for (String s : auxVerb){
            if (normalizedVerb==null){
                continue;
            }
            if (normalizedVerb.contains(s)){
                return true;
            }
        }
        return false;
    }


}
