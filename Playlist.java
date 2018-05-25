import java.util.*;

/**
 * The Playlist class takes a list of N songs. When the play playlist method is called,
 * the playlist plays all the songs in a random order.
 */
public class Playlist{

    // List of songs on the playlist.
    private static ArrayList<String> songs;

    /**
     * The main method takes all the arguments and adds them as songs to the playlist.
     * The songs are played each once in a random order by the play playlist method.
     * @param args
     */
    public static void main(String [] args)
    {
        songs = new ArrayList<String>();
        ArrayList<String> newArrayList = new ArrayList<String>(Arrays.asList(args));
        songs.addAll(newArrayList);
        playPlaylist();
    }

    /**
     * Constructor
     */
    public Playlist(){
        songs = new ArrayList<String>();
    }

    /**
     * Adds a song to the current list of songs.
     * @param song
     */
    public static void addSong(String song){
        songs.add(song);
    }

    /**
     * Adds a list of songs current list of songs.
     * @param songs
     */
    public static void addSongs(ArrayList<String> songs){
        songs.addAll(songs);
    }

    /**
     * Removes a song from the current list of songs.
     * @param song
     */
    public static void removeSong(String song){
        songs.remove(song);
    }

    /**
     * Clears all the songs from the playlist.
     */
    public static void clearPlaylist(){
        songs.clear();
    }

    /**
     * Plays all the songs on the playlist and removes the song after they are played.
     * A temporary playlist is created so each song will only play once in a random order.
     */
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
