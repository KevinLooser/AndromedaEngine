package game;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import engine.IHud;
import engine.Window;
import engine.graph.FontTexture;
import engine.objects.GameObject;
import engine.objects.TextItem;
import org.joml.Vector4f;

public class Hud implements IHud {

    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);

    private static final String CHARSET = "ISO-8859-1";

    private final List<GameObject> gameObjects;

    private final TextItem statusTextItem;

    private FontTexture fontTexture;

    public Hud(String statusText) throws Exception {
        fontTexture = new FontTexture(FONT, CHARSET);
        this.statusTextItem = new TextItem(statusText, fontTexture);
        this.statusTextItem.setPosition(0, 0, 0);
        this.statusTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 1));

        // Create list that holds the items that compose the HUD
        gameObjects = new ArrayList<>();
        gameObjects.add(statusTextItem);
    }

    public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }

    public void addStatusText(String statusText) throws Exception {
        TextItem textItem = new TextItem(statusText, fontTexture);
        gameObjects.add(textItem);
    }

    public void setStatusText(String statusText, TextItem textItem) {
        textItem.setText(statusText);
    }

    @Override
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void updateSize(Window window) {
        this.statusTextItem.setPosition(10f, window.getHeight() - 35f, 0);
    }
}