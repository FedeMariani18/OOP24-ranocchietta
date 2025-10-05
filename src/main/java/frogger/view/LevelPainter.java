package frogger.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import frogger.common.Constants;
import frogger.common.Direction;
import frogger.common.LoadSave;
import frogger.common.Pair;
import frogger.common.Position;
import frogger.controller.GameController;
import frogger.model.interfaces.PickableObject;
import frogger.model.interfaces.PlayerObject;
import frogger.model.interfaces.PowerUp;

/**
 * class in charge of paint the Level of the Game.
 */
public class LevelPainter {
    private final GameController controller;
    private final Font myFont = new Font("MyFont", 1, Constants.BLOCK_HEIGHT / 2);
    private BufferedImage background;
    private BufferedImage heart;
    private BufferedImage death;

    /**
     * Constructs a LevelPainter with the specified GameController.
     * Initializes required images.
     *
     * @param controller the GameController managing the game state
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "The exposure of GameController is intentional: its lifecycle is managed externally to LevelPainter."
    )
    public LevelPainter(final GameController controller) {
        this.controller = controller;
    }

    /**
     * Paints the entire level including background, lives, obstacles, player,
     * score, and power-ups.
     *
     * @param g the Graphics context to draw on
     */
    public void paintLevel(final Graphics g) {
        paintBackground(g);
        paintLives(g);
        paintObstacles(g);
        paintPlayer(g);
        paintScore(g);
        paintPickableObject(g);
        paintTotalCoins(g);
        paintTimerPowerUp(g);
    }

    /**
     * Paints all obstacles present in the game.
     *
     * @param g the Graphics context to draw on
     */
    private void paintObstacles(final Graphics g) {
        for (final var obstacle : getController().getGame().getObstacles()) {
            g.drawImage(obstacle.getImage(), (int) this.getController().getXinPixel(obstacle.getPos().x()), 
                (int) this.getController().getYinPixel(obstacle.getPos().y()), 
                obstacle.getDimension().width() * Constants.BLOCK_WIDTH, 
                obstacle.getDimension().height() * Constants.BLOCK_HEIGHT, null);
        }
    }

    /**
     * Paints the player character on the screen.
     * If the player is dead, it displays a death image.
     * Otherwise, it rotates the player's image based on their direction.
     *
     * @param g the Graphics context to draw on
     */
    private void paintPlayer(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        final var player = getController().getGame().getPlayer();
        final BufferedImage playerImage = this.choosePlayerSprite(); 
        final int playerX = (int) getController().getXinPixel(player.getPos().x());
        final int playerY = (int) getController().getYinPixel(player.getPos().y());
        final int playerWidth = player.getDimension().width() * Constants.BLOCK_WIDTH;
        final int playerHeight = player.getDimension().height() * Constants.BLOCK_HEIGHT;

        if (player.isDead()) {
            g.drawImage(death, playerX, playerY, Constants.BLOCK_WIDTH, Constants.BLOCK_HEIGHT, null);
        } else {
            // Calculate the rotation angle based on the direction
            final double angle = calculateRotation(getController().getGame().getPlayer());

            // Apply the rotation
            g2d.rotate(angle, playerX + playerWidth / 2.0, playerY + playerHeight / 2.0);
            g2d.drawImage(playerImage, playerX, playerY, playerWidth, playerHeight, null);
            g2d.rotate(-angle, playerX + playerWidth / 2.0, playerY + playerHeight / 2.0); // Restore the rotation
        }

    }

    private BufferedImage choosePlayerSprite() {
        float r = 0;
        if(getController().getGame().getPlayer().isJumping()) {
            Position rest = new Position(Math.abs(getController().getGame().getPlayer().getPos().x() - Math.round(getController().getGame().getPlayer().getPos().x())),
             Math.abs(getController().getGame().getPlayer().getPos().y() - Math.round(getController().getGame().getPlayer().getPos().y())));

            if(rest.x() == 0) {
                r = rest.y();
            } else if(rest.y() == 0) {
                r = rest.x();
            }

            if(r <= 0.2) {
                return LoadSave.getSprite("ranocchietta_sprites.png").getSubimage(0, 0, 50, 50);
            } else if(r <= 0.4) {
                return LoadSave.getSprite("ranocchietta_sprites.png").getSubimage(55, 0, 50, 60);
            } else if(r <= 0.6) {
                return LoadSave.getSprite("ranocchietta_sprites.png").getSubimage(115, 0, 50, 75);
            } else if(r <= 0.8) {
                return LoadSave.getSprite("ranocchietta_sprites.png").getSubimage(171, 0, 50, 70);
            } else {
                return LoadSave.getSprite("ranocchietta_sprites.png").getSubimage(227, 0, 50, 60);
            }
        }
        return LoadSave.getSprite("ranocchietta.png");
    }

