package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários da classe Ship e suas subclasses.
 * Inclui uso extensivo das principais anotações e assertivas do JUnit 5.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Testes da hierarquia Ship (Barca, Caravela, Nau, etc.)")
class ShipTest {

    private Ship barca;
    private Ship nau;

    @BeforeAll
    static void initAll() {
        System.out.println("🚢 Início dos testes da classe Ship...");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("✅ Todos os testes de Ship terminados.");
    }

    @BeforeEach
    void setUp() {
        barca = Ship.buildShip("barca", Compass.NORTH, new Position(5, 5));
        nau = Ship.buildShip("nau", Compass.NORTH, new Position(5, 5));
    }

    @AfterEach
    void tearDown() {
        System.out.println("🧹 Teste terminado, limpando dados temporários.");
    }

    // ---------------------- TESTES UNITÁRIOS ----------------------

    @Test
    @DisplayName("buildShip deve criar as instâncias corretas")
    void testBuildShipFactory() {
        Ship galeao = Ship.buildShip("galeao", Compass.EAST, new Position(1, 1));
        Ship invalido = Ship.buildShip("banana", Compass.SOUTH, new Position(1, 1));

        assertAll("Verificar criação de navios válidos e inválidos",
                () -> assertNotNull(galeao, "Galeão deve ser criado"),
                () -> assertNull(invalido, "Tipo inválido deve retornar null"),
                () -> assertEquals("Galeao", galeao.getCategory())
        );
    }

    @Test
    @DisplayName("toString deve conter categoria, bearing e posição")
    void testToString() {
        Ship barca = new Barge(Compass.NORTH, new Position(0, 0));
        String str = barca.toString();
        assertAll("Verificar conteúdo de toString",
                () -> assertTrue(str.contains("Barca"), "toString() deve conter 'Barca'"),
                () -> assertTrue(str.contains(barca.getBearing().toString()), "toString() deve conter o bearing"),
                () -> assertTrue(str.contains(barca.getPosition().toString()), "toString() deve conter a posição")
        );
    }


    @Test
    @DisplayName("Limites da Carrack (Nau) devem refletir as posições geradas")
    void testBoundaries() {
        // NORTH => (5,5), (6,5), (7,5)
        assertAll("Verificar limites da Nau",
                () -> assertEquals(5, nau.getTopMostPos(), "Top deve ser 5"),
                () -> assertEquals(7, nau.getBottomMostPos(), "Bottom deve ser 7"),
                () -> assertEquals(5, nau.getLeftMostPos(), "Left deve ser 5"),
                () -> assertEquals(5, nau.getRightMostPos(), "Right deve ser 5")
        );
    }

    @Test
    @DisplayName("shoot e stillFloating devem interagir corretamente")
    void testShootAndStillFloating() {
        Ship caravel = Ship.buildShip("caravela", Compass.NORTH, new Position(2, 2));
        assertTrue(caravel.stillFloating(), "Navio novo deve flutuar");

        // Acertar todas as posições
        caravel.getPositions().forEach(p -> caravel.shoot(p));

        assertFalse(caravel.stillFloating(), "Navio deve afundar após todos os tiros");
    }

    @Test
    @DisplayName("tooCloseTo deve detetar navios adjacentes")
    void testTooCloseTo() {
        Ship fragata = Ship.buildShip("fragata", Compass.NORTH, new Position(5, 5));
        Ship caravela = Ship.buildShip("caravela", Compass.NORTH, new Position(7, 5));

        assertTrue(fragata.tooCloseTo(caravela), "Navios estão demasiado próximos");
    }

    @Test
    @DisplayName("occupies deve retornar true se o navio ocupa a posição")
    void testOccupies() {
        assertTrue(barca.occupies(new Position(5, 5)));
        assertFalse(barca.occupies(new Position(6, 5)));
    }

    @Test
    @DisplayName("Deve lançar exceção para bearing nulo")
    void testInvalidBearingThrowsException() {
        assertThrows(AssertionError.class, () ->
                        new Carrack(null, new Position(1, 1)),
                "Esperava AssertionError para bearing nulo"
        );
    }

    // ---------------------- TESTES PARAMETRIZADOS ----------------------

    @ParameterizedTest(name = "Caravela com bearing {0} deve ter tamanho 2")
    @CsvSource({"NORTH,2", "SOUTH,2", "EAST,2", "WEST,2"})
    @DisplayName("Verificar tamanho da Caravela em várias direções")
    void testCaravelSizeWithBearings(String bearingName, int expectedSize) {
        Compass bearing = Compass.valueOf(bearingName);
        Ship caravela = Ship.buildShip("caravela", bearing, new Position(1, 1));
        assertEquals(expectedSize, caravela.getSize());
    }

    // ---------------------- TESTES ANINHADOS ----------------------

    @Nested
    @DisplayName("Testes específicos da Barca")
    class BarcaTests {

        @Test
        @DisplayName("Barca deve ter apenas 1 posição")
        void testBargeSize() {
            assertEquals(1, barca.getPositions().size());
            assertEquals(new Position(5, 5), barca.getPositions().get(0));
        }

        @Test
        @DisplayName("Barca deve afundar após 1 tiro certeiro")
        void testBargeSink() {
            assertTrue(barca.stillFloating(), "Inicialmente flutua");
            barca.shoot(new Position(5, 5));
            assertFalse(barca.stillFloating(), "Afunda após um tiro certo");
        }
    }



