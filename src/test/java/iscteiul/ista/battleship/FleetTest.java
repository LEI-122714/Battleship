package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static iscteiul.ista.battleship.IFleet.BOARD_SIZE;
import static iscteiul.ista.battleship.IFleet.FLEET_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de unidade da Fleet (foco em branch coverage)")
class FleetTest {

    private Fleet fleet;
    private IShip frigate;
    private IShip galleon;
    private IShip barge;

    @BeforeEach
    void setUp() {
        fleet   = new Fleet();
        frigate = new Frigate(Compass.SOUTH, new Position(0, 0));
        galleon = new Galleon(Compass.EAST,  new Position(6, 6));
        barge   = new Barge(Compass.NORTH,   new Position(3, 3));
    }

    @Test
    @DisplayName("addShip: aceita navios válidos, rejeita colisão e limite de frota")
    void addShip_acceptsAndRejectsCorrectly() {
        assertTrue(fleet.addShip(frigate), "Fragata deve ser adicionada");
        assertTrue(fleet.addShip(galleon), "Galeão deve ser adicionado");

        IShip colliding = new Frigate(Compass.SOUTH, new Position(0, 1)); // encostado à fragata
        assertFalse(fleet.addShip(colliding), "Navio em colisão não deve ser adicionado");
        assertEquals(2, fleet.getShips().size());

        Fleet filled = new Fleet();
        int added = 0;

        for (int r = 0; r < BOARD_SIZE && added < FLEET_SIZE + 1; r += 2) {
            for (int c = 0; c < BOARD_SIZE && added < FLEET_SIZE + 1; c += 2) {
                Barge b = new Barge(Compass.NORTH, new Position(r, c));
                if (filled.addShip(b)) {
                    added++;
                }
            }
        }

        assertTrue(added >= FLEET_SIZE + 1,
                "Deve ser possível adicionar pelo menos FLEET_SIZE+1 barcas sem colisão");

        int sizeBefore = filled.getShips().size();
        assertTrue(sizeBefore >= FLEET_SIZE + 1,
                "Depois de encher, o tamanho da frota deve ser maior do que FLEET_SIZE");

        Barge extra = new Barge(Compass.NORTH, new Position(1, BOARD_SIZE - 1));
        boolean result = filled.addShip(extra);
        assertFalse(result, "Depois de exceder o limite, nenhum navio deve ser aceite");
    }

    @Test
    @DisplayName("addShip: rejeita navios completamente fora do tabuleiro em qualquer lado")
    void addShip_rejectsShipsOutsideBoardEdges() {
        Fleet f = new Fleet();

        IShip left = new Frigate(Compass.EAST, new Position(0, -1));
        assertFalse(f.addShip(left));

        IShip right = new Frigate(Compass.WEST, new Position(0, BOARD_SIZE));
        assertFalse(f.addShip(right));

        IShip above = new Frigate(Compass.SOUTH, new Position(-1, 0));
        assertFalse(f.addShip(above));

        IShip below = new Frigate(Compass.NORTH, new Position(BOARD_SIZE, 0));
        assertFalse(f.addShip(below));

        IShip inside = new Frigate(Compass.SOUTH, new Position(2, 2));
        assertTrue(f.addShip(inside));
        assertEquals(1, f.getShips().size());
    }

    @Test
    @DisplayName("getShipsLike: devolve só as categorias pedidas e lida com categoria inexistente")
    void getShipsLike_handlesMatchesAndNoMatches() {
        fleet.addShip(frigate);
        fleet.addShip(galleon);
        fleet.addShip(barge);

        List<IShip> frigates = fleet.getShipsLike("Fragata");
        assertEquals(1, frigates.size());
        assertTrue(frigates.contains(frigate));

        List<IShip> galleons = fleet.getShipsLike("Galeao");
        assertEquals(1, galleons.size());
        assertTrue(galleons.contains(galleon));

        List<IShip> submarines = fleet.getShipsLike("Submarino");
        assertNotNull(submarines);
        assertTrue(submarines.isEmpty());
    }

    @Test
    @DisplayName("getFloatingShips: ignora navios afundados e devolve só os que flutuam")
    void getFloatingShips_onlyReturnsShipsStillFloating() {
        fleet.addShip(frigate);
        fleet.addShip(barge);

        IPosition bargePos = barge.getPositions().get(0);
        bargePos.shoot();

        IPosition frigatePos = frigate.getPositions().get(0);
        frigatePos.shoot();

        List<IShip> floating = fleet.getFloatingShips();

        assertTrue(floating.contains(frigate), "Fragata ainda deve estar a flutuar");
        assertFalse(floating.contains(barge), "Barca afundada não deve aparecer na lista");
    }

    @Test
    @DisplayName("shipAt: lida com frota vazia, posição ocupada e posição vazia")
    void shipAt_coversAllBranches() {

        assertNull(fleet.shipAt(new Position(9, 9)));

        fleet.addShip(frigate);
        fleet.addShip(galleon);

        IPosition posFrigate = frigate.getPositions().get(0);
        assertEquals(frigate, fleet.shipAt(posFrigate));

        IPosition empty = new Position(BOARD_SIZE - 1, BOARD_SIZE - 1);
        assertNull(fleet.shipAt(empty));
    }

    @Test
    @DisplayName("Métodos de impressão funcionam com e sem navios e validam categoria nula")
    void printingMethods_coverBranches() {
        assertDoesNotThrow(() -> {
            fleet.printStatus();
            fleet.printShipsByCategory("Barca");
        });

        fleet.addShip(frigate);
        fleet.addShip(galleon);
        fleet.addShip(barge);

        assertDoesNotThrow(() -> {
            fleet.printStatus();
            fleet.printAllShips();
            fleet.printFloatingShips();
            fleet.printShipsByCategory("Fragata");
            fleet.printShipsByCategory("Galeao");
            fleet.printShipsByCategory("Barca");
        });

        assertThrows(AssertionError.class, () -> fleet.printShipsByCategory(null));
    }
}
