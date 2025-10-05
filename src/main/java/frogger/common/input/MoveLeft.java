package frogger.common.input;

import frogger.common.Constants;
import frogger.common.Position;
import frogger.model.interfaces.Game;
import frogger.model.interfaces.PlayerObject;

/**
 * Command to move the player one unit to the left, if possible.
 */
public class MoveLeft implements Command {

    /**
     * {@inheritDoc}
     * Moves the player left if not at the minimum X boundary.
     */
    @Override
    public void execute(final Game game) {
        final PlayerObject player = game.getPlayer();
        if (player.getPos().x() > Constants.MIN_X) {
            player.setLookingLeft();
            //player.setPos(new Position(player.getPos().x() - 1, player.getPos().y()));
            player.setJump(true);
        }
    }

}
