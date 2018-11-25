package CryptoExtract;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.graph.Graph;
import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.shared.JenaException;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDFS;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class OntologyBridge {

    public static HashSet<String> prefixes = new HashSet<>();
    public static String url =  "http://localhost:3030/CryptoExtract/";
    public static String ns = "http://www.semanticweb.org/ruirua/ontologies/CryptoExtract#";


    public OntologyBridge() {
       // prefixes.add("http://www.semanticweb.org/ruirua/ontologies/CryptoExtract#");
        prefixes.add("http://www.w3.org/2000/01/rdf-schema#");
        prefixes.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        prefixes.add("http://www.semanticweb.org/ruirua/ontologies/CryptoExtract#");

    }


    public String cleanObject(String obj){
        for (String s : prefixes){
           obj= obj.replace(s,"");
        }

        return obj;
    }

    public void select(String url){

        RDFConnection conn = RDFConnectionFactory.connect(url);
        //conn.load(s);

        QueryExecution qExec = conn.query("select * where { ?s ?x ?z}" ) ;
        ResultSet rs = qExec.execSelect() ;
        while(rs.hasNext()) {
            QuerySolution qs = rs.next() ;
            Resource subject = qs.getResource("s") ;
            System.out.println("Subject: "+cleanObject(subject.toString())) ;
        }
        qExec.close() ;
        conn.close() ;
    }

    ///////////


    public void insertEntityIntoOntology(String entity, String rel, String entityType){
        String relation ="";
        if (rel.equals("instanceOf")){
            relation="rdf:type";
        }
        entityType=entityType.replaceAll(" ", "_");
        entity=entity.replaceAll(" ", "_");
        RDFConnection conn = RDFConnectionFactory.connect(url);
        String batata = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX crypto: <http://www.semanticweb.org/ruirua/ontologies/CryptoExtract#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX re: <http://www.w3.org/2000/10/swap/reason#>\n" +
                "insert {crypto:" + entity +" " + relation + " crypto:" + entityType + " } where {}\n";
        UpdateRequest request = UpdateFactory.create(batata);
        conn.update(request);
        conn.close() ;

    }



    public void update(String url){

        RDFConnection conn = RDFConnectionFactory.connect(url);
        String batata = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX crypto: <http://www.semanticweb.org/ruirua/ontologies/CryptoExtract#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX re: <http://www.w3.org/2000/10/swap/reason#>\n" +
                "insert {crypto:Diana rdf:type crypto:Programador } where {}\n";
        UpdateRequest request = UpdateFactory.create(batata);
        conn.update(request);
        conn.close() ;
    }


     public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);
        OntologyBridge ob = new OntologyBridge();
        ob.update(url);
        System.out.println("");

    }
}
