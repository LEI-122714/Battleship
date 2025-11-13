package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste para a classe Caravel (barcos de tamanho 2) LEI122687")
class CaravelTest { //Tamanho do barco = 2

    @Test
    @DisplayName("Valida tamanho da Caravela (metadados)")
    void testMetadata() {
        Caravel c = new Caravel(Compass.NORTH, new Position(0, 0));
        assertAll("metadata",
                () -> assertNotNull(c, "Instância não deve ser nula"),
                () -> assertEquals(2, c.getSize(), "Tamanho deve ser 2")
        );
    }

    @Test
    @DisplayName("Valida que getSize() retorna sempre 2 para qualquer orientação")
    void testGetSizeMethod() {
        Caravel c1 = new Caravel(Compass.NORTH, new Position(0, 0));
        Caravel c2 = new Caravel(Compass.EAST, new Position(3, 3));

        assertAll("getSize",
                () -> assertEquals(2, c1.getSize(), "getSize() deve retornar 2 para qualquer Caravel"),
                () -> assertEquals(2, c2.getSize(), "getSize() deve retornar 2 para qualquer Caravel")
        );
    }

    @Test
    @DisplayName("Valida posições para NORTH (convenção do projeto)")
    void testPositionsNorth() {
        IPosition start = new Position(5, 5);
        Caravel c = new Caravel(Compass.NORTH, start);
        List<IPosition> positions = c.getPositions();

        assertAll("north positions",
                () -> assertEquals(2, positions.size(), "Deve ter exatamente 2 posições"),
                () -> assertEquals(5, positions.get(0).getRow(), "posição inicial: row"),
                () -> assertEquals(5, positions.get(0).getColumn(), "posição inicial: column"),
                () -> assertEquals(6, positions.get(1).getRow(), "segunda posição row (esperado +1)"),
                () -> assertEquals(5, positions.get(1).getColumn(), "segunda posição column (igual)")
        );
    }

    @Test
    @DisplayName("Valida posições para SOUTH (comportamento esperado do projeto)")
    void testPositionsSouth() {
        IPosition start = new Position(5, 5);
        Caravel c = new Caravel(Compass.SOUTH, start);
        List<IPosition> positions = c.getPositions();

        assertAll("south positions",
                () -> assertEquals(2, positions.size(), "Deve ter exatamente 2 posições"),
                () -> assertEquals(5, positions.get(0).getRow(), "posição inicial: row"),
                () -> assertEquals(5, positions.get(0).getColumn(), "posição inicial: column"),
                () -> assertEquals(6, positions.get(1).getRow(), "segunda posição row (esperado +1)"),
                () -> assertEquals(5, positions.get(1).getColumn(), "segunda posição column (igual)")
        );
    }

    @Test
    @DisplayName("Valida posições para EAST (cresce para a direita)")
    void testPositionsEast() {
        IPosition start = new Position(5, 5);
        Caravel c = new Caravel(Compass.EAST, start);
        List<IPosition> positions = c.getPositions();

        assertAll("east positions",
                () -> assertEquals(2, positions.size(), "Deve ter exatamente 2 posições"),
                () -> assertEquals(5, positions.get(0).getRow(), "posição inicial row"),
                () -> assertEquals(5, positions.get(0).getColumn(), "posição inicial column"),
                () -> assertEquals(5, positions.get(1).getRow(), "segunda posição row (igual)"),
                () -> assertEquals(6, positions.get(1).getColumn(), "segunda posição column (esperado +1)")
        );
    }

    @Test
    @DisplayName("Valida posições para WEST (cresce para a esquerda/implementação do projeto)")
    void testPositionsWest() {
        IPosition start = new Position(5, 5);
        Caravel c = new Caravel(Compass.WEST, start);
        List<IPosition> positions = c.getPositions();

        assertAll("west positions",
                () -> assertEquals(2, positions.size(), "Deve ter exatamente 2 posições"),
                () -> assertEquals(5, positions.get(0).getRow(), "posição inicial row"),
                () -> assertEquals(5, positions.get(0).getColumn(), "posição inicial column"),
                () -> assertEquals(5, positions.get(1).getRow(), "segunda posição row (igual)"),
                () -> assertEquals(6, positions.get(1).getColumn(), "segunda posição column (esperado +1)")
        );
    }

    @Test
    @DisplayName("Construtor lança AssertionError se Compass for null (devido ao Ship.java)")
    void testNullCompassThrows() {
        assertThrows(AssertionError.class, () -> new Caravel(null, new Position(1, 1)),
                "Deve lançar AssertionError se a bússola for null (ver Ship.java)");
    }

    @Test
    @DisplayName("Construtor lança AssertionError se Position for null (devido ao Ship.java)")
    void testNullPositionThrows() {
        assertThrows(AssertionError.class, () -> new Caravel(Compass.NORTH, null),
                "Deve lançar AssertionError se a posição for null (ver Ship.java)");
    }
}