    /**
     * Calculates the rotation angle for the player based on their current direction.
     *
     * @param player the player object whose direction is used to compute rotation
     * @return the rotation angle in radians
     */
    private double calculateRotation(final PlayerObject player) {
        return switch (player.getDirection()) {
            case Direction.UP -> 0;
            case Direction.RIGHT -> Math.PI / 2;
            case Direction.DOWN -> Math.PI;
            case Direction.LEFT -> -Math.PI / 2;
        };
    }

    /**
     * Paints the background image of the game.
     *
     * @param g the Graphics context to draw on
     */
    private void paintBackground(final Graphics g) {
        g.drawImage(background, 0, 0, Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT, null);
    }

    /**
     * Paints the player's remaining lives as heart icons.
     *
     * @param g the Graphics context to draw on
     */
    private void paintLives(final Graphics g) {
        final int lives = this.getController().getGame().getPlayer().getLives();
        final int heartWidth = Constants.BLOCK_WIDTH;
        final int heartHeight = Constants.BLOCK_HEIGHT;
        // Calcola la posizione X per centrare le vite in alto
        final int startX = 0;
        final int y = (int) this.getController().getYinPixel(6); // In alto

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, startX + i * heartWidth, y, heartWidth, heartHeight, null);
        }
    }

    /**
     * Paints the current score of the player.
     *
     * @param g the Graphics context to draw on
     */
    private void paintScore(final Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(myFont);
        g.drawString("Punteggio: " + this.getController().getGame().getPlayer().getScore(), 
        (int) this.getController().getXinPixel(Constants.MAX_X - 3), (int) this.getController()
        .getYinPixel(Constants.MAX_Y - 0.5));
    }

    /**
     * Paints the total number of coins collected by the player.
     *
     * @param g the Graphics context to draw on
     */
    private void paintTotalCoins(final Graphics g) {

        final int startX = (int) getController().getXinPixel(Constants.MIN_X + 0.1);
        final int startY = (int) getController().getYinPixel(Constants.MAX_Y - 1.5);
        final String coinText = String.valueOf(getController().getCoins());

        g.setColor(Color.YELLOW);
        g.setFont(myFont);
        g.drawString(coinText, startX, startY);

        // Draw the coin image right after the text
        final int textWidth = g.getFontMetrics().stringWidth(coinText);
        final int coinSize = (int) (Constants.BLOCK_HEIGHT / 1.5);
        final int coinX = startX + textWidth + 5;
        final int coinY = startY - coinSize + g.getFontMetrics().getDescent();

        g.drawImage(LoadSave.getSprite("coin.png"), coinX, coinY, coinSize, coinSize, null);
    }

    /**
     * Paints all power-ups currently present in the level.
     *
     * @param g the Graphics context to draw on
     */
    private void paintPickableObject(final Graphics g) {
        for (final PickableObject obj : getController().getGame().getPickableObjects()) {
            g.drawImage(obj.getImage(), 
            (int) this.getController().getXinPixel(obj.getPos().x()), 
            (int) this.getController().getYinPixel(obj.getPos().y()), 
            null);
        }
    }

    /**
     * Displays the power-up image and its remaining duration.
     *
     * @param g the Graphics context to draw on
     */
    private void paintTimerPowerUp(final Graphics g) {
        int yOffset = 0;
        final List<PowerUp> powerUps = getController().getGame().getPickableObjectManager().getActivePowerUps();
        for (final PowerUp powerUp : powerUps) {
            final float duration = powerUp.getTimer();
            if (duration > 0) {
                final String durationStr = String.format("%.1f", duration);
                final int strWidth = g.getFontMetrics(myFont).stringWidth(durationStr);
                final int imgSize = Constants.BLOCK_HEIGHT / 2;
                final int imgX = (int) this.getController().getXinPixel(Constants.MAX_X) - imgSize;
                final int imgY = (int) this.getController().getYinPixel(Constants.MAX_Y - 2) - yOffset;
                final int rectX = imgX - 4;
                final int rectY = imgY - 4;
                final int rectHigh = strWidth + imgSize + 16;
                final int rectWidth = imgSize + 16;
                final Color colorStr = new Color(0, 0, 0, 80);
                final int xStr = imgX + imgSize + 5;
                final int yStr = imgY + imgSize;

                if (powerUp.getImage() != null) {
                    g.drawImage(powerUp.getImage(), imgX, imgY, imgSize, imgSize, null);
                }
                g.setColor(colorStr);
                g.fillRect(rectX, rectY, rectHigh, rectWidth);
                g.setColor(Color.WHITE);

                g.setFont(myFont);
                g.drawString(durationStr, xStr, yStr);

                yOffset -= Constants.BLOCK_HEIGHT;
            }

        }
    }

    /**
     * Imports required images such as the background, heart (life), and death image.
     * Loads resources from the classpath.
     */
    public void importImg() {
        background = LoadSave.getSprite("background.png");
        heart = LoadSave.getSprite("heart.png");
        death = LoadSave.getSprite("death.png");
    }

    /**
     * Returns the GameController associated with this painter.
     * The exposure of GameController is intentional: its lifecycle is managed externally to LevelPainter.
     *
     * @return the game controller
     */
    private GameController getController() {
        return this.controller;
    }
}
