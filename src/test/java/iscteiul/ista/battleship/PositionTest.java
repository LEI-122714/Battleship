package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários da classe Position")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PositionTest {

    private Position pos1;
    private Position pos2;

    @BeforeEach
    public void setup() {
        pos1 = new Position(2, 3);
        pos2 = new Position(2, 4);
    }

    @Test
    @DisplayName("Construtor inicializa corretamente")
    public void testConstructor() {
        assertAll(
                () -> assertEquals(2, pos1.getRow()),
                () -> assertEquals(3, pos1.getColumn()),
                () -> assertFalse(pos1.isOccupied()),
                () -> assertFalse(pos1.isHit())
        );
    }

    @Test
    @DisplayName("equals funciona corretamente")
    public void testEquals() {
        Position pos3 = new Position(2, 3);
        assertEquals(pos1, pos3);
        assertNotEquals(pos1, pos2);
    }

    @Test
    @DisplayName("isAdjacentTo reconhece posições vizinhas")
    public void testIsAdjacentTo() {
        Position diagonal = new Position(3, 4);
        Position naoVizinho = new Position(5, 7);

        assertTrue(pos1.isAdjacentTo(pos2));     // posicao ao lado
        assertTrue(pos1.isAdjacentTo(diagonal));
        assertFalse(pos1.isAdjacentTo(naoVizinho));
    }

    @Test
    @DisplayName("occupy altera estado para occupied")
    public void testOccupy() {
        assertFalse(pos1.isOccupied());
        pos1.occupy();
        assertTrue(pos1.isOccupied());
    }

    @Test
    @DisplayName("shoot altera estado para hit")
    public void testShoot() {
        assertFalse(pos1.isHit());
        pos1.shoot();
        assertTrue(pos1.isHit());
    }

    @Test
    @DisplayName("toString devolve a string correta")
    public void testToString() {
        String s = pos1.toString();
        assertEquals("Linha = 2 Coluna = 3", s);
    }
}