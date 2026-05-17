package com.makeit.app.service;

import com.makeit.app.dto.reto.CreateRetoRequest;
import com.makeit.app.model.Categoria;
import com.makeit.app.model.ProgresoDiario;
import com.makeit.app.model.RetoCatalogo;
import com.makeit.app.model.Usuario;
import com.makeit.app.repository.CategoriaRepository;
import com.makeit.app.repository.ProgresoDiarioRepository;
import com.makeit.app.repository.RetoCatalogoRepository;
import com.makeit.app.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RetoCatalogoService {

    private final RetoCatalogoRepository retoCatalogoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProgresoDiarioRepository progresoDiarioRepository;

    public RetoCatalogoService(
            RetoCatalogoRepository retoCatalogoRepository,
            CategoriaRepository categoriaRepository,
            UsuarioRepository usuarioRepository,
            ProgresoDiarioRepository progresoDiarioRepository
    ) {
        this.retoCatalogoRepository = retoCatalogoRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.progresoDiarioRepository = progresoDiarioRepository;
    }

    @Transactional(readOnly = true)
    public List<RetoCatalogo> obtenerRetosActivos() {
        return retoCatalogoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
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

    @Transactional
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

    @Transactional
    public RetoCatalogo crearRetoYAsignarloHoy(String username, CreateRetoRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        RetoCatalogo reto = crearReto(request);

        HashSet<Categoria> categorias = new HashSet<>(usuario.getCategoriasPreferidas());
        categorias.add(reto.getCategoria());
        usuario.setCategoriasPreferidas(categorias);

        LocalDate hoy = LocalDate.now();
        ProgresoDiario progreso = progresoDiarioRepository.findByUsuarioAndRetoCatalogoIdAndFecha(usuario, reto.getId(), hoy)
                .orElseGet(() -> {
                    ProgresoDiario nuevo = new ProgresoDiario();
                    nuevo.setUsuario(usuario);
                    nuevo.setFecha(hoy);
                    nuevo.setRetoCatalogo(reto);
                    return nuevo;
                });
        progreso.setRetoCatalogo(reto);
        progreso.setCompletado(false);
        progresoDiarioRepository.save(progreso);
        usuarioRepository.save(usuario);

        return reto;
    }
}
