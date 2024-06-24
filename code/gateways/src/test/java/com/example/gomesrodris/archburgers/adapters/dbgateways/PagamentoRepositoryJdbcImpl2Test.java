package com.example.gomesrodris.archburgers.adapters.database;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.domain.entities.ConfirmacaoPagamento;
import com.example.gomesrodris.archburgers.domain.repositories.PagamentoRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;
import com.example.gomesrodris.archburgers.testUtils.RealDatabaseTestHelper;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PagamentoRepositoryJdbcImpl2Test {
    private static RealDatabaseTestHelper realDatabase;
    private DatabaseConnection databaseConnection;

    private PagamentoRepository repository;

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
        repository = new PagamentoRepositoryJdbcImpl2(databaseConnection);
    }

    @AfterEach
    void tearDown() {
        databaseConnection.close();
    }

    @Test
    void salvarConfirmacaoPagamento() throws SQLException {
        var saved = repository.salvarConfirmacaoPagamento(new ConfirmacaoPagamento(null, FormaPagamento.DINHEIRO,
                "Teste Unitario Repository",
                LocalDateTime.of(2024, 6, 19, 21, 1, 50)));

        assertThat(saved.id()).isEqualTo(2);

        try (var conn = databaseConnection.jdbcConnection();
             var stmt = conn.prepareStatement("select forma_pagamento,info_adicional,data_hora_pagamento " +
                     "from confirmacao_pagamento where pagamento_id = ?")) {

            stmt.setInt(1, saved.id());
            var rs = stmt.executeQuery();

            assertThat(rs.next()).isTrue();

            assertThat(rs.getString(1)).isEqualTo("DINHEIRO");
            assertThat(rs.getString(2)).isEqualTo("Teste Unitario Repository");
            assertThat(rs.getObject(3, LocalDateTime.class)).isEqualTo(
                    LocalDateTime.of(2024, 6, 19, 21, 1, 50));
        }
    }
}