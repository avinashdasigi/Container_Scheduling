public class Container {
    public int uId;
   // public int vmId;
    public int numberOfPes;
    public float ram;
    public long bw;
    public float disk;
    Container(int uId,int numberOfPes,float ram,long bw,float disk){
        this.uId=uId;
        this.numberOfPes=numberOfPes;
        this.ram=ram;
        this.bw=bw;
        this.disk=disk;
    }

    public int getuId() {
        return uId;
    }

    public int getNumberOfPes() {
        return numberOfPes;
    }

    public float getRam() {
        return ram;
    }

    public long getBw() {
        return bw;
    }

    public float getDisk() {
        return disk;
    }
}
