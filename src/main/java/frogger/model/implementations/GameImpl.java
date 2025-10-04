package frogger.model.implementations;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import frogger.common.Constants;
import frogger.common.GameState;
import frogger.common.Pair;
import frogger.model.interfaces.Game;
import frogger.model.interfaces.Lane;
import frogger.model.interfaces.Level;
import frogger.model.interfaces.MovingObject;
import frogger.model.interfaces.PickableObject;
import frogger.model.interfaces.PlayerObject;

/**
 * Implementation of the {@link Game} interface.
 * Manages the main game logic, player state, level progression, collisions, and power-ups.
 */
public class GameImpl implements Game {

    /** Delay in milliseconds before the player respawns after death. */
    private static final int RESPAWN_DELAY = 1000; // 1 secondo

    /** Factory for generating random levels. */
    private final RandomLevelFactory levelFactory = new RandomLevelFactory();
    /** The player object. */
    private final PlayerObjectImpl player;
    /** The current level. */
    private Level level;
    /** Timestamp of the player's death, used for respawn timing. */
    private long deathTime;

    private final PickableObjectManagerImpl pickableObjectManager;

    /**
     * Constructs a new GameImpl instance with the specified player dimension and skin.
     *
     * @param dimension the size of the player
     * @param skin the image representing the player's skin
     */
    public GameImpl(final Pair dimension, final String skin) { 
        this.player = new PlayerObjectImpl(dimension, skin);
        level = levelFactory.createLevel();
        pickableObjectManager = new PickableObjectManagerImpl(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkGameOver() {
        if (this.player.getLives() == 0) {
            GameState.setState(GameState.DEAD);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScore() {
        return this.player.getScore();
    }

    /**
     * {@inheritDoc}
     * Handles collision detection between the player and obstacles, trunks, and eagles.
     * Also manages player respawn timing.
     */
    @Override
    public void checkCollision() {

        if (this.player.isDead()) {
            if (deathTime == 0) {
                deathTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - deathTime >= RESPAWN_DELAY) {
                this.player.respawn();
                deathTime = 0;
            }
            return; // Avoid further checks during respawn
        }

        this.getPickableObjects().stream()
            .map(PickableObjectImpl.class :: cast)
            .filter(x -> x.getHitBox().intersects(this.player.getHitBox()))
            .findFirst()
            .ifPresent(x -> {
                this.pickableObjectManager.addPickableObject(x);
                this.level.removePickableObject(x);
            }
        );
        
        System.out.println(this.player.getPos());
        if (this.player.getPos().y() > Constants.MIN_Y && this.player.getPos().y() <= 0) {
            if (this.level.getAllObstacles().stream().anyMatch(x -> x.getHitBox().intersects(this.player.getHitBox()))) {
                this.player.playerHit();
            }
        } else if (this.player.getPos().y() > 0 && this.player.getPos().y() < Constants.MAX_Y) {

            this.player.setAttached(false);

            getObstacles().stream().forEach(x -> {
                if (x instanceof Trunk) {
                    ((Trunk) x).removeObj();
                }
            });

            if (this.level.getAllObstacles().stream().anyMatch(x -> x.getHitBox().intersects(this.player.getHitBox()))) {
                getObstacles().stream().filter(x -> x.getHitBox().intersects(this.player.getHitBox())).forEach(x -> {
                    if (x instanceof Trunk) {
                        if (!this.player.isAttached()) {
                            ((Trunk) x).setObj(this.player);
                            this.player.setAttached(true);
                        }
                    } else if (x instanceof Eagle) {
                        this.player.playerHit();
                    }
                });
            } else {
                System.out.println(this.player.getPos());
                this.player.playerHit();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MovingObject> getObstacles() {
        return level.getAllObstacles();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns the internal PlayerObject reference. Modifying the returned object will affect this GameImpl.
     * </p>
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "Player is managed externally and this exposure is intentional"
    )
    @Override
    public PlayerObject getPlayer() {
        return this.player;
    }

    /**
     * {@inheritDoc}
     * Checks if the player has reached the end of the level and advances to a new level if so.
     */
    @Override
    public void checkNewLevel() {
        if (this.player.getPos().y() == Constants.MAX_Y) {
            this.level = this.levelFactory.createLevel();
            this.player.addPoints(Constants.POINT_LEVEL_COMPLETED);
            this.player.resetPosition();
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "Level is managed externally and this exposure is intentional"
    )
    @Override
    public Level getLevel() {
        return this.level;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns the internal Level reference. Modifying the returned object will affect this GameImpl.
     * </p>
     */
    @Override
    public Lane getCurrentLane() {
        return level.getLanes().get((int) this.player.getPos().y() + Constants.MAX_Y);
    }

    /**
     * {@inheritDoc}
     * Checks if the current lane is completed and awards points if not.
     */
    @Override
    public void checkProgress() {
        if (!getCurrentLane().isCompleted()) {
            this.player.addPoints(Constants.POINT_PER_LANE);
            this.getCurrentLane().setCompleted();
        }
    }

    /**
     * Checks if any eagle should be triggered based on the player's position.
     */
    public void checkEagleTrigger() {
        final double epsilon = 1e-6;
        for (final var eagle : this.level.getEagles()) {
            if (Math.abs(eagle.getTrigger() - this.getPlayer().getPos().y()) < epsilon) {
                eagle.start();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean gameIsPaused() {
        return GameState.getState() == GameState.PAUSE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PickableObject> getPickableObjects() {
        return this.level.getPickableObjects();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "PickableObjectManager is managed externally and this exposure is intentional"
    )
    @Override
    public PickableObjectManagerImpl getPickableObjectManager() {
        return this.pickableObjectManager;
    }
}
