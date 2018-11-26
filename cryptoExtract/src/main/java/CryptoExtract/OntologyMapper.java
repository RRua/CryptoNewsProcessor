package CryptoExtract;

import javafx.util.Pair;

import java.util.*;

public class OntologyMapper {

    public Map<String, Pair<String,String>> mapPersonRelated = new HashMap<>(); // Pairt (rdf:type, dataProperty   )
    public Map<String, Pair<String,String>> mapIssueRelated = new HashMap<>();
    public Map<String, Pair<String,String>> mapEventRelated = new HashMap<>();

    public static HashSet<Triple<String,String,String>> relationsToSend = new HashSet<>();

    /*

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

     */

    public OntologyMapper() {
        this.mapPersonRelated = new HashMap<>();
        this.mapIssueRelated = new HashMap<>();

        mapIssueRelated.put("vulnerabilidade", new Pair<>("Issue", "Security") );
        mapIssueRelated.put("problema", new Pair<>("Issue", "General") );
        mapIssueRelated.put("ataque", new Pair<>("Issue", "General") );
        mapIssueRelated.put("malicioso", new Pair<>("Issue", "General") );
        mapIssueRelated.put("falhar", new Pair<>("Issue", "General") );
        mapIssueRelated.put("roubar", new Pair<>("Issue", "Security") );
        mapIssueRelated.put("invasor", new Pair<>("Issue", "Security") );

        mapPersonRelated.put("programador", new Pair<>("Person", "Programmer") );
        mapPersonRelated.put("presidente", new Pair<>("Person", "President") );
        mapPersonRelated.put("CEO", new Pair<>("Person", "CEO") );
        mapPersonRelated.put("fundador", new Pair<>("Person", "Founder") );
        mapPersonRelated.put("criador", new Pair<>("Person", "Creator") );
        mapPersonRelated.put("hacker", new Pair<>("Person", "Hacker") );
        mapPersonRelated.put("expert", new Pair<>("Person", "Expert") );
        mapPersonRelated.put("acionista", new Pair<>("Person", "Stockholder") );
        mapPersonRelated.put("proprietário", new Pair<>("Person", "Owner") );
        mapPersonRelated.put("investigador", new Pair<>("Person", "Researcher") );
        mapPersonRelated.put("expert", new Pair<>("Person", "Secretary") );
        mapPersonRelated.put("usuário", new Pair<>("Person", "User") );
        mapPersonRelated.put("utilizador", new Pair<>("Person", "User") );
        mapPersonRelated.put("analista", new Pair<>("Person", "Analyst") );
        mapPersonRelated.put("técnico", new Pair<>("Person", "Technician") );
        mapPersonRelated.put("ministro", new Pair<>("Person", "Minister") );
        mapPersonRelated.put("expert", new Pair<>("Person", "Expert") );
        mapPersonRelated.put("líder", new Pair<>("Person", "Leader") );
        mapPersonRelated.put("deputado", new Pair<>("Person", "Deputee") );
        mapPersonRelated.put("governador", new Pair<>("Person", "Governor") );
        mapPersonRelated.put("famoso", new Pair<>("Person", "Famous") );
        mapPersonRelated.put("milionário", new Pair<>("Person", "Millionaire") );
        mapPersonRelated.put("bilionário", new Pair<>("Person", "Billionaire") );
        mapPersonRelated.put("administrador", new Pair<>("Person", "Administrator") );

    }

    public Pair<String,String> mapGets( String obj){
        if (mapPersonRelated.containsKey(obj)){
            return mapPersonRelated.get(obj);
        }
        if (mapIssueRelated.containsKey(obj)){
            return mapIssueRelated.get(obj);
        }
        if (mapIssueRelated.containsKey(obj)){
            return mapIssueRelated.get(obj);
        }
        return null;
    }



    public String getInstance (String id, String rel, String type ){
        String batata = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX crypto: <http://www.semanticweb.org/ruirua/ontologies/CryptoExtract#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"+
                "PREFIX re: <http://www.w3.org/2000/10/swap/reason#>\n";
        batata+= "insert {crypto:" + id +" " + rel + " crypto:" + type + " } where {}\n";

        return batata;
    }


