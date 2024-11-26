package org.example;

public class ListaEnlazada {

    Nodo cabeza;
    int tamano=0;
    public void listaEnlazada(){
        cabeza = null;
    }

    public void addPrimero(Pasajero pas){
        if(cabeza==null){
            cabeza = new Nodo(pas);
        } else{
            Nodo temp = cabeza;
            Nodo nuevo = new Nodo(pas);
            nuevo.enlazarSiguiente(temp);
            cabeza=nuevo;
        }
        tamano++;
    }

    public void imprimirLista() {
        if (cabeza == null) {
            System.out.println("La lista está vacía.");
        } else {
            Nodo actual = cabeza;
            while (actual != null) {
                // Suponiendo que Pasajero tiene un método toString bien definido
                System.out.println(actual.obtenerPasajero());
                actual = actual.obtenerSiguiente();  // Avanza al siguiente nodo
            }
        }
    }

    public Pasajero obtenerPasajeroLista(int index){
        int contador=0;
        Nodo temporal = cabeza;
        while (contador<index){
            temporal=temporal.obtenerSiguiente();
            contador++;
        }
        return temporal.obtenerPasajero();
    }

    public int tamano(){
        return tamano;
    }

    public boolean listaVacia(){
        boolean listaVacia=false;
        if(cabeza==null){
            listaVacia=true;
        }
        return listaVacia;
    }
    public Pasajero buscarPorID(String id) {
        Nodo temporal = cabeza;

        // Recorrer la lista enlazada
        while (temporal != null) {
            if (temporal.obtenerPasajero().getId().equals(id)) {
                return temporal.obtenerPasajero();  // Si encuentra el pasajero, lo retorna
            }
            temporal = temporal.obtenerSiguiente();
        }

        return null;  // Si no se encuentra, retorna null
    }
}
