package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CarrinhoRepositoryJdbcImpl implements CarrinhoRepository {
    @Language("SQL")
    private final String SQL_SELECT_CARRINHO_BY_CLIENTE = """
                select carrinho_id,id_cliente_identificado,nome_cliente_nao_identificado,observacoes,data_hora_criado
                from carrinho where id_cliente_identificado = ?
            """;

    private final DatabaseConnection databaseConnection;

    @Autowired
    public CarrinhoRepositoryJdbcImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public Carrinho getCarrinhoSalvoByCliente(IdCliente idCliente) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(SQL_SELECT_CARRINHO_BY_CLIENTE)) {
            stmt.setInt(1, idCliente.id());

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                return null;

            int rsIdCliente = rs.getInt("id_cliente_identificado");

            return Carrinho.carrinhoSalvoClienteIdentificado(rs.getInt("carrinho_id"),
                    new IdCliente(rsIdCliente),
                    rs.getString("observacoes"),
                    rs.getTimestamp("data_hora_criado").toLocalDateTime());
        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public Carrinho salvarCarrinho(Carrinho carrinho) {
        return null;
    }
}
