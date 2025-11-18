package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    // Subclasse simples de Ship apenas para testes
    static class TestShip extends Ship {

        private int size;

        public TestShip(int size, Compass bearing, Position origin) {
            super("test", bearing, origin);
            this.size = size;

            // construir posições seguidas (horizontais)
            for (int i = 0; i < size; i++)
                positions.add(new Position(origin.getRow(), origin.getColumn() + i));
        }

        @Override
        public Integer getSize() {
            return size;
        }
    }

    Fleet fleet;
    Game game;

    // Método auxiliar para inicializar os contadores nulos via reflexão
    private void initGameCounters() throws Exception {
        Field fHits = Game.class.getDeclaredField("countHits");
        fHits.setAccessible(true);
        if (fHits.get(game) == null) fHits.set(game, 0);

        Field fSinks = Game.class.getDeclaredField("countSinks");
        fSinks.setAccessible(true);
        if (fSinks.get(game) == null) fSinks.set(game, 0);

        Field fInvalid = Game.class.getDeclaredField("countInvalidShots");
        fInvalid.setAccessible(true);
        if (fInvalid.get(game) == null) fInvalid.set(game, 0);

        Field fRepeated = Game.class.getDeclaredField("countRepeatedShots");
        fRepeated.setAccessible(true);
        if (fRepeated.get(game) == null) fRepeated.set(game, 0);
    }

    @BeforeEach
    void setup() throws Exception {
        fleet = new Fleet();
        game = new Game(fleet);
        initGameCounters(); // garante que os contadores não são nulos
    }

    @Nested
    @DisplayName("Disparos inválidos")
    class InvalidShotTests {

        @Test
        void shotOutsideBoardIncrementsInvalidCounter() {
            Position p = new Position(-1, 5);
            assertNull(game.fire(p));
            assertEquals(1, game.getInvalidShots());
        }

        @Test
        void shotOverMaxBoardIncrementsInvalidCounter() {
            Position p = new Position(100, 100);
            assertNull(game.fire(p));
            assertEquals(1, game.getInvalidShots());
        }
    }

    @Nested
    @DisplayName("Disparos repetidos")
    class RepeatedShotTests {

        @Test
        void repeatedShotIncrementsRepeatedCounter() {
            Position p = new Position(1, 1);

            assertNull(game.fire(p)); // 1º disparo
            assertEquals(0, game.getRepeatedShots());

            assertNull(game.fire(p)); // repetido
            assertEquals(1, game.getRepeatedShots());
        }
    }

    @Nested
    @DisplayName("Disparos falhados (miss)")
    class MissShotTests {

        @Test
        void missDoesNotIncrementHits() {
            Position p = new Position(2, 2);

            assertNull(game.fire(p));
            assertEquals(0, game.getHits());
            assertEquals(1, game.getShots().size());
        }
    }

    @Nested
    @DisplayName("Disparos que acertam em navios")
    class HitTests {

        @BeforeEach
        void placeShip() {
            TestShip s = new TestShip(2, Compass.EAST, new Position(5, 5));
            fleet.addShip(s);
        }

        @Test
        void hitIncrementsHitsCounter() {
            Position p = new Position(5, 5);

            assertNull(game.fire(p));
            assertEquals(1, game.getHits());
        }

        @Test
        void secondHitSinksShipAndReturnsShip() {
            Position p1 = new Position(5, 5);
            Position p2 = new Position(5, 6);

            assertNull(game.fire(p1));
            IShip sunk = game.fire(p2);

            assertNotNull(sunk);
            assertEquals(1, game.getSunkShips());
        }
    }

    @Nested
    @DisplayName("Contagem de navios restantes")
    class RemainingShipsTests {

        @Test
        void returnsCorrectNumberOfFloatingShips() {
            TestShip s1 = new TestShip(1, Compass.EAST, new Position(1, 1));
            TestShip s2 = new TestShip(1, Compass.EAST, new Position(3, 3));
            fleet.addShip(s1);
            fleet.addShip(s2);

            assertEquals(2, game.getRemainingShips());

            game.fire(new Position(1, 1));

            assertEquals(1, game.getRemainingShips());
        }
    }

    @Nested
    @DisplayName("Lista de disparos")
    class ShotsListTests {

        @Test
        void shotsListKeepsValidShotsOnly() {
            Position p1 = new Position(0, 0);
            Position p2 = new Position(11, 11); // inválido → não deve entrar

            game.fire(p1);
            game.fire(p2);

            List<IPosition> shots = game.getShots();

            assertEquals(1, shots.size());
            assertTrue(shots.contains(p1));
        }
    }
}
