import java.util.ArrayList;

public class Vm {
    public int uId;
    //public int hostId;
    public int totalNumberOfPesVm;
    public float totalRamVm;
    public long totalBwVm;
    public float totalDiskVm;

    public int remainingNumberOfPesVm;
    public float remainingRamVm;
    public long remainingBwVm;
    public float remainingDiskVm;

    public int tempNumberOfPesVm;
    public float tempRamVm;
    public long tempBwVm;
    public float tempDiskVm;

    public int finalPeWastedAfterRequestVm;
    public float finalRamWastedAfterRequestVm;

    public double vfValueVm;

    public ArrayList<Container> containerList= new ArrayList<Container>();
    public static ArrayList<Container> cMir= new ArrayList<Container>();
    Vm(int uId,int numberOfPes,float ram,long bw,float disk){
        this.uId=uId;
        this.totalNumberOfPesVm=numberOfPes;
        this.totalRamVm=ram;
        this.totalBwVm=bw;
        this.totalDiskVm=disk;
        tempNumberOfPesVm=totalNumberOfPesVm;
        tempRamVm=totalRamVm;
        tempBwVm=totalBwVm;
        tempDiskVm=totalDiskVm;

    }
    public int getuId() {
        return uId;
    }

    public int getNumberOfPes() {
        return totalNumberOfPesVm;
    }

    public float getRam() {
        return totalRamVm;
    }

    public long getBw() {
        return totalBwVm;
    }

    public float getDisk() {
        return totalDiskVm;
    }
    void addContainer(Container c){
        containerList.add(c);
        cMir.add(c);

    }
    void updateVm(Container c){
        remainingNumberOfPesVm= tempNumberOfPesVm- c.getNumberOfPes();
        remainingRamVm= tempRamVm - c.getRam();
        remainingBwVm= tempBwVm- c.getBw();
        remainingDiskVm = tempDiskVm - c.getDisk();
        tempNumberOfPesVm=remainingNumberOfPesVm;
        tempRamVm=remainingRamVm;
        tempBwVm=remainingBwVm;
        tempDiskVm=remainingDiskVm;

    }
}
