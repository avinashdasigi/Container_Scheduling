import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class Host {
    public int uId;
    public int totalNumberOfPesHost;
    public float totalRamHost;
    public long totalBwHost;
    public float totalDiskHost;
    public int remainingNumberOfPesHost;
    public float remainingRamHost;
    public long remainingBwHost;
    public float remainingDiskHost;
    public int tempNumberOfPesHost;
    public float tempRamHost;
    public long tempBwHost;
    public float tempDiskHost;
    public int netPeUsedOnHost;
    public float netRamUsedOnHost;
    public int netPeWastedOnHost;
    public float netRamWastedOnHost;

    public int finalPeWastedAfterRequestHost;
    public float finalRamWastedAfterRequestHost;

    public double pfValueHost;

    public int vmMaxPe;
    public float vmMaxRam;

    public   ArrayList<Vm> vmList= new ArrayList<Vm>();
    Host(int uId,int numberOfPes,float ram,long bw,float disk){
        this.uId=uId;
        this.totalNumberOfPesHost=numberOfPes;
        this.totalRamHost=ram;
        this.totalBwHost=bw;
        this.totalDiskHost=disk;
        tempNumberOfPesHost=totalNumberOfPesHost;
        tempRamHost=totalRamHost;
        tempBwHost=totalBwHost;
        tempDiskHost=totalDiskHost;
    }
    public int getuId() {
        return uId;
    }

    public int getNumberOfPes() {
        return totalNumberOfPesHost;
    }

    public float getRam() {
        return totalRamHost;
    }

    public long getBw() {
        return totalBwHost;
    }

    public float getDisk() {
        return totalDiskHost;
    }

    void addVm(Vm v){
        vmList.add(v);
    }
    void updateHost(Vm v){
        remainingNumberOfPesHost= tempNumberOfPesHost- v.getNumberOfPes();
        remainingRamHost= tempRamHost - v.getRam();
        remainingBwHost= tempBwHost- v.getBw();
        remainingDiskHost = tempDiskHost - v.getDisk();
        tempNumberOfPesHost=remainingNumberOfPesHost;
        tempRamHost=remainingRamHost;
        tempBwHost=remainingBwHost;
        tempDiskHost=remainingDiskHost;

    }
    void totalAllocatedResourcestoHostVm(){
        System.out.println("Total allocated resources on Host and Vm:");
        String hnum= "Host " + uId;
        System.out.println(hnum + " Details:");
        System.out.println("Id:"+uId);
        System.out.println("PEs:"+ totalNumberOfPesHost);
        System.out.println("RAM:"+ totalRamHost);
        System.out.println("B/W"+ totalBwHost);
        System.out.println("Disk:"+ totalDiskHost);
        //System.out.println(vmList);
        ListIterator<Vm> it1 = vmList.listIterator();
        Vm v = null;
        Container c= null;
        ArrayList<Container> clist= null;
        while (it1.hasNext()){
            v=it1.next();
            String hnum1= "VM " + v.uId;
            System.out.println(hnum1 + " Details:");
            System.out.println("Id:"+ v.uId);
            System.out.println("PEs:"+ v.totalNumberOfPesVm);
            System.out.println("RAM:"+ v.totalRamVm);
            System.out.println("B/W:"+ v.totalBwVm);
            System.out.println("Disk:"+ v.totalDiskVm);
            System.out.println(v.containerList);
            clist=v.containerList;
            ListIterator<Container> it2 = clist.listIterator();
            while(it2.hasNext()){
                c=it2.next();
                String hnum2= "Container" + c.uId;
                System.out.println(hnum2 + " Details");
                System.out.println("Id:"+ c.uId);
                System.out.println("PEs:"+ c.numberOfPes);
                System.out.println("RAM:"+ c.ram);
                System.out.println("B/W:"+ c.bw);
                System.out.println("Disk:"+ c.disk);
            }
        }
    }
    void remainingResourcesOnHostVm(){
            System.out.println("Remaining resources on Host and Vm:");
            String hnum= "Host " + uId;
            System.out.println(hnum + " Details:");
            System.out.println("Id:"+uId);
            System.out.println("PEs:"+ remainingNumberOfPesHost);
            System.out.println("RAM:"+ remainingRamHost);
            System.out.println("B/W"+ remainingBwHost);
            System.out.println("Disk:"+ remainingDiskHost);
            //System.out.println(vmList);
            ListIterator<Vm> it1 = vmList.listIterator();
            Vm v = null;
            Container c= null;
            ArrayList<Container> clist= null;
            while (it1.hasNext()){
                v=it1.next();
                String hnum1= "VM " + v.uId;
                System.out.println(hnum1 + " Details:");
                System.out.println("Id:"+ v.uId);
                System.out.println("PEs:"+ v.remainingNumberOfPesVm);
                System.out.println("RAM:"+ v.remainingRamVm);
                System.out.println("B/W:"+ v.remainingBwVm);
                System.out.println("Disk:"+ v.remainingDiskVm);
                System.out.println(v.containerList);
                clist=v.containerList;
                ListIterator<Container> it2 = clist.listIterator();
                while(it2.hasNext()){
                    c=it2.next();
                    String hnum2= "Container" + c.uId;
                    System.out.println(hnum2 + " Details");
                    System.out.println("Id:"+ c.uId);
                    System.out.println("PEs:"+ c.numberOfPes);
                    System.out.println("RAM:"+ c.ram);
                    System.out.println("B/W:"+ c.bw);
                    System.out.println("Disk:"+ c.disk);
                }
            }
    }
    void NetResourcesUtiizedOnhost(){
        netPeUsedOnHost=0;
        netRamUsedOnHost=0;
        ListIterator<Vm> it1 = vmList.listIterator();
        Vm v = null;
        Container c= null;
        ArrayList<Container> clist= null;
        while (it1.hasNext()){
            v=it1.next();
            clist=v.containerList;
            ListIterator<Container> it2 = clist.listIterator();
            while(it2.hasNext()){
                c=it2.next();
                netPeUsedOnHost=netPeUsedOnHost+c.numberOfPes;
                netRamUsedOnHost=netRamUsedOnHost + c.ram;
            }
        }
    }
    void calResoursesWastedOnHost(){
        netPeWastedOnHost=totalNumberOfPesHost-netPeUsedOnHost;
        netRamWastedOnHost=totalRamHost-netRamUsedOnHost;
       // System.out.println(netPeWastedOnHost);
        //System.out.println(netRamWastedOnHost);
    }
    void finalPeAfterRequest(){

    }
}
