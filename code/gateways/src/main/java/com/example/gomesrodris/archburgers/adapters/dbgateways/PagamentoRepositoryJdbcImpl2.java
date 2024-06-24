package com.example.gomesrodris.archburgers.adapters.dbgateways;

import com.example.gomesrodris.archburgers.domain.entities.ConfirmacaoPagamento;
import com.example.gomesrodris.archburgers.domain.repositories.PagamentoRepository;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * Implementation of the Repository based on a relational database via JDBC
 */
@Repository
public class PagamentoRepositoryJdbcImpl2 implements PagamentoRepository {
    @Language("SQL")
    private static final String SQL_INSERT = """
                insert into confirmacao_pagamento (forma_pagamento, data_hora_pagamento, info_adicional)
                values (?,?,?)
                returning pagamento_id
            """.stripIndent();

    private final DatabaseConnection databaseConnection;

    @Autowired
    public PagamentoRepositoryJdbcImpl2(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public ConfirmacaoPagamento salvarConfirmacaoPagamento(ConfirmacaoPagamento confirmacaoPagamento) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(SQL_INSERT)) {
            stmt.setString(1, confirmacaoPagamento.formaPagamento().name());
            stmt.setObject(2, confirmacaoPagamento.dataHoraPagamento());
            stmt.setString(3, confirmacaoPagamento.infoAdicional());

            var rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new IllegalStateException("Inconsistent state! Insert was supposed to return 1");
            }

            return confirmacaoPagamento.withId(rs.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }
}
