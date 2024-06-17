package com.example.gomesrodris.archburgers.adapters.controllers;

import com.example.gomesrodris.archburgers.adapters.dto.FormaPagamentoDto;
import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class PagamentoController {

    @GetMapping("/pagamento/opcoes")
    public List<FormaPagamentoDto> listFormasPagamento() {
        return Arrays.stream(FormaPagamento.values()).map(
                        formaPagamento -> new FormaPagamentoDto(formaPagamento.name(), formaPagamento.getDescricao()))
                .toList();
    }
}
