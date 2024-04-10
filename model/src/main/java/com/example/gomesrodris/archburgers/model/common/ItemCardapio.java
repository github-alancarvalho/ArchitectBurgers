package com.example.gomesrodris.archburgers.model.common;

public record ItemCardapio(int id, Tipo tipo, String nome, String descricao) {

    public enum Tipo {
        SANDUICHE,
        ACOMPANHAMENTO,
        BEBIDA,
        SOBREMESA
    }
}
