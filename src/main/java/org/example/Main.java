package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
        //Traemos la lista de pasajeros
        Scanner scanner = new Scanner(System.in);
        HashTable tablaHash = new HashTable();
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("src/main/java/org/example/utils/pasajeros.json");

        List<Pasajero> pasajerosNave = objectMapper.readValue(jsonFile, new TypeReference<List<Pasajero>>() {
        });


        System.out.println("Cuantos pasajeros se agregaran: ");
        int cantidadDePasajeros = scanner.nextInt();

        System.out.println("Cuantas cabinas tiene la nave: ");
        int cantidadDeCabinas = scanner.nextInt();
        int filas = (int) Math.sqrt(cantidadDePasajeros);
        int columnas = (int) Math.ceil((double) cantidadDePasajeros / filas);


        // Crear una matriz NxN de Pasajeros


        Pasajero[][] pasajeros = new Pasajero[filas][columnas];



        // Llenar la matriz con objetos Pasajero
        if (pasajerosNave.size() >= filas*columnas) {
            int index = 0;
            // Llenar la matriz con objetos Pasajero desde la lista
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    pasajeros[i][j] = pasajerosNave.get(index);
                    index++;

                }
            }
        } else {
            System.out.println("La lista no contiene suficientes pasajeros para llenar la matriz.");
            System.exit(0);
        }

        // Simulación de Random Walk con Markov Chain
        MarkovHealthSimulator simulator = new MarkovHealthSimulator();
        System.out.println("Simulando Random Walk en salud de los pasajeros...");
        for (int i = 0; i < 5; i++) { // Simulamos para los primeros 5 pasajeros
            Pasajero pasajero = pasajeros[i / columnas][i % columnas];
            simulator.simularSaludPasajero(pasajero);
        }
        //Creacion de lista enlazada
        ListaEnlazada[] arregloHash = new ListaEnlazada[cantidadDeCabinas];
        for (int i = 0; i < arregloHash.length; i++) {
            arregloHash[i] = new ListaEnlazada();  // Inicializar cada entrada como una lista enlazada
        }

        // Insertar pasajeros en la tabla hash, manejando colisiones con listas enlazadas
        int contarPasajeros=0;
        for (int i = 0; i < pasajeros.length; i++) {
            for (int j = 0; j < pasajeros[i].length; j++) {
                if (contarPasajeros<cantidadDePasajeros){
                    int indiceHash = tablaHash.calcularIndice(pasajeros[i][j], cantidadDeCabinas);
                    //System.out.println("Índice hash: " + indiceHash);

                    // Agregar pasajero a la lista enlazada en el índice calculado
                    arregloHash[indiceHash].addPrimero(pasajeros[i][j]);
                    contarPasajeros++;
                }
            }
        }

        // Imprimir el contenido de la tabla hash con sus listas enlazadas
        System.out.println();
        for (int i = 0; i < arregloHash.length; i++) {
            System.out.println("Tripulantes Contenidos en la cabina con indice " + i + ":");
            arregloHash[i].imprimirLista(); // Imprimir los elementos en la lista enlazada del índice
            System.out.println();
        }

        // Buscar pasajero por ID
        String idBuscar = "6705869de69af767f75e92d8";

        // Buscar pasajero en la tabla hash
        Pasajero pasajeroEncontrado = tablaHash.buscarPasajeroPorID(idBuscar, arregloHash, cantidadDeCabinas);

        // Verificar si se encontró el pasajero
        if (pasajeroEncontrado != null) {
            System.out.println("Pasajero encontrado: " + pasajeroEncontrado.Name() + " " + pasajeroEncontrado.Surname());
        } else {
            System.out.println("Pasajero no encontrado con ID: " + idBuscar);
        }





        // Evaluar pasajeros
        evaluarSaludPasajerosReservoir(pasajeros, 5); // Evaluar 5 pasajeros aleatorios usando reservoir sampling

        // Inicializar simulador de eventos en tiempo real
        List<Pasajero> pasajerosList = new ArrayList<>();
        for (int i = 0; i < pasajeros.length; i++) {
            for (int j = 0; j < pasajeros[i].length; j++) {
                pasajerosList.add(pasajeros[i][j]);
            }
        }

        // Inicializar el filtro Bloom para las cabinas
        BloomFilterCabinas bloomFilter = new BloomFilterCabinas(9);

        // Simular eventos y procesar flujos de datos en tiempo real
        EventSimulator simulador = new EventSimulator(pasajerosList);
        simulador.procesarEventosEnTiempoReal(bloomFilter);

        // Verificar el estado de las cabinas afectadas por eventos
        for (int i = 1; i <= 9; i++) {
            String cabinaID = "Cabina" + i;
            System.out.println("Cabina " + cabinaID + " afectada: " + bloomFilter.cabinaAfectada(cabinaID));
        }


    }

    // Reservoir sampling y evaluar salud
    public static void evaluarSaludPasajerosReservoir(Pasajero[][] pasajeros, int numPasajeros) {
        Random random = new Random();
        List<Pasajero> flatList = new ArrayList<>();

        // Convertir la matriz en una lista
        for (int i = 0; i < pasajeros.length; i++) {
            for (int j = 0; j < pasajeros[i].length; j++) {
                flatList.add(pasajeros[i][j]);
            }
        }

        // Aplicar Reservoir Sampling para seleccionar pasajeros
        Pasajero[] reservoir = new Pasajero[Math.min(numPasajeros, flatList.size())];
        for (int i = 0; i < reservoir.length; i++) {
            reservoir[i] = flatList.get(i);
        }

        for (int i = reservoir.length; i < flatList.size(); i++) {
            int j = random.nextInt(i + 1);
            if (j < reservoir.length) {
                reservoir[j] = flatList.get(i);
            }
        }

        // Evaluar el nivel de salud de los pasajeros
        for (Pasajero pasajero : reservoir) {
            evaluarSalud(pasajero);
        }
    }

    // Método para evaluar la salud de un pasajero
    public static void evaluarSalud(Pasajero pasajero) {
        int nivelSalud = pasajero.getNivelSalud();
        System.out.print("Pasajero: " + pasajero.Name() + " " + pasajero.Surname() + " - Nivel de Salud: " + nivelSalud + " - " + pasajero.getEstadoSalud() + "\n");
    }
}