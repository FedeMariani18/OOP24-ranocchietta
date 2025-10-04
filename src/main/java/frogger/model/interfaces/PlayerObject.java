package frogger.model.interfaces;

import frogger.common.Direction;

/**
 * Represents the player object in the game, providing methods to manage
 * its state, direction, score, lives, and interactions.
 */
public interface PlayerObject extends GameObject {

    /**
     * Returns the number of lives the player has.
     *
     * @return the number of lives
     */
    int getLives();

    /**
     * Handles the event when the player is hit (e.g., loses a life).
     */
    void playerHit();

    /**
     * Returns the current direction the player is facing.
     *
     * @return the current direction
     */
    Direction getDirection();

    /**
     * Sets the player's direction to right.
     */
    void setLookingRight();

    /**
     * Sets the player's direction to left.
     */
    void setLookingLeft();

    /**
     * Sets the player's direction to down.
     */
    void setLookingDown();

    /**
     * Sets the player's direction to up.
     */
    void setLookingUp();

    /**
     * Adds the specified number of points to the player's score.
     *
     * @param points the number of points to add
     */
    void addPoints(int points);

    /**
     * Returns the current score of the player.
     *
     * @return the player's score
     */
    int getScore();

    /**
     * Resets the player's position to the starting point.
     */
    void resetPosition();

    /**
     * Returns whether the player is attached to another object (e.g., a log).
     *
     * @return true if attached, false otherwise
     */
    boolean isAttached();

    /**
     * Sets whether the player is attached to another object.
     *
     * @param b true if attached, false otherwise
     */
    void setAttached(boolean b);

    /**
     * Returns whether the player is dead.
     *
     * @return true if dead, false otherwise
     */
    boolean isDead();

    /**
     * Respawns the player, resetting its state after death.
     */
    void respawn();

    /**
     * Adds one life to the player.
     */
    void addLife();

    /**
     * Sets the score multiplier for the player.
     *
     * @param sMultiplier the score multiplier to set
     */
    void setScoreMultiplier(int sMultiplier);

    /**
     * Makes the player jump, if applicable.
     */
    void setJump(boolean state);

    /**
     * Returns whether the player is currently jumping.
     */
    boolean isJumping();

    void move();
}
