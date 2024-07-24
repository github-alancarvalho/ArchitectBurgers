package com.example.gomesrodris.archburgers.domain.usecaseparam;

import com.example.gomesrodris.archburgers.domain.utils.StringUtils;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
import org.jetbrains.annotations.Nullable;

/**
 * Parâmetros para criação de carrinho. Oferece tres possíveis combinações de atributos:
 * <ul>
 *     <li>idCliente: Para associar o carrinho a um cliente cadastrado</li>
 *     <li>Apenas nomeCliente: Cliente não identificado, chamar pelo nome apenas para este pedido</li>
 *     <li>nomeCliente, cpf, email: Cadastra o cliente para próximos pedidos</li>
 * </ul>
 */
public record CriarCarrinhoParam(
        @Nullable Integer idCliente,

        @Nullable String nomeCliente,

        @Nullable String cpf,
        @Nullable String email
) {
    public boolean isClienteIdentificado() {
        if (idCliente != null && StringUtils.isEmpty(nomeCliente) && StringUtils.isEmpty(cpf) && StringUtils.isEmpty(email)) {
            return true;
        } else if (idCliente == null && StringUtils.isNotEmpty(nomeCliente)) {
            return false;
        } else {
            throw new IllegalArgumentException("Combinação de parâmetros inválidos. Usar {idCliente} " +
                    "ou {nomeCliente} ou {nomeCliente, cpf, email}");
        }
    }

    @Nullable
    public Cpf getCpfValidado() {
        if (StringUtils.isEmpty(cpf)) {
            return null;
        } else {
            return new Cpf(cpf);
        }
    }
}
