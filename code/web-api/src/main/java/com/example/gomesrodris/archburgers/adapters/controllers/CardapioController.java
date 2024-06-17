package com.example.gomesrodris.archburgers.adapters.controllers;

import com.example.gomesrodris.archburgers.adapters.dto.GenericOperationResponse;
import com.example.gomesrodris.archburgers.adapters.dto.ItemCardapioDto;
import com.example.gomesrodris.archburgers.apiutils.WebUtils;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.usecaseports.CardapioUseCasesPort;
import com.example.gomesrodris.archburgers.domain.utils.StringUtils;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
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
public class CardapioController {
    private final Logger LOGGER = LoggerFactory.getLogger(CardapioController.class);

    private final CardapioUseCasesPort cardapioUseCases;

    @Autowired
    public CardapioController(CardapioUseCasesPort cardapioUseCases) {
        this.cardapioUseCases = cardapioUseCases;
    }

    @Operation(summary = "Consulta todos os itens do cardápio. Para uso do frontend de pedidos e também do sistema de administração do cardápio",
            parameters = {@Parameter(name = "tipo", description = "Filtro opcional por tipo (LANCHE, ACOMPANHAMENTO, BEBIDA, SOBREMESA)")})
    @GetMapping(path = "/cardapio")
    public ResponseEntity<List<ItemCardapioDto>> listItems(@RequestParam(value = "tipo", required = false) String tipo) {
        TipoItemCardapio filtroTipo;

        try {
            filtroTipo = StringUtils.isEmpty(tipo) ? null : TipoItemCardapio.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, "Tipo inválido: " + tipo);
        }

        List<ItemCardapio> result = cardapioUseCases.listarItensCardapio(filtroTipo);

        return WebUtils.okResponse(result.stream().map(ItemCardapioDto::fromEntity).toList());
    }

    @Operation(summary = "Grava um novo item no cardápio",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Não enviar os campos id e valor.formatted"))
    @PostMapping("/cardapio")
    public ResponseEntity<ItemCardapioDto> salvarNovoItem(@RequestBody ItemCardapioDto itemCardapioDto) {
        try {
            if (itemCardapioDto == null)
                throw new IllegalArgumentException("Missing request body");
            ItemCardapio item = itemCardapioDto.toEntity();
            if (item.id() != null)
                throw new IllegalArgumentException("Novo objeto não pode ter um ID");

            var saved = cardapioUseCases.salvarItemCardapio(item);
            return WebUtils.okResponse(ItemCardapioDto.fromEntity(saved));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao salvar novo item: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao salvar novo item");
        }
    }

    @Operation(summary = "Atualiza um item do cardápio")
    @PutMapping("/cardapio/{idItemCardapio}")
    public ResponseEntity<ItemCardapioDto> atualizarItem(@RequestBody ItemCardapioDto itemCardapioDto,
                                                         @PathVariable("idItemCardapio") Integer idItemCardapio) {
        try {
            if (idItemCardapio == null)
                throw new IllegalArgumentException("Missing idItemCardapio path param");
            if (itemCardapioDto == null)
                throw new IllegalArgumentException("Missing request body");

            ItemCardapio item = itemCardapioDto.toEntity().withId(idItemCardapio);

            var saved = cardapioUseCases.salvarItemCardapio(item);
            return WebUtils.okResponse(ItemCardapioDto.fromEntity(saved));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao salvar item: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao salvar item");
        }
    }

    @Operation(summary = "Exclui um item do cardápio")
    @DeleteMapping("/cardapio/{idItemCardapio}")
    public ResponseEntity<GenericOperationResponse> atualizarItem(@PathVariable("idItemCardapio") Integer idItemCardapio) {
        try {
            if (idItemCardapio == null)
                throw new IllegalArgumentException("Missing idItemCardapio path param");

            cardapioUseCases.excluirItemCardapio(idItemCardapio);
            return WebUtils.okResponse(new GenericOperationResponse(true));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao excluir item: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao excluir item");
        }
    }
}
