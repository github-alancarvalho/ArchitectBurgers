package com.example.gomesrodris.archburgers.domain.serviceports;

import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;

public interface ClienteServicesPort {

    Cliente getClienteByCpf(Cpf cpf);

}
