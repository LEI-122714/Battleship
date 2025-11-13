package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
@DisplayName("Testes da classe Fleet")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FleetTest {

    private Fleet fleet;

    @BeforeEach
    void createFleet() {
        fleet = new Fleet();
    }

    @Test
    @DisplayName("A frota começa vazia: lista de ships vazia e nao nula")
    void testFleetStartsEmpty() {
        assertNotNull(fleet.getShips());
        assertTrue(fleet.getShips().isEmpty());
    }

    @Test
    @DisplayName("Adicionar um navio Barge à frota funciona corretamente")
    void testAddBarge() {
        Position pos = new Position(1, 1);
        Barge barge = new Barge(Compass.NORTH, pos);

        boolean result = fleet.addShip(barge);

        assertTrue(result);
        assertEquals(1, fleet.getShips().size());
        assertEquals(barge, fleet.getShips().get(0));
    }

    @Test
    @DisplayName("Adicionar dois navios proximos falha porque ha risco de colisao")
    void testAddCollisionFails() {
        Position pos1 = new Position(2, 2);
        Position pos2 = new Position(2, 3);
        //barge1 e barge2 estao lado a lado
        Barge barge1 = new Barge(Compass.NORTH, pos1);
        Barge barge2 = new Barge(Compass.NORTH, pos2);

        assertTrue(fleet.addShip(barge1));   //funciona
        assertFalse(fleet.addShip(barge2));  //risco de colisao entao falha
        assertEquals(1, fleet.getShips().size());
    }

    @Test
    @DisplayName("getShipsLike devolve apenas os navios da categoria dada")
    void testGetShipsLike() {
        Barge barge = new Barge(Compass.NORTH, new Position(0, 0));
        Caravel caravel = new Caravel(Compass.EAST, new Position(5, 5));

        fleet.addShip(barge);
        fleet.addShip(caravel);

        List<IShip> barges = fleet.getShipsLike("Barca");
        assertEquals(1, barges.size());
        assertEquals("Barca", barges.get(0).getCategory());
    }

    @Test
    @DisplayName("getFloatingShips devolve apenas os navios que flutuam")
    void testGetFloatingShips() {
        Barge barge = new Barge(Compass.NORTH, new Position(0, 0));
        fleet.addShip(barge);

        //ainda esta a flutuar (nao foi atingido)
        assertEquals(1, fleet.getFloatingShips().size());

        //atingido
        barge.shoot(new Position(0, 0));

        //nao deve estar a flutuar (deixa de aparecer na lista de floating ships)
        assertEquals(0, fleet.getFloatingShips().size());
    }

    @Test
    @DisplayName("shipAt devolve o navio correto que se encontra na posicao dada")
    void testShipAt() {
        Barge barge = new Barge(Compass.NORTH, new Position(3, 3));
        fleet.addShip(barge);

        IShip ship = fleet.shipAt(new Position(3, 3));
        assertNotNull(ship);
        assertEquals(barge, ship);
    }

    @Test
    @DisplayName("shipAt devolve null quando nao ha nada na posicao")
    void testShipAtReturnsNull() {
        Barge barge = new Barge(Compass.NORTH, new Position(2, 2));
        fleet.addShip(barge);

        IShip ship = fleet.shipAt(new Position(9, 9));
        assertNull(ship);
    }


    @Test
    @DisplayName("Não é possível adicionar navios fora do tabuleiro")
    void testAddShipOutsideBoardFails() {
        Position pos = new Position(15, 15);
        Barge barge = new Barge(Compass.NORTH, pos);

        boolean result = fleet.addShip(barge);
        assertFalse(result);
    }
}