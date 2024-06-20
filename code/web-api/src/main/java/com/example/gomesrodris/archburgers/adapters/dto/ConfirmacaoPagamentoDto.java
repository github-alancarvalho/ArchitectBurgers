package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.domain.exception.DomainArgumentException;
import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;

public record ConfirmacaoPagamentoDto(
        Integer idPedido,
        String formaPagamento,
        String infoAdicional) {

    public int validarIdPedido() {
        if (idPedido == null)
            throw new DomainArgumentException("idPedido deve ser informado");
        return idPedido;
    }

    public FormaPagamento validarFormaPagamento() {
        if (formaPagamento == null)
            throw new DomainArgumentException("formaPagamento deve ser informado");

        return FormaPagamento.valueOf(formaPagamento);
    }
}
