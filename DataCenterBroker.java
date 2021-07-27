import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;

public class DataCenterBroker {
    int  hostuId=0;
    int  vmuId=0;
    int  contuId=0;
    public   ArrayList<Host> hostRepo= new ArrayList<Host>();
    public   ArrayList<Vm> vmRepo= new ArrayList<Vm>();
    public   ArrayList<Container> contRepo= new ArrayList<Container>();
    double w1=0.5;
    double w2=0.5;
    double flag1;
    double flag2;
    double maxPfValueHost;
    double maxVfValueVm;
    double minPfValueHost;
    double minVfValueVm;
    int maxPeVm;
    double maxRamVm;
    int selectedVmuId;
    int selectedHostuId;
    int hostIdOfSelectedVm;
    int newBestFitVm;
    int newMaxFitVm;
    int newBestFitHost;
    int newMaxFitHost;
    int newMaxFitCont;
    int initHost;
    int initVm;
    int initCont;

    int contNum;
    int vmNum;
    int hostNum;
    DataCenter obj;

    DataCenterBroker(){
        DataCenter dc= new DataCenter(1,10,5000,10000,1000000,1000000);
        System.out.println("Total  DataCenter Resources:");
        System.out.println("DC PEs:" + dc.totalnumberOfPes);
        System.out.println("DC PEs:" + dc.totalram);
        getDC(dc);
        Host h1= createPm(20,45);
        initHost++;
        Host h2=createPm(15,25);
        initHost++;
        Vm v1=createVm(1,1);
        initVm++;
        Vm v2=createVm(2,4);
        initVm++;
        placeVmOnPm(h1,v1);
        placeVmOnPm(h1,v2);
        Vm v3=createVm(3,5);
        initVm++;
        Vm v4=createVm(5,10);
        initVm++;
        placeVmOnPm(h2,v3);
        placeVmOnPm(h2,v4);
        Container c1=createCont(1,1,25000,64);
        initCont++;
        Container c2=createCont(2,3,25000,64);
        initCont++;
        Container c3=createCont(1,2,25000,64);
        initCont++;
        Container c4=createCont(5,10,25000,64);
        initCont++;
        placeContOnVm(v1,c1);
        placeContOnVm(v2,c2);
        placeContOnVm(v3,c3);
        placeContOnVm(v4,c4);
       // getTotalResourcesOnHost(h1);
        //getTotalResourcesOnHost(h2);
        updateResourcesOnHost(h1,v1,c1);
        updateResourcesOnHost(h1,v2,c2);
        updateResourcesOnHost(h2,v3,c3);
        updateResourcesOnHost(h2,v4,c4);
        //getRemainingResourcesOnHost(h1);
        //getRemainingResourcesOnHost(h2);
        h1.NetResourcesUtiizedOnhost();
        h1.calResoursesWastedOnHost();
        h2.NetResourcesUtiizedOnhost();
        h2.calResoursesWastedOnHost();
        findPeMaxOnEachVm();
        /*System.out.println(hostRepo);
        System.out.println(vmRepo);
        System.out.println(contRepo);*/
    }

