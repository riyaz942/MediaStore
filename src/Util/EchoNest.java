package flashbar.com.testapplication;

import android.graphics.Bitmap;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class EchoNest
{
    private static final String API_KEY="SNGP2F06C2TFRKCCZ";

    private static final String ARTIST_API="http://developer.echonest.com/api/v4/artist/images?api_key=";

    /*http://developer.echonest.com/api/v4/artist/images?api_key=SNGP2F06C2TFRKCCZ&id=ARH6W4X1187B99274F&format=json&results=1&start=0&license=unknown*/

    private static JSONObject getResponse(String echoNestUrl) {

        JSONObject jsonObject=null;
        try {
            URL url = new URL(echoNestUrl);

            URLConnection connection = url.openConnection();

            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            Log.e("builder String", builder.toString());

            jsonObject = new JSONObject(builder.toString());
        }
        catch (Exception e) {
            Log.e("results:",e.toString());
        }
        return jsonObject;
    }

    private static String getArtistImageUrl(JSONObject jsonObject)
    {
          String url=null;

          jsonObject=jsonObject.optJSONObject("response");

        JSONObject status=jsonObject.optJSONObject("status");

        if(status.optString("message").equals("Success"))
        {
            JSONArray jsonArray=jsonObject.optJSONArray("images");
            url=jsonArray.optJSONObject(0).optString("url");
        }
        else
        {
            return null;
        }
        return url;
    }

    public static Bitmap getArtistBitmapImage(String artistName) throws IOException {

        Bitmap bitmap=null;
        artistName=URLEncoder.encode(artistName, "UTF-8");

        String url=ARTIST_API+API_KEY+"&name="+artistName+"&format=json&results=1&start=0";
        JSONObject jsonObject=getResponse(url);
        String imageUrl=getArtistImageUrl(jsonObject);

        if(url!=null)
        bitmap= Constants.getBitmapFromUrl(imageUrl);
        else
        return null;

       return bitmap;
    }
}

/*http://developer.echonest.com/api/v4/artist/images?api_key=SNGP2F06C2TFRKCCZ&id=ARH6W4X1187B99274F&format=json&results=1&start=0&license=unknown*/