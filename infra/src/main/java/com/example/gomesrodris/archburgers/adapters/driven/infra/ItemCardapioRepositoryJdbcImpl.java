package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
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
    private static final String SQL_SELECT_BY_ID = """
                select item_cardapio_id, tipo, nome, descricao, valor
                from item_cardapio
                where item_cardapio_id = ?
            """.stripIndent();

    @Language("SQL")
    private static final String SQL_SELECT_ALL_ITEMS = """
                select item_cardapio_id, tipo, nome, descricao, valor
                from item_cardapio 
                order by case 
                    when tipo = 'L' then 1
                    when tipo = 'A' then 2
                    when tipo = 'B' then 3
                    when tipo = 'S' then 4
                    end
                asc, item_cardapio_id asc
            """.stripIndent();

    @Language("SQL")
    private static final String SQL_SELECT_BY_CARRINHO = """
                select ci.num_sequencia, item.item_cardapio_id, item.tipo, item.nome, item.descricao, item.valor
                from item_cardapio item 
                inner join carrinho_item ci ON ci.item_cardapio_id = item.item_cardapio_id
                where ci.carrinho_id = ?
                order by ci.num_sequencia
            """.stripIndent();

    private final DatabaseConnection databaseConnection;

    @Autowired
    public ItemCardapioRepositoryJdbcImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public ItemCardapio findById(int id) {
        var result = getItems(SQL_SELECT_BY_ID, id, ItemCardapio.class);
        if (result.isEmpty())
            return null;

        return result.getFirst();
    }

    @Override
    public List<ItemCardapio> findAll() {
        return getItems(SQL_SELECT_ALL_ITEMS, null, ItemCardapio.class);
    }

    @Override
    public List<ItemPedido> findByCarrinho(int idCarrinho) {
        return getItems(SQL_SELECT_BY_CARRINHO, idCarrinho, ItemPedido.class);
    }

    private <T> @NotNull List<T> getItems(String query, Integer param, Class<T> returnClass) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(query)) {

            if (param != null) {
                stmt.setInt(1, param);
            }

            ResultSet rs = stmt.executeQuery();
            List<T> results = new ArrayList<>();

            while (rs.next()) {
                ItemCardapio itemCardapio = new ItemCardapio(
                        rs.getInt("item_cardapio_id"),
                        TipoItemCardapio.getByAbreviacao(rs.getString("tipo")),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        new ValorMonetario(rs.getBigDecimal("valor"))
                );

                if (returnClass == ItemCardapio.class) {
                    results.add((T) itemCardapio);

                } else if (returnClass == ItemPedido.class) {
                    ItemPedido itemPedido = new ItemPedido(
                            rs.getInt("num_sequencia"),
                            itemCardapio
                    );
                    results.add((T) itemPedido);
                } else {
                    throw new IllegalArgumentException("Unsupported type: " + returnClass.getName());
                }
            }

            return results;
        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }



}
