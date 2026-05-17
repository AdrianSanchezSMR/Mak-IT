package com.makeit.app.controller;

import com.makeit.app.dto.reto.CreateRetoRequest;
import com.makeit.app.model.RetoCatalogo;
import com.makeit.app.service.RetoCatalogoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retos")
public class RetoCatalogoController {

    private final RetoCatalogoService retoCatalogoService;

    public RetoCatalogoController(RetoCatalogoService retoCatalogoService) {
        this.retoCatalogoService = retoCatalogoService;
    }

    @GetMapping
    public List<RetoCatalogo> listarActivos() {
        return retoCatalogoService.obtenerRetosActivos();
    }

    @GetMapping("/aleatorio")
    public ResponseEntity<RetoCatalogo> obtenerRetoAleatorio(
            @RequestParam(required = false) Long categoriaId
    ) {
        RetoCatalogo reto = retoCatalogoService.obtenerRetoAleatorio(categoriaId);
        if (reto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RetoCatalogo crearReto(
            @AuthenticationPrincipal String username,
            @Valid @RequestBody CreateRetoRequest request
    ) {
        return retoCatalogoService.crearRetoYAsignarloHoy(username, request);
    }
}