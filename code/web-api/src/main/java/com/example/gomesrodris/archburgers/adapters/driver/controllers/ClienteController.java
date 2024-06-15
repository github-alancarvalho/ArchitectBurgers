package com.example.gomesrodris.archburgers.adapters.driver.controllers;

import com.example.gomesrodris.archburgers.adapters.dto.ClienteDto;
import com.example.gomesrodris.archburgers.apiutils.WebUtils;
import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.usecaseports.ClienteUseCasesPort;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
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
public class ClienteController {
    private final ClienteUseCasesPort clienteUseCases;

    @Autowired
    public ClienteController(ClienteUseCasesPort clienteUseCases) {
        this.clienteUseCases = clienteUseCases;
    }

    @Operation(description = "Busca um cliente por CPF, para identificação no início da compra",
            parameters = @Parameter(name = "cpf", required = true))
    @GetMapping(path = "/cliente")
    public ResponseEntity<ClienteDto> getClientePorCpf(@RequestParam("cpf") String cpfParam) {
        if (cpfParam == null || cpfParam.isBlank()) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, "Parametro cpf deve ser informado");
        }

        Cpf cpf;
        try {
            cpf = new Cpf(cpfParam);
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        }

        Cliente cliente = clienteUseCases.getClienteByCpf(cpf);

        if (cliente == null) {
            return WebUtils.errorResponse(HttpStatus.NOT_FOUND, "Cliente com CPF [" + cpfParam + "] não encontrado");
        }

        return WebUtils.okResponse(ClienteDto.fromEntity(cliente));
    }

    @Operation(description = "Lista todos os clientes")
    @GetMapping(path = "/clientes")
    public ResponseEntity<List<ClienteDto>> getClientes() {
        var clientes = clienteUseCases.listTodosClientes();
        return WebUtils.okResponse(clientes.stream().map(ClienteDto::fromEntity).toList());
    }
}
