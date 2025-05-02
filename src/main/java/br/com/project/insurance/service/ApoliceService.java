package br.com.project.insurance.service;

import br.com.project.insurance.dto.request.ApoliceRequest;
import br.com.project.insurance.dto.response.ApoliceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApoliceService {

    void criarApolice(ApoliceRequest apolice, Integer usuarioId);

    void atualizarApolice(ApoliceRequest apolice, Integer usuarioId);

    List<ApoliceResponse> buscaApolicePorIdOuTodas(Integer apoliceId);

    String uploadCsv(MultipartFile file);
}
