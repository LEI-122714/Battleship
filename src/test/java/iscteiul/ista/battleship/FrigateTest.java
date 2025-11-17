package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários da classe Frigate")
class FrigateTest {

    private Position basePos;

    @BeforeEach
    void setUp() {
        basePos = new Position(2, 3); // posição inicial genérica
    }

    // ---------------------------------------------------------
    // CONSTRUÇÃO E TAMANHO
    // ---------------------------------------------------------

    @Test
    @DisplayName("Deve criar uma Frigate com tamanho 4")
    void testFrigateSize() {
        Frigate frigate = new Frigate(Compass.NORTH, basePos);
        assertEquals(4, frigate.getSize());
    }

    @Test
    @DisplayName("Deve lançar AssertionError se o bearing for null (por causa do assert no Ship)")
    void testInvalidBearingThrowsException() {
        assertThrows(AssertionError.class, () -> new Frigate(null, basePos));
    }

    // ---------------------------------------------------------
    // POSIÇÕES DE ACORDO COM A ORIENTAÇÃO
    // ---------------------------------------------------------

    @Test
    @DisplayName("Deve criar as posições corretas quando virada a Norte")
    void testPositionsNorth() {
        Frigate frigate = new Frigate(Compass.NORTH, basePos);

        assertEquals(4, frigate.getPositions().size());
        assertTrue(frigate.getPositions().contains(new Position(2, 3)));
        assertTrue(frigate.getPositions().contains(new Position(3, 3)));
        assertTrue(frigate.getPositions().contains(new Position(4, 3)));
        assertTrue(frigate.getPositions().contains(new Position(5, 3)));
    }

    @Test
    @DisplayName("Deve criar as posições corretas quando virada a Sul (igual ao código atual)")
    void testPositionsSouth() {
        Frigate frigate = new Frigate(Compass.SOUTH, basePos);

        assertEquals(4, frigate.getPositions().size());
        assertTrue(frigate.getPositions().contains(new Position(2, 3)));
        assertTrue(frigate.getPositions().contains(new Position(3, 3)));
        assertTrue(frigate.getPositions().contains(new Position(4, 3)));
        assertTrue(frigate.getPositions().contains(new Position(5, 3)));
    }

    @Test
    @DisplayName("Deve criar as posições corretas quando virada a Este")
    void testPositionsEast() {
        Frigate frigate = new Frigate(Compass.EAST, basePos);

        assertEquals(4, frigate.getPositions().size());
        assertTrue(frigate.getPositions().contains(new Position(2, 3)));
        assertTrue(frigate.getPositions().contains(new Position(2, 4)));
        assertTrue(frigate.getPositions().contains(new Position(2, 5)));
        assertTrue(frigate.getPositions().contains(new Position(2, 6)));
    }

    @Test
    @DisplayName("Deve criar as posições corretas quando virada a Oeste (igual ao código atual)")
    void testPositionsWest() {
        Frigate frigate = new Frigate(Compass.WEST, basePos);

        // Oeste NO CÓDIGO = mesma lógica do EAST
        assertEquals(4, frigate.getPositions().size());
        assertTrue(frigate.getPositions().contains(new Position(2, 3)));
        assertTrue(frigate.getPositions().contains(new Position(2, 4)));
        assertTrue(frigate.getPositions().contains(new Position(2, 5)));
        assertTrue(frigate.getPositions().contains(new Position(2, 6)));
    }

    // ---------------------------------------------------------
    // MÉTODOS HERDADOS DE SHIP
    // ---------------------------------------------------------

    @Test
    @DisplayName("Método occupies() deve retornar true para uma posição ocupada")
    void testOccupiesTrue() {
        Frigate frigate = new Frigate(Compass.EAST, basePos);
        Position pos = new Position(2, 4);
        assertTrue(frigate.occupies(pos));
    }

    @Test
    @DisplayName("Método occupies() deve retornar false para uma posição não ocupada")
    void testOccupiesFalse() {
        Frigate frigate = new Frigate(Compass.EAST, basePos);
        Position pos = new Position(5, 5);
        assertFalse(frigate.occupies(pos));
    }

    @Test
    @DisplayName("toString deve seguir o formato definido em Ship: [categoria bearing posição]")
    void testToString() {
        Frigate frigate = new Frigate(Compass.NORTH, basePos);
        String texto = frigate.toString();

        String bearingName = frigate.getBearing().name();
        String bearingString = frigate.getBearing().toString();

        assertAll(
                () -> assertTrue(texto.contains("Fragata"), "Categoria não aparece no toString"),
                () -> assertTrue(texto.contains(bearingName) || texto.contains(bearingString), "Bearing não aparece no toString"),
                () -> assertTrue(texto.contains("Linha = 2"), "Linha não aparece corretamente no toString"),
                () -> assertTrue(texto.contains("Coluna = 3"), "Coluna não aparece corretamente no toString"),
                () -> assertTrue(texto.startsWith("["), "toString deve começar com ["),
                () -> assertTrue(texto.endsWith("]"), "toString deve terminar com ]")
        );
    }


}
