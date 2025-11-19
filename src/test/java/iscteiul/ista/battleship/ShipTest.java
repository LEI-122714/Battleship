package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Ship e subclasses (cobertura de branch)")
class ShipTest {

    /**
     * Pequeno navio fictício apenas para testes dos métodos genéricos de Ship.
     * Permite controlar exactamente as posições para testar limites e adjacência.
     */
    private static class TestShip extends Ship {

        private final int size;

        TestShip(List<IPosition> positions) {
            super("Teste", Compass.NORTH, positions.get(0));
            this.positions.addAll(positions);
            this.size = positions.size();
        }

        @Override
        public Integer getSize() {
            return size;
        }
    }

    @Test
    @DisplayName("buildShip cria o tipo correcto ou null para tipo inválido")
    void testBuildShipFactoryBranches() {
        Ship barca   = Ship.buildShip("barca",    Compass.NORTH, new Position(0, 0));
        Ship caravela= Ship.buildShip("caravela", Compass.EAST,  new Position(1, 1));
        Ship nau     = Ship.buildShip("nau",      Compass.SOUTH, new Position(2, 2));
        Ship fragata = Ship.buildShip("fragata",  Compass.WEST,  new Position(3, 3));
        Ship galeao  = Ship.buildShip("galeao",   Compass.NORTH, new Position(4, 4));
        Ship invalido= Ship.buildShip("submarino",Compass.NORTH, new Position(5, 5));

        assertAll(
                () -> assertTrue(barca   instanceof Barge),
                () -> assertTrue(caravela instanceof Caravel),
                () -> assertTrue(nau     instanceof Carrack),
                () -> assertTrue(fragata instanceof Frigate),
                () -> assertTrue(galeao  instanceof Galleon),
                () -> assertNull(invalido, "Tipo inválido deve devolver null")
        );
    }

    @Test
    @DisplayName("Construtor de Ship (via Barge) aceita argumentos válidos")
    void testConstructorValidArguments() {
        Ship s = new Barge(Compass.NORTH, new Position(0, 0));
        assertEquals("Barca", s.getCategory());
        assertEquals(Compass.NORTH, s.getBearing());
        assertEquals(new Position(0, 0), s.getPosition());
        assertEquals(1, s.getPositions().size());
    }

    @Test
    @DisplayName("Construtor lança AssertionError com bearing nulo")
    void testConstructorNullBearing() {
        assertThrows(AssertionError.class,
                () -> new Barge(null, new Position(0, 0)));
    }

    @Test
    @DisplayName("Construtor lança AssertionError com posição nula")
    void testConstructorNullPosition() {
        assertThrows(AssertionError.class,
                () -> new Barge(Compass.NORTH, null));
    }

    @Test
    @DisplayName("stillFloating funciona em navio de uma só posição")
    void testStillFloatingSingleCellShip() {
        Ship barca = new Barge(Compass.NORTH, new Position(2, 2));

        assertTrue(barca.stillFloating());

        barca.shoot(new Position(2, 2));
        assertFalse(barca.stillFloating());
    }

    @Test
    @DisplayName("stillFloating em navio maior: algumas posições atingidas vs todas")
    void testStillFloatingMultiCellShip() {
        Ship fragata = new Frigate(Compass.NORTH, new Position(0, 0));

        assertTrue(fragata.stillFloating());

        for (int i = 0; i < fragata.getSize() - 1; i++) {
            fragata.getPositions().get(i).shoot();
        }
        assertTrue(fragata.stillFloating());

        fragata.getPositions().get(fragata.getSize() - 1).shoot();
        assertFalse(fragata.stillFloating());
    }

    @Test
    @DisplayName("Métodos de limites usam correctamente todas as posições")
    void testBoundaryMethodsCustomShip() {

        TestShip ship = new TestShip(Arrays.asList(
                new Position(5, 5),
                new Position(3, 6),
                new Position(7, 4)
        ));

        assertAll(
                () -> assertEquals(3, ship.getTopMostPos()),
                () -> assertEquals(7, ship.getBottomMostPos()),
                () -> assertEquals(4, ship.getLeftMostPos()),
                () -> assertEquals(6, ship.getRightMostPos())
        );
    }

    @Test
    @DisplayName("Limites funcionam também para navio de uma posição")
    void testBoundaryMethodsSingleCell() {
        Ship barca = new Barge(Compass.NORTH, new Position(8, 1));

        assertAll(
                () -> assertEquals(8, barca.getTopMostPos()),
                () -> assertEquals(8, barca.getBottomMostPos()),
                () -> assertEquals(1, barca.getLeftMostPos()),
                () -> assertEquals(1, barca.getRightMostPos())
        );
    }

