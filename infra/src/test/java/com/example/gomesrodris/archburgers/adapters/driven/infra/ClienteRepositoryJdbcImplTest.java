package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.repositories.ClienteRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
import com.example.gomesrodris.archburgers.testUtils.RealDatabaseTestHelper;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteRepositoryJdbcImplTest {
    private static RealDatabaseTestHelper realDatabase;
    private ConnectionPool connectionPool;

    private ClienteRepository repository;

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
        connectionPool = realDatabase.getConnectionPool();
        repository = new ClienteRepositoryJdbcImpl(connectionPool);
    }

    @AfterEach
    void tearDown() {
        connectionPool.close();
    }

    @Test
    void getClienteByCpf() {
        var cliente = repository.getClienteByCpf(new Cpf("12332112340"));

        assertThat(cliente).isEqualTo(new Cliente(1, "Roberto Carlos",
                new Cpf("12332112340"), "roberto.carlos@example.com"));
    }

    @Test
    void getClienteByCpf_notFound() {
        var cliente = repository.getClienteByCpf(new Cpf("11122233396"));
        assertThat(cliente).isNull();
    }
}