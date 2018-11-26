package CryptoExtract;

import javafx.util.Pair;

import java.util.*;

public class OntologyMapper {

    public Map<String, Pair<String,String>> mapPersonRelated = new HashMap<>(); // Pairt (rdf:type, dataProperty   )
    public Map<String, Pair<String,String>> mapIssueRelated = new HashMap<>();
    public Map<String, Pair<String,String>> mapEventRelated = new HashMap<>();

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



    public String getInstance (String id, String rel, Iterable<String> types ){
        String batata = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX crypto: <http://www.semanticweb.org/ruirua/ontologies/CryptoExtract#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX re: <http://www.w3.org/2000/10/swap/reason#>\n";

        for (String type : types){
            batata+= "insert {crypto:" + id +" " + rel + " crypto:" + type + " } where {}\n";
        }
        return batata;
    }


    // list of triple (subj, lemma,type)
    public void prepareSubjectToOntology(List<Triple<String,String,String>> subjects, String relBetween){
        if (subjects.size()==1){ // simple subject

                Pair<String,String> p = mapGets(subjects.get(0).second);
                if (p==null){ // is currency or other
                    List<String> l = new ArrayList<>();
                    l.add(subjects.get(0).third);
                    System.out.println(getInstance(subjects.get(0).second, "rdf:type" , l ));
                    l.remove(0);
                    //l.add(p.getValue());
                    //System.out.println(getInstance(subjects.get(0).first, "owl:instanceOf" , l )); // data property

                }
                else{
                    List<String> l = new ArrayList<>();
                    l.add(p.getKey());
                    System.out.println(getInstance(subjects.get(0).first, "rdf:type" , l ));
                    l.remove(0);
                    l.add(p.getValue());
                    System.out.println(getInstance(subjects.get(0).first, "owl:instanceOf" , l )); // data property
                }



        }
        else if (subjects.size()==2){ // composed subject ( e.g investidores de bitcoin
            if (relBetween.equals("de")){ // OF
                    Pair<String,String> p = mapGets(subjects.get(0).second);
                if (p==null){
                    List<String> l = new ArrayList<>();
                    l.add(subjects.get(0).third);
                    System.out.println(getInstance(subjects.get(0).first, "rdf:type" , l ));
                    l.remove(0);
                   // l.add(p.getValue());
                   // System.out.println(getInstance(subjects.get(0).first, "owl:instanceOf" , l )); // object property
                   // l.remove(0);
                    l.add((subjects.get(1).second ));
                    System.out.println(getInstance(subjects.get(0).first, "owl:Of" , l )); // object property
                }
                else{
                    List<String> l = new ArrayList<>();
                    l.add(p.getKey());
                    System.out.println(getInstance(subjects.get(0).first, "rdf:type" , l ));
                    l.remove(0);
                    l.add(p.getValue());
                    System.out.println(getInstance(subjects.get(0).first, "owl:instanceOf" , l )); // object property
                    l.remove(0);
                    l.add((subjects.get(1).second ));
                    System.out.println(getInstance(subjects.get(0).first, "owl:Of" , l )); // object property
                }
            }
        }
    }


}
