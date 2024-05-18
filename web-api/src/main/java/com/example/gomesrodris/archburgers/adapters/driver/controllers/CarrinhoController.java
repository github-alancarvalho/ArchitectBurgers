package com.example.gomesrodris.archburgers.adapters.driver.controllers;

import com.example.gomesrodris.archburgers.adapters.driven.infra.TransactionManager;
import com.example.gomesrodris.archburgers.adapters.dto.AddItemCarrinhoDto;
import com.example.gomesrodris.archburgers.adapters.dto.CarrinhoDto;
import com.example.gomesrodris.archburgers.apiutils.WebUtils;
import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.services.CarrinhoServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarrinhoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarrinhoController.class);

    private final CarrinhoServices carrinhoServices;
    private final TransactionManager transactionManager;

    @Autowired
    public CarrinhoController(CarrinhoServices carrinhoServices, TransactionManager transactionManager) {
        this.carrinhoServices = carrinhoServices;
        this.transactionManager = transactionManager;
    }

    @PostMapping(path = "/carrinho")
    public ResponseEntity<CarrinhoDto> iniciarCarrinho(@RequestBody CarrinhoServices.CarrinhoParam param) {

        Carrinho carrinho;
        try {
            carrinho = transactionManager.runInTransaction(() -> carrinhoServices.criarCarrinho(param));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao criar/recuperar carrinho: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao criar/recuperar carrinho");
        }

        return WebUtils.okResponse(CarrinhoDto.fromEntity(carrinho));
    }

    @PostMapping(path = "/carrinho/{idCarrinho}")
    public ResponseEntity<CarrinhoDto> addItemCarrinho(@PathVariable("idCarrinho") Integer idCarrinho, @RequestBody AddItemCarrinhoDto param) {

        Carrinho carrinho;
        try {
            if (idCarrinho == null)
                throw new IllegalArgumentException("Path param idCarrinho missing");
            if (param == null)
                throw new IllegalArgumentException("Request body missing");

            carrinho = transactionManager.runInTransaction(() -> carrinhoServices.addItem(
                    idCarrinho, param.getValidIdItemCardapio()));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao adicionar item no carrinho: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao adicionar item no carrinho");
        }

        return WebUtils.okResponse(CarrinhoDto.fromEntity(carrinho));
    }
}
