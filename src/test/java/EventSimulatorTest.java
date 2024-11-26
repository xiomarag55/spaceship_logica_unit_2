package org.example;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class EventSimulatorTest {

    private List<Pasajero> pasajeros;
    private EventSimulator eventSimulator;
    private BloomFilterCabinas bloomFilter;

    @BeforeEach
    public void setup() {
        pasajeros = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            pasajeros.add(new Pasajero("ID" + i, "Name" + i, "Surname" + i, 30, "Male", "Family" + i, "Single", 0, 10, 5000.0, 80));
        }
        eventSimulator = new EventSimulator(pasajeros);
        bloomFilter = new BloomFilterCabinas(9); // Usamos la implementación real
    }

    @Test
    public void testProcesarEventosEnTiempoReal() {
        // Simula el procesamiento de eventos
        eventSimulator.procesarEventosEnTiempoReal(bloomFilter);

        // Verifica que al menos un evento haya sido registrado
        assertTrue(bloomFilter.cabinaAfectada("Cabina1") || bloomFilter.cabinaAfectada("Cabina2"));
        // Agrega más aserciones específicas según la lógica de tu implementación
    }

    @Test
    public void testEvaluarSalud() {
        Pasajero pasajero = pasajeros.get(0);

        // Evaluar salud con diferentes niveles
        pasajero.setNivelSalud(85);
        eventSimulator.evaluarSalud(pasajero); // Método que no devuelve nada
        assertEquals("Salud Perfecta", pasajero.getEstadoSalud()); // Método nuevo que debes agregar a Pasajero

        pasajero.setNivelSalud(65);
        eventSimulator.evaluarSalud(pasajero);
        assertEquals("Salud Buena", pasajero.getEstadoSalud());

        pasajero.setNivelSalud(45);
        eventSimulator.evaluarSalud(pasajero);
        assertEquals("Salud Estable", pasajero.getEstadoSalud());

        pasajero.setNivelSalud(25);
        eventSimulator.evaluarSalud(pasajero);
        assertEquals("Salud Regular", pasajero.getEstadoSalud());

        pasajero.setNivelSalud(10);
        eventSimulator.evaluarSalud(pasajero);
        assertEquals("Salud Crítica", pasajero.getEstadoSalud());
    }
}