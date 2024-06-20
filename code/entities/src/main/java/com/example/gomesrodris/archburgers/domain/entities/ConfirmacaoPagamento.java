package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public record ConfirmacaoPagamento(
        @Nullable Integer id,
        @NotNull FormaPagamento formaPagamento,
        @Nullable String infoAdicional,
        @NotNull LocalDateTime dataHoraPagamento
) {
    public ConfirmacaoPagamento withId(Integer newId) {
        return new ConfirmacaoPagamento(newId, formaPagamento, infoAdicional, dataHoraPagamento);
    }
}
