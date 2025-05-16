package frogger.model.implementations;

import java.util.Random;
import java.util.Set;

import frogger.common.Constants;
import frogger.common.Direction;
import frogger.common.Pair;
import frogger.common.Position;
import frogger.common.RandomUtils;
import frogger.model.interfaces.MovingObjectFactory;

/**
 * Class that extends AbstractRandomEntitySpawner to specify the behaviour spawning type Eagle entity.
 */
public class RandomEaglesSpawner extends AbstractRandomEntitySpawner<Eagle> {

    private final Random ran = new Random();
    private final MovingObjectFactory obstaclesFactory = new MovingObjectFactoryImpl();

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isValidPosition(final Position pos, final Set<Position> used) {
        return !used.contains(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Position generatePosition() {
        final int y = ran.nextBoolean() ? Constants.MIN_Y - 1 : Constants.MAX_Y + 1;
        return new Position(RandomUtils.randomX(), y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Eagle createEntity(final Position pos) {
        final Pair dim = new Pair(Constants.EAGLE_WIDTH, Constants.EAGLE_HEIGHT);
        final Direction dir = pos.y() == Constants.MIN_Y - 1 ? Direction.UP : Direction.DOWN;
        int triggerRow = Constants.MIN_Y;
        while (triggerRow == Constants.MIN_Y || triggerRow == Constants.MAX_Y) {
            triggerRow = RandomUtils.randomY();
        }
        final float speed = ran.nextFloat(Constants.MIN_SPEED, Constants.MAX_SPEED);
        final Eagle eagle = obstaclesFactory.createMovingObject(pos, dim, speed, dir, Eagle.class);
        eagle.setTrigger(triggerRow);
        return eagle;
    }

}
