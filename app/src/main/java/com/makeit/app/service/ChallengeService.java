package com.makeit.app.service;

import com.makeit.app.dto.challenge.ChallengeResponse;
import com.makeit.app.dto.challenge.CheckInResponse;
import com.makeit.app.dto.challenge.InterestsResponse;
import com.makeit.app.dto.challenge.UpdateInterestsRequest;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChallengeService {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final RetoCatalogoRepository retoCatalogoRepository;
    private final ProgresoDiarioRepository progresoDiarioRepository;

    public ChallengeService(
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository,
            RetoCatalogoRepository retoCatalogoRepository,
            ProgresoDiarioRepository progresoDiarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.retoCatalogoRepository = retoCatalogoRepository;
        this.progresoDiarioRepository = progresoDiarioRepository;
    }

    @Transactional(readOnly = true)
    public InterestsResponse getInterests(String username) {
        Usuario usuario = getUsuarioByUsername(username);
        List<Long> idsOrdenados = usuario.getCategoriasPreferidas()
                .stream()
                .map(Categoria::getId)
                .sorted()
                .toList();
        return new InterestsResponse(idsOrdenados);
    }

    @Transactional
    public InterestsResponse updateInterests(String username, UpdateInterestsRequest request) {
        Usuario usuario = getUsuarioByUsername(username);

        List<Long> idsSinDuplicados = request.getCategoriaIds()
                .stream()
                .distinct()
                .toList();

        List<Categoria> categorias = categoriaRepository.findByIdIn(idsSinDuplicados);
        if (categorias.size() != idsSinDuplicados.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alguna categoria no existe");
        }

        usuario.setCategoriasPreferidas(new HashSet<>(categorias));
        usuarioRepository.save(usuario);

        List<Long> idsOrdenados = usuario.getCategoriasPreferidas()
                .stream()
                .map(Categoria::getId)
                .sorted()
                .toList();
        return new InterestsResponse(idsOrdenados);
    }

    @Transactional(readOnly = true)
    public ChallengeResponse getTodayChallenge(String username) {
        Usuario usuario = getUsuarioByUsername(username);
        LocalDate hoy = LocalDate.now();

        List<ProgresoDiario> progresosHoy = progresoDiarioRepository.findByUsuarioAndFechaOrderByIdAsc(usuario, hoy);
        if (!progresosHoy.isEmpty()) {
            ProgresoDiario progreso = progresosHoy.get(0);
            return toChallengeResponse(progreso.getRetoCatalogo(), Boolean.TRUE.equals(progreso.getCompletado()));
        }

        List<Long> categoriasPreferidasIds = usuario.getCategoriasPreferidas()
                .stream()
                .map(Categoria::getId)
                .toList();

        List<RetoCatalogo> candidatos = categoriasPreferidasIds.isEmpty()
                ? retoCatalogoRepository.findByActivoTrue()
                : retoCatalogoRepository.findByCategoriaIdInAndActivoTrue(categoriasPreferidasIds);

        if (candidatos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay retos disponibles para hoy");
        }

        RetoCatalogo retoSeleccionado = selectDeterministicChallenge(candidatos, usuario.getId(), hoy);

        ProgresoDiario progreso = new ProgresoDiario();
        progreso.setUsuario(usuario);
        progreso.setRetoCatalogo(retoSeleccionado);
        progreso.setFecha(hoy);
        progreso.setCompletado(false);
        progresoDiarioRepository.save(progreso);

        return toChallengeResponse(retoSeleccionado, false);
    }

    @Transactional
    public List<ChallengeResponse> getTodayChallenges(String username) {
        getTodayChallenge(username);
        Usuario usuario = getUsuarioByUsername(username);
        LocalDate hoy = LocalDate.now();

        return progresoDiarioRepository.findByUsuarioAndFechaOrderByIdAsc(usuario, hoy)
                .stream()
                .map(progreso -> toChallengeResponse(
                        progreso.getRetoCatalogo(),
                        Boolean.TRUE.equals(progreso.getCompletado())
                ))
                .toList();
    }

    @Transactional
    public CheckInResponse checkInTodayChallenge(String username, Long challengeId) {
        Usuario usuario = getUsuarioByUsername(username);
        LocalDate hoy = LocalDate.now();

        ProgresoDiario progreso = progresoDiarioRepository
                .findByUsuarioAndRetoCatalogoIdAndFecha(usuario, challengeId, hoy)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Ese reto no corresponde al reto del dia de hoy"
                ));

        progreso.setCompletado(true);
        progresoDiarioRepository.save(progreso);

        return new CheckInResponse("Check-in registrado correctamente", challengeId, true);
    }

    private Usuario getUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private RetoCatalogo selectDeterministicChallenge(List<RetoCatalogo> retos, Long userId, LocalDate date) {
        List<RetoCatalogo> ordenados = retos.stream()
                .sorted(Comparator.comparing(RetoCatalogo::getId))
                .collect(Collectors.toList());

        long seed = Objects.hash(userId, date.toString());
        Random random = new Random(seed);
        int index = random.nextInt(ordenados.size());
        return ordenados.get(index);
    }

    private ChallengeResponse toChallengeResponse(RetoCatalogo reto, boolean completadoHoy) {
        return new ChallengeResponse(
                reto.getId(),
                reto.getTitulo(),
                reto.getDescripcion(),
                reto.getCategoria().getId(),
                reto.getCategoria().getNombre(),
                completadoHoy
        );
    }
}