    Vm createVm(int numberofPEs, float ram){
        vmNum++;
        Vm newVm=vmType(numberofPEs,ram);
        vmRepo.add(newVm);
        return newVm;
    }
    Host createPm(int numberofPEs, float ram){

        Boolean flag=dcCheck(numberofPEs,ram);
        if(flag){
            Host newHost=pmType(numberofPEs,ram);
            hostNum++;
            hostRepo.add(newHost);
            updateDC(newHost);
            return newHost;
        }
        else{
            System.out.println("Data Center is full. Cannot create a new Host.");
            return null;
        }

    }
    Container createCont(int numberofPEs, float ram,long bw,float disk){
        contNum++;
        contuId++;
        Container newCont= new Container(contuId,numberofPEs,ram,bw,disk);
        contRepo.add(newCont);
        return newCont;
    }
    Vm vmType(int numberofPEs, float ram){
        if(numberofPEs<=1 && ram<=1){
            vmuId++;
            Vm vmType1= new Vm(vmuId,1,1,200,5);
            return vmType1;
        }
        else if((numberofPEs<=1) && (ram>1 && ram<=2)){
            vmuId++;
            Vm vmType2= new Vm(vmuId,1,2,200,5);
            return vmType2;
        }
        else if((numberofPEs>1 && numberofPEs<=2) && (ram>2 && ram<=4)){
            vmuId++;
            Vm vmType3= new Vm(vmuId,2,4,200,5);
            return vmType3;
        }
        else if((numberofPEs>2 && numberofPEs<=4) && (ram>4 && ram<=8)){
            vmuId++;
            Vm vmType4= new Vm(vmuId,4,8,200,5);
            return vmType4;
        }
        else if((numberofPEs>4 && numberofPEs<=8) && (ram>8 && ram<=16)){
            vmuId++;
            Vm vmType4= new Vm(vmuId,8,16,200,5);
            return vmType4;
        }
        else{
            return null;
        }
    }
    Host pmType(int numberofPEs, float ram){
      if(numberofPEs<=18 && ram<=30){
          hostuId++;
          Host htype1= new Host(hostuId,18,30,250000,1024);
          return htype1;
      }
      else if((numberofPEs>18 && numberofPEs<=35) && (ram>30 && ram<=50)){
          hostuId++;
          Host htype2= new Host(hostuId,35,60,500000,2048);
          return htype2;
      }
      else{
          return null;
      }
    }
    void placeVmOnPm(Host h, Vm v){
        h.addVm(v);
    }
    void placeContOnVm(Vm v, Container c){
        v.addContainer(c);
    }
    void getTotalResourcesOnHost(Host h){
        h.totalAllocatedResourcestoHostVm();
    }
    void updateResourcesOnHost(Host h, Vm v,Container c){
        h.updateHost(v);
        v.updateVm(c);
    }
    void getRemainingResourcesOnHost(Host h){
        h.remainingResourcesOnHostVm();
    }

    double pfCalService(Host h,int pe,float ram){
        h.finalPeWastedAfterRequestHost=h.netPeWastedOnHost-pe;
        h.finalRamWastedAfterRequestHost=h.netRamWastedOnHost-ram;
        h.pfValueHost=(w1*(h.finalPeWastedAfterRequestHost/h.totalNumberOfPesHost)) + (w2*(h.finalRamWastedAfterRequestHost/h.totalRamHost));
        double pf= h.pfValueHost;
        return pf;
    }
    double vfCalService(Vm v,int pe,float ram,double pf){
        v.finalPeWastedAfterRequestVm = v.remainingNumberOfPesVm-pe;
        v.finalRamWastedAfterRequestVm = v.remainingRamVm-ram;
        v.vfValueVm = (w1 * (v.finalPeWastedAfterRequestVm / v.totalNumberOfPesVm)) + (w2 * (v.finalRamWastedAfterRequestVm / v.totalRamVm)) + pf;
        double vf=v.vfValueVm;
        return vf;
    }
    void maxFitNew(int pe,float ram,long bw,float disk){
        ListIterator<Host> it1 = hostRepo.listIterator();
       // Map<Double,Integer> m= new HashMap<Double,Integer>();
        Map<Double,Map<Vm,Host>> m= new HashMap<Double,Map<Vm,Host>>();

        Host h=null;
        boolean mark=false;
        while (it1.hasNext()) {
            h = it1.next();
            if((h.netPeWastedOnHost>=pe && h.netRamWastedOnHost>=ram) && (h.vmMaxPe>=pe && h.vmMaxRam>=ram)){
                double pf=pfCalService(h,pe,ram);
                ArrayList<Vm> vlist = new ArrayList<Vm>();
                vlist = h.vmList;
                Vm v=null;
                ListIterator<Vm> it2 = vlist.listIterator();
                while (it2.hasNext()) {
                    v = it2.next();
                    if(v.remainingNumberOfPesVm>=pe && v.remainingRamVm>=ram){
                        //int hostID=h.uId;
                        //int vmID=v.uId;
                        double vf=vfCalService(v,pe,ram,pf);
                        //vmHost.put(vmID,hostID);
                        Map<Vm,Host> vmHost= new  HashMap<Vm,Host>();
                        vmHost.put(v,h);
                        m.put(vf,vmHost);
                        mark= true;
                        /*int vmID= v.uId;
                        m.put(vf,vmID);*/
                    }
                }
            }
        }
        if(mark){
            Double maxKey = Collections.max(m.keySet());
            Map<Vm,Host> ref=m.get(maxKey);
            mapContToVmMaxFit(ref,pe,ram,bw,disk);
        }
        else{
            //System.out.println("All VMs are full.Instantiate a new VM");
            findHostForNewVmMaxFit(pe,ram,bw,disk);
        }
        System.out.println(hostRepo);
    }


