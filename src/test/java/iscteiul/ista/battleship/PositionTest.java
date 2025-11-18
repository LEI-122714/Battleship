package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários da classe Position")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PositionTest {

    private Position pos1;
    private Position pos2;

    @BeforeEach
    void setup() {
        pos1 = new Position(2, 3);
        pos2 = new Position(2, 4);
    }

    // ---------------- 基础行为：构造、get/set、状态 ----------------
    @Nested
    @DisplayName("Comportamento básico")
    class BasicBehaviour {

        @Test
        @DisplayName("Construtor define corretamente row e column")
        void constructorSetsRowAndColumn() {
            assertEquals(2, pos1.getRow());
            assertEquals(3, pos1.getColumn());
        }

        @Test
        @DisplayName("Novo Position começa sem ocupado nem atingido")
        void newPositionStartsFreeAndNotHit() {
            assertFalse(pos1.isOccupied());
            assertFalse(pos1.isHit());
        }

        @Test
        @DisplayName("occupy altera estado para ocupado")
        void occupyMarksAsOccupied() {
            assertFalse(pos1.isOccupied());
            pos1.occupy();
            assertTrue(pos1.isOccupied());
        }

        @Test
        @DisplayName("shoot altera estado para hit")
        void shootMarksAsHit() {
            assertFalse(pos1.isHit());
            pos1.shoot();
            assertTrue(pos1.isHit());
        }

        @Test
        @DisplayName("toString devolve a string correta")
        void toStringReturnsCorrectString() {
            String s = pos1.toString();
            assertEquals("Linha = 2 Coluna = 3", s);
        }
    }

    // ---------------- equals / hashCode：分支覆盖 ----------------
    @Nested
    @DisplayName("equals e hashCode")
    class EqualsAndHashCode {

        @Test
        @DisplayName("equals é verdadeiro para o mesmo objeto (this == other)")
        void equalsSameObject() {
            assertTrue(pos1.equals(pos1));    // 触发 if (this == otherPosition)
        }

        @Test
        @DisplayName("equals é falso quando comparado com null")
        void equalsNull() {
            assertFalse(pos1.equals(null));   // instanceof 分支为 false
        }

        @Test
        @DisplayName("equals é falso quando comparado com objeto de outra classe")
        void equalsDifferentClass() {
            Object otherType = "not a position";
            assertFalse(pos1.equals(otherType));  // instanceof IPosition 为 false，走 else false
        }

        @Test
        @DisplayName("equals é verdadeiro para posições com mesma row e column")
        void equalsSameRowAndColumn() {
            Position same = new Position(2, 3);
            assertTrue(pos1.equals(same));
            assertTrue(same.equals(pos1));
        }

        @Test
        @DisplayName("equals é falso quando row é diferente (primeira parte do && é falsa)")
        void equalsDifferentRow() {
            Position diffRow = new Position(5, 3);   // row 不同，column 相同
            assertFalse(pos1.equals(diffRow));       // 触发 row 比较为 false，&& 短路
        }

        @Test
        @DisplayName("equals é falso quando row é igual mas column é diferente (segunda parte do && é falsa)")
        void equalsSameRowDifferentColumn() {
            Position diffCol = new Position(2, 99);  // row 相同，column 不同
            assertFalse(pos1.equals(diffCol));       // 先 row true，再 column false
        }

        @Test
        @DisplayName("hashCode é igual para objetos iguais e, normalmente, diferente para objetos diferentes")
        void hashCodeRespectsEqualsContract() {
            Position same = new Position(2, 3);
            Position diff = new Position(1, 1);

            assertEquals(pos1, same);
            assertEquals(pos1.hashCode(), same.hashCode());

            // 不强制不同对象 hash 不同，但通常会不同
            assertNotEquals(pos1, diff);
        }
    }

    // ---------------- isAdjacentTo：所有 true / false 情况 ----------------
    @Nested
    @DisplayName("Relações de adjacência")
    class Adjacency {

        @Test
        @DisplayName("Uma posição é adjacente a si própria (diferença 0,0)")
        void adjacentToItself() {
            assertTrue(pos1.isAdjacentTo(pos1));
        }

        @Test
        @DisplayName("Diferença máxima 1 em row e column continua adjacente (diagonal)")
        void diagonalAdjacentWithinOne() {
            Position diag = new Position(3, 4); // row +1, col +1
            assertTrue(pos1.isAdjacentTo(diag));
            assertTrue(diag.isAdjacentTo(pos1));
        }

        @Test
        @DisplayName("Diferença em row maior que 1 torna não adjacente (primeira condição do && é falsa)")
        void notAdjacentRowTooFar() {
            Position farRow = new Position(5, 3); // row 差 3, col 相同
            assertFalse(pos1.isAdjacentTo(farRow));  // abs(row diff) > 1，第一部分 false，&& 短路
        }

        @Test
        @DisplayName("Diferença em column maior que 1 torna não adjacente (segunda condição do && é falsa)")
        void notAdjacentColumnTooFar() {
            Position farCol = new Position(2, 10); // row 相同, column 差 7
            assertFalse(pos1.isAdjacentTo(farCol));  // 第一部分 true, 第二部分 false
        }

        @Test
        @DisplayName("Posições longe em row e column não são adjacentes")
        void notAdjacentRowAndColumnTooFar() {
            Position far = new Position(10, 10);
            assertFalse(pos1.isAdjacentTo(far));
        }
    }
}
