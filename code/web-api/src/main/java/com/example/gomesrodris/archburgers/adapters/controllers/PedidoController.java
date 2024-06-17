package com.example.gomesrodris.archburgers.adapters.controllers;

import com.example.gomesrodris.archburgers.adapters.driven.infra.TransactionManager;
import com.example.gomesrodris.archburgers.adapters.dto.PedidoDto;
import com.example.gomesrodris.archburgers.apiutils.WebUtils;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.usecaseports.PedidoUseCasesPort;
import com.example.gomesrodris.archburgers.domain.usecases.PedidoUseCases;
import com.example.gomesrodris.archburgers.domain.utils.StringUtils;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PedidoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoController.class);

    private final PedidoUseCasesPort pedidoUseCases;
    private final TransactionManager transactionManager;

    @Autowired
    public PedidoController(PedidoUseCasesPort pedidoUseCases, TransactionManager transactionManager) {
        this.pedidoUseCases = pedidoUseCases;
        this.transactionManager = transactionManager;
    }

    @Operation(summary = "Cria um pedido a partir do carrinho informado",
            description = "O pedido recebe todos os itens do carrinho, e após a criação do pedido o carrinho é excluído")
    @PostMapping(path = "/pedidos")
    public ResponseEntity<PedidoDto> criarPedido(@RequestBody PedidoUseCases.CriarPedidoParam param) {

        Pedido pedido;
        try {
            pedido = transactionManager.runInTransaction(() -> pedidoUseCases.criarPedido(param));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao criar pedido: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao criar pedido");
        }

        return WebUtils.okResponse(PedidoDto.fromEntity(pedido));
    }

    @Operation(summary = "Lista os pedidos conforme critério informado",
            parameters = {
                    @Parameter(name = "status", description = "Filtra por status do pedido"),
                    @Parameter(name = "atraso", description = "Filtra pedidos em atraso (RECEBIDO ou EM PREPARAÇÃO criados há mais de 20 minutos)")
            })
    @GetMapping(path = "/pedidos")
    public ResponseEntity<List<PedidoDto>> listarPedidos(
            @RequestParam(value = "status", required = false) String filtroStatus,
            @RequestParam(value = "atraso", required = false) String filtroAtraso) {
        List<Pedido> result;
        try {
            StatusPedido parsedFiltroStatus = StringUtils.isEmpty(filtroStatus)
                    ? null : StatusPedido.valueOf(filtroStatus);

            boolean isFiltroAtraso = Boolean.parseBoolean(filtroAtraso);

            if (isFiltroAtraso) {
                result = pedidoUseCases.listarPedidosComAtraso();
            } else {
                result = pedidoUseCases.listarPedidosByStatus(parsedFiltroStatus);
            }

        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao listar pedidos: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao listar pedidos");
        }

        return WebUtils.okResponse(result.stream().map(PedidoDto::fromEntity).toList());
    }

    @PostMapping(path = "/pedidos/{idPedido}/validar")
    public ResponseEntity<PedidoDto> validarPedido(@PathVariable("idPedido") Integer idPedido) {
        Pedido pedido;
        try {
            pedido = transactionManager.runInTransaction(() -> pedidoUseCases.validarPedido(idPedido));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao atualizar pedido: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao atualizar pedido");
        }

        return WebUtils.okResponse(PedidoDto.fromEntity(pedido));
    }

    @PostMapping(path = "/pedidos/{idPedido}/cancelar")
    public ResponseEntity<PedidoDto> cancelarPedido(@PathVariable("idPedido") Integer idPedido) {
        Pedido pedido;
        try {
            pedido = transactionManager.runInTransaction(() -> pedidoUseCases.cancelarPedido(idPedido));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao atualizar pedido: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao atualizar pedido");
        }

        return WebUtils.okResponse(PedidoDto.fromEntity(pedido));
    }

    @PostMapping(path = "/pedidos/{idPedido}/setPronto")
    public ResponseEntity<PedidoDto> setPedidoPronto(@PathVariable("idPedido") Integer idPedido) {
        Pedido pedido;
        try {
            pedido = transactionManager.runInTransaction(() -> pedidoUseCases.setPronto(idPedido));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao atualizar pedido: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao atualizar pedido");
        }

        return WebUtils.okResponse(PedidoDto.fromEntity(pedido));
    }

    @PostMapping(path = "/pedidos/{idPedido}/finalizar")
    public ResponseEntity<PedidoDto> finalizarPedido(@PathVariable("idPedido") Integer idPedido) {
        Pedido pedido;
        try {
            pedido = transactionManager.runInTransaction(() -> pedidoUseCases.finalizarPedido(idPedido));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao atualizar pedido: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao atualizar pedido");
        }

        return WebUtils.okResponse(PedidoDto.fromEntity(pedido));
    }
}
