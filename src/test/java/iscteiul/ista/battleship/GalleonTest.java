package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários da classe Galleon")
class GalleonTest {

    private Position basePos;

    @BeforeEach
    void setUp() {
        basePos = new Position(2, 3); // posição inicial genérica
    }

    // ---------------------------------------------------------
    // CONSTRUÇÃO E TAMANHO
    // ---------------------------------------------------------

    @Test
    @DisplayName("Deve criar um Galleon com tamanho 5")
    void testGalleonSize() {
        Galleon galleon = new Galleon(Compass.NORTH, basePos);
        assertEquals(5, galleon.getSize());
    }

    @Test
    @DisplayName("Deve lançar AssertionError se bearing for null")
    void testNullBearingThrowsException() {
        assertThrows(AssertionError.class, () -> new Galleon(null, basePos));
    }

    // ---------------------------------------------------------
    // POSIÇÕES POR ORIENTAÇÃO
    // ---------------------------------------------------------

    @Test
    @DisplayName("Deve criar as posições corretas para orientação NORTH")
    void testPositionsNorth() {
        Galleon g = new Galleon(Compass.NORTH, basePos);

        assertEquals(5, g.getPositions().size());
        assertTrue(g.getPositions().contains(new Position(2, 3)));
        assertTrue(g.getPositions().contains(new Position(2, 4)));
        assertTrue(g.getPositions().contains(new Position(2, 5)));
        assertTrue(g.getPositions().contains(new Position(3, 4)));
        assertTrue(g.getPositions().contains(new Position(4, 4)));
    }

    @Test
    @DisplayName("Deve criar as posições corretas para orientação SOUTH")
    void testPositionsSouth() {
        Galleon g = new Galleon(Compass.SOUTH, basePos);

        assertEquals(5, g.getPositions().size());
        assertTrue(g.getPositions().contains(new Position(2, 3)));
        assertTrue(g.getPositions().contains(new Position(3, 3)));
        assertTrue(g.getPositions().contains(new Position(4, 2)));
        assertTrue(g.getPositions().contains(new Position(4, 3)));
        assertTrue(g.getPositions().contains(new Position(4, 4)));
    }

    @Test
    @DisplayName("Deve criar as posições corretas para orientação EAST")
    void testPositionsEast() {
        Galleon g = new Galleon(Compass.EAST, basePos);

        assertEquals(5, g.getPositions().size());
        assertTrue(g.getPositions().contains(new Position(2, 3)));
        assertTrue(g.getPositions().contains(new Position(3, 1)));
        assertTrue(g.getPositions().contains(new Position(3, 2)));
        assertTrue(g.getPositions().contains(new Position(3, 3)));
        assertTrue(g.getPositions().contains(new Position(4, 3)));
    }

    @Test
    @DisplayName("Deve criar as posições corretas para orientação WEST")
    void testPositionsWest() {
        Galleon g = new Galleon(Compass.WEST, basePos);

        assertEquals(5, g.getPositions().size());
        assertTrue(g.getPositions().contains(new Position(2, 3)));
        assertTrue(g.getPositions().contains(new Position(3, 3)));
        assertTrue(g.getPositions().contains(new Position(3, 4)));
        assertTrue(g.getPositions().contains(new Position(3, 5)));
        assertTrue(g.getPositions().contains(new Position(4, 3)));
    }

    // ---------------------------------------------------------
    // MÉTODOS HERDADOS DE SHIP
    // ---------------------------------------------------------

    @Test
    @DisplayName("Método occupies() deve retornar true se a posição for ocupada")
    void testOccupiesTrue() {
        Galleon g = new Galleon(Compass.NORTH, basePos);
        assertTrue(g.occupies(new Position(2, 3)));
    }

    @Test
    @DisplayName("Método occupies() deve retornar false se a posição não for ocupada")
    void testOccupiesFalse() {
        Galleon g = new Galleon(Compass.NORTH, basePos);
        assertFalse(g.occupies(new Position(9, 9)));
    }

    @Test
    @DisplayName("toString deve conter o nome e a direção do Galleon")
    void testToString() {
        Galleon g = new Galleon(Compass.SOUTH, basePos);
        String txt = g.toString();

        String bearingName = g.getBearing().name();
        String bearingStr = g.getBearing().toString();

        assertTrue(txt.contains("Galeao"));
        assertTrue(txt.contains(bearingName) || txt.contains(bearingStr));
    }

    // ---------------------------------------------------------
    // EXTRA: TESTES DE INTEGRAÇÃO SIMPLES COM MÉTODOS HERDADOS
    // ---------------------------------------------------------

    @Test
    @DisplayName("Método shoot() deve marcar uma posição atingida")
    void testShootMarksHit() {
        Galleon g = new Galleon(Compass.NORTH, basePos);
        Position p = new Position(2, 3);
        g.shoot(p);

        // A posição atingida deve estar marcada como "hit"
        assertTrue(g.getPositions().get(0).isHit());
    }

    @Test
    @DisplayName("Método stillFloating() deve retornar false após todas as posições serem atingidas")
    void testStillFloatingAfterAllHits() {
        Galleon g = new Galleon(Compass.NORTH, basePos);

        // Simula disparos em todas as posições
        for (IPosition pos : g.getPositions()) {
            g.shoot(pos);
        }

        assertFalse(g.stillFloating());
    }
}
