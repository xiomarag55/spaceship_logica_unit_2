package org.example;

import java.util.List;
import java.util.Random;

public class EventSimulator {
    private List<Pasajero> pasajeros;
    private Random random = new Random();

    public EventSimulator(List<Pasajero> pasajeros) {
        this.pasajeros = pasajeros;
    }

    // Procesar eventos en tiempo real y monitorear salud
    public void procesarEventosEnTiempoReal(BloomFilterCabinas bloomFilter) {
        int eventCount = 0;

        while (eventCount < 10) {  // Simulación de 10 eventos
            int index = random.nextInt(pasajeros.size());
            Pasajero pasajero = pasajeros.get(index);

            // Simular un cambio en el estado de salud del pasajero
            int nuevoNivelSalud = random.nextInt(101);
            pasajero.setNivelSalud(nuevoNivelSalud);
            System.out.println("Evento: Cambio en salud de " + pasajero.Name() + " " + pasajero.Surname() + " a nivel: " + nuevoNivelSalud);

            // Marcar la cabina como afectada por el evento
            String cabinaID = "Cabina" + (index + 1);  // Relacionar el pasajero con una cabina
            bloomFilter.registrarEvento(cabinaID);

            // Evaluar el estado de salud del pasajero
            evaluarSalud(pasajero);

            try {
                Thread.sleep(1000);  // Pausa de 1 segundo entre eventos para simular tiempo real
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            eventCount++;
        }
    }

    // Método para evaluar el estado de salud de los pasajeros
    public void evaluarSalud(Pasajero pasajero) {
        int nivelSalud = pasajero.getNivelSalud();
        System.out.print("Pasajero: " + pasajero.Name() + " " + pasajero.Surname() + " - Nivel de Salud: " + nivelSalud + " - ");

        if (nivelSalud > 80) {
            System.out.println("Salud Perfecta");
        } else if (nivelSalud > 60) {
            System.out.println("Salud Buena");
        } else if (nivelSalud > 40) {
            System.out.println("Salud Estable");
        } else if (nivelSalud > 20) {
            System.out.println("Salud Regular");
        } else {
            System.out.println("Salud Crítica");
        }
    }
}