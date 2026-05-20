package dev.matheus.ClinicalAgenda.core.dtos;

import java.util.List;

public record PaginaResponse<T>(
        List<T> conteudo,
        int paginaAtual,
        int tamanhoPagina,
        long totalElementos,
        int totalPaginas
) {}
