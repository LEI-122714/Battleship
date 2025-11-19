package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para a classe Tasks, focados na funcao firingRound
 * (maximizar coverage de branches: for + if (sh != null)).
 */
@DisplayName("Testes para Tasks (firingRound)")
class TasksTest {

    /**
     * Stub de Game para controlar o comportamento de fire()
     * sem depender da implementação completa.
     *
     * Usa um array de resultados predefinidos: em cada chamada de fire,
     * devolve o elemento seguinte do array (ou null se já nao houver).
     */
    static class StubGame extends Game {

        private final List<IPosition> receivedShots = new ArrayList<>();
        private final IShip[] results;
        private int callCount = 0;

        StubGame(IShip... results) {
            super(new Fleet());   // precisamos de um IFleet mas nao vamos usa-lo
            this.results = results;
        }

        @Override
        public IShip fire(IPosition pos) {
            receivedShots.add(pos);
            IShip toReturn = null;
            if (callCount < results.length) {
                toReturn = results[callCount];
            }
            callCount++;
            return toReturn;
        }

        int getCallCount() {
            return callCount;
        }

        List<IPosition> getReceivedShots() {
            return receivedShots;
        }
    }

    /**
     * Le o valor da constante privada NUMBER_SHOTS em Tasks
     * para que o teste nao dependa do valor literal (3).
     */
    private int getNumberShots() {
        try {
            Field f = Tasks.class.getDeclaredField("NUMBER_SHOTS");
            f.setAccessible(true);
            return f.getInt(null);
        } catch (Exception e) {
            throw new RuntimeException("Nao foi possivel obter NUMBER_SHOTS por reflection", e);
        }
    }

    /**
     * Cria um Scanner com N linhas de coordenadas "linha coluna",
     * por exemplo:
     *
     * 0 0
     * 1 1
     * 2 2
     */
    private Scanner createScannerWithNPositions(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(i).append(" ").append(i).append("\n");
        }
        ByteArrayInputStream in =
                new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
        return new Scanner(in);
    }

    // ---------------------------------------------------------------------
    // Cenário 1: todas as chamadas a game.fire(...) devolvem null
    // -> cobre o ramo (sh == null) do if dentro de firingRound
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("firingRound: todas as jogadas sao falhadas (sh == null)")
    void firingRound_allMisses() {
        int nShots = getNumberShots();

        // game.fire vai devolver sempre null
        StubGame game = new StubGame(
                new IShip[nShots]   // array de nShots elementos, todos null
        );

        Scanner scanner = createScannerWithNPositions(nShots);

        Tasks.firingRound(scanner, game);

        // for deve ter corrido exactamente NUMBER_SHOTS vezes
        assertEquals(nShots, game.getCallCount(), "fire deve ser chamado NUMBER_SHOTS vezes");
        assertEquals(nShots, game.getReceivedShots().size(),
                "Numero de posicoes enviadas para game.fire deve ser NUMBER_SHOTS");
    }

    // ---------------------------------------------------------------------
    // Cenário 2: pelo menos uma chamada a game.fire devolve um navio
    // -> cobre o ramo (sh != null) do if dentro de firingRound
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("firingRound: pelo menos um tiro acerta num navio (sh != null)")
    void firingRound_withHit() {
        int nShots = getNumberShots();

        // vamos garantir que a primeira chamada devolve um navio,
        // as restantes podem ser null
        IShip hitShip = new Barge(Compass.NORTH, new Position(0, 0));
        IShip[] results = new IShip[nShots];
        results[0] = hitShip;        // primeira chamada => sh != null
        // restantes ficam null

        StubGame game = new StubGame(results);
        Scanner scanner = createScannerWithNPositions(nShots);

        assertDoesNotThrow(() -> Tasks.firingRound(scanner, game),
                "firingRound nao deve lancar excecoes mesmo com hits");

        // continua a valer que fire é chamado NUMBER_SHOTS vezes
        assertEquals(nShots, game.getCallCount());
        assertEquals(nShots, game.getReceivedShots().size());
    }

}
