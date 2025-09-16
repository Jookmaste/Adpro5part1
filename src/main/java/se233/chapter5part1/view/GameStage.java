package se233.chapter5part1.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.chapter5part1.Launcher;
import se233.chapter5part1.model.GameCharacter;
import se233.chapter5part1.model.Keys;

import java.util.ArrayList;
import java.util.List;

public class GameStage extends Pane {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public final static int GROUND = 300;
    private Image gameStageImg;
    private List<GameCharacter> gameCharacterList = new ArrayList();
    private Keys keys;

    public GameStage() {
        keys = new Keys();
        gameStageImg = new Image(Launcher.class.getResourceAsStream("assets/Background.png"));
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);
        gameCharacterList.add(new GameCharacter(0,30, 30,"assets/Character1.png", 4, 3 ,2, 111,97, KeyCode.A, KeyCode.D, KeyCode.W));
        gameCharacterList.add(new GameCharacter(1, GameStage.WIDTH-60, 30, "assets/Character2.png", 4, 4 ,1, 129,66, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP));
        getChildren().add(backgroundImg);
        getChildren().addAll(gameCharacterList);
    }
    public List<GameCharacter> getGameCharacterList() {

        return gameCharacterList;
    }
    public Keys getKeys() {

        return keys;
    }
}