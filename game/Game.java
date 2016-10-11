import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Game 
{
    private Parser parser;
    private Room [] arrayRoom;
    private Player player;
    private Professor professor;
    private Random rnd;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        rnd = new Random();
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms, and items and link their exits and objects added to each room together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office, labyrinth, teleport;
        Item rock, wood, cookie, amuletOfWisdom;

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        labyrinth = new Room("in the University Labyrinth");
        teleport = new Room("in a room with blank walls and no obvious exit");

        //create array of all the rooms
        arrayRoom = new Room [] {outside, theater, pub, lab, office, labyrinth, teleport};

        // create the items
        rock = new Item("rock", "a blue ore", 55.0);
        wood = new Item("wood", "a long plank", 4.6);
        cookie = new Item("cookie", "a magic cookie", 2);
        amuletOfWisdom = new Item("amuletOfWisdom", "a portable item which you need to win the game", 2);

        //add objects to room
        outside.addItem(rock);
        outside.addItem(wood);
        outside.addItem(cookie);
        int z = rnd.nextInt(arrayRoom.length - 1);
        arrayRoom[z].addItem(amuletOfWisdom); 
        // I put in a -1 by the length of the Array,since I do not want to put the 
        // amulet of wisdom inside the teleport room, since there are no professors there. 

        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", labyrinth);

        theater.setExit("west", outside);
        theater.setExit("south",teleport);

        pub.setExit("east", outside);
        pub.setExit("west", labyrinth);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        labyrinth.setExit("east", outside);
        labyrinth.setExit("south",pub);

        player = new Player(outside);  // start the player outside
        professor = new Professor(outside);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.look());
    }

    /**
     *If in teleport room, either teleport to different room or reprint the description of the room, and in every other room reprint the description of the room.
     */
    private void look(){
        if(getRoom() == arrayRoom[6]){
            setRandomRoom();
        }
        System.out.println(player.look());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
        
        String cmnd = command.toString();

        try {
            FileWriter writer = new FileWriter("zuul.log", true);
            writer.write(cmnd + "\n");
            writer.close();
        }
        catch(IOException e) {

        }

        CommandWord commandWord = command.getCommandWord();
        switch (commandWord) {
            case UNKNOWN:
            System.out.println("I don't know what you mean...");
            break;

            case HELP:
            printHelp();
            break;

            case LOOK:
            look();
            break;

            case GO:
            goRoom(command);
            break;

            case TAKE:
            takeItem(command);
            break;

            case DROP:
            dropItem(command);
            break;

            case INVENTORY:
            inventory(command);
            break;

            case EAT:
            eatMagicCookie(command);
            break;

            case QUIT:
            wantToQuit = quit(command);
            break;
        }
        professor.act();

        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
        else{
            String direction = command.getSecondWord();
            System.out.println(player.goRoom(direction));
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Return the current room
     * @return currentRoom
     */
    public Room getRoom()
    {
        return player.getRoom();
    }

    /**
     * Sets a random Room as the current room.
     * @param new random room
     */
    public void setRandomRoom()
    {
        int x = rnd.nextInt(4);
        if(x == 2){
            int y = rnd.nextInt(6);
            Room NR = arrayRoom[y];
            player.setRoom(NR);
        }
    }

    /** 
     * Try to take an item from current room to put in the bag. If there is an item from HashSet items, enter the new
     * item, otherwise print an error message.
     */
    private void takeItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("take what?");
            return;
        }
        String item = command.getSecondWord();
        System.out.println(player.takeItem(item));
    }

    /** 
     * Try to take an item from current the bag to put in the current room. If there is an item from HashSet bag, enter the new
     * item, otherwise print an error message.
     */
    private void dropItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("drop what?");
            return;
        }
        String item = command.getSecondWord();
        System.out.println(player.dropItem(item));
    }

    /** 
     * Print all the items in one's bag. If there are no items, print that you are carrying nothing.
     */
    private void inventory(Command command) 
    {
        if(command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("huh?");
            return;
        }
        System.out.println(player.getInventory());
    }

    /** 
     * Print the result of a cookie being or not being eaten. If there are no items, print that you are carrying nothing.
     * @param Coomand, cookie we are trying to eat
     */
    private void eatMagicCookie(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("eat what?");
            return;
        }
        String item = command.getSecondWord();
        System.out.println(player.eatMagicCookie(item));
    }
}
