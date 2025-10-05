package frogger.common.input;

import frogger.common.Constants;
import frogger.common.Position;
import frogger.model.interfaces.Game;
import frogger.model.interfaces.PlayerObject;

/**
 * Command to move the player one unit to the right, if possible.
 */
public class MoveRight implements Command {

    /**
     * {@inheritDoc}
     * Moves the player right if not at the maximum X boundary.
     */
    @Override
    public void execute(final Game game) {
        final PlayerObject player = game.getPlayer();
        if (player.getPos().x() < Constants.MAX_X) {
            player.setLookingRight();
            //player.setPos(new Position(player.getPos().x() + 1, player.getPos().y()));
            player.setJump(true);
        }
    }

}
