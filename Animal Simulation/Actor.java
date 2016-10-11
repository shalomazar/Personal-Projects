import java.util.List;
/**
 * Write a description of class Actor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Actor
{

    // instance variables - replace the example below with your own

    /**
     * Constructor for objects of class Actor
     */
    public Actor()
    {
        
    }

    abstract public void act(List<Actor> newActors);

    abstract public boolean isAlive();
}

