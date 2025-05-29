package frogger.controller;

import frogger.view.GameScene;

public interface Controller {
    /**
     * Initializes the game scene and sets up necessary components.
     * @param gameScene the game scene to initialize
     */
    void init(GameScene gameScene);

    /**
     * Contains the core logic of the game loop to be executed each frame.
     * Must be implemented by subclasses.
     */
    void loop();

    /**
     * Trasformer of a logical x-coordinate (in game units) to a pixel value on screen.
     * @param x the logical x-coordinate
     * @return the corresponding x-coordinate in pixels
     */
    double getXinPixel(double x);

    /**
     * Trasformer of a logical y-coordinate (in game units) to a pixel value on screen.
     * @param x the logical y-coordinate
     * @return the corresponding y-coordinate in pixels
     */
    double getYinPixel(double y);
}