    void findHostForNewVmMaxFit(int pe,float ram,long bw,float disk){
        Map<Double,Host> m = new HashMap<Double,Host>();
        ListIterator<Host> it2 = hostRepo.listIterator();
        Host h=null;
        Boolean mark=false;
        while (it2.hasNext()) {
            h = it2.next();
            if(h.remainingNumberOfPesHost>=pe && h.remainingRamHost>=ram){
                double pf=pfCalService(h,pe,ram);
                m.put(pf,h);
                mark=true;
            }
        }
        if(mark){
            Double maxKey = Collections.max(m.keySet());
            Host getHost =m.get(maxKey);
            newMaxFitVm++;
            Container instaCont;
            instaCont=createCont(pe,ram,bw,disk);
            newMaxFitCont++;
            Vm instaVm=createVm(pe,ram);
            if((instaVm.totalNumberOfPesVm>getHost.tempNumberOfPesHost) || (instaVm.totalRamVm>getHost.tempRamHost)){
                createNewHostForVmMaxFit(pe,ram,bw,disk);
                return;
            }
            placeVmOnPm(getHost,instaVm);
            placeContOnVm(instaVm,instaCont);
            getHost.updateHost(instaVm);
            instaVm.updateVm(instaCont);
            getHost.NetResourcesUtiizedOnhost();
            getHost.calResoursesWastedOnHost();
            findPeMaxOnEachVm();
        }
        else{
            //System.out.println("All Hosts are full.Instantiate a new Host");
            createNewHostForVmMaxFit(pe,ram,bw,disk);
        }
    }
    void  createNewHostForVmMaxFit(int pe,float ram,long bw,float disk)
    {
        Host instaHost;
        instaHost=createPm(pe,ram);
        if(instaHost==null){
            System.out.println("Expand your DataCenter.Cannot satisfy the container request for pe :"+ pe +"and ram:" + ram);
            return;
        }
        newMaxFitHost++;
        Vm instaVm=createVm(pe,ram);
        newMaxFitVm++;
        Container  instaCont=createCont(pe,ram,bw,disk);
        newMaxFitCont++;
        placeVmOnPm(instaHost,instaVm);
        placeContOnVm(instaVm,instaCont);
        instaHost.updateHost(instaVm);
        instaVm.updateVm(instaCont);
        instaHost.NetResourcesUtiizedOnhost();
        instaHost.calResoursesWastedOnHost();
        findPeMaxOnEachVm();

    }
    void mapContToVmMaxFit(Map<Vm,Host> ref,int pe,float ram,long bw,float disk){
        Set st = ref.entrySet();
        Iterator it = st.iterator();
        Map.Entry entry = (Map.Entry) it.next();
        Vm getVm= (Vm) entry.getKey();
        Host getHost= (Host) entry.getValue();
        Container instaCont;
        instaCont=createCont(pe,ram,bw,disk);
        newMaxFitCont++;
        placeContOnVm(getVm,instaCont);
        getVm.updateVm(instaCont);
       // getHost.updateHost(getVm);
        getHost.NetResourcesUtiizedOnhost();
        getHost.calResoursesWastedOnHost();
        findPeMaxOnEachVm();
    }
    void bestFitNew(int pe,float ram,long bw,float disk){
        ListIterator<Host> it1 = hostRepo.listIterator();
        // Map<Double,Integer> m= new HashMap<Double,Integer>();
        Map<Double,Map<Vm,Host>> m= new HashMap<Double,Map<Vm,Host>>();

        Host h=null;
        boolean mark=false;
        while (it1.hasNext()) {
            h = it1.next();
            if((h.netPeWastedOnHost>=pe && h.netRamWastedOnHost>=ram) && (h.vmMaxPe>=pe && h.vmMaxRam>=ram)){
                double pf=pfCalService(h,pe,ram);
                ArrayList<Vm> vlist = new ArrayList<Vm>();
                vlist = h.vmList;
                Vm v=null;
                ListIterator<Vm> it2 = vlist.listIterator();
                while (it2.hasNext()) {
                    v = it2.next();
                    if(v.remainingNumberOfPesVm>=pe && v.remainingRamVm>=ram){
                        //int hostID=h.uId;
                        //int vmID=v.uId;
                        double vf=vfCalService(v,pe,ram,pf);
                        //vmHost.put(vmID,hostID);
                        Map<Vm,Host> vmHost= new  HashMap<Vm,Host>();
                        vmHost.put(v,h);
                        m.put(vf,vmHost);
                        mark= true;
                        /*int vmID= v.uId;
                        m.put(vf,vmID);*/
                    }
                }
            }
        }
        if(mark){
            Double minKey = Collections.min(m.keySet());
            Map<Vm,Host> ref=m.get(minKey);
            mapContToVmBestFit(ref,pe,ram,bw,disk);
        }
        else{
           // System.out.println("All VMs are full.Instantiate a new VM");
            findHostForNewVmBestFit(pe,ram,bw,disk);
        }
        //System.out.println(hostRepo);
    }
    void  mapContToVmBestFit(Map<Vm,Host> ref,int pe,float ram,long bw,float disk){
        Set st = ref.entrySet();
        Iterator it = st.iterator();
        Map.Entry entry = (Map.Entry) it.next();
        Vm getVm= (Vm) entry.getKey();
        Host getHost= (Host) entry.getValue();
        Container instaCont;
        instaCont=createCont(pe,ram,bw,disk);
        placeContOnVm(getVm,instaCont);
        getVm.updateVm(instaCont);
        // getHost.updateHost(getVm);
        getHost.NetResourcesUtiizedOnhost();
        getHost.calResoursesWastedOnHost();
        findPeMaxOnEachVm();
    }
    void findHostForNewVmBestFit(int pe,float ram,long bw,float disk){
        Map<Double,Host> m = new HashMap<Double,Host>();
        ListIterator<Host> it2 = hostRepo.listIterator();
        Host h=null;
        Boolean mark=false;
        while (it2.hasNext()) {
            h = it2.next();
            if(h.remainingNumberOfPesHost>=pe && h.remainingRamHost>=ram){
                double pf=pfCalService(h,pe,ram);
                m.put(pf,h);
                mark=true;
            }
        }
        if(mark){
            Double minKey = Collections.min(m.keySet());
            Host getHost =m.get(minKey);
            newBestFitVm++;
            Container instaCont;
            instaCont=createCont(pe,ram,bw,disk);
            Vm instaVm=createVm(pe,ram);
            if((instaVm.totalNumberOfPesVm>getHost.tempNumberOfPesHost) || (instaVm.totalRamVm>getHost.tempRamHost)){
                createNewHostForVmBestFit(pe,ram,bw,disk);
                return;
            }

            placeVmOnPm(getHost,instaVm);
            placeContOnVm(instaVm,instaCont);
            getHost.updateHost(instaVm);
            instaVm.updateVm(instaCont);
            getHost.NetResourcesUtiizedOnhost();
            getHost.calResoursesWastedOnHost();
            findPeMaxOnEachVm();
        }
        else{
           // System.out.println("All Hosts are full.Instantiate a new Host");
            createNewHostForVmBestFit(pe,ram,bw,disk);
        }
    }
    void createNewHostForVmBestFit(int pe,float ram,long bw,float disk){
        Host instaHost;
        instaHost=createPm(pe,ram);
        if(instaHost==null){
            System.out.println("Expand your DataCenter.Cannot satisfy the container request for pe :"+ pe +"and ram:" + ram);
            return;
        }
        newBestFitHost++;
        Vm instaVm=createVm(pe,ram);
        newBestFitVm++;
        Container  instaCont=createCont(pe,ram,bw,disk);
        placeVmOnPm(instaHost,instaVm);
        placeContOnVm(instaVm,instaCont);
        instaHost.updateHost(instaVm);
        instaVm.updateVm(instaCont);
        instaHost.NetResourcesUtiizedOnhost();
        instaHost.calResoursesWastedOnHost();
        findPeMaxOnEachVm();
    }

