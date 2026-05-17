package com.makeit.app.config;

import com.makeit.app.model.Categoria;
import com.makeit.app.model.RetoCatalogo;
import com.makeit.app.repository.CategoriaRepository;
import com.makeit.app.repository.RetoCatalogoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedInitialCatalog(
            CategoriaRepository categoriaRepository,
            RetoCatalogoRepository retoCatalogoRepository
    ) {
        return args -> {
            Categoria salud = findOrCreateCategory(
                    categoriaRepository,
                    "Salud",
                    "Habitos sencillos para cuidar cuerpo y mente"
            );
            Categoria estudio = findOrCreateCategory(
                    categoriaRepository,
                    "Estudio",
                    "Retos para aprender y avanzar cada dia"
            );
            Categoria hogar = findOrCreateCategory(
                    categoriaRepository,
                    "Hogar",
                    "Pequenas acciones para ordenar tu entorno"
            );
            Categoria creatividad = findOrCreateCategory(
                    categoriaRepository,
                    "Creatividad",
                    "Ideas rapidas para practicar y crear"
            );

            if (retoCatalogoRepository.count() == 0) {
                List<RetoSeed> retos = List.of(
                        new RetoSeed(salud, "Camina 10 minutos", "Da un paseo corto y consciente."),
                        new RetoSeed(salud, "Bebe un vaso de agua", "Haz una pausa y rehidrata el cuerpo."),
                        new RetoSeed(estudio, "Repasa un tema pendiente", "Dedica 15 minutos a un tema que tengas atrasado."),
                        new RetoSeed(estudio, "Resume una idea clave", "Escribe en una frase algo que hayas aprendido hoy."),
                        new RetoSeed(hogar, "Ordena una superficie", "Elige una mesa, escritorio o estante y dejalo despejado."),
                        new RetoSeed(hogar, "Prepara algo para manana", "Deja lista una cosa que te facilite el dia siguiente."),
                        new RetoSeed(creatividad, "Haz un boceto rapido", "Dibuja o apunta una idea en menos de 10 minutos."),
                        new RetoSeed(creatividad, "Crea una mini lista", "Escribe tres ideas para un proyecto futuro.")
                );
                retos.forEach(reto -> createChallenge(retoCatalogoRepository, reto));
            }
        };
    }

    private Categoria findOrCreateCategory(
            CategoriaRepository categoriaRepository,
            String nombre,
            String descripcion
    ) {
        return categoriaRepository.findByNombreIgnoreCase(nombre)
                .orElseGet(() -> {
                    Categoria categoria = new Categoria();
                    categoria.setNombre(nombre);
                    categoria.setDescripcion(descripcion);
                    return categoriaRepository.save(categoria);
                });
    }

    private void createChallenge(RetoCatalogoRepository retoCatalogoRepository, RetoSeed seed) {
        RetoCatalogo reto = new RetoCatalogo();
        reto.setCategoria(seed.categoria());
        reto.setTitulo(seed.titulo());
        reto.setDescripcion(seed.descripcion());
        reto.setActivo(true);
        retoCatalogoRepository.save(reto);
    }

    private record RetoSeed(Categoria categoria, String titulo, String descripcion) {
    }
}