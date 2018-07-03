package jp.ac.titech.itpro.sdl.game.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.MainActivity;

public class CSVReader {
    public static int[][] read(String filename){
        try {
            InputStream is = MainActivity.instance.getAssets().open(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            List<int[]> data = new ArrayList<>();
            String line = br.readLine();
            while(line != null){
                data.add(atoi(line.split(",")));
                line = br.readLine();
            }
            br.close();
            return data.toArray(new int[data.size()][data.get(0).length]);
        }catch (IOException e){
            Log.e("error", filename + " is not found.");
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
