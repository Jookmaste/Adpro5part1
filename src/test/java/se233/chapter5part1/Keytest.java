package se233.chapter5part1;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se233.chapter5part1.model.Keys;
import static org.junit.jupiter.api.Assertions.*;

public class Keytest {
    private Keys keys;

    @BeforeEach
    void setUp() {
        keys = new Keys();
    }

    @Test
    void singleKeyPress_shouldChangeStateToPressed() {
        keys.isPressed(KeyCode.A);
        assertTrue(keys.isPressed(KeyCode.A), "Key A must be pressed");
        keys.remove(KeyCode.A);
        assertFalse(keys.isPressed(KeyCode.A), "Key A must not be pressed after release");
    }

    @Test
    void multipleKeysPress_shouldTrackIndependentStates() {
        keys.isPressed(KeyCode.A);
        keys.isPressed(KeyCode.D);

        assertTrue(keys.isPressed(KeyCode.A), "Key A must be pressed");
        assertTrue(keys.isPressed(KeyCode.D), "Key D must be pressed");
        assertFalse(keys.isPressed(KeyCode.W), "Key W must not be pressed");

        keys.remove(KeyCode.A);
        assertFalse(keys.isPressed(KeyCode.A), "Key A must not be pressed after release");
        assertTrue(keys.isPressed(KeyCode.D), "Key D must remain pressed");
    }
}