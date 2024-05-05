package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;
import com.example.gomesrodris.archburgers.testUtils.RealDatabaseTestHelper;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CarrinhoRepositoryJdbcImplTest {
    private static RealDatabaseTestHelper realDatabase;
    private DatabaseConnection databaseConnection;

    private CarrinhoRepository carrinhoRepository;

    @BeforeAll
    static void beforeAll() throws Exception {
        realDatabase = new RealDatabaseTestHelper();
        realDatabase.beforeAll();
    }

    @AfterAll
    static void afterAll() {
        realDatabase.afterAll();
    }

    @BeforeEach
    void setUp() {
        databaseConnection = realDatabase.getConnectionPool();
        carrinhoRepository = new CarrinhoRepositoryJdbcImpl(databaseConnection);
    }

    @AfterEach
    void tearDown() {
        databaseConnection.close();
    }

    @Test
    void getCarrinhoSalvoByCliente_naoExiste() {
        var carrinhoSalvo = carrinhoRepository.getCarrinhoSalvoByCliente(new IdCliente(77));
        assertThat(carrinhoSalvo).isNull();
    }

    @Test
    void getCarrinhoSalvoByCliente() {
        var carrinhoSalvo = carrinhoRepository.getCarrinhoSalvoByCliente(new IdCliente(2));
        assertThat(carrinhoSalvo).isNotNull();
        assertThat(carrinhoSalvo.id()).isEqualTo(1);

        assertThat(carrinhoSalvo.idClienteIdentificado()).isEqualTo(new IdCliente(2));
        assertThat(carrinhoSalvo.nomeClienteNaoIdentificado()).isNull();

        assertThat(carrinhoSalvo.observacoes()).isEqualTo("Sem cebola");
        assertThat(carrinhoSalvo.dataHoraCarrinhoCriado()).isEqualTo(LocalDateTime.of(2024, 4, 30, 15, 32, 58));
    }

    @Test
    void salvarCarrinho() {
    }
}