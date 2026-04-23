import static org.junit.jupiter.api.Assertions.*;

import hu.nye.models.Player;
import org.junit.jupiter.api.Test;

class PlayerTest {
    @Test
    void testConstructorAndGetters() {
        Player player = new Player("John Doe", 'X');
        assertEquals("John Doe", player.getName());
        assertEquals('X', player.getDisc());
    }

    @Test
    void testConstructorAndGettersFails() {
        Player player = new Player("John Doe", 'X');
        assertNotEquals("Test Elek", player.getName());
        assertNotEquals('O', player.getDisc());
    }
}