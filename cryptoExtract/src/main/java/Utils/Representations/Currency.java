package Utils.Representations;

import org.json.simple.JSONObject;

public class Currency {

    public  String currencyName = ""; // e.g. Bitcoin
    public  String currencySymbol = ""; // e.g. BTC
    public  String currencyCoinMarketCapID = ""; // e.g. 1
    public  String currencyMarketSlug = ""; // e.g. bitcoin



    public static Currency fromJSONObject(JSONObject jo ){
        Currency c = new Currency();
        c.currencyCoinMarketCapID = ((Long) jo.get("id")).toString();
        c.currencyName = ((String) jo.get("name"));
        c.currencySymbol = ((String) jo.get("symbol"));
        c.currencyMarketSlug = ((String) jo.get("website_slug"));
        return c;
    }

}
