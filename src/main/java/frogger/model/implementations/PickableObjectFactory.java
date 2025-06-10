package frogger.model.implementations;

import java.util.Random;

import frogger.common.Constants;
import frogger.common.Pair;
import frogger.common.Position;
import frogger.model.interfaces.PickableObject;

final class PickableObjectFactory {

    /**
     * Private constructor to prevent instantiation of the factory class.
     * This class is intended to be used as a utility for creating PickableObject instances.
     */
    private PickableObjectFactory() { }

    /**
     * Creates a new instance of a PickableObject based on the specified type and position.
     *
     * @param type       the class type of the PickableObject to create
     * @param position   the position where the PickableObject will be placed
     * @param dimension  the dimensions of the PickableObject
     * @return a new instance of the specified PickableObject type, or null if the type is not recognized
     */
    public static PickableObject createPickableObject(final Class<? extends PickableObject> type,
                                                      final Position position, final Pair dimension) {
        return switch (type.getSimpleName()) {
            case "Coin" -> new Coin(position, dimension);
            case "PowerUpImpl" -> getRandomPowerUpType(position, dimension);
            default -> null;
        };
    }

    /**
     * Creates a random power-up type at the specified position with the given dimensions.
     * The selection is based on predefined probabilities for each power-up type.
     *
     * @param position   the position where the power-up will be placed
     * @param dimension  the dimensions of the power-up
     * @return a new instance of a PowerUp based on random selection
     */
    private static PickableObject getRandomPowerUpType(final Position position, final Pair dimension) {
        final Random random = new Random();
        final int rand = random.nextInt(1, 101); // 1-100
        final PowerUpType selectedType;

        if (rand < Constants.EXTRA_LIFE_THRESHOLD) { 
            selectedType = PowerUpType.EXTRA_LIFE;
        } else {
            selectedType = random.nextBoolean() ? PowerUpType.FREEZE : PowerUpType.X2_SCORE;
        }

        return switch (selectedType) {
            case FREEZE -> new FreezePowerUp(position, dimension, Constants.FREEZE_DURATION);
            case EXTRA_LIFE -> new ExtraLifePowerUp(position, dimension);
            case X2_SCORE -> new DoubleScorePowerUp(position, dimension, Constants.DOUBLE_SCORE_DURATION);
        };
    }
}
