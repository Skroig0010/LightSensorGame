package jp.ac.titech.itpro.sdl.game.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapWithProperty{
    public class Property{
        public int gid;
        public int[] ids;
        public int x;
        public int y;
        public boolean canRelease;

        public Property(int gid, int[]ids, int x, int y, boolean canRelease){
            this.gid = gid;
            this.ids = ids;
            this.x = x;
            this.y = y;
            this.canRelease = canRelease;
        }
    }
    public int[][] data;
    public Map<Integer, Property> properties = new HashMap<>();
}
