package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Repository based on a relational database via JDBC
 */
@Repository
public class ItemCardapioRepositoryJdbcImpl implements ItemCardapioRepository {
    public static final String SQL_SELECT_ITEMS_BY_TIPO = """
        select item_cardapio_id, tipo, nome, descricao, valor
        from item_cardapio
    """.stripIndent();

    private final ConnectionPool connectionPool;

    @Autowired
    public ItemCardapioRepositoryJdbcImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<ItemCardapio> findAll() {
        try (var connection = connectionPool.getConnection();
             var stmt = connection.prepareStatement(SQL_SELECT_ITEMS_BY_TIPO)) {

            ResultSet rs = stmt.executeQuery();
            List<ItemCardapio> results = new ArrayList<>();

            while (rs.next()) {
                results.add(new ItemCardapio(
                        rs.getInt(1),
                        TipoItemCardapio.getByAbreviacao(rs.getString(2)),
                        rs.getString(3),
                        rs.getString(4),
                        new ValorMonetario(rs.getBigDecimal(5))
                ));
            }

            return results;
        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }
}
