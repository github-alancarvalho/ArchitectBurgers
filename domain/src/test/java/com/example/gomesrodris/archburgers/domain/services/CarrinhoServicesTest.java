package com.example.gomesrodris.archburgers.domain.services;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ClienteRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarrinhoServicesTest {
    @Mock
    private CarrinhoRepository carrinhoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private Clock clock;

    private CarrinhoServices carrinhoServices;

    @BeforeEach
    void setUp() {
        carrinhoServices = new CarrinhoServices(carrinhoRepository, clienteRepository, itemCardapioRepository, clock);
    }

    @Test
    void criarCarrinho_clienteIdentificado_carrinhoExistente() {
        when(carrinhoRepository.getCarrinhoSalvoByCliente(new IdCliente(123))).thenReturn(carrinhoSalvoCliente123);

        when(itemCardapioRepository.findByCarrinho(88)).thenReturn(List.of(
                new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
        ));

        var result = carrinhoServices.criarCarrinho(new CarrinhoServices.CarrinhoParam(123, null, null, null));
        assertThat(result).isEqualTo(carrinhoSalvoCliente123.withItens(List.of(
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                ))
        );
    }

    @Test
    void criarCarrinho_clienteIdentificado_novoCarrinho() throws Exception {
        when(carrinhoRepository.getCarrinhoSalvoByCliente(new IdCliente(123))).thenReturn(null);
        when(clock.localDateTime()).thenReturn(dateTime);

        when(carrinhoRepository.salvarCarrinhoVazio(carrinhoVazioCliente123)).thenReturn(
                carrinhoVazioCliente123.withId(99));

        when(clienteRepository.getClienteById(123)).thenReturn(cliente123);

        var result = carrinhoServices.criarCarrinho(new CarrinhoServices.CarrinhoParam(123, null, null, null));
        assertThat(result).isEqualTo(carrinhoVazioCliente123.withId(99));
    }

    @Test
    void criarCarrinho_clienteNaoIdentificado_novoCarrinho() throws Exception {
        when(clock.localDateTime()).thenReturn(dateTime);

        when(carrinhoRepository.salvarCarrinhoVazio(carrinhoNaoIdentificado)).thenReturn(
                carrinhoNaoIdentificado.withId(101));

        var result = carrinhoServices.criarCarrinho(new CarrinhoServices.CarrinhoParam(null, "João", null, null));
        assertThat(result).isEqualTo(carrinhoNaoIdentificado.withId(101));
    }

    @Test
    void criarCarrinho_cadastrarNovoCliente_novoCarrinho() throws Exception {
        when(clock.localDateTime()).thenReturn(dateTime);

        when(clienteRepository.salvarCliente(clienteSemId)).thenReturn(cliente123);

        when(carrinhoRepository.salvarCarrinhoVazio(carrinhoVazioCliente123)).thenReturn(
                carrinhoVazioCliente123.withId(102));

        var result = carrinhoServices.criarCarrinho(new CarrinhoServices.CarrinhoParam(null, "Cliente", "12332112340", "cliente123@example.com"));
        assertThat(result).isEqualTo(carrinhoVazioCliente123.withId(102));
    }

    // // // Predefined test objects
    private final Cliente cliente123 = new Cliente(new IdCliente(123), "Cliente", new Cpf("12332112340"), "cliente123@example.com");
    private final Cliente clienteSemId = new Cliente(null, "Cliente", new Cpf("12332112340"), "cliente123@example.com");

    private final LocalDateTime dateTime = LocalDateTime.of(2024, 4, 29, 15, 30);

    private final Carrinho carrinhoSalvoCliente123 = Carrinho.carrinhoSalvoClienteIdentificado(
            88, new IdCliente(123), null, dateTime);

    private final Carrinho carrinhoNaoIdentificado = Carrinho.newCarrinhoVazioClienteNaoIdentificado("João", dateTime);

    private final Carrinho carrinhoVazioCliente123 = Carrinho.newCarrinhoVazioClienteIdentificado(new IdCliente(123), dateTime);
}