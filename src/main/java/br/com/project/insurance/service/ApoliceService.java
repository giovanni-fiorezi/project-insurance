package br.com.project.insurance.service;

import br.com.project.insurance.dto.request.ApoliceRequest;
import br.com.project.insurance.dto.response.ApoliceResponse;

import java.util.List;

public interface ApoliceService {

    void criarApolice(ApoliceRequest apolice, Integer usuarioId);

    void atualizarApolice(ApoliceRequest apolice, Integer usuarioId);

    List<ApoliceResponse> buscarApolicePeloId(Integer apoliceId);
}
