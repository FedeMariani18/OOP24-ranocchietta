package frogger.model.implementations;

import java.util.Random;
import java.util.Set;

import frogger.common.Constants;
import frogger.common.Direction;
import frogger.model.interfaces.Lane;
import frogger.model.interfaces.Level;
import frogger.model.interfaces.LevelFactory;
import frogger.model.interfaces.PickableObject;
import frogger.model.interfaces.RandomSpawnerFactory;

/**
 * {@inheritDoc}.
 */
public class LevelFactoryImpl implements LevelFactory {

    private final Random ran = new Random();

    /**
     * {@inheritDoc}
     */
    @Override
    public Level randomLevel() {
        final RandomSpawnerFactory fact = new RandomSpawnerFactoryImpl();
        final Level level = new LevelImpl();
        int laneIndex = Constants.MIN_Y;

        //add Eagles
        fact.randomEagleSpawner().spawn(Constants.MIN_EAGLES_NUMBER, Constants.MAX_EAGLES_NUMBER).forEach(level::addEagle);
        //add Power ups
        fact.randomPowerUpSpawner(Set.of()).spawn(Constants.MIN_POWER_UP_NUMBER, Constants.MAX_POWER_UP_NUMBER)
        .forEach(level::addPickableObject);
        //add Coins
        fact.randomCoinSpawner().spawn(Constants.MIN_COIN_NUMBER, Constants.MAX_COIN_NUMBER)
        .forEach(level::addPickableObject); //da mettere nelle parentesi: level.getPickableObjects().stream().map(PickableObject::getPos)

        //build the level and abb obstacle to the lane
        final Lane start = new Ground();
        level.addLane(start);
        laneIndex++;
        for (int i = 0; i < Constants.ROAD_LANES; i++) {
            final Lane road = createLane(Road.class, laneIndex);
            fact.randomCarSpawner(laneIndex, road.getSpeed(), road.getDirection()).spawn(Constants.MIN_OBSTACLES_NUMBER,
            Constants.MAX_OBSTACLES_NUMBER).forEach(road::addMovingObject);
            level.addLane(road);
            laneIndex++;
        }
        final Lane mid = new Ground();
        level.addLane(mid);
        laneIndex++;
        for (int i = 0; i < Constants.RIVER_LANES; i++) {
            final Lane river = createLane(River.class, laneIndex);
            fact.randomTrunkSpawner(laneIndex, river.getSpeed(), river.getDirection()).spawn(Constants.MIN_OBSTACLES_NUMBER,
            Constants.MAX_OBSTACLES_NUMBER).forEach(river::addMovingObject);
            level.addLane(river);
            laneIndex++;
        }
        final Lane end = new Ground();
        level.addLane(end);
        return level;
    }

    /**
     * Creates a lane with random speed and a certain direction.
     * @param type the type of lane to create (Road or River)
     * @param y the y coordinate of the lane, needed to determine the direction
     * @return the lane
     */
    private Lane createLane(final Class<? extends Lane> type, final int y) {
        final Direction dir = y % 2 == 0 ? Direction.RIGHT : Direction.LEFT;
        final float speed = ran.nextFloat(Constants.MIN_SPEED, Constants.MAX_SPEED);
        final Lane lane;
        if (type.equals(Road.class)) {
            lane = new Road(speed, dir);
        } else {
            lane = new River(speed, dir);
        }
        return lane;
    }

}
