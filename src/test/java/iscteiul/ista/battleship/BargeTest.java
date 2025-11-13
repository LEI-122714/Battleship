package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste para a classe Barge (barcos de tamanho 1) LEI122687")
class BargeTest { // Tamanho do barco = 1

    @Test
    @DisplayName("Valida tamanho da Barge (metadados)")
    void testMetadata() {
        IPosition start = new Position(3, 3);
        // Construtor de Barge usa (Compass, IPosition)
        Barge b = new Barge(Compass.NORTH, start);

        assertAll("metadata",
                () -> assertNotNull(b, "Instância não deve ser nula"),
                () -> assertEquals(1, b.getSize(), "Tamanho deve ser 1")
        );
    }

    @Test
    @DisplayName("Valida posição única da Barge (independente da orientação)")
    void testPosition() {
        IPosition start = new Position(5, 5);

        // Barge.java ignora a orientação e apenas adiciona a posição inicial
        // CORREÇÃO: Usa a classe "Barge"
        Barge bNorth = new Barge(Compass.NORTH, start);
        List<IPosition> posNorth = bNorth.getPositions();

        // CORREÇÃO: Usa a classe "Barge"
        Barge bEast = new Barge(Compass.EAST, start);
        List<IPosition> posEast = bEast.getPositions();

        assertAll("single position NORTH",
                () -> assertEquals(1, posNorth.size(), "Deve ter exatamente 1 posição"),
                () -> assertEquals(5, posNorth.get(0).getRow(), "posição inicial: row"),
                () -> assertEquals(5, posNorth.get(0).getColumn(), "posição inicial: column")
        );

        assertAll("single position EAST",
                () -> assertEquals(1, posEast.size(), "Deve ter exatamente 1 posição"),
                () -> assertEquals(5, posEast.get(0).getRow(), "posição inicial: row (EAST)"),
                () -> assertEquals(5, posEast.get(0).getColumn(), "posição inicial: column (EAST)")
        );
    }

    @Test
    @DisplayName("Construtor lança AssertionError se Compass for null (devido ao Ship.java)")
    void testNullCompassThrows() {
        // Assumindo que Ship.java (pai) usa 'assert' (baseado no seu log de erro anterior)
        assertThrows(AssertionError.class, () -> new Barge(null, new Position(1, 1)),
                "Deve lançar AssertionError se a bússola for null (ver Ship.java)");
    }

    @Test
    @DisplayName("Construtor lança AssertionError se Position for null (devido ao Ship.java)")
    void testNullPositionThrows() {
        // Assumindo que Ship.java (pai) usa 'assert' (baseado no seu log de erro anterior)
        assertThrows(AssertionError.class, () -> new Barge(Compass.NORTH, null),
                "Deve lançar AssertionError se a posição for null (ver Ship.java)");
    }
}