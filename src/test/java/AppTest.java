package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testBloomFilter() {
        // Crear una instancia de BloomFilterCabinas
        BloomFilterCabinas bloomFilter = new BloomFilterCabinas(9);

        // Verificar que una cabina no ha sido afectada antes de registrar un evento
        String cabinaIDNoAfectada = "Cabina2";
        assertFalse(bloomFilter.cabinaAfectada(cabinaIDNoAfectada),
                "La cabina no debería estar afectada antes del registro");

        // Registrar un evento que afecta a "Cabina1"
        String cabinaID = "Cabina1";
        bloomFilter.registrarEvento(cabinaID);

        // Verificar que la cabina afectada ahora esté registrada en el filtro
        assertTrue(bloomFilter.cabinaAfectada(cabinaID),
                "La cabina debería estar afectada después de registrar el evento");
    }

    @Test
    public void testPasajeroSalud() {
        // Crear un objeto Pasajero
        Pasajero pasajero = new Pasajero("ID1", "John", "Doe", 30, "Male", "Family1", "Single", 0, 5, 5000, 85);

        // Comprobar que el nivel de salud es correcto
        assertEquals(85, pasajero.getNivelSalud(),
                "El nivel de salud inicial debería ser 85");

        // Cambiar el nivel de salud y verificar la actualización
        pasajero.setNivelSalud(60);
        assertEquals(60, pasajero.getNivelSalud(),
                "El nivel de salud debería haberse actualizado a 60");

        // Verificar que el estado de salud corresponde al nuevo nivel
        assertEquals("Salud Buena", pasajero.getEstadoSalud(),
                "El estado de salud debería ser 'Salud Buena' para un nivel de 60");

        // Probar un nivel de salud crítico
        pasajero.setNivelSalud(10);
        assertEquals("Salud Crítica", pasajero.getEstadoSalud(),
                "El estado de salud debería ser 'Salud Crítica' para un nivel de 10");
    }
}