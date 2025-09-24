package se233.chapter5part1;

import javafx.scene. input.KeyCode;
import org. junit. jupiter.api.BeforeAll;
import org. junit. jupiter.api.BeforeEach;
import org. junit. jupiter.api. Test;
import se233.chapter5part1.model.GameCharacter;
import se233.chapter5part1.model.Keys;
import se233.chapter5part1.view.GameStage;

import java.lang.reflect.Field;

import static org. junit. jupiter.api.Assertions .*;

public class GameCharacterTest {
    Field xVelocityField, yVelocityField, yAccelerationField;
    private GameCharacter gameCharacter;

    @BeforeAll
    public static void initJfxRuntime() {
        javafx.application. Platform.startup(() -> {})  ;
    }

    @BeforeEach
    public void setup() throws NoSuchFieldException {
        gameCharacter = new GameCharacter(0, 30, 30, "assets/Character1.png", 4, 3, 2, 111, 97, KeyCode.A, KeyCode.D, KeyCode.W);
        xVelocityField = gameCharacter.getClass().getDeclaredField("xVelocity");
        yVelocityField = gameCharacter.getClass().getDeclaredField("yVelocity");
        yAccelerationField = gameCharacter.getClass().getDeclaredField("yAcceleration");
        xVelocityField.setAccessible(true);
        yVelocityField.setAccessible(true);
        yAccelerationField.setAccessible(true);

        GameStage.WIDTH = 800;

    }

    @Test
    public void respawn_givenNewGameCharacter_thenCoordinatesAre30_30() {
        gameCharacter.respawn();
        assertEquals(30, gameCharacter.getX(), "Initial x");
        assertEquals(30, gameCharacter.getY(), "Initial y");
    }

    @Test
    public void respawn_givenNewGameCharacter_thenScoreIs0 () {
        gameCharacter.respawn();
        assertEquals(0, gameCharacter.getScore(), "Initial score");
    }

    @Test
    public void moveX_givenMoveRightOnce_thenXCoordinateIncreasedByXVelocity() throws IllegalAccessException {
        gameCharacter.respawn();
        gameCharacter.moveRight();
        gameCharacter.moveX();
        assertEquals(30 + xVelocityField.getInt(gameCharacter), gameCharacter.getX(), "Move right x");
    }

    @Test
    public void moveY_givenTwoConsecutiveCalls_thenYVelocityIncreases() throws IllegalAccessException {
        gameCharacter.respawn();
        gameCharacter.moveY();
        int yVelocity1 = yVelocityField.getInt(gameCharacter);
        gameCharacter.moveY();
        int yVelocity2 = yVelocityField.getInt(gameCharacter);
        assertTrue(yVelocity2 > yVelocity1, "Velocity is increasing");
    }

    @Test
    public void moveY_givenTwoConsecutiveCalls_thenYAccelerationUnchanged() throws IllegalAccessException {
        gameCharacter.respawn();
        gameCharacter.moveY();
        int yAcceleration1 = yAccelerationField.getInt(gameCharacter);
        gameCharacter.moveY();
        int yAcceleration2 = yAccelerationField.getInt(gameCharacter);
        assertTrue(yAcceleration1 == yAcceleration2, "Acceleration is not change");
    }

    @Test
    public void checkReachGameWall_LeftBoundary() {
        gameCharacter = new GameCharacter(0, -50, 30, "assets/Character1.png", 4, 3, 2, 111, 97, KeyCode.A, KeyCode.D, KeyCode.W);
        gameCharacter.checkReachGameWall();
        assertEquals(0, gameCharacter.getX(), "x negative should reset to 0");
    }

    @Test
    public void checkReachGameWall_RightBoundary() {
        int characterWidth = 111;
        int overX = 800 - characterWidth + 50;
        gameCharacter = new GameCharacter(0, overX, 30, "assets/Character1.png", 4, 3, 2, characterWidth, 97, KeyCode.A, KeyCode.D, KeyCode.W);
        gameCharacter.checkReachGameWall();
        assertEquals(800 - characterWidth, gameCharacter.getX(), "x dose not fit with screen");
    }

    @Test
    public void jump_whenCanJump_shouldInitiateJump() {
        // arrange
        gameCharacter.canJump = true;
        gameCharacter.isFalling = false;
        gameCharacter.isJumping = false;
        // act
        gameCharacter.jump();
        // assert
        assertFalse(gameCharacter.canJump, "after jump, canJump = false");
        assertTrue(gameCharacter.isJumping, "after jump, isJumping = true");
        assertFalse(gameCharacter.isFalling, "after jump, isFalling = false");
        assertEquals(gameCharacter.yMaxVelocity, gameCharacter.yVelocity, "after jump, yVelocity = yMaxVelocity");
    }

    @Test
    public void jump_whenCannotJump_shouldDoNothing() {
        // arrange
        gameCharacter.canJump = false;
        gameCharacter.isFalling = true;
        gameCharacter.isJumping = false;
        gameCharacter.yVelocity = 5;
        // act
        gameCharacter.jump();
        // assert
        assertFalse(gameCharacter.isJumping, "can't jump while canJump = false");
        assertTrue(gameCharacter.isFalling, "continue falling");
        assertEquals(5, gameCharacter.yVelocity, "yVelocity dose not change");
    }

    @Test
    void collided_whenMovingRightAndTouchOnXAxis_shouldSetXToLeftOfTarget() {
        GameCharacter target = new GameCharacter(1, 200, 30, "assets/Character1.png", 4, 3, 2, 50, 50, KeyCode.A, KeyCode.D, KeyCode.W);
        gameCharacter = new GameCharacter(0, 100, 30, "assets/Character1.png", 4, 3, 2, 50, 50, KeyCode.A, KeyCode.D, KeyCode.W);

        gameCharacter.isMoveRight = true;
        gameCharacter.isMoveLeft = false;

        gameCharacter.collided(target);

        assertEquals(100, gameCharacter.getX());
    }

    @Test
    void collided_whenFallingAndTouchOnYAxis_shouldLandOnTargetAndIncreaseScore() {
        int targetY = 151;
        GameCharacter target = new GameCharacter(1, 100, targetY, "assets/Character1.png", 4, 3, 2, 50, 50, KeyCode.A, KeyCode.D, KeyCode.W);
        gameCharacter = new GameCharacter(0, 100, 100, "assets/Character1.png", 4, 3, 2, 50, 50, KeyCode.A, KeyCode.D, KeyCode.W);

        gameCharacter.isFalling = true;
        gameCharacter.yVelocity = 0;

        int oldScore = gameCharacter.getScore();

        boolean result = gameCharacter.collided(target);

        assertTrue(result, "return true if hit y");
        assertEquals(oldScore + 1, gameCharacter.getScore());
        assertTrue(Math.abs(target.getY() - gameCharacter.getY()) <= 1, "y after landed must be close to target's Y");
    }
}