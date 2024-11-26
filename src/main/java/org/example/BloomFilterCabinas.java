package org.example;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

public class BloomFilterCabinas {
    private BloomFilter<String> bloomFilter;

    // Inicializar el filtro Bloom con el número estimado de cabinas
    public BloomFilterCabinas(int expectedInsertions) {
        bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), expectedInsertions);
    }

    // Registrar un evento que afecta a una cabina
    public void registrarEvento(String cabinaID) {
        bloomFilter.put(cabinaID);
    }

    // Verificar si una cabina ha sido afectada
    public boolean cabinaAfectada(String cabinaID) {
        return bloomFilter.mightContain(cabinaID);
    }
}
