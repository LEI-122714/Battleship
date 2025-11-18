package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da logica do jogo")
class GameTest {

    private Fleet fleet;
    private Game game;
    private final int BOARD_SIZE = Fleet.BOARD_SIZE;

    // inicializa frota e jogo
    @BeforeEach
    void setUp() {
        fleet = new Fleet();
        game = new Game(fleet);
    }

    @Nested
    @DisplayName("Testes do construtor e getters")
    class ConstructorAndGetterTests {

        @Test
        @DisplayName("Inicializa o jogo e os contadores corretamente")
        void testGameInitialization() {
            assertTrue(game.getShots().isEmpty());
            assertEquals(0, game.getInvalidShots());
            assertEquals(0, game.getRepeatedShots());
            assertEquals(0, game.getHits());
            assertEquals(0, game.getSunkShips());
        }

        @Test
        @DisplayName("Devolve o nr correto de navios restantes")
        void testGetRemainingShips() {

            IPosition pos = new Position(0, 0);
            Barge barge = new Barge(Compass.NORTH, pos);
            fleet.addShip(barge);

            assertEquals(1, game.getRemainingShips());


            game.fire(pos);

            assertEquals(0, game.getRemainingShips());
        }
    }


    @Nested
    @DisplayName("Testes do metodo fire")
    class FireMethodTests {

        @Test
        @DisplayName("Conta como tiro invalido se foi fora do limite do tabuleiro")
        void testFire_InvalidShot() {

            IPosition invalidPos = new Position(BOARD_SIZE+5, 5); //ex linha 15 invalida

            IShip result = game.fire(invalidPos);

            assertNull(result);
            assertEquals(1, game.getInvalidShots());
            assertTrue(game.getShots().isEmpty());
        }

        @Test
        @DisplayName("Deve contar como tiro repetido")
        void testFire_RepeatedShot() {
            IPosition pos = new Position(1, 1);


            game.fire(pos);


            IShip result = game.fire(pos);

            assertNull(result);
            assertEquals(0, game.getInvalidShots());
            assertEquals(1, game.getRepeatedShots());
            assertEquals(1, game.getShots().size());
        }

        @Test
        @DisplayName("Deve registar um miss e adicionar a lista de tiros")
        void testFire_Miss() {
            IPosition pos = new Position(2, 2);

            IShip result = game.fire(pos);

            assertNull(result);
            assertEquals(0, game.getHits());
            assertEquals(1, game.getShots().size());
        }

        @Test
        @DisplayName("Deve registar um hit (acerto) sem afundar (Caravel tamanho 2)")
        void testFire_Hit_NotSunk() {
            IPosition pos1 = new Position(3, 3);
            IPosition pos2 = new Position(3, 4);


            Caravel caravel = new Caravel(Compass.EAST, pos1);
            fleet.addShip(caravel);


            IShip result = game.fire(pos1);

            assertNull(result);
            assertEquals(1, game.getHits());
            assertEquals(0, game.getSunkShips());
            assertTrue(caravel.getPositions().get(0).isHit());
            assertTrue(caravel.stillFloating());
        }

        @Test
        @DisplayName("Regista um hit e afundar o navio (Barge tamanho 1)")
        void testFire_Hit_AndSunk() {
            IPosition pos = new Position(4, 4);
            Barge barge = new Barge(Compass.NORTH, pos);
            fleet.addShip(barge);


            IShip result = game.fire(pos);

            assertEquals(barge, result);
            assertEquals(1, game.getHits());
            assertEquals(1, game.getSunkShips());
            assertFalse(barge.stillFloating());
        }

        @Test
        @DisplayName("Deve afundar um navio de tamanho 2 apos dois hits")
        void testFire_Hit_ThenSunk_Caravel() {
            IPosition pos1 = new Position(5, 5);
            IPosition pos2 = new Position(5, 6);
            Caravel caravel = new Caravel(Compass.EAST, pos1);
            fleet.addShip(caravel);


            IShip result1 = game.fire(pos1);


            assertNull(result1);
            assertEquals(1, game.getHits());
            assertEquals(0, game.getSunkShips());


            IShip result2 = game.fire(pos2);

            assertEquals(caravel, result2);
            assertEquals(2, game.getHits());
            assertEquals(1, game.getSunkShips());
            assertFalse(caravel.stillFloating());
        }

    }






}