    @Test
    @DisplayName("occupies devolve true quando posição pertence ao navio")
    void testOccupiesTrue() {
        Ship nau = new Carrack(Compass.NORTH, new Position(0, 0));
        IPosition first = nau.getPositions().get(0);

        assertTrue(nau.occupies(first));
    }

    @Test
    @DisplayName("occupies devolve false quando posição não pertence ao navio")
    void testOccupiesFalse() {
        Ship nau = new Carrack(Compass.NORTH, new Position(0, 0));
        IPosition far = new Position(10, 10);

        assertFalse(nau.occupies(far));
    }

    @Test
    @DisplayName("occupies lança AssertionError se posição for null")
    void testOccupiesNullPosition() {
        Ship nau = new Carrack(Compass.NORTH, new Position(0, 0));
        assertThrows(AssertionError.class, () -> nau.occupies(null));
    }

    @Test
    @DisplayName("tooCloseTo(IPosition) true quando há posição adjacente")
    void testTooCloseToPositionTrue() {
        TestShip ship = new TestShip(Arrays.asList(
                new Position(5, 5),
                new Position(3, 6),
                new Position(7, 4)
        ));

        IPosition near = new Position(6, 5); // adjacente a (5,5)
        assertTrue(ship.tooCloseTo(near));
    }

    @Test
    @DisplayName("tooCloseTo(IPosition) false quando não há posição adjacente")
    void testTooCloseToPositionFalse() {
        TestShip ship = new TestShip(Arrays.asList(
                new Position(5, 5),
                new Position(3, 6),
                new Position(7, 4)
        ));

        IPosition far = new Position(20, 20);
        assertFalse(ship.tooCloseTo(far));
    }

    @Test
    @DisplayName("tooCloseTo(IShip) true quando algum ponto do outro navio é adjacente")
    void testTooCloseToShipTrue() {
        TestShip ship1 = new TestShip(Arrays.asList(
                new Position(5, 5),
                new Position(3, 6)
        ));
        TestShip ship2 = new TestShip(Arrays.asList(
                new Position(6, 5)  // adjacente a (5,5)
        ));

        assertTrue(ship1.tooCloseTo(ship2));
    }

    @Test
    @DisplayName("tooCloseTo(IShip) false quando navios estão longe")
    void testTooCloseToShipFalse() {
        TestShip ship1 = new TestShip(Arrays.asList(
                new Position(0, 0)
        ));
        TestShip ship2 = new TestShip(Arrays.asList(
                new Position(10, 10)
        ));

        assertFalse(ship1.tooCloseTo(ship2));
    }

    @Test
    @DisplayName("tooCloseTo(IShip) lança AssertionError se navio for null")
    void testTooCloseToShipNull() {
        Ship ship = new Barge(Compass.NORTH, new Position(0, 0));
        assertThrows(AssertionError.class, () -> ship.tooCloseTo((IShip) null));
    }

    @Test
    @DisplayName("shoot acerta posição correcta e afeta o estado")
    void testShootHit() {
        Ship barca = new Barge(Compass.NORTH, new Position(1, 1));

        assertTrue(barca.stillFloating());
        barca.shoot(new Position(1, 1));
        assertFalse(barca.stillFloating());
    }

    @Test
    @DisplayName("shoot em posição que não pertence ao navio não altera o estado")
    void testShootMiss() {
        Ship barca = new Barge(Compass.NORTH, new Position(1, 1));

        barca.shoot(new Position(5, 5)); // nenhuma posição coincide
        assertTrue(barca.stillFloating());
    }

    @Test
    @DisplayName("shoot lança AssertionError se posição for null")
    void testShootNullPosition() {
        Ship barca = new Barge(Compass.NORTH, new Position(1, 1));
        assertThrows(AssertionError.class, () -> barca.shoot(null));
    }

    @Test
    @DisplayName("toString inclui categoria, bearing e posição")
    void testToString() {
        Ship barca = new Barge(Compass.EAST, new Position(4, 2));
        String s = barca.toString();

        assertAll(
                () -> assertTrue(s.contains("Barca")),
                () -> assertTrue(s.contains(barca.getBearing().toString())),
                () -> assertTrue(s.contains(barca.getPosition().toString()))
        );
    }

}
