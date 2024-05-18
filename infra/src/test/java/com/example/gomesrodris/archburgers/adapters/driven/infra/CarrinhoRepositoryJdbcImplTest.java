package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import com.example.gomesrodris.archburgers.testUtils.RealDatabaseTestHelper;
import org.junit.jupiter.api.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    void getCarrinhoSalvoById() {
        var carrinhoSalvo = carrinhoRepository.getCarrinho(1);
        assertThat(carrinhoSalvo).isNotNull();
        assertThat(carrinhoSalvo.id()).isEqualTo(1);

        assertThat(carrinhoSalvo.idClienteIdentificado()).isEqualTo(new IdCliente(2));
        assertThat(carrinhoSalvo.nomeClienteNaoIdentificado()).isNull();

        assertThat(carrinhoSalvo.observacoes()).isEqualTo("Sem cebola");
        assertThat(carrinhoSalvo.dataHoraCarrinhoCriado()).isEqualTo(LocalDateTime.of(2024, 4, 30, 15, 32, 58));
    }

    @Test
    void salvarCarrinhoVazio_clienteIdentificado() {
        Carrinho carrinho = Carrinho.newCarrinhoVazioClienteIdentificado(new IdCliente(1),
                LocalDateTime.of(2024, 4, 30, 20, 21, 40));

        var result = carrinhoRepository.salvarCarrinhoVazio(carrinho);

        assertThat(result).isNotNull();
        assertThat(result.id()).isNotNull();

        var carrinhoSalvo = carrinhoRepository.getCarrinho(result.id());
        assertThat(carrinhoSalvo.idClienteIdentificado()).isEqualTo(new IdCliente(1));
        assertThat(carrinhoSalvo.dataHoraCarrinhoCriado()).isEqualTo(
                LocalDateTime.of(2024, 4, 30, 20, 21, 40)
        );
    }

    @Test
    void salvarCarrinhoVazio_clienteNaoIdentificado() {
        Carrinho carrinho = Carrinho.newCarrinhoVazioClienteNaoIdentificado("Sr. Anônimo",
                LocalDateTime.of(2024, 4, 30, 20, 21, 40));

        var result = carrinhoRepository.salvarCarrinhoVazio(carrinho);

        assertThat(result).isNotNull();
        assertThat(result.id()).isNotNull();

        var carrinhoSalvo = carrinhoRepository.getCarrinho(result.id());
        assertThat(carrinhoSalvo.idClienteIdentificado()).isNull();
        assertThat(carrinhoSalvo.nomeClienteNaoIdentificado()).isEqualTo("Sr. Anônimo");
        assertThat(carrinhoSalvo.dataHoraCarrinhoCriado()).isEqualTo(
                LocalDateTime.of(2024, 4, 30, 20, 21, 40)
        );
    }

    @Test
    void salvarItemCarrinho() throws SQLException {
        var carrinhoSalvo = carrinhoRepository.getCarrinhoSalvoByCliente(new IdCliente(2));
        assertThat(carrinhoSalvo).isNotNull();

        carrinhoRepository.salvarItemCarrinho(carrinhoSalvo, new ItemPedido(3,
                        new ItemCardapio(7, TipoItemCardapio.SOBREMESA, "Mini churros",
                                "Mini churros de doce de leite", new ValorMonetario("0.99"))
                )
        );

        try (var conn = databaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("select * from carrinho_item where carrinho_id = 1 and item_cardapio_id = 7 and num_sequencia = 3");
            ResultSet rs = stmt.executeQuery();

            assertThat(rs.next()).isTrue(); // Record exists
        }
    }
}