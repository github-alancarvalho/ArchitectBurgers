package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
import com.example.gomesrodris.archburgers.testUtils.RealDatabaseTestHelper;
import org.junit.jupiter.api.*;

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
    void getCarrinhoByClienteId_naoExiste() {
        var carrinhoSalvo = carrinhoRepository.getCarrinhoByClienteId(77);
        assertThat(carrinhoSalvo).isNull();
    }

    @Test
    void getCarrinhoByClienteId() {
        var carrinhoSalvo = carrinhoRepository.getCarrinhoByClienteId(2);
        assertThat(carrinhoSalvo).isNotNull();
        assertThat(carrinhoSalvo.clienteIdentificado()).isEqualTo(
                new Cliente(2, "Wanderleia", new Cpf("99988877714"), "wanderleia@example.com"));
    }

    @Test
    void salvarCarrinho() {
    }
}