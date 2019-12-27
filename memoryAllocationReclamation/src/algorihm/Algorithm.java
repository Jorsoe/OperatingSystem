package algorihm;

import operate.Allocation;
import memory.Memory;
import memory.Memory.Zone;
import static memory.Memory.lastFreeZoneLocation;
import static memory.Memory.zones;

public class Algorithm {

    public static void firstAdapt(int size)
    {
        Boolean result = false;
        for (lastFreeZoneLocation = 0;lastFreeZoneLocation < zones.size();lastFreeZoneLocation++)
        {
            Memory.Zone tempZone = zones.get(lastFreeZoneLocation);
            if(tempZone.zoneState && size<tempZone.zoneSize)
            {
                result = Allocation.executeAllocation(size,lastFreeZoneLocation,tempZone);
                if (result)
                {
                    System.out.println("已成功分配大小为<"+size+">的内存！");
                }
                else
                {
                    System.out.println("无可用空闲内存空间！");
                }
                return;
            }
        }
    }

    public static void nextAdapt(int size)
    {
        Boolean result = false;
        Memory.Zone tempZone = zones.get(lastFreeZoneLocation);
        if (tempZone.zoneState)
        {
            if (tempZone.zoneSize > size)
            {
                result = Allocation.executeAllocation(size,lastFreeZoneLocation,tempZone);
                return;
            }
        }
        for (int i=(lastFreeZoneLocation+1)%zones.size();i!=lastFreeZoneLocation;i=(i+1)%zones.size())
        {
            tempZone = zones.get(i);
            if (tempZone.zoneState)
            {
                if (tempZone.zoneSize > size)
                {
                    result = Allocation.executeAllocation(size,lastFreeZoneLocation,tempZone);
                    if (result)
                    {
                        System.out.println("已成功分配大小为<"+size+">的内存！");
                    }
                    else
                    {
                        System.out.println("无可用空闲内存空间！");
                    }
                    return;
                }
            }
        }
    }

    public static void bestAdapt(int size)
    {
        int target = -1;
        int minSize = size;
        for (lastFreeZoneLocation = 0;lastFreeZoneLocation < zones.size();lastFreeZoneLocation++)
        {
            Zone tempZone = zones.get(lastFreeZoneLocation);
            if (tempZone.zoneState)
            {
                if (tempZone.zoneSize > size)
                {
                    if (tempZone.zoneSize - size <= minSize)
                    {
                        minSize = tempZone.zoneSize - size;
                        target = lastFreeZoneLocation;
                    }
                }
            }
        }
        if (target == -1)
        {
            System.out.println("无可用空闲内存空间！");
        }
        else
        {
            Allocation.executeAllocation(size,target,zones.get(target));
            System.out.println("已成功分配大小为<"+size+">的内存！");
        }
    }

    public static void worstAdapt(int size)
    {
        int target = -1;
        int maxSize = 0;
        for (lastFreeZoneLocation = 0;lastFreeZoneLocation < zones.size();lastFreeZoneLocation++)
        {
            Zone tempZone = zones.get(lastFreeZoneLocation);
            if (tempZone.zoneState)
            {
                if (tempZone.zoneSize > size)
                {
                    if (maxSize < tempZone.zoneSize - size)
                    {
                        maxSize = tempZone.zoneSize - size;
                        target = lastFreeZoneLocation;
                    }
                }
            }
        }
        if (target == -1)
        {
            System.out.println("无可用空闲内存空间！");
        }
        else
        {
            Allocation.executeAllocation(size,target,zones.get(target));
            System.out.println("已成功分配大小为<"+size+">的内存！");
        }
    }
}
