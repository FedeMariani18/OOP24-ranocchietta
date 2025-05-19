package frogger.common.input;

import frogger.common.Constants;
import frogger.common.Position;
import frogger.model.interfaces.Game;
import frogger.model.interfaces.PlayerObject;

/**
 * Command to move the player one unit up, if possible.
 */
public class MoveUp implements Command {

    /**
     * {@inheritDoc}
     * Moves the player up if not at the maximum Y boundary.
     */
    @Override
    public void execute(final Game game) {
        final PlayerObject player = game.getPlayer();
        if (player.getPos().y() < Constants.MAX_Y) {
            player.setLookingUp();
            player.setPos(new Position(player.getPos().x(), player.getPos().y() + 1));
        }
    }

}
