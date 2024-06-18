package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;

public record ConfirmacaoPagamento(
        Integer id,
        FormaPagamento formaPagamento,
        ValorMonetario valor,
        String infoAdicional
) {
}
