package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public final class Carrinho {
    private final @Nullable Integer id;
    private final @Nullable IdCliente idClienteIdentificado;
    private final @Nullable String nomeClienteNaoIdentificado;
    private final @NotNull List<ItemCardapio> itens;
    private final @Nullable String observacoes;
    private final @NotNull LocalDateTime dataHoraCarrinhoCriado;

    public static Carrinho carrinhoSalvoClienteIdentificado(@NotNull Integer id,
                                                            @NotNull IdCliente idCliente,
                                                            @Nullable String observacoes,
                                                            @NotNull LocalDateTime dataHoraCarrinhoCriado) {
        return new Carrinho(id, idCliente, null, List.of(), observacoes, dataHoraCarrinhoCriado);
    }

    public static Carrinho newCarrinhoVazioClienteIdentificado(@NotNull IdCliente idCliente,
                                                               @NotNull LocalDateTime dataHoraCarrinhoCriado) {
        return new Carrinho(null, idCliente, null, List.of(), null, dataHoraCarrinhoCriado);
    }

    public static Carrinho newCarrinhoVazioClienteNaoIdentificado(@NotNull String nomeCliente,
                                                                  @NotNull LocalDateTime dataHoraCarrinhoCriado) {
        return new Carrinho(null, null, nomeCliente, List.of(), null, dataHoraCarrinhoCriado);
    }

    private Carrinho(
            @Nullable Integer id,
            @Nullable IdCliente idClienteIdentificado,
            @Nullable String nomeClienteNaoIdentificado,

            @NotNull List<ItemCardapio> itens,

            @Nullable String observacoes,

            @NotNull LocalDateTime dataHoraCarrinhoCriado
    ) {
        this.id = id;
        this.idClienteIdentificado = idClienteIdentificado;
        this.nomeClienteNaoIdentificado = nomeClienteNaoIdentificado;
        this.itens = itens;
        this.observacoes = observacoes;
        this.dataHoraCarrinhoCriado = dataHoraCarrinhoCriado;
    }

    public ValorMonetario getValorTotal() {
        return ItemCardapio.somarValores(itens);
    }

    public Carrinho withId(Integer newId) {
        return new Carrinho(newId, idClienteIdentificado, nomeClienteNaoIdentificado, itens, observacoes, dataHoraCarrinhoCriado);
    }

    public Carrinho withItens(List<ItemCardapio> newItens) {
        return new Carrinho(id, idClienteIdentificado, nomeClienteNaoIdentificado,
                newItens, observacoes, dataHoraCarrinhoCriado);
    }

    public @Nullable Integer id() {
        return id;
    }

    public @Nullable IdCliente idClienteIdentificado() {
        return idClienteIdentificado;
    }

    public @Nullable String nomeClienteNaoIdentificado() {
        return nomeClienteNaoIdentificado;
    }

    public @NotNull List<ItemCardapio> itens() {
        return itens;
    }

    public @Nullable String observacoes() {
        return observacoes;
    }

    public @NotNull LocalDateTime dataHoraCarrinhoCriado() {
        return dataHoraCarrinhoCriado;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Carrinho) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.idClienteIdentificado, that.idClienteIdentificado) &&
                Objects.equals(this.nomeClienteNaoIdentificado, that.nomeClienteNaoIdentificado) &&
                Objects.equals(this.itens, that.itens) &&
                Objects.equals(this.observacoes, that.observacoes) &&
                Objects.equals(this.dataHoraCarrinhoCriado, that.dataHoraCarrinhoCriado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idClienteIdentificado, nomeClienteNaoIdentificado, itens, observacoes, dataHoraCarrinhoCriado);
    }

    @Override
    public String toString() {
        return "Carrinho[" +
                "id=" + id + ", " +
                "idClienteIdentificado=" + idClienteIdentificado + ", " +
                "nomeClienteNaoIdentificado=" + nomeClienteNaoIdentificado + ", " +
                "itens=" + itens + ", " +
                "observacoes=" + observacoes + ", " +
                "dataHoraCarrinhoCriado=" + dataHoraCarrinhoCriado + ']';
    }

}
