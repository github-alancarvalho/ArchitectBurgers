package com.example.gomesrodris.archburgers.apiutils;

import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ValorMonetarioSerializer extends JsonSerializer<ValorMonetario> {
    @Override
    public void serialize(ValorMonetario value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(value.asBigDecimal());
    }
}
