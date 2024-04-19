package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.DomainConstants;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCardapioRepositoryJdbcImpl.class);

    public static final String SQL_SELECT_ITEMS_BY_TIPO = """
        select item_cardapio_id, tipo, nome, descricao, valor
        from item_cardapio
        where tipo = ?
    """.stripIndent();

    private final ConnectionPool connectionPool;

    @Autowired
    public ItemCardapioRepositoryJdbcImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<ItemCardapio> findByTipo(TipoItemCardapio tipo) {
        try (var connection = connectionPool.getConnection();
             var stmt = connection.prepareStatement(SQL_SELECT_ITEMS_BY_TIPO)) {
            stmt.setString(1, tipo.getAbreviacao());

            ResultSet rs = stmt.executeQuery();
            List<ItemCardapio> results = new ArrayList<>();

            while (rs.next()) {
                results.add(new ItemCardapio(
                        rs.getInt(1),
                        TipoItemCardapio.getByAbreviacao(rs.getString(2)),
                        rs.getString(3),
                        rs.getString(4),
                        Money.of(rs.getBigDecimal(5), DomainConstants.CODIGO_MOEDA)
                ));
            }

            return results;
        } catch (SQLException e) {
            LOGGER.error("Error while fetching data from database: {}", e, e);
            throw new RuntimeException(e);
        }
    }
}
