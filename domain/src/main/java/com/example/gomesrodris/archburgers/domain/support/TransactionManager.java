package com.example.gomesrodris.archburgers.domain.support;

import java.util.function.Supplier;

/**
 * Permite ao serviço de negócio declarar que uma sequência de operações deve ser executada em
 * uma transação ("tudo ou nada").
 * <br />
 * Os adapters de infraestrutura serão responsáveis por fornecer uma implementação apropriada
 * que respeite as propriedades esperadas da Transação.
 */
public interface TransactionManager {
    <T> T runInTransaction(Supplier<T> task) throws Exception;
}
