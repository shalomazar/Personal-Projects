//package com.company;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DirectoryObj {
    private List<DirEntry> dEntryLst;
    private HashMap<Integer,String> dirAttrMap;
    private int theN;

    /**
     * Constructor to create Directory Object for non-root
     * @param fat32
     * @param f32
     * @param dir
     */
    public DirectoryObj(String fat32,fat32Reader f32,int dir,int n) {
        dEntryLst = new ArrayList<DirEntry>();
        //map attributes number values to their string names
        dirAttrMap = new HashMap<Integer, String>();
        dirAttrMap.put(1,"ATTR_READ_ONLY");
        dirAttrMap.put(2,"ATTR_HIDDEN");
        dirAttrMap.put(4,"ATTR_SYSTEM");
        dirAttrMap.put(8,"ATTR_VOLUME_ID");
        dirAttrMap.put(16,"ATTR_DIRECTORY");
        dirAttrMap.put(32,"ATTR_ARCHIVE");
        try {
            theN = n;
            getDirInfo(fat32,f32,dir,n);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Loop through fat32 image of the root directory and create DirEntry objects and add them to a list
     * @param fat32
     * @param f32
     * @param currentDir
     * @throws IOException
     */
    public void getClusInfo(String fat32,fat32Reader f32,int currentDir)throws IOException {
        int offNum = 0;
        int varNum = 0;
        boolean done = false;
        int getDot = f32.getBytesData(fat32,currentDir + varNum + offNum,1);
        if(theN != 2 && getDot != 46){
            varNum += 32;
        }
        while(!done) {
            int dirNameNumStart = f32.getBytesData(fat32,currentDir + varNum + offNum,1);
            getDot = f32.getBytesData(fat32,currentDir + varNum + offNum,1);
            int getDotDot = f32.getBytesData(fat32,currentDir + varNum + offNum,2);
            int offSetShortName = currentDir + varNum + offNum;
            String DIR_Name = f32.getBytesChar(fat32, currentDir + varNum + offNum, 8);
            offNum += 8;
            String DIR_Name_ext = f32.getBytesChar(fat32, currentDir + varNum + offNum, 3);
            offNum += 3;
            int DIR_Attr = f32.getBytesData(fat32, currentDir + varNum + offNum, 1);
            offNum += 9;
            int DIR_FstClusHI = f32.getBytesData(fat32, currentDir + varNum + offNum, 2);
            offNum += 6;
            int DIR_FstClusLO = f32.getBytesData(fat32, currentDir + varNum + offNum, 2);
            offNum += 2;
            int DIR_fileSize = f32.getBytesData(fat32,currentDir + varNum + offNum,4);

            //If there are 32 bytes of 0s then that is the end of the directory entries
            if (DIR_Name.equals("") || varNum >= f32.getBytesPerClus()) {
                int endOfNames = f32.getBytesData(fat32, currentDir + varNum + offNum, 32);
                if (endOfNames == 0 || varNum >= f32.getBytesPerClus()) {
                    done = true;
                }
            }
            else {
                String DirNameFull = "";
                //If it is 8 then it is a volume ID, so don't add it to lists
                if (DIR_Attr != 0) {
                    //if it is a directory then it has no extension
                    if (DIR_Attr == 16 || DIR_Attr == 8) {
                        DirNameFull = DIR_Name;
                    } else {
                        DirNameFull = DIR_Name + "." + DIR_Name_ext;
                    }
                    //add info to lists
                    //parse the short name directory before it is added to the list.
                    DirNameFull = DirNameFull.toLowerCase().replaceAll(" ", "");
                    String dirEntryStr = dirAttrMap.get(DIR_Attr);
                    int nextClusNum = getNextClusNum(DIR_FstClusHI,DIR_FstClusLO);
                    int fileLoc = getFileLocation(f32,nextClusNum);
                    DirEntry dEntry = new DirEntry(DirNameFull, DIR_FstClusHI, DIR_FstClusLO, DIR_Attr, dirEntryStr, DIR_fileSize, nextClusNum, fileLoc, offSetShortName);
                    dEntryLst.add(dEntry);

                }
                offNum = 0;//reset offset number
                if(getDot == 46 && getDotDot != 11822){
                    varNum += 32;
                }else {
                    varNum += 64;//update varNum to move onto the next short name dir
                }
            }
        }
    }

    /**
     * Writes new file to directory
     * @param f32
     * @param fat32
     * @param currentDir
     * @param short_name
     * @param first_free_cluster
     * @param size
     * @throws IOException
     */
    public void writeToDirectory(fat32Reader f32, String fat32,DirectoryObj dirObj, int currentDir, String short_name, int first_free_cluster, int size) throws IOException {
        int varNum = 0;
        int currentClus = 0;
        int fir_free_clus = 0;
        boolean newClus = false;
        List<Integer> clusters = new ArrayList<Integer>();
        DirEntry dMonster = dEntryLst.get(0);
        if(dMonster.getDirAttr() != 8){
            clusters = getClusters(fat32,f32,dMonster.getNextClusNum());
        }else{
            clusters = getClusters(fat32,f32,f32.getRootClus());
        }
        boolean done = false;
        String[] full_dir_name = short_name.split("\\.");
        short_name = full_dir_name[0];
        String ext = full_dir_name[1];
        for(int i = short_name.length(); i < 8; i++) {
            short_name += " ";
        }
        for(int k = ext.length(); k < 3; k++) {
            ext += " ";
        }
        byte[] name = short_name.getBytes(StandardCharsets.US_ASCII);
        byte[] Bext = ext.getBytes(StandardCharsets.US_ASCII);
        int is_empty = f32.getBytesData(fat32,currentDir + varNum,1);
        if(theN != 2 && is_empty != 46){
            varNum += 32;
        }
        while(!done) {
            if (varNum >= f32.getBytesPerClus()) {
                currentClus++;
                try {
                    currentDir = getFileLocation(f32,clusters.get(currentClus));

                }catch (Exception e){
                    //do nothing
                }
                varNum = 64;
                /*
                * If the reached the end of the current cluster in the directory
                * move to the next cluster in the directory
                */
                    if(currentClus >= clusters.size()){
                        varNum = 0;
                        fir_free_clus = f32.getFreeList(fat32,dirObj,f32).get(0);
                        clusters.add(fir_free_clus);

                        int free_clus = f32.getFatTable() + (4 * clusters.get(currentClus - 1));
                        // If the cluster is the root directory we change the cluster to the root cluster
                        if(free_clus == f32.getFatTable()){
                            free_clus = f32.getFatTable() + (4 * f32.getRootClus());
                        }
                        //Update fat table
                        ByteBuffer fc = ByteBuffer.allocate(4);
                        fc.order(ByteOrder.LITTLE_ENDIAN);
                        fc.asIntBuffer().put(fir_free_clus);
                        byte[] C = fc.array();
                        f32.getOut().put(free_clus, C[0]);
                        f32.getOut().put(free_clus + 1, C[1]);
                        f32.getOut().put(free_clus + 2, C[2]);
                        // The cluster number will never get bigger than 256^3
                        // Therefore we hard coded a 0. We hard coded a 0 because our array would only span up
                        // to index 2.
                        f32.getOut().put(free_clus + 3, (byte) 0x00);
                        //Add EOC to the fat of the next cluster.
                        byte[] eoc = new byte[4];
                        eoc[0] = (byte) 0x0F;
                        eoc[1] = (byte) 0xFF;
                        eoc[2] = (byte) 0xFF;
                        eoc[3] = (byte) 0xF8;
                        int pos = f32.getFatTable() + ((fir_free_clus) * 4);
                        for (int i = 0; i < eoc.length; i++) {
                            f32.getOut().put(pos + i, eoc[i]);
                        }
                        newClus = true;
                        done = true;
                    }
                    currentDir = getFileLocation(f32,clusters.get(currentClus));
                    if(!newClus && clusters.get(0) == f32.getRootClus()) {
                        varNum = 64;
                    }else if(!newClus){
                        varNum = 32;
                    }
                }



            is_empty = f32.getBytesData(fat32, currentDir + varNum, 1);
            if (is_empty == 229 || is_empty == 0) {
                //Write
                for (int i = 0; i < name.length; i++) {
                    f32.getOut().put(currentDir + varNum+ i, name[i]);
                }
                for (int j = 0; j < Bext.length; j++) {
                    f32.getOut().put(currentDir + varNum + 8 + j, Bext[j]);
                }
                // Writing attribute value to file
                f32.getOut().put(currentDir + varNum + 11, (byte) 0x20);
                String hex = Integer.toHexString(first_free_cluster);
                int leftover = 8 - hex.length();
                for (int i = 0; i < leftover; i++) {
                    hex = "0" + hex;
                }
                int hi = Integer.parseInt(hex.substring(0, 4), 16);
                int lo = Integer.parseInt(hex.substring(4, 8), 16);
                ByteBuffer hiB = ByteBuffer.allocate(4);
                ByteBuffer loB = ByteBuffer.allocate(4);
                hiB.order(ByteOrder.LITTLE_ENDIAN);
                loB.order(ByteOrder.LITTLE_ENDIAN);
                hiB.asIntBuffer().put(hi);
                loB.asIntBuffer().put(lo);
                byte[] ahi = hiB.array();
                byte[] alo = loB.array();
                f32.getOut().put(currentDir + varNum + 20, ahi[0]);
                f32.getOut().put(currentDir + varNum + 21, ahi[1]);
                f32.getOut().put(currentDir + varNum + 26, alo[0]);
                f32.getOut().put(currentDir + varNum + 27, alo[1]);
                ByteBuffer sizeB = ByteBuffer.allocate(8);
                sizeB.order(ByteOrder.LITTLE_ENDIAN);
                sizeB.asIntBuffer().put(size);
                byte[] asize = sizeB.array();
                f32.getOut().put(currentDir + varNum + 28, asize[0]);
                f32.getOut().put(currentDir + varNum + 29, asize[1]);
                f32.getOut().put(currentDir + varNum + 30, asize[2]);
                f32.getOut().put(currentDir + varNum + 31, asize[3]);
                String time = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
                int hour = Integer.parseInt(time.substring(0,2));
                int minute = Integer.parseInt(time.substring(2,4));
                int seconds = Integer.parseInt(time.substring(4,6))/2;
                String date = java.time.LocalDateTime.now().toString();
                int year = Integer.parseInt(date.substring(0,4));
                int month = Integer.parseInt(date.substring(5,7));
                int day = Integer.parseInt(date.substring(8,10));
                if(hour >= 20){
                    day++;
                }
                hour = (hour + 4) % 24;
                int x = (hour * 2048) + (minute * 32) + (int) Math.floor(seconds / 2);
                byte h = (byte) (x % 256);
                byte g = (byte) Math.floor(x / 256);
                int y = ((year - 1980 ) * 512) + (month * 32) + day;
                byte i = (byte) (y % 256);
                byte u = (byte) Math.floor(y / 256);
                f32.getOut().put(currentDir + varNum + 13, (byte) 0);
                f32.getOut().put(currentDir + varNum + 14, (byte) 0);
                f32.getOut().put(currentDir + varNum + 15, (byte) 0);
                f32.getOut().put(currentDir + varNum + 16, (byte) 0);
                f32.getOut().put(currentDir + varNum + 17, (byte) 0);
                f32.getOut().put(currentDir + varNum + 18, (byte) 0);
                f32.getOut().put(currentDir + varNum + 19, (byte) 0);
                f32.getOut().put(currentDir + varNum + 22, h);
                f32.getOut().put(currentDir + varNum + 23, g);
                f32.getOut().put(currentDir + varNum + 24, i);
                f32.getOut().put(currentDir + varNum + 25, u);
                done = true;
            }
            else {
                int getDotDot = f32.getBytesData(fat32, currentDir + varNum, 2);
                if (is_empty == 46 && getDotDot != 11822) {
                    varNum += 32;
                } else {
                    varNum += 64;//update varNum to move onto the next short name dir
                }
            }
        }
    }

    /**
     * Gets all directory info across clusters
     * @param fat32
     * @param f32
     * @param currentDir
     * @param n
     * @throws IOException
     */
    public void getDirInfo(String fat32,fat32Reader f32,int currentDir,int n) throws IOException {
        //int locationLocation = getFileLocation(f32,N);
        List<Integer> clusters = getClusters(fat32,f32,n);
        int numOfCluses = clusters.size();
        int currentClus = 0;
        for(int i = 0; i < numOfCluses;i++){
            //update currentClus
            currentClus = getFileLocation(f32, clusters.get(i));
            getClusInfo(fat32,f32,currentClus);
        }
    }

    /**
     * Gets the string from the start byte to the end byte
     * @param fat32
     * @param f32
     * @param n
     * @param start
     * @param end
     * @return read
     * @throws IOException
     */
    public String getReadInfo(String fat32,fat32Reader f32,int n, int start, int end, DirEntry dirEntry) throws IOException {
        List<Integer> clusters = getClusters(fat32,f32,n);
        int numOfCluses = clusters.size();
        int x = f32.getBytesPerClus();
        int loc = 0;
        int o = start % x;
        int z = end % x;
        int s = (int) Math.floor(start/x);
        int e = (int) Math.floor(end /x) + 1;
        String read = "";
        if(loc + end > dirEntry.getFileSize() || e > numOfCluses){
            System.out.println("Error: attempt to read beyond end of file");
        }
        else {
            for (int i = s; i < e; i++) {
                //update currentClus
                if(i != e - 1) {
                    loc = getFileLocation(f32, clusters.get(i));
                    String read1 = f32.getBytesChar(fat32, loc + o, x);
                    read = read + read1;
                }
                else {
                    loc = getFileLocation(f32, clusters.get(i));
                    read += f32.getBytesChar(fat32, loc + o, z);
                }
            }
        }
        return read;
    }

    /**
     * Gets the list of clusters
     * @param fat32img
     * @param f32
     * @param N
     * @return clusterSpan
     * @throws IOException
     */
    public List<Integer> getClusters(String fat32img,fat32Reader f32,int N) throws IOException {
        List<Integer> clustersSpan = new ArrayList<Integer>();
        int BPB_NumFATs = f32.getBPB_NumFATs();
        int BPB_ResvdSecCnt = f32.getBPB_ResvdSecCnt();
        int RootDirSectors = f32.getRootDirSectors();
        int Fatsz = f32.getFATsz();
        int BPB_BytsPerSec = f32.getBPB_BytsPerSec();
        int eoc = 268435448;
        while(N < eoc) {
            //add to clusters list
            clustersSpan.add(N);
            int FATOffset = N * 4;
            int FirstDataSector =  BPB_ResvdSecCnt + (BPB_NumFATs * Fatsz) + RootDirSectors;
            int ThisFATSecNum = BPB_ResvdSecCnt + (FATOffset / BPB_BytsPerSec);
            int thisFATEntOffset = (FATOffset % BPB_BytsPerSec);
            int fatTable = ThisFATSecNum * f32.getBytesPerClus();
            //update n
            N = f32.getBytesData(fat32img, fatTable + thisFATEntOffset, 4);

        }
        return clustersSpan;

    }

    /**
     * Gets fat table
     * @param f32
     * @return fat_table
     */
    public int getFatTable(fat32Reader f32){
        int BPB_ResvdSecCnt = f32.getBPB_ResvdSecCnt();
        int BPB_BytsPerSec = f32.getBPB_BytsPerSec();
        int FATOffset = 0;
        int ThisFATSecNum = BPB_ResvdSecCnt + (FATOffset / BPB_BytsPerSec);
        return ThisFATSecNum * f32.getBytesPerClus();
    }

    /**
     * Get entry obj by name of directory entry
     * @param dirName
     * @return
     */
    public DirEntry getDirEntryByName(String dirName){
        for(DirEntry dE : dEntryLst){
            if (dE.getDirName().equalsIgnoreCase(dirName)){
                return dE;
            }
        }
        return null;
    }

    /**
     * Gets the dEntry list
     * @return
     */
    public List<DirEntry> getdEntryLst() {
        return dEntryLst;
    }

    /**
     * Get directory entry by number in the list
     * @param dirNum
     * @return
     */
    public DirEntry getDirEntry(int dirNum){
        return dEntryLst.get(dirNum);
    }

    /**
     * Gets the next cluster number value.
     * @param hiClus hi cluster
     * @param loClus lo cluster
     * @return
     */
    private int getNextClusNum(int hiClus,int loClus){
        String hiClusHex = Integer.toHexString(hiClus);
        String loClusHex = Integer.toHexString(loClus);
        String nextClusNumStr = "0x" + hiClusHex + loClusHex;
        int nextClusNum = Integer.parseInt(nextClusNumStr.split("0x")[1],16);
        return nextClusNum;
    }

    /**
     * Gets the location in bytes of the beginning of a cluster
     * @param f32
     * @param n
     * @return cluster location types bytes per second
     */
    public int getFileLocation(fat32Reader f32, int n){
        int BPB_BytsPerSec = f32.getBPB_BytsPerSec();
        int BPB_SecPerClus = f32.getBPB_SecPerClus();
        int FirstDataSector = f32.getFirstDataSector();
        int FATOffset = n * 4;
        int FirstSectorofCluster = ((n - 2) * BPB_SecPerClus) + FirstDataSector;
        return FirstSectorofCluster * f32.getBytesPerClus();
    }

}

