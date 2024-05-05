package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
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
    @Language("SQL")
    private static final String SQL_SELECT_ALL_ITEMS = """
        select item_cardapio_id, tipo, nome, descricao, valor
        from item_cardapio
    """.stripIndent();

    @Language("SQL")
    private static final String SQL_SELECT_BY_CARRINHO = """
        select item.item_cardapio_id, item.tipo, item.nome, item.descricao, item.valor
        from item_cardapio item 
        inner join carrinho_item ci ON ci.item_cardapio_id = item.item_cardapio_id
        where ci.carrinho_id = ?
    """.stripIndent();

    private final DatabaseConnection databaseConnection;

    @Autowired
    public ItemCardapioRepositoryJdbcImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public List<ItemCardapio> findAll() {
        return getItems(SQL_SELECT_ALL_ITEMS, null);
    }

    @Override
    public List<ItemCardapio> findByCarrinho(int idCarrinho) {
        return getItems(SQL_SELECT_BY_CARRINHO, idCarrinho);
    }

    private @NotNull List<ItemCardapio> getItems(String query, Integer param) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(query)) {

            if (param != null) {
                stmt.setInt(1, param);
            }

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
