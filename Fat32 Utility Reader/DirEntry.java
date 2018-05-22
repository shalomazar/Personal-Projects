//package com.company;
import java.util.ArrayList;
import java.util.List;

public class DirEntry {

    private String dirName;
    private int hiClus;
    private int loClus;
    private int nextClusNum;
    private int dirAttr;
    private String dirAttrName;
    private int fileSize;
    private int location;
    private List<Integer> clusters;
    private String nextClusNumHex;
    private int offSetShortName;

    /**
     * Constructor
     * @param dirName short filename
     * @param hiClus hi cluster number
     * @param loClus lo cluster number
     * @param dirAttr directory attribute as a number
     * @param dirAttrName directory attribute name
     * @param fileSize file size
     */
    public DirEntry(String dirName, int hiClus, int loClus,int dirAttr, String dirAttrName, int fileSize,int nextClusNum, int location, int offSetShortName){
        this.dirName = dirName;
        this.hiClus = hiClus;
        this.loClus = loClus;
        this.dirAttr = dirAttr;
        this.fileSize = fileSize;
        this.dirAttrName = dirAttrName;
        this.location = location;
        clusters = new ArrayList<Integer>();
        this.nextClusNum = nextClusNum;
        nextClusNumHex = "0x" + Integer.toHexString(this.nextClusNum);
        this.offSetShortName = offSetShortName;
    }

    /**
     * Get directory name
     * @return
     */
    public String getDirAttrName() {
        return dirAttrName;
    }

    /**
     * Gets the file/directory attribute
     * @return directory attribute
     */
    public int getDirAttr() {
        return dirAttr;
    }

    /**
     * Gets the directory short name
     * @return directory name
     */
    public String getDirName() {
        return dirName;
    }

    /**
     * Gets the hi word cluster value
     * @return hi word
     */
    public int getHiClus() {
        return hiClus;
    }
    /**
     * Gets the lo word cluster value
     * @return lo word
     */
    public int getLoClus() {
        return loClus;
    }

    /**
     * Gets the file size
     * @return file size
     */
    public int getFileSize() {
        return fileSize;
    }

    /**
     * Adds a cluster to a list
     * @param val
     */
    public void addToClusterList(int val){
        clusters.add(val);
    }

    /**
     * Gets the cluster list
     * @return clusters
     */
    public List<Integer> getClusters() {
        return clusters;
    }

    /**
     * Gets the next cluster number in hex
     * @return next cluster number in hex
     */
    public String getNextClusNumHex() {
        return nextClusNumHex;
    }

    /**
     * Gets the next cluster number in decimal
     * @return next cluster number in decimal
     */
    public int getNextClusNum() {
        return nextClusNum;
    }

    /**
     * Gets the location of the current entry
     * @return location
     */
    public int getLocation() {
        return location;
    }

    /**
     * Gets the short name of the file in a directory
     * @return getOffSetShortName
     */
    public int getOffSetShortName() {
        return offSetShortName;
    }
}