    // list of triple (subj, lemma,type)
    public void prepareSubjectToOntology(Triple<String,String,String> subj, String relBetween){
        String  [] subjects = subj.first.split(" ");
        String allsubs=subj.first.replaceAll(" ", "_");
        if (subjects.length==1){ // simple subject

                Pair<String,String> p = mapGets(subj.second);
                if (p==null){ // is currency or other
                    //     System.out.println(getInstance(subj.second, "rdf:type" , subj.third ));
                    relationsToSend.add(new Triple<>(subj.second,"rdf:type", subj.third) );
                }
                else{
                    //    System.out.println(getInstance(subj.first, "rdf:type" , p.getKey() ));
                    relationsToSend.add(new Triple<>(subj.first,"rdf:type", p.getKey()) );
                    //    System.out.println(getInstance(subj.first, "owl:instanceOf" , p.getValue() )); // data property
                    relationsToSend.add(new Triple<>(subj.first,"owl:instanceOf", p.getValue()) );
                }



        }
        else if (subjects.length==2){ // composed subject ( e.g investidores de bitcoin
            if (relBetween.equals("de")){ // OF
                    Pair<String,String> p = mapGets(subj.second);
                if (p==null){
                    //     System.out.println(getInstance(allsubs, "rdf:type" , subj.third ));
                    relationsToSend.add(new Triple<>(allsubs,"rdf:type", subj.third) );
                    //     System.out.println(getInstance(allsubs, "owl:Of", subjects[1]  )); // object property
                    relationsToSend.add(new Triple<>(allsubs,"owl:Of", subjects[1]) );

                }
                else{
                    //System.out.println(getInstance(subjects[1], "rdf:type" , p.getKey()));
                    relationsToSend.add(new Triple<>(subjects[1],"rdf:type", p.getKey()) );
                  //  System.out.println(getInstance(subjects[1], "owl:instanceOf" , p.getValue() )); // object property
                    relationsToSend.add(new Triple<>(subjects[1],"owl:instanceOf",p.getValue()) );
                    //   System.out.println(getInstance(subjects[1], "owl:Of" , subj.second )); // object property
                    relationsToSend.add(new Triple<>(subjects[1],"owl:instanceOf",subj.second) );
                }
            }
        }
    }


    // T (pred, real pred, type) , coisos
    public void preparePredToOntology (Triple<String,String,String> originalTriple,Triple<String,String,String> classifiedTriple, Set<Pair<String,String>> related, RelationFilter rel ){
        List<String> l = new ArrayList<>();
        if (classifiedTriple.third.equals("Statement")){
            //     System.out.println(getInstance(originalTriple.third.replaceAll(" ", "_"), "rdf:type" , "Statement" ));
            relationsToSend.add(new Triple<>(originalTriple.third.replaceAll(" ", "_"),"rdf:type", "Statement") );
            //    System.out.println(getInstance(classifiedTriple.first.replaceAll(" ", "_"), "owl:said" , originalTriple.third.replaceAll(" ", "_") )); // data property
            relationsToSend.add(new Triple<>(classifiedTriple.first.replaceAll(" ", "_"),"rdf:said", originalTriple.third.replaceAll(" ", "_")) );

            // related to
            for (Pair<String,String> p : related){
                String mat=rel.matchEvent(p.getKey());
                if (rel.usefulRelations.containsKey(mat)){
                    //          System.out.println(getInstance(originalTriple.third.replaceAll(" ", "_") , "owl:relatedTo" , rel.usefulRelations.get(mat) ));
                    relationsToSend.add(new Triple<>(originalTriple.third.replaceAll(" ", "_"),"owl:relatedTo" , rel.usefulRelations.get(mat)) );
                }
                else{
                    //          System.out.println(getInstance(originalTriple.third.replaceAll(" ", "_") , "owl:relatedTo" , p.getKey() ));
                    relationsToSend.add(new Triple<>(originalTriple.third.replaceAll(" ", "_"),"owl:relatedTo" , p.getKey()) );

                }
            }

        }
        else {
            String mat = rel.matchEvent(classifiedTriple.second);
            if (rel.usefulRelations.get(mat).equals("Action")){
                //       System.out.println(getInstance( classifiedTriple.second, "rdf:type" , "Action" ));
                relationsToSend.add(new Triple<>(classifiedTriple.second,"rdf:type", "Action") );
                //       System.out.println(getInstance( classifiedTriple.first.replaceAll(" ", "_"), "owl:"+classifiedTriple.second , classifiedTriple.third ));
                relationsToSend.add(new Triple<>(classifiedTriple.first.replaceAll(" ", "_"),"owl:"+classifiedTriple.second ,  classifiedTriple.third ) );
            }
            else if (rel.usefulRelations.get(mat).equals("State")){
                if (!classifiedTriple.first.equals(classifiedTriple.first)){
                    //           System.out.println(getInstance( classifiedTriple.third, "rdf:type" , "State" ));
                    relationsToSend.add(new Triple<>(classifiedTriple.third,"rdf:type", "State") );

                }
                //        System.out.println(getInstance( classifiedTriple.first.replaceAll(" ", "_"), "owl:"+classifiedTriple.second , classifiedTriple.third ));
                relationsToSend.add(new Triple<>(classifiedTriple.first.replaceAll(" ", "_"),"owl:"+classifiedTriple.second,classifiedTriple.third));
            }
        }








    }


}