    /*void maxFit(int pe,float ram,long bw,float disk){
        ListIterator<Host> it1 = hostRepo.listIterator();
        Host h=null;
        Vm v=null;
        boolean mark=false;
        ArrayList<Vm> vlist = new ArrayList<Vm>();
        maxVfValueVm=0;
        double temp1=0;
        double temp2=0;
        maxPfValueHost=0;
        flag1 = Integer.MIN_VALUE;
        flag2 = Integer.MIN_VALUE;
        while (it1.hasNext()){
            h=it1.next();
            if(h.netPeWastedOnHost<pe && h.netRamWastedOnHost<ram) {
                h.finalPeWastedAfterRequestHost = h.netPeWastedOnHost;
                h.finalRamWastedAfterRequestHost = h.netRamWastedOnHost;
                h.pfValueHost = (w1 * (h.finalPeWastedAfterRequestHost / h.totalNumberOfPesHost)) + (w2 * (h.finalRamWastedAfterRequestHost / h.totalRamHost));
                vlist = h.vmList;
                ListIterator<Vm> it2 = vlist.listIterator();
                while (it2.hasNext()) {
                    v = it2.next();
                    v.finalPeWastedAfterRequestVm = v.remainingNumberOfPesVm;
                    v.finalRamWastedAfterRequestVm = v.remainingRamVm;
                    v.vfValueVm = (w1 * (v.finalPeWastedAfterRequestVm / v.totalNumberOfPesVm)) + (w2 * (v.finalRamWastedAfterRequestVm / v.totalRamVm)) + h.pfValueHost;
                }
            }

            if((h.netPeWastedOnHost>=pe && h.netRamWastedOnHost>=ram) && (h.vmMaxPe<pe || h.vmMaxRam<ram)) {
                h.finalPeWastedAfterRequestHost = h.netPeWastedOnHost;
                h.finalRamWastedAfterRequestHost = h.netRamWastedOnHost;
                h.pfValueHost = (w1 * (h.finalPeWastedAfterRequestHost / h.totalNumberOfPesHost)) + (w2 * (h.finalRamWastedAfterRequestHost / h.totalRamHost));
                vlist = h.vmList;
                ListIterator<Vm> it2 = vlist.listIterator();
                while (it2.hasNext()) {
                    v = it2.next();
                    v.finalPeWastedAfterRequestVm = v.remainingNumberOfPesVm;
                    v.finalRamWastedAfterRequestVm = v.remainingRamVm;
                    v.vfValueVm = (w1 * (v.finalPeWastedAfterRequestVm / v.totalNumberOfPesVm)) + (w2 * (v.finalRamWastedAfterRequestVm / v.totalRamVm)) + h.pfValueHost;
                }
            }

            if((h.netPeWastedOnHost>=pe && h.netRamWastedOnHost>=ram) && (h.vmMaxPe>=pe && h.vmMaxRam>=ram)){
                h.finalPeWastedAfterRequestHost=h.netPeWastedOnHost-pe;
                h.finalRamWastedAfterRequestHost=h.netRamWastedOnHost-ram;
                h.pfValueHost=(w1*(h.finalPeWastedAfterRequestHost/h.totalNumberOfPesHost)) + (w2*(h.finalRamWastedAfterRequestHost/h.totalRamHost));
                temp1= h.pfValueHost;
                mark= true;
                vlist=h.vmList;
                ListIterator<Vm> it2 = vlist.listIterator();
                while (it2.hasNext()){
                    v=it2.next();
                    if(v.remainingNumberOfPesVm>=pe && v.remainingRamVm>=ram ){
                        v.finalPeWastedAfterRequestVm=v.remainingNumberOfPesVm-pe;
                        v.finalRamWastedAfterRequestVm=v.remainingRamVm-ram;
                        v.vfValueVm=(w1*(v.finalPeWastedAfterRequestVm/v.totalNumberOfPesVm)) + (w2*(v.finalRamWastedAfterRequestVm/v.totalRamVm)) + h.pfValueHost ;
                        temp2=v.vfValueVm;
                    }
                    else{
                        //v.vfValueVm=0;
                        temp2=0;
                    }
                    if(temp2 > flag1){
                        maxVfValueVm=temp2;
                        flag1=maxVfValueVm;
                        selectedVmuId=v.uId;
                        hostIdOfSelectedVm=h.uId;
                    }
                }
            }
            else{
               // h.pfValueHost=0;
                temp1=0;
            }
            if(temp1 > flag2){
                maxPfValueHost= temp1;
                flag2=maxPfValueHost;
                selectedHostuId=h.uId;
            }

            }
        ListIterator<Vm> it3 = vmRepo.listIterator();
        ListIterator<Host> it4 = hostRepo.listIterator();
        ListIterator<Host> it5 = hostRepo.listIterator();
        Vm getVm= null;
        Host getHost=null;
        Host getHostOfVm=null;
        Vm found;
        while(it3.hasNext()){
            getVm= it3.next();
            if(getVm.uId==selectedVmuId){
                break;
            }
        }
        while(it4.hasNext()){
            getHost= it4.next();
            if(getHost.uId==selectedHostuId){
                break;
            }
        }
        while(it5.hasNext()){
            getHostOfVm= it5.next();
            if(getHostOfVm.uId==hostIdOfSelectedVm){
                break;
            }
        }
        if(maxVfValueVm!=0){
            Container instaCont;
            instaCont=createCont(pe,ram,bw,disk);
            placeContOnVm(getVm,instaCont);
            getVm.updateVm(instaCont);
           // getHost.updateHost(getVm);
            getHostOfVm.NetResourcesUtiizedOnhost();
            getHostOfVm.calResoursesWastedOnHost();
            findPeMaxOnEachVm();

            //getHostOfVm.totalAllocatedResourcestoHostVm();
            //getHostOfVm.remainingResourcesOnHostVm();

        }
        if(maxVfValueVm==0 && mark==true){
            System.out.println("All VMs are full.Instantiate a new VM");
            newMaxFitVm++;
            Container instaCont;
            instaCont=createCont(pe,ram,bw,disk);
            Vm instaVm=createVm(pe,ram);
            placeVmOnPm(getHost,instaVm);
            placeContOnVm(instaVm,instaCont);
            getHost.updateHost(instaVm);
            instaVm.updateVm(instaCont);
            getHost.NetResourcesUtiizedOnhost();
            getHost.calResoursesWastedOnHost();
           // getHost.totalAllocatedResourcestoHostVm();
            //getHost.remainingResourcesOnHostVm();
        }
        if(maxPfValueHost==0 && mark==false){
            System.out.println("All Hosts are full.Instantiate a new Host");
            newMaxFitHost++;
            Host instaHost;
            instaHost=createPm(pe,ram);
            if(instaHost==null){
                System.out.println("Expand your DataCenter.Cannot satisfy the container request for pe :"+ pe +"and ram:" + ram);
                return;
            }
            Vm instaVm=createVm(pe,ram);
            Container  instaCont=createCont(pe,ram,bw,disk);
            placeVmOnPm(instaHost,instaVm);
            placeContOnVm(instaVm,instaCont);
            instaHost.updateHost(instaVm);
            instaVm.updateVm(instaCont);
            instaHost.NetResourcesUtiizedOnhost();
            instaHost.calResoursesWastedOnHost();
            findPeMaxOnEachVm();
            //instaHost.totalAllocatedResourcestoHostVm();
            //instaHost.remainingResourcesOnHostVm();
        }
        System.out.println(hostRepo);

    }
    void bestFit(int pe,float ram,long bw,float disk){
        ListIterator<Host> it1 = hostRepo.listIterator();
        Host h=null;
        Vm v=null;
        boolean mark=false;
        ArrayList<Vm> vlist = new ArrayList<Vm>();
        minVfValueVm=0;
        double temp1=1;
        double temp2=1;
        minPfValueHost=0;
        flag1 = Integer.MAX_VALUE;
        flag2 = Integer.MAX_VALUE;
        while (it1.hasNext()){
            h=it1.next();
            if(h.netPeWastedOnHost<pe && h.netRamWastedOnHost<ram) {
                h.finalPeWastedAfterRequestHost = h.netPeWastedOnHost;
                h.finalRamWastedAfterRequestHost = h.netRamWastedOnHost;
                h.pfValueHost = (w1 * (h.finalPeWastedAfterRequestHost / h.totalNumberOfPesHost)) + (w2 * (h.finalRamWastedAfterRequestHost / h.totalRamHost));
                vlist = h.vmList;
                ListIterator<Vm> it2 = vlist.listIterator();
                while (it2.hasNext()) {
                    v = it2.next();
                    v.finalPeWastedAfterRequestVm = v.remainingNumberOfPesVm;
                    v.finalRamWastedAfterRequestVm = v.remainingRamVm;
                    v.vfValueVm = (w1 * (v.finalPeWastedAfterRequestVm / v.totalNumberOfPesVm)) + (w2 * (v.finalRamWastedAfterRequestVm / v.totalRamVm)) + h.pfValueHost;
                }
            }

            if((h.netPeWastedOnHost>=pe && h.netRamWastedOnHost>=ram) && (h.remainingNumberOfPesHost>=pe && h.remainingRamHost>=ram) && (h.vmMaxPe<pe || h.vmMaxRam<ram)) {
                h.finalPeWastedAfterRequestHost = h.netPeWastedOnHost;
                h.finalRamWastedAfterRequestHost = h.netRamWastedOnHost;
                h.pfValueHost = (w1 * (h.finalPeWastedAfterRequestHost / h.totalNumberOfPesHost)) + (w2 * (h.finalRamWastedAfterRequestHost / h.totalRamHost));
                temp1= h.pfValueHost;
                vlist = h.vmList;
                ListIterator<Vm> it2 = vlist.listIterator();
                while (it2.hasNext()) {
                    v = it2.next();
                    v.finalPeWastedAfterRequestVm = v.remainingNumberOfPesVm;
                    v.finalRamWastedAfterRequestVm = v.remainingRamVm;
                    v.vfValueVm = (w1 * (v.finalPeWastedAfterRequestVm / v.totalNumberOfPesVm)) + (w2 * (v.finalRamWastedAfterRequestVm / v.totalRamVm)) + h.pfValueHost;
                }
            }

            if((h.netPeWastedOnHost>=pe && h.netRamWastedOnHost>=ram) && (h.remainingNumberOfPesHost>=pe && h.remainingRamHost>=ram) && (h.vmMaxPe>=pe && h.vmMaxRam>=ram)){
                h.finalPeWastedAfterRequestHost=h.netPeWastedOnHost-pe;
                h.finalRamWastedAfterRequestHost=h.netRamWastedOnHost-ram;
                h.pfValueHost=(w1*(h.finalPeWastedAfterRequestHost/h.totalNumberOfPesHost)) + (w2*(h.finalRamWastedAfterRequestHost/h.totalRamHost));
                temp1= h.pfValueHost;
                mark= true;
                vlist=h.vmList;
                ListIterator<Vm> it2 = vlist.listIterator();
                while (it2.hasNext()){
                    v=it2.next();
                    if(v.remainingNumberOfPesVm>=pe && v.remainingRamVm>=ram ){
                        v.finalPeWastedAfterRequestVm=v.remainingNumberOfPesVm-pe;
                        v.finalRamWastedAfterRequestVm=v.remainingRamVm-ram;
                        v.vfValueVm=(w1*(v.finalPeWastedAfterRequestVm/v.totalNumberOfPesVm)) + (w2*(v.finalRamWastedAfterRequestVm/v.totalRamVm)) + h.pfValueHost ;
                        temp2=v.vfValueVm;
                    }
                    else{
                        //v.vfValueVm=0;
                       // temp2=0;
                    }
                    if(temp2 < flag1){
                        minVfValueVm=temp2;
                        flag1=minVfValueVm;
                        selectedVmuId=v.uId;
                        hostIdOfSelectedVm=h.uId;
                    }
                }
            }
            else{
                // h.pfValueHost=0;
               // temp1=0;
            }
            if(temp1 < flag2){
                minPfValueHost= temp1;
                flag2=minPfValueHost;
                selectedHostuId=h.uId;
            }

        }
        ListIterator<Vm> it3 = vmRepo.listIterator();
        ListIterator<Host> it4 = hostRepo.listIterator();
        ListIterator<Host> it5 = hostRepo.listIterator();
        Vm getVm= null;
        Host getHost=null;
        Host getHostOfVm=null;
        Vm found;
        while(it3.hasNext()){
            getVm= it3.next();
            if(getVm.uId==selectedVmuId){
                break;
            }
        }
        while(it4.hasNext()){
            getHost= it4.next();
            if(getHost.uId==selectedHostuId){
                break;
            }
        }
        while(it5.hasNext()){
            getHostOfVm= it5.next();
            if(getHostOfVm.uId==hostIdOfSelectedVm){
                break;
            }
        }
        if(minVfValueVm!=0 && minVfValueVm!=1  ){
            Container instaCont;
            instaCont=createCont(pe,ram,bw,disk);
            placeContOnVm(getVm,instaCont);
            getVm.updateVm(instaCont);
            // getHost.updateHost(getVm);
            getHostOfVm.NetResourcesUtiizedOnhost();
            getHostOfVm.calResoursesWastedOnHost();
            findPeMaxOnEachVm();

            //getHostOfVm.totalAllocatedResourcestoHostVm();
            //getHostOfVm.remainingResourcesOnHostVm();

        }
        if(minVfValueVm==0){
            System.out.println("All VMs are full.Instantiate a new VM");
            newBestFitVm++;
            Container instaCont;
            instaCont=createCont(pe,ram,bw,disk);
            Vm instaVm=createVm(pe,ram);
            placeVmOnPm(getHost,instaVm);
            placeContOnVm(instaVm,instaCont);
            getHost.updateHost(instaVm);
            instaVm.updateVm(instaCont);
            getHost.NetResourcesUtiizedOnhost();
            getHost.calResoursesWastedOnHost();
            // getHost.totalAllocatedResourcestoHostVm();
            //getHost.remainingResourcesOnHostVm();
        }
        if(minPfValueHost==0 ){
            System.out.println("All Hosts are full.Instantiate a new Host");
            newBestFitHost++;
            Host instaHost;
            instaHost=createPm(pe,ram);
            if(instaHost==null){
                System.out.println("Expand your DataCenter.Cannot satisfy the container request for pe :"+ pe +"and ram:" + ram);
                return;
            }
            Vm instaVm=createVm(pe,ram);
            Container  instaCont=createCont(pe,ram,bw,disk);
            placeVmOnPm(instaHost,instaVm);
            placeContOnVm(instaVm,instaCont);
            instaHost.updateHost(instaVm);
            instaVm.updateVm(instaCont);
            instaHost.NetResourcesUtiizedOnhost();
            instaHost.calResoursesWastedOnHost();
            findPeMaxOnEachVm();
            //instaHost.totalAllocatedResourcestoHostVm();
            //instaHost.remainingResourcesOnHostVm();
        }
        System.out.println(hostRepo);

    }*/
    void findPeMaxOnEachVm(){
        flag1 = Integer.MIN_VALUE;
        flag2 = Integer.MIN_VALUE;
        ListIterator<Host> it1 = hostRepo.listIterator();
        Host h;
        Vm v;
        ArrayList<Vm> vlist= new ArrayList<Vm>();
        while(it1.hasNext()){
            h=it1.next();
            vlist=h.vmList;
            ListIterator<Vm> it2 = vlist.listIterator();
            while(it2.hasNext()){
                v=it2.next();
                if(v.remainingNumberOfPesVm>flag1){
                    h.vmMaxPe=v.remainingNumberOfPesVm;
                    flag1=h.vmMaxPe;
                }
                if(v.remainingRamVm>flag2){
                    h.vmMaxRam=v.remainingRamVm;
                    flag2=h.vmMaxRam;
                }
            }
        }
    }
    void getDC(DataCenter dc){
       obj=dc;
    }
    DataCenter getDCObject(){
        return obj;
    }
    void updateDC(Host h){
        obj.totalnumberOfPes=obj.totalnumberOfPes-h.totalNumberOfPesHost;
        obj.totalram=obj.totalram-h.totalRamHost;
        obj.totalbw=obj.totalbw-h.totalBwHost;
        obj.totaldisk=obj.totaldisk-h.totalDiskHost;
    }
    Boolean dcCheck(int numberofPEs, float ram){
        if(obj.totalnumberOfPes>=numberofPEs && obj.totalram>=ram){
            return true;
        }
        return false;
    }

