package org.example;

public class HashTable {

    public int calcularIndice(Pasajero tripulante, int tamano){
        int indice;
        String id = tripulante.getId();
        int sumaTotalAscii=0;
        for (int i = 0; i < id.length(); i++) {
            char caracter = id.charAt(i);  // Obtiene el carácter en la posición i
            int asciiValor = (int) caracter;  // Convierte el carácter a su valor ASCII
            asciiValor = asciiValor*i;
            sumaTotalAscii += asciiValor;
        }
        indice = sumaTotalAscii % tamano;

        return indice;
    }

    public Pasajero buscarPasajeroPorID(String Idbuscado, ListaEnlazada[] arregloHash, int cantidadCabinas){
        // Crear un pasajero temporal solo para calcular el índice
        Pasajero pasajeroTemporal = new Pasajero();
        pasajeroTemporal.setId(Idbuscado);

        // Calcular el índice usando el método de hash
        int indiceHash = calcularIndice(pasajeroTemporal, cantidadCabinas);

        // Buscar en la lista enlazada en la posición calculada
        return arregloHash[indiceHash].buscarPorID(Idbuscado);

    }

    public int buscarTripulanteId(String id, int tamano){
        int indice;
        String idTem = id;
        int sumaTotalAscii=0;
        for (int i = 0; i < idTem.length(); i++) {
            char caracter = idTem.charAt(i);  // Obtiene el carácter en la posición i
            int asciiValor = (int) caracter;  // Convierte el carácter a su valor ASCII
            asciiValor = asciiValor*i;
            sumaTotalAscii += asciiValor;
        }
        indice = sumaTotalAscii % tamano;

        return indice;
    }

}