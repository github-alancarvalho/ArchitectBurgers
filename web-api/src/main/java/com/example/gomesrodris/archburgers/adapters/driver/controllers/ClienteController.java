package com.example.gomesrodris.archburgers.adapters.driver.controllers;

import com.example.gomesrodris.archburgers.adapters.dto.ClienteDto;
import com.example.gomesrodris.archburgers.apiutils.WebUtils;
import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.services.ClienteServices;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class ClienteController {
    private final ClienteServices clienteServices;

    @Autowired
    public ClienteController(ClienteServices clienteServices) {
        this.clienteServices = clienteServices;
    }

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

        Cliente cliente = clienteServices.getClienteByCpf(cpf);

        if (cliente == null) {
            return WebUtils.errorResponse(HttpStatus.NOT_FOUND, "Cliente com CPF [" + cpfParam + "] não encontrado");
        }

        return WebUtils.okResponse(ClienteDto.fromEntity(cliente));
    }
}
