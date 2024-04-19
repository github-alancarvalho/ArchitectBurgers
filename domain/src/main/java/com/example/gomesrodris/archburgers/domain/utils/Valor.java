package com.example.gomesrodris.archburgers.domain.utils;

import com.example.gomesrodris.archburgers.domain.DomainConstants;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

public class Valor {
    public static MonetaryAmount of(Number valor) {
        return Money.of(valor, DomainConstants.CODIGO_MOEDA);
    }

    public static MonetaryAmount of(String valorStr) {
        return Money.of(new BigDecimal(valorStr), DomainConstants.CODIGO_MOEDA);
    }
}
