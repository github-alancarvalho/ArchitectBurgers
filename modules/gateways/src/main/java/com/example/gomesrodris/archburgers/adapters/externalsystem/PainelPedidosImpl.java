package com.example.gomesrodris.archburgers.adapters.externalsystem;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.external.PainelPedidos;
import org.springframework.stereotype.Service;

/**
 * Adapter para o Painel de Pedidos, que faz o envio das notificações para o sistema do Painel
 */
@Service
public class PainelPedidosImpl implements PainelPedidos {
    /**
     * Implementação dummy. Serviço real do Painel de Pedidos ainda não disponível!
     */
    @Override
    public void notificarPedidoPronto(Pedido pedido) {
        System.out.println(
                "--------------------------------------------------------------\n" +
                "--  NOTIFICAÇÃO PEDIDO PRONTO      : " + pedido.id() + "\n" +
                "--------------------------------------------------------------\n");
    }
}
