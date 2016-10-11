

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestPlayer.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TestPlayer
{
    private Room room1;
    private Room room2;
    private Item item1;
    private Item item2;
    private Player player1;
    private java.lang.String string1Room1;
    private java.lang.String string1Room2;

    
    
    
    
    

    /**
     * Default constructor for test class TestPlayer
     */
    public TestPlayer()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        room1 = new Room("in room 1");
        room2 = new Room("in room 2");
        room1.setExit("north", room2);
        room2.setExit("south", room1);
        item1 = new Item("gold", "sack of gold", 6.8);
        item2 = new Item("potatoes", "sack of potatoes", 15.6);
        room1.addItem(item1);
        room2.addItem(item2);
        player1 = new Player(room1);
        string1Room1 = room1.getLongDescription();
        string1Room2 = room2.getLongDescription();
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void playerInRoom1()
    {
        assertSame(room1, player1.getRoom());
    }

    @Test
    public void setRoomToRoom2()
    {
        player1.setRoom(room2);
        assertEquals(room2, player1.getRoom());
    }

    @Test
    public void lookTest1()
    {
        assertEquals(string1Room1, player1.look());
    }

    @Test
    public void lookTest2()
    {
        player1.setRoom(room2);
        assertEquals(string1Room2, player1.look());
    }

    @Test
    public void goRoomTest1()
    {
        assertEquals(string1Room2, player1.goRoom("north"));
        assertEquals(string1Room1, player1.goRoom("south"));
    }
}






