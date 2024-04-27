package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.repositories.ClienteRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ClienteRepositoryJdbcImpl implements ClienteRepository {
    private final static String SQL_SELECT_CLIENTE_BY_CPF = "select cliente_id, nome, cpf, email from cliente where cpf = ?";

    private final DatabaseConnection databaseConnection;

    public ClienteRepositoryJdbcImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public @Nullable Cliente getClienteByCpf(@NotNull Cpf cpf) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(SQL_SELECT_CLIENTE_BY_CPF)) {
            stmt.setString(1, cpf.cpfNum());

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                return null;

            return new Cliente(
                    rs.getInt(1),
                    rs.getString(2),
                    new Cpf(rs.getString(3)),
                    rs.getString(4));
        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }
}
