package com.example.gomesrodris.archburgers.adapters.controllers;

import com.example.gomesrodris.archburgers.adapters.datasource.TransactionManager;
import com.example.gomesrodris.archburgers.adapters.dto.ConfirmacaoPagamentoDto;
import com.example.gomesrodris.archburgers.adapters.dto.PagamentoDto;
import com.example.gomesrodris.archburgers.adapters.dto.PedidoDto;
import com.example.gomesrodris.archburgers.adapters.presenters.QrCodePresenter;
import com.example.gomesrodris.archburgers.apiutils.WebUtils;
import com.example.gomesrodris.archburgers.controller.PagamentoController;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.exception.DomainArgumentException;
import com.example.gomesrodris.archburgers.domain.usecaseparam.DescricaoFormaPagamento;
import com.example.gomesrodris.archburgers.domain.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PagamentoApiHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PagamentoApiHandler.class);

    private final PagamentoController pagamentoController;
    private final TransactionManager transactionManager;
    private final QrCodePresenter qrCodePresenter;

    public PagamentoApiHandler(PagamentoController pagamentoController,
                               TransactionManager transactionManager,
                               QrCodePresenter qrCodePresenter) {
        this.pagamentoController = pagamentoController;
        this.transactionManager = transactionManager;
        this.qrCodePresenter = qrCodePresenter;
    }

    @Operation(summary = "Lista opcoes de pagamento disponiveis")
    @GetMapping("/pagamento/opcoes")
    public List<DescricaoFormaPagamento> listFormasPagamento() {
        return pagamentoController.listarFormasPagamento();
    }

    @Operation(summary = "Grava confirmação de pagamento para o pedido, movendo o mesmo para status RECEBIDO. Para formas de pagamento sem integração externa")
    @PostMapping("/pagamento/confirmacao")
    public ResponseEntity<PedidoDto> confirmacaoPagamento(@RequestBody ConfirmacaoPagamentoDto param) {
        Pedido pedidoUpdated;
        try {
            if (param == null)
                throw new DomainArgumentException("Request obj deve ser informado");
            if (param.idPedido() == null)
                throw new DomainArgumentException("idPedido deve ser informado");

            pedidoUpdated = transactionManager.runInTransaction(() -> pagamentoController.finalizarPagamento(param.idPedido(), null));
        } catch (DomainArgumentException ae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, ae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao salvar confirmacao de pagamento: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao salvar confirmacao de pagamento");
        }

        return WebUtils.okResponse(PedidoDto.fromEntity(pedidoUpdated));
    }

    @GetMapping("/pagamento/consulta/{idPedido}")
    public ResponseEntity<PagamentoDto> consultarPagamento(@PathVariable("idPedido") String idPedidoParam) {
        try {
            Pagamento pagamento = getPagamento(idPedidoParam);

            if (pagamento == null) {
                return WebUtils.errorResponse(HttpStatus.NOT_FOUND, "Pagamento para Pedido [" + idPedidoParam + "] não encontrado");
            }

            return WebUtils.okResponse(PagamentoDto.fromEntity(pagamento));

        } catch (DomainArgumentException ae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, ae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao obter pagamento: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao obter pagamento");
        }
    }

    @GetMapping(value = "/pagamento/consulta/{idPedido}/qrcode")
    public ResponseEntity<byte[]> consultarPagamentoQrCode(@PathVariable("idPedido") String idPedidoParam) {
        try {
            Pagamento pagamento = getPagamento(idPedidoParam);

            if (pagamento == null) {
                return WebUtils.errorResponse(HttpStatus.NOT_FOUND, "Pagamento para Pedido [" + idPedidoParam + "] não encontrado");
            }

            if (StringUtils.isEmpty(pagamento.codigoPagamentoCliente())) {
                return WebUtils.errorResponse(HttpStatus.NOT_FOUND, "Pedido [" + idPedidoParam + "] não contém um código de pagamento");
            }

            byte[] image = qrCodePresenter.renderQrCode(pagamento.codigoPagamentoCliente());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(image);

        } catch (DomainArgumentException ae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, ae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao obter pagamento: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao obter pagamento");
        }
    }

    private Pagamento getPagamento(String idPedidoParam) {
        if (idPedidoParam == null)
            throw new DomainArgumentException("idPedido path param deve ser informado");

        int idPedido;
        try {
            idPedido = Integer.parseInt(idPedidoParam);
        } catch (NumberFormatException e) {
            throw new DomainArgumentException("idPedido invalido");
        }

        return pagamentoController.consultarPagamento(idPedido);
    }
}
