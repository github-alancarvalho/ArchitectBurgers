package com.example.gomesrodris.archburgers.model.entities;

public record ItemCardapio(int id, Tipo tipo, String nome, String descricao) {

    public enum Tipo {
        SANDUICHE,
        ACOMPANHAMENTO,
        BEBIDA,
        SOBREMESA
    }
}
