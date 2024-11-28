package org.example;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MarkovHealthSimulator {
    private final double[][] transitionMatrix;
    private final int numStates;
    private final Random random;

    // Estados posibles
    private static final String[] ESTADOS = {"Saludable", "Enfermo", "Recuperando"};

    public MarkovHealthSimulator() {
        this.transitionMatrix = new double[][] {
                {0.7, 0.2, 0.1}, // Probabilidades desde "Saludable"
                {0.3, 0.5, 0.2}, // Probabilidades desde "Enfermo"
                {0.4, 0.1, 0.5}  // Probabilidades desde "Recuperando"
        };
        this.numStates = transitionMatrix.length;
        this.random = new Random();
    }

    /**
     * Simula un random walk basado en Markov para un pasajero.
     */


    public void simularSaludPasajero(Pasajero pasajero) {
        int estadoActual = determinarEstadoInicial(pasajero.getNivelSalud());
        System.out.println("Iniciando simulación para " + pasajero.Name() +
                " en estado: " + ESTADOS[estadoActual]);

        Set<Integer> visitados = new HashSet<>();
        visitados.add(estadoActual);

        int coverTime = 0;

        // Registro adicional
        int[] visitasPorEstado = new int[numStates];
        StringBuilder historial = new StringBuilder();
        historial.append("Estado inicial: ").append(ESTADOS[estadoActual]).append("\n");

        // Iniciar la simulación
        while (visitados.size() < numStates) {
            coverTime++;
            visitasPorEstado[estadoActual]++;
            estadoActual = elegirProximoEstado(estadoActual);
            visitados.add(estadoActual);

            historial.append("Paso ").append(coverTime)
                    .append(": Nuevo estado -> ").append(ESTADOS[estadoActual]).append("\n");
        }

        // Añadir la última visita
        visitasPorEstado[estadoActual]++;

        // Generar reporte detallado
        System.out.println(historial);
        System.out.println("Resumen para " + pasajero.Name() + ":");
        System.out.println("Tiempo de cobertura: " + coverTime + " pasos.");
        System.out.println("Visitas por estado:");
        for (int i = 0; i < numStates; i++) {
            System.out.println("- " + ESTADOS[i] + ": " + visitasPorEstado[i] + " visitas.");
        }
        System.out.println();
    }

    /**
     * Determina el estado inicial en base al nivel de salud.
     */
    private int determinarEstadoInicial(int nivelSalud) {
        if (nivelSalud > 60) return 0; // Saludable
        if (nivelSalud > 30) return 2; // Recuperando
        return 1; // Enfermo
    }

    /**
     * Elige el próximo estado basado en la matriz de transición.
     */
    private int elegirProximoEstado(int estadoActual) {
        double[] probabilidades = transitionMatrix[estadoActual];
        double randomValue = random.nextDouble();
        double acumulado = 0.0;

        for (int i = 0; i < probabilidades.length; i++) {
            acumulado += probabilidades[i];
            if (randomValue <= acumulado) {
                return i;
            }
        }
        return estadoActual; // Fallback en caso de errores de redondeo
    }

}
