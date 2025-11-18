package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Game - global coverage tests")
public class GameTest {

    private Fleet fleet;
    private Game game;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
        game = new Game(fleet);
    }

    @Nested
    @DisplayName("fire() behaviour")
    class FireTests {

        @Test
        @DisplayName("Invalid shot (negative row/col) increases invalid counter and does nothing else")
        void invalidShotNegativeCoordinates() {
            IPosition outside = new Position(-1, -1);

            IShip result = game.fire(outside);

            assertNull(result);
            assertEquals(1, game.getInvalidShots());
            assertTrue(game.getShots().isEmpty());
            assertEquals(0, game.getRepeatedShots());
        }

        @Test
        @DisplayName("Invalid shot with row > BOARD_SIZE is also counted as invalid")
        void invalidShotRowAboveBoard() {
            IPosition outside = new Position(Fleet.BOARD_SIZE + 1, 0);

            IShip result = game.fire(outside);

            assertNull(result);
            assertEquals(1, game.getInvalidShots());
        }

        @Test
        @DisplayName("Invalid shot with negative column is also counted as invalid")
        void invalidShotNegativeColumn() {
            IPosition outside = new Position(0, -1);

            IShip result = game.fire(outside);

            assertNull(result);
            assertEquals(1, game.getInvalidShots());
        }

        @Test
        @DisplayName("Invalid shot with column > BOARD_SIZE is also counted as invalid")
        void invalidShotColumnAboveBoard() {
            IPosition outside = new Position(0, Fleet.BOARD_SIZE + 1);

            IShip result = game.fire(outside);

            assertNull(result);
            assertEquals(1, game.getInvalidShots());
        }

        @Test
        @DisplayName("First valid shot on empty board is stored as a normal shot")
        void firstValidShotOnEmptyBoard() {
            IPosition p = new Position(0, 0);

            IShip result = game.fire(p);

            assertNull(result);
            assertEquals(0, game.getInvalidShots());
            assertEquals(0, game.getRepeatedShots());
            List<IPosition> shots = game.getShots();
            assertEquals(1, shots.size());
            assertEquals(p, shots.get(0));
        }

        @Test
        @DisplayName("Second valid shot at different position is not counted as repeated")
        void secondDifferentShotIsNotRepeated() {
            IPosition first = new Position(0, 0);
            IPosition second = new Position(0, 1);

            game.fire(first);
            game.fire(second);

            assertEquals(0, game.getInvalidShots());
            assertEquals(0, game.getRepeatedShots());
            assertEquals(2, game.getShots().size());
        }

        @Test
        @DisplayName("Second shot to the same valid position is counted as repeated")
        void repeatedValidShotIncrementsRepeatedCounter() {
            IPosition p = new Position(0, 0);

            game.fire(p);
            IShip result = game.fire(p);

            assertNull(result);
            assertEquals(0, game.getInvalidShots());
            assertEquals(1, game.getRepeatedShots());
            assertEquals(1, game.getShots().size());
        }

        @Test
        @DisplayName("Hit on a ship increases hits but does not sink the ship")
        void hitShipButNotSunk() {

            IShip caravel = new Caravel(Compass.SOUTH, new Position(0, 0));
            assertTrue(fleet.addShip(caravel));

            IShip result = game.fire(new Position(0, 0));

            assertNull(result);
            assertEquals(1, game.getHits());
            assertEquals(0, game.getSunkShips());
        }

        @Test
        @DisplayName("Last hit on a ship sinks it and returns the ship")
        void hitAndSinkShip() {
            IShip caravel = new Caravel(Compass.SOUTH, new Position(0, 0));
            assertTrue(fleet.addShip(caravel));

            game.fire(new Position(0, 0));

            IShip result = game.fire(new Position(1, 0));

            assertNotNull(result);
            assertEquals(caravel, result);
            assertEquals(2, game.getHits());
            assertEquals(1, game.getSunkShips());
        }
    }

    @Nested
    @DisplayName("Statistics and board printing")
    class StatsAndPrintingTests {

        @Test
        @DisplayName("getRemainingShips returns number of floating ships")
        void remainingShips() {
            assertEquals(0, game.getRemainingShips());

            fleet.addShip(new Caravel(Compass.SOUTH, new Position(0, 0)));
            assertEquals(1, game.getRemainingShips());
        }

        @Test
        @DisplayName("print methods do not throw exceptions on empty game")
        void printMethodsDoNotThrowOnEmptyState() {
            game.printValidShots();
            game.printFleet();
        }

        @Test
        @DisplayName("print methods do not throw when there are shots and ships")
        void printMethodsDoNotThrowWithData() {
            fleet.addShip(new Caravel(Compass.SOUTH, new Position(0, 0)));
            game.fire(new Position(5, 5));

            game.printValidShots();
            game.printFleet();
        }
    }
}
