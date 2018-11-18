package Utils.Representations;

import Utils.CoinMarketCapBridge;
import Utils.GenericUtils;
import com.sun.deploy.panel.JHighDPITable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CurrencyGazeteer implements Gazeteer, Serializable {

    public HashMap<String, Currency> coins = new HashMap();  // currencies ( contains both abbreviation and full name)
    public static String fileName = "Currency.json";
    public static boolean loaded = false;


    public CurrencyGazeteer (){
        if (!loaded)
            this.coins = loadContent();
    }


    private HashMap<String, Currency> getContent(){
        if (loaded){
             return coins;
        }
        else {
            loaded=true;
            coins = loadContent();
        }
        return coins;
    }

    private boolean fileExists(String fileName) {
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            File file = new File(fileName);
            if (file.exists()){
                return true;
            }
        }
        catch (Exception e){
            return false;
        }

        return false;
    }

    public JSONArray fromJSONFile (String file){
        JSONParser parser = new JSONParser();
        JSONArray ja = new JSONArray();

        try {
            Object obj = parser.parse(new FileReader(file));
            ja = (JSONArray) obj;
            return ja;
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return ja;
    }

    public HashMap<String, Currency> fromJSONArray (JSONArray ja){
        HashMap<String, Currency> m = new HashMap<>();
        for (Object ob: ja){
            JSONObject job = ((JSONObject) ob);
            Currency cu = Currency.fromJSONObject(job);
           m.put(cu.currencyName.toLowerCase(),cu );
            m.put(cu.currencyMarketSlug.toLowerCase(), cu);
            m.put(cu.currencySymbol.toLowerCase(), cu);
        }
        return m;

    }


    private HashMap<String, Currency> loadContent() {
       HashMap<String, Currency> m = new HashMap<>();
       if (fileExists(fileName)){
           return  fromJSONArray(fromJSONFile(fileName));
       }
       else{
           for (Currency c : CoinMarketCapBridge.getCurrencies(new File(fileName)).values()){
               m.put(c.currencyName.toLowerCase(), c);
               m.put(c.currencyMarketSlug.toLowerCase(), c);
               m.put(c.currencySymbol.toLowerCase(), c);
           }
           loaded=true;
           return m;
       }

    }

    @Override
    public void toFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String s : coins.keySet()){
            sb.append(s + "\n");
        }
        GenericUtils.writeFile(new File(path) , sb.toString());

    }

    @Override
    public void fromFile(String path) {

    }




    @Override
    public boolean isInGazeteer(String toFind) {
        return coins.containsKey(toFind.toLowerCase());
    }

    @Override
    public boolean serialize(String outputFile) {
        return false;
    }

    @Override
    public Gazeteer Deserialize(String inputFile) {
        return null;
    }


}
