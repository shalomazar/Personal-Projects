

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class zuultest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class zuultest
{
    private Room room1;
    private Item item1;

    /**
     * Default constructor for test class zuultest
     */
    public zuultest()
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
        room1 = new Room("green room");
        item1 = new Item("gold", "yellow bar", 2.3);
        room1.addItem(item1);
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
    public void lookTest()
    {
        CommandWords commandW1 = new CommandWords();
        assertEquals(true, commandW1.isCommand("look"));
    }

    @Test
    public void itemTest()
    {
        Item item1 = new Item("rock", "a blue ore", 5.3);
        Item item2 = new Item("wood", "plank of wood", 2.3);
        assertEquals("a blue ore", item1.getDescription());
        assertEquals("wood", item2.getName());
    }
}

