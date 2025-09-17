package se233.chapter5part1;

import javafx.scene.input.KeyCode;
import org. junit. jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se233.chapter5part1.model.GameCharacter;

import static org. junit. jupiter.api.Assertions .*;

public class GameCharacterTest {
    Field xVelocityField, yVelocityField, yAccelerationField;
    private GameCharacter gameCharacter;

    @BeforeAll
    public void setup() throws NoSuchFieldException {
        javafx.application.Platform.startup(() -> {
        });
    }
    @BeforeEach
    public void setUp() throws NoSuchFieldException {
        gameCharacter = new GameCharacter(0,30, 30,"assets/Character1.png", 4, 3
                ,2, 111,97, KeyCode.A, KeyCode.D, KeyCode.W);
    }
    @Test
    public void respawn_givenNewGameCharacter_thenCoordinatesAre30_30() {
        gameCharacter.respawn();
        assertEquals(30, gameCharacter.getX(), "Initial x");
        assertEquals(30, gameCharacter.getY(), "Initial y");
    }
    @Test
    public void respawn_givenNewGameCharacter_thenScoreIs0() {
        gameCharacter.respawn();
        assertEquals(0, gameCharacter.getScore(), "Initial score");
    }
}