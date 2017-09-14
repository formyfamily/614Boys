package news;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by thinkpad on 2017/9/12.
 */

class PictureSearchThread extends Thread {
    private final String USER_AGENT = "Mozilla/5.0";
    private final int pictureNum = 3;
    private ArrayList<String> searchPictureResults = new ArrayList<String>();
    private ArrayList<String> keywords;
    public PictureSearchThread(ArrayList<String> keywords){
        this.keywords = keywords;
    }
    public void run() {
        try {
            String keywordStr = "";
            for (String e:keywords) keywordStr += e + '+';
            keywordStr = keywordStr.substring(0,keywordStr.length()-1);
            String url = "https://api.cognitive.microsoft.com/bing/v5.0/images/search?q=" + keywordStr + "&count=" +
                    Integer.toString(pictureNum) + "&mkt=zh-CN";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            //添加请求头
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Ocp-Apim-Subscription-Key", "5c07a0345ed64772ba9cf7c1e17dca93");
            con.setRequestProperty("Host", "api.cognitive.microsoft.com");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //打印结果
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("value");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                searchPictureResults.add(jo.getString("thumbnailUrl"));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getSearchPictureResults(){
        return(searchPictureResults);
    }
}

public class PictureSearcher{
    private PictureSearcher(){}
    private static PictureSearcher searcher = null;
    public static PictureSearcher getInstance(){
        if (searcher == null) searcher = new PictureSearcher();
        return(searcher);
    }
    public ArrayList<String> addPictureToNews(ArrayList<String> keywords){
        if (keywords == null) return(new ArrayList<String>());
        if (keywords.isEmpty()) return(new ArrayList<String>());
        try {
            PictureSearchThread thread = new PictureSearchThread(keywords);
            thread.start();
            thread.join();
            return(thread.getSearchPictureResults());
        }catch(Exception e){
            e.printStackTrace();
            return(null);
        }
    }
}