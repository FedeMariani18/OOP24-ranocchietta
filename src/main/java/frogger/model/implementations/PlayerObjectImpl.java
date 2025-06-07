package frogger.model.implementations;

import frogger.common.Constants;
import frogger.common.Direction;
import frogger.common.Pair;
import frogger.common.Position;
import frogger.model.interfaces.PlayerObject;

/**
 * Implementation of the PlayerObject interface representing the player character in the game.
 */
public class PlayerObjectImpl extends GameObjectImpl implements PlayerObject {

    /** The starting position of the player. */
    private final Position startPosition = new Position(0, Constants.MIN_Y);

    /** The current direction the player is facing. */
    private Direction direction;
    /** The number of lives the player has. */
    private int lives;
    /** The current score of the player. */
    private int score;
    /** Whether the player is attached to another object (e.g., a log). */
    private boolean attached;
    /** Whether the player is dead. */
    private boolean dead;
    /** The multiplier for the score. */
    private int scoreMoliplier = 1;

    /**
     * Constructs a new PlayerObjectImpl with the given dimension and skin.
     *
     * @param dimension the size of the player
     * @param skin the image representing the player's skin
     */
    public PlayerObjectImpl(final Pair dimension, final String skin) {
        super(new Position(0, Constants.MIN_Y), dimension);
        super.setImage(skin);
        this.lives = 3;
        this.score = 0;
        this.direction = Direction.UP;
        this.attached = false;
        this.dead = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLives() {
        return this.lives;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerHit() {
        this.lives--;
        this.dead = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLookingRight() {
        this.direction = Direction.RIGHT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLookingLeft() {
        this.direction = Direction.LEFT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLookingDown() {
        this.direction = Direction.DOWN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLookingUp() {
        this.direction = Direction.UP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPoints(final int points) {
        this.score += points * scoreMoliplier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScore() {
        return this.score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetPosition() {
        setLookingUp();
        setPos(startPosition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAttached() {
        return this.attached;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttached(final boolean b) {
        this.attached = b;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDead() {
        return this.dead;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void respawn() {
        this.dead = false;
        resetPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLife() {
        this.lives++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void SetScoreMultiplier(int sMultiplier) {
        this.scoreMoliplier = (sMultiplier > 0) ? sMultiplier : 1;
    }
}
