package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários do enum Compass")
class CompassTest {

    @Test
    @DisplayName("getDirection devolve o caracter correto")
    public void testGetDirection() {
        assertAll(
                () -> assertEquals('n', Compass.NORTH.getDirection()),
                () -> assertEquals('s', Compass.SOUTH.getDirection()),
                () -> assertEquals('e', Compass.EAST.getDirection()),
                () -> assertEquals('o', Compass.WEST.getDirection()),
                () -> assertEquals('u', Compass.UNKNOWN.getDirection())
        );
    }

    @Test
    @DisplayName("toString devolve a string correta (string do caracter correspondente)")
    public void testToString() {
        assertAll(
                () -> assertEquals("n", Compass.NORTH.toString()),
                () -> assertEquals("s", Compass.SOUTH.toString()),
                () -> assertEquals("e", Compass.EAST.toString()),
                () -> assertEquals("o", Compass.WEST.toString()),
                () -> assertEquals("u", Compass.UNKNOWN.toString())
        );
    }

    @Test
    @DisplayName("charToCompass converte bem os caracteres")
    public void testCharToCompass() {
        assertEquals(Compass.NORTH, Compass.charToCompass('n'));
        assertEquals(Compass.SOUTH, Compass.charToCompass('s'));
        assertEquals(Compass.EAST, Compass.charToCompass('e'));
        assertEquals(Compass.WEST, Compass.charToCompass('o'));
        assertEquals(Compass.UNKNOWN, Compass.charToCompass('a')); //ex de caracter que nao e n,s,e ou o
    }
}