    void peEffeciency(){
        ListIterator<Host> it1=hostRepo.listIterator();
        Host h=null;
        double remPes=0;
        double totalPes=0;
        double remRam=0;
        double totalRam=0;
        double usedPes=0;
        double usedRam=0;
        double utilPe=0;
        double utilRam=0;
        Container c= null;
        Vm v=null;
        ArrayList<Vm> vlist= null;
        ArrayList<Container> clist= null;
        while(it1.hasNext()){
            h=it1.next();
            totalPes=totalPes+h.totalNumberOfPesHost;
            totalRam=totalRam+h.totalRamHost;
            vlist=h.vmList;
            ListIterator<Vm> it2 = vlist.listIterator();
            while(it2.hasNext()) {
                v = it2.next();
                /*remPes = remPes + v.remainingNumberOfPesVm;
                remRam = remRam + v.remainingRamVm;*/
                clist=v.containerList;
                ListIterator<Container> it3= clist.listIterator();
                while(it3.hasNext()){
                    c= it3.next();
                    usedPes=usedPes + c.numberOfPes;
                    usedRam=usedRam + c.ram;

                }
            }
        }
        /*usedPes=totalPes-remPes;
        usedRam=totalRam-remRam;*/
       /* System.out.println(usedPes);
        System.out.println(totalPes);
        System.out.println(usedRam);
        System.out.println(totalRam);*/
        utilPe=(usedPes/totalPes);
        utilRam=(usedRam/totalRam);
        System.out.println(utilPe*100+"%");
        System.out.println(utilRam*100+"%");

       // e=usedPes/totalPes;

    }

}

