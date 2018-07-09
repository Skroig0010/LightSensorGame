package jp.ac.titech.itpro.sdl.game.util;

import android.util.Log;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.MainActivity;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class JsonReader {
    public static MapWithProperty read(String filename) {
        try {
            InputStream is = MainActivity.instance.getAssets().open(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String txt = "";
            String line = br.readLine();
            while(line != null){
                txt += line;
                line = br.readLine();
            }
            br.close();
            MapWithProperty mapWithProperty = new MapWithProperty();

            // マップレイヤーの読み込み
            JSONArray json = new JSONObject(txt).getJSONArray("layers");
            JSONObject jsonMapData = json.getJSONObject(0);
            int width = jsonMapData.getInt("width");
            int height = jsonMapData.getInt("height");
            JSONArray jsonData = jsonMapData.getJSONArray("data");
            int[][] map = new int[height][width];
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    map[y][x] = jsonData.getInt(x + y * width) - 1;
                }
            }
            mapWithProperty.data = map;

            // プロパティレイヤーの読み込み
            JSONArray properties = json.getJSONObject(1).getJSONArray("objects");
            for(int i = 0; i < properties.length(); i++){
                JSONObject property = properties.getJSONObject(i);
                int gid;
                // tiledではgidが1大きいので減らす
                try {
                    gid = property.getInt("gid") - 1;
                }catch (JSONException e){
                    gid = 0;
                }
                int[] propertyId = new int[1];
                int x = property.getInt("x");
                int y = property.getInt("y");
                try {
                    Object propertyIdObject = property.getJSONObject("properties").get("id");
                    if (propertyIdObject instanceof String) {
                        propertyId = atoi(((String) propertyIdObject).split(","));
                    } else {
                        propertyId[0] = (int) propertyIdObject;
                    }
                }catch (JSONException e){
                    propertyId = null;
                }

                // polylineがあったら読み込む
                List<Vector2> polyline = new ArrayList<>();
                try{
                    JSONArray polyline1 = property.getJSONArray("polyline");
                    for (int j = 0; j < polyline1.length(); j++) {
                        JSONObject point = polyline1.getJSONObject(j);
                        polyline.add(new Vector2((float)point.getDouble("x") + x, (float)point.getDouble("y") + y));
                    }
                }catch (JSONException e){
                    polyline = null;
                }

                // canReleaseがあったら読み込む
                boolean canRelease;
                try {
                    canRelease = property.getJSONObject("properties").getBoolean("canRelease");
                }catch(JSONException e){
                    canRelease = false;
                }

                int rx = (x + 8) / 16;
                int ry = (y - 16 + 8) / 16;
                mapWithProperty.properties.put(rx + ry * width, mapWithProperty.new Property(gid, propertyId, x, y - 16, polyline, canRelease));
            }

            return mapWithProperty;

        } catch (IOException e) {
            Log.e("error", filename + " is not found.");
            return null;
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
            return null;
        }
    }

    private static int[] atoi(String[] ss){
        int[] is = new int[ss.length];
        for (int c = 0; c < ss.length; c++) {
            is[c] = Integer.parseInt(ss[c]);
        }
        return is;
    }
}
