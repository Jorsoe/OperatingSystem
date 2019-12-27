package memory;

import java.util.LinkedList;
import java.util.List;

public class Memory {

    public int size;
    public static int lastFreeZoneLocation;
    public static List<Zone> zones;

    public static class Zone{
        public int zoneSize;
        public int headAddress;
        public Boolean zoneState;

        public Zone(int zoneSize, int headAddress) {
            this.zoneSize = zoneSize;
            this.headAddress = headAddress;
            this.zoneState = true;
        }
    }

    public Memory(int size) {
        this.size = size;
        this.lastFreeZoneLocation = 0;
        this.zones = new LinkedList<>();
        zones.add(new Zone(size,0));
    }
}
