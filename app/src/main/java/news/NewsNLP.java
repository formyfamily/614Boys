package news;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by thinkpad on 2017/9/9.
 */

public class NewsNLP {
    private HashMap<String,Double> scores = new HashMap<String,Double>();

    public boolean setByJsonObject(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("Keywords");
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                scores.put(jsonObject1.getString("word"),jsonObject1.getDouble("score"));
            }
            return(true);
        }
        catch (Exception e) {
            e.printStackTrace();
            return(false);
        }
    }

    public HashMap<String,Double> getScores() {
        return(scores);
    }
}
