package com.example.gomesrodris.archburgers.domain.valueobjects;

public record FormaPagamento(
        IdFormaPagamento id,
        String descricao,
        Boolean integracaoExterna
) {
    public static final FormaPagamento DINHEIRO = new FormaPagamento(
            new IdFormaPagamento("DINHEIRO"),
            "Pagamento em dinheiro direto ao caixa", false);

    public static final FormaPagamento CARTAO_MAQUINA = new FormaPagamento(
            new IdFormaPagamento("CARTAO_MAQUINA"),
            "Pagamento em cartão em máquina simples sem integração", false);
}