    // ---------------------- TESTE DESATIVADO ----------------------

    @Disabled("Teste desativado temporariamente — exemplo de uso de @Disabled")
    @Test
    @DisplayName("Exemplo de teste desativado")
    void disabledTestExample() {
        fail("Não deve correr — este teste está desativado");
    }

    // ---------------------- TESTES ADICIONAIS PARA 100% COVERAGE ----------------------

    @Test
    @DisplayName("stillFloating deve retornar true e false corretamente em navios maiores")
    void testStillFloatingFullCoverage() {
        Ship fragata = Ship.buildShip("fragata", Compass.NORTH, new Position(0, 0));
        // Nenhum tiro ainda → deve flutuar
        assertTrue(fragata.stillFloating(), "Navio novo deve flutuar");

        // Atira em todas exceto a última posição
        for (int i = 0; i < fragata.getSize() - 1; i++)
            fragata.getPositions().get(i).shoot();
        assertTrue(fragata.stillFloating(), "Ainda flutua se apenas algumas posições atingidas");

        // Atira na última posição
        fragata.getPositions().get(fragata.getSize() - 1).shoot();
        assertFalse(fragata.stillFloating(), "Afunda após todos os tiros");
    }

    @Test
    @DisplayName("tooCloseTo com navio não adjacente deve retornar false")
    void testTooCloseToFalse() {
        Ship fragata = Ship.buildShip("fragata", Compass.NORTH, new Position(0, 0));
        Ship caravela = Ship.buildShip("caravela", Compass.NORTH, new Position(10, 10)); // longe

        assertFalse(fragata.tooCloseTo(caravela), "Navios não adjacentes não são 'too close'");
    }

    @Test
    @DisplayName("shoot com posição não ocupada não deve lançar exceção")
    void testShootNoHit() {
        Ship barca = new Barge(Compass.NORTH, new Position(0, 0));
        // Pos não pertence ao navio
        barca.shoot(new Position(5, 5));
        assertTrue(barca.stillFloating(), "Navio não atingido deve continuar flutuando");
    }

    @Test
    @DisplayName("getTop/Bottom/Left/RightMostPos deve funcionar com múltiplas posições")
    void testBoundaryMethodsMultiplePositions() {
        Ship nau = Ship.buildShip("nau", Compass.EAST, new Position(0, 0));
        // EAST => posicoes: (0,0),(0,1),(0,2)
        assertAll("Testar limites com múltiplas posições",
                () -> assertEquals(0, nau.getTopMostPos()),
                () -> assertEquals(0, nau.getBottomMostPos()),
                () -> assertEquals(0, nau.getLeftMostPos()),
                () -> assertEquals(2, nau.getRightMostPos())
        );
    }


    // tentaiva de 100% coverage

    @Test
    @DisplayName("stillFloating retorna true se algumas posições estão hitadas")
    void testStillFloatingPartialHit() {
        Ship barca = new Barge(Compass.NORTH, new Position(0, 0));
        // Barge tem apenas 1 posição, então vamos usar Carrack para múltiplas
        Ship nau = new Carrack(Compass.NORTH, new Position(0, 0));
        nau.getPositions().get(0).shoot(); // só a primeira posição acertada
        assertTrue(nau.stillFloating(), "Navio parcialmente atingido ainda flutua");
    }


    @Test
    @DisplayName("Limites com navio de 1 posição")
    void testBoundariesSinglePosition() {
        Ship barca = new Barge(Compass.NORTH, new Position(3, 7));
        assertAll("Limites de navio de tamanho 1",
                () -> assertEquals(3, barca.getTopMostPos()),
                () -> assertEquals(3, barca.getBottomMostPos()),
                () -> assertEquals(7, barca.getLeftMostPos()),
                () -> assertEquals(7, barca.getRightMostPos())
        );
    }

    @Test
    @DisplayName("tooCloseTo(IPosition) retorna false quando nenhuma posição adjacente")
    void testTooCloseToPositionFalse() {
        Ship barca = new Barge(Compass.NORTH, new Position(0, 0));
        Position farAway = new Position(5, 5);
        assertFalse(barca.tooCloseTo(farAway));
    }

    @Test
    @DisplayName("tooCloseTo(IShip) retorna false quando nenhum navio está adjacente")
    void testTooCloseToShipFalse() {
        Ship barca = new Barge(Compass.NORTH, new Position(0, 0));
        Ship caravela = new Caravel(Compass.NORTH, new Position(5, 5));
        assertFalse(barca.tooCloseTo(caravela));
    }

    @Test
    @DisplayName("occupies retorna false se nenhuma posição coincide")
    void testOccupiesFalseFullLoop() {
        Ship nau = new Carrack(Compass.NORTH, new Position(0, 0));
        Position notOccupied = new Position(10, 10); // fora de todas as posições
        assertFalse(nau.occupies(notOccupied));
    }

    @Test
    @DisplayName("shoot em posição não ocupada não altera navio")
    void testShootMiss() {
        Ship barca = new Barge(Compass.NORTH, new Position(0, 0));
        Position miss = new Position(5, 5);
        barca.shoot(miss); // nenhum efeito
        assertTrue(barca.stillFloating(), "Navio não é atingido quando posição não coincide");
    }

    @Test
    @DisplayName("Construtor lança AssertionError se posição nula")
    void testConstructorNullPosition() {
        assertThrows(AssertionError.class, () ->
                new Barge(Compass.NORTH, null)
        );
    }

}
