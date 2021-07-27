import java.util.ArrayList;
import java.util.ListIterator;

public class DataCenter {
    public int uId;
    public int numberOfHosts;
    public int totalnumberOfPes;
    public float totalram;
    public long totalbw;
    public float totaldisk;
    DataCenter(int uId,int numberOfHosts,int totalnumberOfPes,float totalram,long totalbw,float totaldisk){
        this.uId=uId;
        this.numberOfHosts=numberOfHosts;
        this.totalnumberOfPes=totalnumberOfPes;
        this.totalram=totalram;
        this.totalbw=totalbw;
        this.totaldisk=totaldisk;
    }
    void updateDC(Host h){
        totalnumberOfPes=totalnumberOfPes-h.totalNumberOfPesHost;
        totalram=totalram-h.totalRamHost;
        totalbw=totalbw-h.totalBwHost;
        totaldisk=totaldisk-h.totalDiskHost;
    }
    void RemainingDCResources(){
        System.out.println("DC PEs left:" + totalnumberOfPes  );
        System.out.println("DC Ram left:" + totalram  );
    }
}
