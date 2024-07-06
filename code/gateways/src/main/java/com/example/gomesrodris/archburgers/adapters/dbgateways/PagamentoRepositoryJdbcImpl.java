package com.example.gomesrodris.archburgers.adapters.dbgateways;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.repositories.PagamentoRepository;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * Implementation of the Repository based on a relational database via JDBC
 */
@Repository
public class PagamentoRepositoryJdbcImpl implements PagamentoRepository {
    @Language("SQL")
    private static final String SQL_INSERT = """
                insert into pagamento (id_pedido,forma_pagamento,valor,
                    status,data_hora_criacao,data_hora_atualizacao,codigo_pagamento_cliente,id_pedido_sistema_externo)
                values (?,?,?,?,?,?,?,?)
                returning pagamento_id
            """.stripIndent();

    private final DatabaseConnection databaseConnection;

    @Autowired
    public PagamentoRepositoryJdbcImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public Pagamento salvarPagamento(Pagamento pagamento) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(SQL_INSERT)) {
            stmt.setInt(1, pagamento.idPedido());
            stmt.setString(2, pagamento.formaPagamento().codigo());
            stmt.setObject(3, pagamento.valor().asBigDecimal());
            stmt.setString(4, pagamento.status().name());
            stmt.setObject(5, pagamento.dataHoraCriacao());
            stmt.setObject(6, pagamento.dataHoraAtualizacao());
            stmt.setString(7, pagamento.codigoPagamentoCliente());
            stmt.setString(8, pagamento.idPedidoSistemaExterno());

            var rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new IllegalStateException("Inconsistent state! Insert was supposed to return 1");
            }

            return pagamento.withId(rs.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }
}
