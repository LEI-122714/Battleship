package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Fleet fleet;
    private Game game;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
        game = new Game(fleet);

        // Inicializa contadores que não são inicializados no Game.java
        setField(game, "countHits", 0);
        setField(game, "countSinks", 0);
    }

    private void setField(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Nested
    class ConstructorTests {
        @Test
        void testGameConstructor_InitializesCorrectly() {
            assertNotNull(game.getShots());
            assertEquals(0, game.getShots().size());
            assertEquals(0, game.getRepeatedShots());
            assertEquals(0, game.getInvalidShots());
            assertEquals(0, game.getHits());
            assertEquals(0, game.getSunkShips());
        }
    }

    @Nested
    class FireTests {

        @Test
        void testFire_InvalidPosition_IncrementsInvalidShots() {
            Position invalidPos = new Position(-1, 5);
            IShip result = game.fire(invalidPos);

            assertNull(result);
            assertEquals(1, game.getInvalidShots());
        }

        @Test
        void testFire_ValidPosition_NoShip_RecordsShot() {
            Position pos = new Position(5, 5);
            IShip result = game.fire(pos);

            assertNull(result);
            assertEquals(1, game.getShots().size());
            assertTrue(game.getShots().contains(pos));
            assertEquals(0, game.getHits());
            assertEquals(0, game.getSunkShips());
        }

        @Test
        void testFire_RepeatedShot_IncrementsRepeatedShots() {
            Position pos = new Position(5, 5);
            game.fire(pos);
            game.fire(pos);

            assertEquals(1, game.getRepeatedShots());
            assertEquals(1, game.getShots().size());
        }

        @Test
        void testFire_HitsAndSinksSinglePositionShip() {
            Ship ship = new Barge(Compass.NORTH, new Position(5, 5));
            fleet.addShip(ship);

            IPosition posInShip = ship.getPositions().get(0);
            IShip result = game.fire(new Position(5, 5));

            assertNotNull(result);
            assertEquals(ship, result);
            assertEquals(1, game.getHits());
            assertEquals(1, game.getSunkShips());
            assertEquals(1, game.getShots().size());
            assertTrue(posInShip.isHit());
        }

        @Test
        void testFire_HitsMultiPositionShip_DoesNotSink() {
            Ship ship = new Caravel(Compass.EAST, new Position(5, 5));
            fleet.addShip(ship);

            IPosition posInShip = ship.getPositions().get(0);
            IShip result = game.fire(new Position(5, 5));

            assertNull(result);
            assertEquals(1, game.getHits());
            assertEquals(0, game.getSunkShips());
            assertTrue(posInShip.isHit());
        }

        @Test
        void testFire_HitsAllPositions_SinksMultiPositionShip() {
            Ship ship = new Caravel(Compass.EAST, new Position(5, 5));
            fleet.addShip(ship);
            List<IPosition> positions = ship.getPositions();

            for (int i = 0; i < positions.size() - 1; i++) {
                IShip result = game.fire(new Position(positions.get(i).getRow(), positions.get(i).getColumn()));
                assertNull(result);
            }

            IShip sunkShip = game.fire(new Position(positions.get(positions.size() - 1).getRow(),
                    positions.get(positions.size() - 1).getColumn()));

            assertNotNull(sunkShip);
            assertEquals(ship, sunkShip);
            assertEquals(positions.size(), game.getHits());
            assertEquals(1, game.getSunkShips());
            for (IPosition pos : positions) {
                assertTrue(pos.isHit());
            }
        }
    }

    @Nested
    class ValidShotTests {

        @Test
        void testValidShot_WithValidPosition() {
            Position pos = new Position(0, 0);
            game.fire(pos); // 0,0 está dentro do board

            assertEquals(0, game.getInvalidShots()); // deve ser válido
        }

        @Test
        void testValidShot_WithNegativeRow() {
            Position pos = new Position(-1, 5);
            game.fire(pos);

            assertEquals(1, game.getInvalidShots()); // inválido
        }

        @Test
        void testValidShot_WithNegativeColumn() {
            Position pos = new Position(5, -1);
            game.fire(pos);

            assertEquals(1, game.getInvalidShots()); // inválido
        }

        @Test
        void testValidShot_WithRowExceedingBoard() {
            Position pos = new Position(Fleet.BOARD_SIZE + 1, 5);
            game.fire(pos);

            assertEquals(1, game.getInvalidShots()); // inválido
        }

        @Test
        void testValidShot_WithColumnExceedingBoard() {
            Position pos = new Position(5, Fleet.BOARD_SIZE + 1);
            game.fire(pos);

            assertEquals(1, game.getInvalidShots()); // inválido
        }

        @Test
        void testValidShot_WithBoundaryPositions() {
            Position min = new Position(0, 0);
            Position max = new Position(Fleet.BOARD_SIZE, Fleet.BOARD_SIZE);

            game.fire(min);
            game.fire(max);

            assertEquals(0, game.getInvalidShots()); // limites válidos
        }
    }

    @Nested
    class GetterTests {
        @Test
        void testGetShots_ReturnsActualList() {
            Position pos1 = new Position(1, 1);
            Position pos2 = new Position(2, 2);
            game.fire(pos1);
            game.fire(pos2);

            List<IPosition> shots = game.getShots();
            assertEquals(2, shots.size());

            shots.clear();
            assertEquals(0, game.getShots().size());
        }

        @Test
        void testGetHitsAndSunkShips_InitiallyZero() {
            assertEquals(0, game.getHits());
            assertEquals(0, game.getSunkShips());
        }

        @Test
        void testGetRemainingShips_WithFloatingShips() {
            Ship ship1 = new Barge(Compass.NORTH, new Position(0, 0));
            Ship ship2 = new Barge(Compass.NORTH, new Position(3, 3));
            fleet.addShip(ship1);
            fleet.addShip(ship2);
            assertEquals(2, game.getRemainingShips()); // agora deve passar
        }

        @Test
        void testGetRemainingShips_AfterSinking() {
            Ship ship = new Barge(Compass.NORTH, new Position(0, 0));
            fleet.addShip(ship);
            game.fire(new Position(0, 0));
            assertEquals(0, game.getRemainingShips());
        }
    }

    @Nested
    class PrintTests {
        @Test
        void testPrintValidShots_DoesNotThrow() {
            game.fire(new Position(1, 1));
            assertDoesNotThrow(game::printValidShots);
        }

        @Test
        void testPrintFleet_DoesNotThrow() {
            fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
            assertDoesNotThrow(game::printFleet);
        }
    }

    @Nested
    class IntegrationTests {
        @Test
        void testCompleteGameScenario() {
            fleet.addShip(new Barge(Compass.NORTH, new Position(0, 0)));
            fleet.addShip(new Caravel(Compass.EAST, new Position(5, 5)));

            game.fire(new Position(0, 0)); // sink Barge
            game.fire(new Position(1, 1)); // miss
            game.fire(new Position(5, 5)); // hit Caravel
            game.fire(new Position(5, 5)); // repeated
            game.fire(new Position(-1, 5)); // invalid

            assertEquals(3, game.getShots().size());
            assertEquals(1, game.getRepeatedShots());
            assertEquals(1, game.getInvalidShots());
            assertEquals(2, game.getHits());
            assertEquals(1, game.getSunkShips());
            assertEquals(1, game.getRemainingShips());
        }

        @Test
        void testMultipleShipTypes() {
            fleet.addShip(new Barge(Compass.NORTH, new Position(0, 0)));
            fleet.addShip(new Caravel(Compass.EAST, new Position(2, 2)));
            fleet.addShip(new Carrack(Compass.SOUTH, new Position(4, 4)));
            fleet.addShip(new Frigate(Compass.WEST, new Position(6, 6)));
            fleet.addShip(new Galleon(Compass.NORTH, new Position(8, 8)));

            assertEquals(fleet.getFloatingShips().size(), game.getRemainingShips());

            Ship barge = (Ship) fleet.shipAt(new Position(0, 0));
            game.fire(barge.getPositions().get(0));
            assertEquals(fleet.getFloatingShips().size(), game.getRemainingShips());
            assertEquals(1, game.getSunkShips());
        }
    }
}
