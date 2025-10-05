package frogger.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Utility class for loading image resources from the classpath.
 */
public final class LoadSave {

    private static final Logger LOGGER = Logger.getLogger(LoadSave.class.getName());

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private LoadSave() { }

    /**
     * Loads an image from the classpath as a {@link BufferedImage}.
     *
     * @param fileName the name of the image file to load, relative to the classpath root.
     * @return the loaded {@code BufferedImage}, or {@code null} if the file is not found or an error occurs.
     */
    public static BufferedImage getSprite(final String fileName) {
        try (InputStream is = LoadSave.class.getResourceAsStream("/" + fileName)) {
            if (is == null) {
                LOGGER.log(Level.SEVERE, "File not found: ", fileName);
                return null;
            }
            return ImageIO.read(is);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading image: ", e.getMessage());
            return null;
        }
    }

    public static BufferedImage getSubSprite(final String fileName, final int x, final int y, final int w, final int h) {
        try (InputStream is = LoadSave.class.getResourceAsStream("/" + fileName)) {
            if (is == null) {
                LOGGER.log(Level.SEVERE, "File not found: ", fileName);
                return null;
            }
            return ImageIO.read(is);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading image: ", e.getMessage());
            return null;
        }
    }
}
