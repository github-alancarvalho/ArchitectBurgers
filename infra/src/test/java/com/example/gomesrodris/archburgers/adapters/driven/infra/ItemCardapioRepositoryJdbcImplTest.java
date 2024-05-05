package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import com.example.gomesrodris.archburgers.testUtils.RealDatabaseTestHelper;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemCardapioRepositoryJdbcImplTest {
    private static RealDatabaseTestHelper realDatabase;
    private DatabaseConnection databaseConnection;

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
        databaseConnection = realDatabase.getConnectionPool();
        repository = new ItemCardapioRepositoryJdbcImpl(databaseConnection);
    }

    @AfterEach
    void tearDown() {
        databaseConnection.close();
    }

    @Test
    void findAll() {
        List<ItemCardapio> byTipo = repository.findAll();

        assertThat(byTipo).hasSize(8);

        assertThat(byTipo.get(0)).isEqualTo(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato", new ValorMonetario("22.90"))
        );

        assertThat(byTipo.get(1)).isEqualTo(
                new ItemCardapio(2, TipoItemCardapio.LANCHE, "Veggie Cheddar",
                        "Hamburger do Futuro com cebolas caramelizadas e cheddar vegano", new ValorMonetario("23.50"))
        );

        assertThat(byTipo.get(7)).isEqualTo(
                new ItemCardapio(8, TipoItemCardapio.SOBREMESA, "Mini churros",
                        "Mini churros de doce de leite", new ValorMonetario("0.99"))
        );
    }

    @Test
    void findByCarrinho() {
        List<ItemCardapio> byTipo = repository.findByCarrinho(1);

        assertThat(byTipo).hasSize(2);

        assertThat(byTipo.get(0)).isEqualTo(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato", new ValorMonetario("22.90"))
        );

        assertThat(byTipo.get(1)).isEqualTo(
                new ItemCardapio(3, TipoItemCardapio.ACOMPANHAMENTO, "Batatas Fritas P",
                        "Batatas fritas porção pequena", new ValorMonetario("8.00"))
        );
    }
}