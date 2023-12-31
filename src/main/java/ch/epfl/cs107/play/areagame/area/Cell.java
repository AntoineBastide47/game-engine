package ch.epfl.cs107.play.areagame.area;

import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.engine.actor.Draggable;
import ch.epfl.cs107.play.engine.actor.Droppable;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Each AreaGame will have its own Cell extension.
 * At minimum a cell is linked to its content
 */
public abstract class Cell implements Interactable {

    /// Content of the cell as a set of Interactable
    protected Set<Interactable> entities;
    protected DiscreteCoordinates coordinates;


    /**
     * Default Cell constructor
     * @param x (int): x-coordinate of this cell
     * @param y (int): y-coordinate of this cell
     */
    public Cell(int x, int y){
        entities = new HashSet<>();
        coordinates = new DiscreteCoordinates(x, y);
    }

    /**
     * Do the given draggableAreaEntity interacts with all Droppable sharing the same cell
     * @param interactor (Interactor), not null
     */
    void dropInteractionOf(Draggable draggable) {
        for(Interactable interactable : entities){
            if(interactable instanceof Droppable) {
                Droppable droppable = (Droppable)interactable;
                if(droppable.canDrop())
                    droppable.receiveDropFrom(draggable);
            }
        }
        if(this instanceof Droppable) {
            Droppable droppable = (Droppable)this;
            if(droppable.canDrop())
                droppable.receiveDropFrom(draggable);
        }

    }

    /**
     * Do the given interactor interacts with all Interactable sharing the same cell
     * @param interactor (Interactor), not null
     */
    void cellInteractionOf(Interactor interactor){
        interactor.interactWith(this, true);
        for(Interactable interactable : entities){
            if(interactable.isCellInteractable())
                interactor.interactWith(interactable, true);
        }
    }

    /**
     * Do the given interactor interacts with all Interactable sharing the same cell
     * @param interactor (Interactor), not null
     */
    void viewInteractionOf(Interactor interactor){
        interactor.interactWith(this, false);
        for(Interactable interactable : entities){
            if(interactable.isViewInteractable())
                interactor.interactWith(interactable, false);
        }
    }

    /**
     * Do the given interactable enter into this Cell
     * @param entity (Interactable), not null
     */
    protected void enter(Interactable entity) {
        entities.add(entity);
    }

    /**
     * Do the given Interactable leave this Cell
     * @param entity (Interactable), not null
     */
    protected void leave(Interactable entity) {
        entities.remove(entity);
    }

    /**
     * Indicate if the given Interactable can leave this Cell
     * @param entity (Interactable), not null
     * @return (boolean): true if entity can leave
     */
    protected abstract boolean canLeave(Interactable entity);

    /**
     * Indicate if the given Interactable can enter this Cell
     * @param entity (Interactable), not null
     * @return (boolean): true if entity can enter
     */
    protected abstract boolean canEnter(Interactable entity);

    /// Cell implements Interactable

    @Override
    public boolean takeCellSpace(){
        return false;
    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {}

    @Override
    public void onEntering(List<DiscreteCoordinates> coordinates) {}

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(coordinates);
    }

}
