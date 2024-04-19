package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.utils.Valor;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.testUtils.RealDatabaseTestHelper;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemCardapioRepositoryJdbcImplTest {
    private static RealDatabaseTestHelper realDatabase;
    private ConnectionPool connectionPool;

    private ItemCardapioRepository repository;

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
        repository = new ItemCardapioRepositoryJdbcImpl(connectionPool);
    }

    @AfterEach
    void tearDown() {
        connectionPool.close();
    }

    @Test
    void findByTipo_L() {
        List<ItemCardapio> byTipo = repository.findByTipo(TipoItemCardapio.LANCHE);

        assertThat(byTipo).hasSize(2);

        assertThat(byTipo).containsExactly(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato", Valor.of("22.90")),
                new ItemCardapio(2, TipoItemCardapio.LANCHE, "Veggie Cheddar",
                        "Hamburger do Futuro com cebolas caramelizadas e cheddar vegano", Valor.of("23.50"))
        );
    }

    @Test
    void findByTipo_B() {
        List<ItemCardapio> byTipo = repository.findByTipo(TipoItemCardapio.BEBIDA);

        assertThat(byTipo).hasSize(2);

        assertThat(byTipo).containsExactly(
                new ItemCardapio(5, TipoItemCardapio.BEBIDA, "Dollynho",
                        "Guaraná Dollynho", Valor.of("5.00")),
                new ItemCardapio(6, TipoItemCardapio.BEBIDA, "Chá gelado",
                        "Chá gelado com limão, feito na casa", Valor.of("6.00"))
        );
    }
}