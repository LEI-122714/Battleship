package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste para a classe Carrack (barcos de tamanho 3) LEI122687")
class CarrackTest { //tamanho do barco = 3.

    @Test
    @DisplayName("Valida tamanho da Carrack (metadados)")
    void testMetadata() {
        Carrack c = new Carrack(Compass.NORTH, new Position(0, 0));
        assertAll("metadata",
                () -> assertNotNull(c, "Instância não deve ser nula"),
                () -> assertEquals(3, c.getSize(), "Tamanho deve ser 3")
        );
    }

    @Test
    @DisplayName("Valida que getSize() retorna sempre 3 para qualquer orientação")
    void testGetSizeMethod() {
        Carrack c1 = new Carrack(Compass.NORTH, new Position(0, 0));
        Carrack c2 = new Carrack(Compass.EAST, new Position(3, 3));

        assertAll("getSize",
                () -> assertEquals(3, c1.getSize(), "getSize() deve retornar 3 para qualquer Carrack"),
                () -> assertEquals(3, c2.getSize(), "getSize() deve retornar 3 para qualquer Carrack")
        );
    }

    @Test
    @DisplayName("Valida posições para NORTH (3 posições)")
    void testPositionsNorth() {
        IPosition start = new Position(5, 5);
        Carrack c = new Carrack(Compass.NORTH, start);
        List<IPosition> positions = c.getPositions();

        assertAll("north positions",
                () -> assertEquals(3, positions.size(), "Deve ter exatamente 3 posições"),
                () -> assertEquals(5, positions.get(0).getRow(), "pos 0: row"),
                () -> assertEquals(6, positions.get(1).getRow(), "pos 1: row (+1)"),
                () -> assertEquals(7, positions.get(2).getRow(), "pos 2: row (+2)"),
                () -> assertEquals(5, positions.get(2).getColumn(), "pos 2: column (igual)")
        );
    }

    @Test
    @DisplayName("Valida posições para SOUTH (3 posições)")
    void testPositionsSouth() {
        IPosition start = new Position(5, 5);
        Carrack c = new Carrack(Compass.SOUTH, start);
        List<IPosition> positions = c.getPositions();

        assertAll("south positions",
                () -> assertEquals(3, positions.size(), "Deve ter exatamente 3 posições"),
                () -> assertEquals(5, positions.get(0).getRow(), "pos 0: row"),
                () -> assertEquals(6, positions.get(1).getRow(), "pos 1: row (+1)"),
                () -> assertEquals(7, positions.get(2).getRow(), "pos 2: row (+2)"),
                () -> assertEquals(5, positions.get(2).getColumn(), "pos 2: column (igual)")
        );
    }

    @Test
    @DisplayName("Valida posições para EAST (3 posições)")
    void testPositionsEast() {
        IPosition start = new Position(5, 5);
        Carrack c = new Carrack(Compass.EAST, start);
        List<IPosition> positions = c.getPositions();

        assertAll("east positions",
                () -> assertEquals(3, positions.size(), "Deve ter exatamente 3 posições"),
                () -> assertEquals(5, positions.get(0).getColumn(), "pos 0: col"),
                () -> assertEquals(6, positions.get(1).getColumn(), "pos 1: col (+1)"),
                () -> assertEquals(7, positions.get(2).getColumn(), "pos 2: col (+2)"),
                () -> assertEquals(5, positions.get(2).getRow(), "pos 2: row (igual)")
        );
    }

    @Test
    @DisplayName("Valida posições para WEST (3 posições)")
    void testPositionsWest() {
        IPosition start = new Position(5, 5);
        Carrack c = new Carrack(Compass.WEST, start);
        List<IPosition> positions = c.getPositions();

        assertAll("west positions",
                () -> assertEquals(3, positions.size(), "Deve ter exatamente 3 posições"),
                () -> assertEquals(5, positions.get(0).getColumn(), "pos 0: col"),
                () -> assertEquals(6, positions.get(1).getColumn(), "pos 1: col (+1)"),
                () -> assertEquals(7, positions.get(2).getColumn(), "pos 2: col (+2)"),
                () -> assertEquals(5, positions.get(2).getRow(), "pos 2: row (igual)")
        );
    }

    @Test
    @DisplayName("Construtor lança AssertionError se Compass for null (devido ao Ship.java)")
    void testNullCompassThrows() {
        // Assumindo que Ship.java (pai) usa 'assert' (baseado no seu log de erro anterior)
        assertThrows(AssertionError.class, () -> new Carrack(null, new Position(1, 1)),
                "Deve lançar AssertionError se a bússola for null (ver Ship.java)");
    }

    @Test
    @DisplayName("Construtor lança AssertionError se Position for null (devido ao Ship.java)")
    void testNullPositionThrows() {
        // Assumindo que Ship.java (pai) usa 'assert' (baseado no seu log de erro anterior)
        assertThrows(AssertionError.class, () -> new Carrack(Compass.NORTH, null),
                "Deve lançar AssertionError se a posição for null (ver Ship.java)");
    }
}