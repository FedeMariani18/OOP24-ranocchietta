package frogger.common.input;

import frogger.common.Constants;
import frogger.common.Position;
import frogger.model.interfaces.Game;
import frogger.model.interfaces.PlayerObject;

/**
 * Command to move the player one unit down, if possible.
 */
public class MoveDown implements Command {

    /**
     * {@inheritDoc}
     * Moves the player down if not at the minimum Y boundary.
     */
    @Override
    public void execute(final Game game) {
        final PlayerObject player = game.getPlayer();
        if (player.getPos().y() > Constants.MIN_Y) {
            player.setLookingDown();
            //player.setPos(new Position(player.getPos().x(), player.getPos().y() - 1));
            player.setJump(true);
        }
    }

}
