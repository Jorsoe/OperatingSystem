package operate;

import memory.Memory;
import java.util.Scanner;

public class Allocation {

    public static Boolean executeAllocation(int size, int location, Memory.Zone tempZone)
    {
        Memory.Zone zone = new Memory.Zone(tempZone.zoneSize - size,tempZone.headAddress + size);
        Memory.zones.add(location+1 ,zone);
        tempZone.zoneSize = size;
        tempZone.zoneState = false;
        return true;
    }

    public static void executeReclamation()
    {
        System.out.print("键入要回收的分区编号：");
        Scanner scanner = new Scanner(System.in);
        int zoneId = scanner.nextInt();
        Memory.Zone zoneTemp = Memory.zones.get(zoneId);
        if (zoneTemp.zoneState)                             //判断内存块是否可被回收
        {
            System.out.println("该分区为空闲状态！");
        }
        if (zoneId < (Memory.zones.size()-1))               //判断该内存块是否为最后一个
        {
            if(Memory.zones.get(zoneId+1).zoneState)        //判断该内存块相邻后一个是否为空闲
            {
                Memory.Zone nextZone = Memory.zones.get(zoneId+1);
                zoneTemp.zoneSize = nextZone.zoneSize + zoneTemp.zoneSize;
                Memory.zones.remove(nextZone);
            }
        }
        if (zoneId >0)                                      //判断该内存块是否为第一个
        {
            if(Memory.zones.get(zoneId-1).zoneState)        //判断该内存块相邻前一个是否为空闲
            {
                Memory.Zone lastZone = Memory.zones.get(zoneId-1);
                lastZone.zoneSize = zoneTemp.zoneSize + lastZone.zoneSize;
                Memory.zones.remove(zoneId);
                zoneId--;
            }
        }
        Memory.zones.get(zoneId).zoneState = true;          //将该内存分区改为空闲
        System.out.println("内存回收成功，大小为: "+Memory.zones.get(zoneId).zoneSize);
    }

    public static void showZones()
    {
        System.out.println("ZoneID\tZoneHeadAddress\tZoneSize\tZoneState");
        for (int i = 0;i < Memory.zones.size();i++)
        {
            Memory.Zone printZone = Memory.zones.get(i);
            System.out.println(i+"\t\t\t"+printZone.headAddress+"\t\t\t\t"+printZone.zoneSize+"\t\t\t"+printZone.zoneState);
        }
    }
}
