package org.example;

public class Nodo {
    Pasajero valor;
    Nodo siguiente;

    public Nodo(Pasajero valor){
        this.valor=valor;
        this.siguiente=null;
    }

    public Pasajero obtenerPasajero(){
        return valor;
    }
    public void enlazarSiguiente(Nodo nodo){
        siguiente=nodo;

    }

    public Nodo obtenerSiguiente(){
        return siguiente;
    }

}
