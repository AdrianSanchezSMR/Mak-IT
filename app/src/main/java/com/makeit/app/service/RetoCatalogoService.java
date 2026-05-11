package com.makeit.app.service;

import com.makeit.app.dto.reto.CreateRetoRequest;
import com.makeit.app.model.Categoria;
import com.makeit.app.model.RetoCatalogo;
import com.makeit.app.repository.CategoriaRepository;
import com.makeit.app.repository.RetoCatalogoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RetoCatalogoService {

    private final RetoCatalogoRepository retoCatalogoRepository;
    private final CategoriaRepository categoriaRepository;

    public RetoCatalogoService(RetoCatalogoRepository retoCatalogoRepository, CategoriaRepository categoriaRepository) {
        this.retoCatalogoRepository = retoCatalogoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<RetoCatalogo> obtenerRetosActivos() {
        return retoCatalogoRepository.findByActivoTrue();
    }

    public RetoCatalogo obtenerRetoAleatorio(Long categoriaId) {
        List<RetoCatalogo> retos = (categoriaId == null)
                ? retoCatalogoRepository.findByActivoTrue()
                : retoCatalogoRepository.findByCategoriaIdAndActivoTrue(categoriaId);

        if (retos.isEmpty()) {
            return null;
        }

        int indice = ThreadLocalRandom.current().nextInt(retos.size());
        return retos.get(indice);
    }

    public RetoCatalogo crearReto(CreateRetoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoria no existe"));

        RetoCatalogo reto = new RetoCatalogo();
        reto.setCategoria(categoria);
        reto.setTitulo(request.getTitulo().trim());
        reto.setDescripcion(request.getDescripcion());
        reto.setActivo(request.getActivo() == null || request.getActivo());

        return retoCatalogoRepository.save(reto);
    }
}
