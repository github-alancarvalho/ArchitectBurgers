package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.repositories.PedidoRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.InfoPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the Repository based on a relational database via JDBC
 */
@Repository
public class PedidoRepositoryJdbcImpl implements PedidoRepository {
    @Language("SQL")
    private static final String SQL_SELECT_PEDIDO_BY_ID = """
            select pedido_id,id_cliente_identificado,nome_cliente_nao_identificado,
               observacoes,status,forma_pagamento,data_hora_pedido
            from pedido where pedido_id = ?
            """;

    @Language("SQL")
    private static final String SQL_SELECT_PEDIDOS_BY_STATUS = """
            select pedido_id,id_cliente_identificado,nome_cliente_nao_identificado,
               observacoes,status,forma_pagamento,data_hora_pedido
            from pedido where status = ?
            """;

    @Language("SQL")
    private static final String SQL_INSERT_PEDIDO = """
            insert into pedido (id_cliente_identificado,nome_cliente_nao_identificado,
               observacoes,status,forma_pagamento,data_hora_pedido)
            values (?,?,?,?,?,?) returning pedido_id;
            """;

    @Language("SQL")
    private static final String SQL_UPDATE_STATUS = """
            update pedido set status = ? where pedido_id = ?;
            """;

    private final DatabaseConnection databaseConnection;

    public PedidoRepositoryJdbcImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public Pedido getPedido(int idPedido) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(SQL_SELECT_PEDIDO_BY_ID)) {
            stmt.setInt(1, idPedido);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                return null;

            return pedidoFromResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public Pedido savePedido(Pedido pedido) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(SQL_INSERT_PEDIDO)) {

            if (pedido.idClienteIdentificado() != null) {
                stmt.setInt(1, pedido.idClienteIdentificado().id());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            stmt.setString(2, pedido.nomeClienteNaoIdentificado());
            stmt.setString(3, pedido.observacoes());
            stmt.setString(4, pedido.status().name());
            stmt.setString(5, pedido.infoPagamento().formaPagamento().name());
            stmt.setObject(6, pedido.dataHoraPedido());

            var rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new IllegalStateException("Unexpected state, query should return");
            }

            var id = rs.getInt(1);

            return pedido.withId(id);

        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pedido> listPedidos(@NotNull StatusPedido filtroStatus) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(SQL_SELECT_PEDIDOS_BY_STATUS)) {
            stmt.setString(1, filtroStatus.name());

            ResultSet rs = stmt.executeQuery();
            List<Pedido> result = new ArrayList<>();

            while (rs.next()) {
                result.add(pedidoFromResultSet(rs));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateStatus(Pedido pedido) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(SQL_UPDATE_STATUS)) {

            stmt.setString(1, pedido.status().name());
            stmt.setInt(2, Objects.requireNonNull(pedido.id(), "Must have ID to update"));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }

    private static @NotNull Pedido pedidoFromResultSet(ResultSet rs) throws SQLException {
        var idClienteIdentificado = rs.getInt("id_cliente_identificado");

        FormaPagamento formaPagamento = null;
        try {
            formaPagamento = FormaPagamento.fromName(rs.getString("forma_pagamento"));
        } catch (SQLException e) {
            throw new RuntimeException("Registro inconsistente! formaPagamento=" + rs.getString("forma_pagamento"));
        }

        StatusPedido status = null;
        try {
            status = StatusPedido.valueOf(rs.getString("status"));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Registro inconsistente! status=" + rs.getString("status"));
        }

        return new Pedido(
                rs.getInt("pedido_id"),
                idClienteIdentificado > 0 ? new IdCliente(idClienteIdentificado) : null,
                rs.getString("nome_cliente_nao_identificado"),
                Collections.emptyList(),
                rs.getString("observacoes"),
                status,
                new InfoPagamento(formaPagamento),
                rs.getTimestamp("data_hora_pedido").toLocalDateTime()
        );
    }
}
