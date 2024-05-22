package com.example.gomesrodris.archburgers.adapters.driver.controllers;

import com.example.gomesrodris.archburgers.adapters.dto.ItemCardapioDto;
import com.example.gomesrodris.archburgers.apiutils.WebUtils;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.serviceports.CardapioServicesPort;
import com.example.gomesrodris.archburgers.domain.services.CardapioServices;
import com.example.gomesrodris.archburgers.domain.utils.StringUtils;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardapioController {
    private final CardapioServicesPort cardapioServices;

    @Autowired
    public CardapioController(CardapioServicesPort cardapioServices) {
        this.cardapioServices = cardapioServices;
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

        List<ItemCardapio> result = cardapioServices.listarItensCardapio(filtroTipo);

        return WebUtils.okResponse(result.stream().map(ItemCardapioDto::fromEntity).toList());
    }
}
