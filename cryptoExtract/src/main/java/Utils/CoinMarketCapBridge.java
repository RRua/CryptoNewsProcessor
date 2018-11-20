package Utils;


import Utils.Representations.Currency;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CoinMarketCapBridge {

    public static String url = "https://api.coinmarketcap.com";
    public static String currenciesUrl = "https://api.coinmarketcap.com/v2/listings/";

    public static Map<String,Currency> getCurrencies(File localFile){
        Map<String,Currency> m = new HashMap<>();
        JSONObject jo = getJSONFromURL(currenciesUrl);
        try {
            JSONArray ja = ((JSONArray) jo.get("data"));
            for (Object o : ja){
                JSONObject j = ((JSONObject)  o);
                Currency c = Currency.fromJSONObject(j);
                m.put( c.currencyName, c);
            }
            GenericUtils.writeFile(localFile, ja.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return m;
    }




    public static JSONObject getJSONFromURL(String url){

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            Integer httpRes = 0;
            String responseContent="";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse resp = client.execute(httpGet);

                HttpEntity responseEntity = resp.getEntity();
                httpRes = resp.getStatusLine().getStatusCode();
                if (responseEntity != null && httpRes==200) {
                    responseContent = EntityUtils.toString(responseEntity);
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        Object obj = parser.parse(responseContent);
                        jsonObject = (JSONObject) obj;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;

                }

            } catch (Exception ex) {
                // handle exception here
            } finally {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }



}
