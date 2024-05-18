package com.example.gomesrodris.archburgers.adapters.driven.infra;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.repositories.PedidoRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.*;
import com.example.gomesrodris.archburgers.testUtils.RealDatabaseTestHelper;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PedidoRepositoryJdbcImplTest {
    private static RealDatabaseTestHelper realDatabase;
    private DatabaseConnection databaseConnection;

    private PedidoRepository pedidoRepository;

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
        pedidoRepository = new PedidoRepositoryJdbcImpl(databaseConnection);
    }

    @AfterEach
    void tearDown() {
        databaseConnection.close();
    }

    @Test
    void getPedido() {
        var pedido = pedidoRepository.getPedido(1);

        assertThat(pedido).isEqualTo(new Pedido(1, null, "Cliente Erasmo",
                Collections.emptyList(), "Sem cebola", StatusPedido.RECEBIDO,
                new InfoPagamento(FormaPagamento.DINHEIRO),
                LocalDateTime.of(2024, 5, 18, 15, 30, 12))
        );
    }

    @Test
    void savePedido() {
        var pedido = new Pedido(null, null, "Wanderley", List.of(
                new ItemPedido(1,
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                ),
                new ItemPedido(2,
                        new ItemCardapio(1001, TipoItemCardapio.BEBIDA, "Refrigerante", "Refrigerante", new ValorMonetario("5.00"))
                )
        ), "Lanche sem cebola", StatusPedido.RECEBIDO,
                new InfoPagamento(FormaPagamento.DINHEIRO), LocalDateTime.of(2024, 5, 18, 15, 30, 12));

        var saved = pedidoRepository.savePedido(pedido);

        assertThat(saved).isEqualTo(pedido.withId(2));

        var loaded = pedidoRepository.getPedido(2);
        assertThat(loaded).isEqualTo(saved.withItens(List.of()));
    }
}