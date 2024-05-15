package com.example.gomesrodris.archburgers.domain.services;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ClienteRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import com.example.gomesrodris.archburgers.domain.utils.StringUtils;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CarrinhoServices {

    private final CarrinhoRepository carrinhoRepository;
    private final ClienteRepository clienteRepository;
    private final ItemCardapioRepository itemCardapioRepository;
    private final Clock clock;

    private final RecuperarCarrinhoPolicy recuperarCarrinhoPolicy;
    private final SalvarClientePolicy salvarClientePolicy;

    public CarrinhoServices(CarrinhoRepository carrinhoRepository,
                            ClienteRepository clienteRepository,
                            ItemCardapioRepository itemCardapioRepository,
                            Clock clock) {
        this.carrinhoRepository = carrinhoRepository;
        this.clienteRepository = clienteRepository;
        this.itemCardapioRepository = itemCardapioRepository;
        this.clock = clock;

        this.recuperarCarrinhoPolicy = new RecuperarCarrinhoPolicy();
        this.salvarClientePolicy = new SalvarClientePolicy();
    }

    /**
     * @apiNote Este método executa múltiplas operações nos repositórios. É esperado que seja
     * executado em um contexto transacional a ser fornecido pelos serviços de infraestrutura
     */
    public Carrinho criarCarrinho(@NotNull CarrinhoParam param) {
        boolean clienteIdentificado = param.isClienteIdentificado();

        param.getCpfValidado(); // Throw early if invalid

        if (clienteIdentificado) {
            var carrinhoSalvo = recuperarCarrinhoPolicy.tryRecuperarCarrinho(
                    new IdCliente(Objects.requireNonNull(param.idCliente, "Unexpected state: idCliente is null")));
            if (carrinhoSalvo != null) {
                var itens = itemCardapioRepository.findByCarrinho(Objects.requireNonNull(carrinhoSalvo.id(), "Object from database should have ID"));
                return carrinhoSalvo.withItens(itens);
            }
        }

        Carrinho newCarrinho;
        if (clienteIdentificado) {
            var cliente = clienteRepository.getClienteById(param.idCliente);
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente invalido! " + param.idCliente);
            }

            newCarrinho = Carrinho.newCarrinhoVazioClienteIdentificado(
                    Objects.requireNonNull(cliente.id(), "Unexpected state: cliente.id() is null"), clock.localDateTime());
        } else {
            Cliente novoCliente = salvarClientePolicy.salvarClienteSeDadosCompletos(param);

            if (novoCliente == null) {
                newCarrinho = Carrinho.newCarrinhoVazioClienteNaoIdentificado(
                        Objects.requireNonNull(param.nomeCliente, "Unexpected state: nomeCliente is null"), clock.localDateTime());
            } else {
                newCarrinho = Carrinho.newCarrinhoVazioClienteIdentificado(
                        Objects.requireNonNull(novoCliente.id(), "Unexpected state: novoCliente.id() is null"), clock.localDateTime());
            }
        }

        return carrinhoRepository.salvarCarrinhoVazio(newCarrinho);
    }

    /**
     * Parâmetros para criação de carrinho. Oferece tres possíveis combinações de atributos:
     * <ul>
     *     <li>idCliente: Para associar o carrinho a um cliente cadastrado</li>
     *     <li>Apenas nomeCliente: Cliente não identificado, chamar pelo nome apenas para este pedido</li>
     *     <li>nomeCliente, cpf, email: Cadastra o cliente para próximos pedidos</li>
     * </ul>
     */
    public record CarrinhoParam(
            @Nullable Integer idCliente,

            @Nullable String nomeCliente,

            @Nullable String cpf,
            @Nullable String email
    ) {
        private boolean isClienteIdentificado() {
            if (idCliente != null && StringUtils.isEmpty(nomeCliente) && StringUtils.isEmpty(cpf) && StringUtils.isEmpty(email)) {
                return true;
            } else if (idCliente == null && StringUtils.isNotEmpty(nomeCliente)) {
                return false;
            } else {
                throw new IllegalArgumentException("Combinação de parâmetros inválidos. Usar {idCliente} " +
                        "ou {nomeCliente} ou {nomeCliente, cpf, email}");
            }
        }

        @Nullable
        private Cpf getCpfValidado() {
            if (StringUtils.isEmpty(cpf)) {
                return null;
            } else {
                return new Cpf(cpf);
            }
        }
    }

    /**
     * Política do fluxo de compra: Se o cliente se identificou e há um carrinho salvo,
     * carrega para permitir continuar a compra
     */
    private class RecuperarCarrinhoPolicy {
        Carrinho tryRecuperarCarrinho(IdCliente idCliente) {
            return carrinhoRepository.getCarrinhoSalvoByCliente(idCliente);
        }
    }

    /**
     * Política do fluxo de compra: Se foram informados dados completos cadastrar o cliente
     */
    private class SalvarClientePolicy {
        Cliente salvarClienteSeDadosCompletos(CarrinhoParam param) {
            Cpf cpf = param.getCpfValidado();
            if (StringUtils.isNotEmpty(param.nomeCliente) && StringUtils.isNotEmpty(param.email) && cpf != null) {
                Cliente checkExistente = clienteRepository.getClienteByCpf(cpf);
                if (checkExistente != null) {
                    throw new IllegalArgumentException("Cliente com CPF " + param.cpf + " já cadastrado");
                }

                Cliente newCliente = new Cliente(null, param.nomeCliente, cpf, param.email);
                return clienteRepository.salvarCliente(newCliente);
            } else {
                return null;
            }
        }
    }
}
