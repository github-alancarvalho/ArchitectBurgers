package com.example.gomesrodris.archburgers.domain.valueobjects;

public enum FormaPagamento {
    DINHEIRO("Pagamento em dinheiro direto ao caixa");

    private final String descricao;

    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
