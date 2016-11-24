import java.util.*;

public class Playlist{

    private static ArrayList<String> songs; 
    
    public static void main(String [] args)
	{
		songs = new ArrayList<String>();
		ArrayList<String> newArrayList = new ArrayList<String>(Arrays.asList(args));
		songs.addAll(newArrayList);
		playPlaylist();
	}
    
    public Playlist(){ 
        songs = new ArrayList<String>();
    }
    
    public static void addSong(String song){
        songs.add(song);
    }
    
    public static void addSongs(ArrayList<String> songs){
        songs.addAll(songs);
    }
    
    public static void removeSong(String song){
        songs.remove(song);
    }
    
    public static void clearPlaylist(){
        songs.clear();
    }
    
    public static void playPlaylist(){
    	ArrayList<String> tp = new ArrayList<String>();
    	tp.addAll(songs);
    	Random rnd = new Random();
    	for(int i = tp.size(); i > 0; i--)
        {
            int x = rnd.nextInt(i);
    		System.out.println("Song played: " + tp.remove(x));
        }
    }
}
