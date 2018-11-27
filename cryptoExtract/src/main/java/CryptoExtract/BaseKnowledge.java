package CryptoExtract;

import Utils.Representations.Currency;
import Utils.Representations.CurrencyGazeteer;
import Utils.Representations.OrganizationGazeteer;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.*;

public class BaseKnowledge {

   public OntModel ontology;
    //public Map<String, Currency> currenciesMap = new HashMap<>();
    public CurrencyGazeteer currencyGazeteer = new CurrencyGazeteer();
    public OrganizationGazeteer organizationGazeteer = new OrganizationGazeteer();

    public static Map<String,String > cache = new HashMap<>();
    public static String [] consideredTypes = {"Currency", "Organization", "System", "Location", "Issue",
            "Person", "CryptoCurrency", "Number", "Thing", "Date", "HOUR", "PERCENTAGE", "Statement" , "Action", "Event", "State", "Sentiment"};


    public BaseKnowledge(String ontoPath){
        if (ontoPath != null) {
            this.init(ontoPath);
        }
        //intialize cache
        cache.put("bitcoin".toLowerCase(),"Currency");
        cache.put("Coinmarketcap".toLowerCase(),"Organization");
        cache.put("website".toLowerCase(),"System");
        cache.put("portal".toLowerCase(),"System");
        cache.put("criptomoedas".toLowerCase(),"Cryptocurrency");
        cache.put("criptomoeda".toLowerCase(),"Currency");
        cache.put("Blockchain".toLowerCase(),"Cryptocurrency");
        cache.put("plataforma".toLowerCase(),"System");
        cache.put("criptoativos".toLowerCase(),"Cryptocurrency");
        cache.put("criptoativo".toLowerCase(),"Cryptocurrency");
        cache.put("mercado".toLowerCase(),"Cryptocurrency");
        cache.put("rede".toLowerCase(),"System");
        cache.put("US".toLowerCase(),"Location");
        cache.put("facebook".toLowerCase(),"Organization");
        cache.put("Twitter".toLowerCase(),"Organization");
        cache.put("Instagram".toLowerCase(),"Organization");
        cache.put("Reddit".toLowerCase(),"Organization");
        cache.put("vulnerabilidade".toLowerCase(),"Issue");
        cache.put("ataque".toLowerCase(),"Issue");
        cache.put("falha".toLowerCase(),"Issue");
        cache.put("invasor".toLowerCase(),"Issue");
        cache.put("Binance".toLowerCase(),"Organization");
        cache.put("P2P".toLowerCase(),"Cryptocurrency");
        cache.put("etoro".toLowerCase(),"Organization");
        cache.put("CFDs".toLowerCase(), "Currency");
        cache.put("CFD".toLowerCase(), "Currency");
        cache.put("moeda".toLowerCase(), "Currency");
        cache.put("moedas".toLowerCase(), "Currency");
        //cache.put("hashrate".toLowerCase(),"Cryptocurrency");

    }


    public void init(String ontology){
        this.ontology = loadOntology(ontology);
    }


    public static OntModel loadOntology(String ontoFile){

        OntModel ontoModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        try
        {
            InputStream in = FileManager.get().open(ontoFile);
            try
            {
                ontoModel.read(in, null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            System.out.println("Ontology " + ontoFile + " loaded.");
        }
        catch (JenaException je)
        {
            System.err.println("ERROR" + je.getMessage());
            je.printStackTrace();
        }
        return ontoModel;
    }


    public String getType(String word, String tag){
        if (cache.containsKey(word.toLowerCase())){
            return cache.get(word.toLowerCase());
        }
        else if( this.currencyGazeteer.isInGazeteer(word.toLowerCase())){
            return "Currency";
        }

        else if (tag.equals("NP00SP0")){
            return "Person";
        }
        else if (tag.equals("NP00G00")){
            return "Location";
        }
        else if (tag.equals("HOUR")){
            return "Hour";
        }
        else if (tag.equals("Z")){
            return "Number";
        }
        else if (tag.equals("PERCENTAGE")){
            return "PERCENTAGE";
        }
        else if (tag.equals("NP00O00")){
            return "Organization";
        }
        if (tag.equals("W")){
            return "Date";
        }

        else if(isDate(word)){
            return "Date";
        }

        else if (tag.equals("NP00V00")){
            // try to get concrete type
            if (cache.containsKey(word.toLowerCase())){
                return cache.get(word.toLowerCase());
            }
            else if( this.currencyGazeteer.isInGazeteer(word)){
                return "Currency";
            }
        }
        return "Thing";
    }

    public boolean isDate(String possibleDate){
        if (possibleDate.contains("-")){
            if (possibleDate.toLowerCase().contains("feira"))
            return true;
        }
        else {
            if (possibleDate.toLowerCase().contains("sabado")|| possibleDate.toLowerCase().contains("domingo"))
                return true;
        }
        return false;
    }

    public String isRelevantEntity(String ent){
        if (cache.containsKey(ent.toLowerCase())){
            return cache.get(ent);
        }
        else if(currencyGazeteer.isInGazeteer(ent.toLowerCase())){
            return "Currency";
        }
        else return null;
    }

    public boolean isType(String possibleType){
        if (possibleType==null){
            return false;
        }
        for(String s : consideredTypes){
            if (s.toLowerCase().equals(possibleType.toLowerCase())){
                return true;
            }
        }
        return false;
    }



    public static void main(String[] args) {

        /*
        String onto = "/Users/ruirua/Downloads/cryptocurrencyenvironment-ontologies-owl-REVISION-HEAD/root-ontology.owl";
        Logger.getRootLogger().setLevel(Level.OFF);
        //String onto = "/Users/ruirua/Downloads/cryptocurrency-ontologies-owl-REVISION-HEAD/root-ontology.owl";
        String SOURCE = "https://cryptocurrencyenvironment.pt/";
        String NS = SOURCE ;//+ "#";
        OntModel base = loadOntology(onto);
       // base.read( SOURCE, "RDF/XML" );

        OntClass paper = base.getOntClass( NS + "Currency@en" );
        //Individual p1 = base.createIndividual( NS + "paper1", paper );
        Individual p1 = base.getIndividual( "BTC" );
        System.out.println("");
        ArrayList<Resource> results = new ArrayList<Resource>();
        ExtendedIterator individuals = base.listIndividuals();
        while (individuals.hasNext()) {
            Resource individual = (Resource) individuals.next();
            System.out.println(individual);
        }
        System.out.println();*/


    }

}
