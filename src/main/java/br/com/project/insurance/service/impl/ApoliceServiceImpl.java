package br.com.project.insurance.service.impl;

import br.com.project.insurance.dto.request.ApoliceRequest;
import br.com.project.insurance.dto.response.ApoliceResponse;
import br.com.project.insurance.entity.Apolice;
import br.com.project.insurance.entity.Parcela;
import br.com.project.insurance.entity.enums.SituacaoParcela;
import br.com.project.insurance.mapper.ApoliceMapper;
import br.com.project.insurance.repository.ApoliceRepository;
import br.com.project.insurance.service.ApoliceService;
import br.com.project.insurance.service.exception.ApoliceNaoEncontradaException;
import br.com.project.insurance.service.exception.FalhaAtualizacaoApoliceException;
import br.com.project.insurance.service.exception.ProcessamentoArquivoException;
import br.com.project.insurance.service.exception.RequisicaoInvalidaException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApoliceServiceImpl implements ApoliceService {

    private static final Logger log = LoggerFactory.getLogger(ApoliceServiceImpl.class);
    private final ApoliceRepository apoliceRepository;
    private final ApoliceMapper mapper;

    public ApoliceServiceImpl(ApoliceRepository apoliceRepository, ApoliceMapper mapper) {
        this.apoliceRepository = apoliceRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void criarApolice(ApoliceRequest request, Integer usuarioId) {
        try {
            Apolice apolice = ApoliceMapper.toEntity(request, usuarioId);
            apoliceRepository.save(apolice);

        } catch (DataIntegrityViolationException ex) {
            throw new RequisicaoInvalidaException("Dados inválidos para criar a apólice.");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void atualizarApolice(ApoliceRequest request, Integer usuarioId) {
        if (request.id() == null) {
            throw new RequisicaoInvalidaException("Id da request não pode ser null");
        }

        try {
            Apolice apolice = buildApolice(request, usuarioId);
            apoliceRepository.save(apolice);

        } catch (ApoliceNaoEncontradaException ex) {
            throw ex;

        } catch (Exception e) {
            log.error("Erro ao atualizar a apolice com ID {}: {}", request.id(), e.getMessage(), e);
            throw new FalhaAtualizacaoApoliceException("Erro ao atualizar apolice");
        }
    }

    @Override
    public List<ApoliceResponse> buscaApolicePorIdOuTodas(Integer apoliceId) {
        if(apoliceId != null) {
            Apolice apolice = apoliceRepository.findById(apoliceId)
                    .orElseThrow(() -> new ApoliceNaoEncontradaException("Id de apolice não existe"));

            return List.of(ApoliceMapper.toResponse(apolice));
        }

        return apoliceRepository.findAll()
                .stream()
                .map(ApoliceMapper::toResponse)
                .toList();
    }

    @Override
    public String uploadCsv(MultipartFile file) {
        try {
            String uploadDir = "src/main/resources/"; // Salvar o arquivo dentro do Resource
            Path filePath = Paths.get(uploadDir + file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath);
            return "Arquivo salvo com sucesso.";

        } catch (IOException e) {
            throw new ProcessamentoArquivoException("Erro ao processar planilha: " + e.getMessage());
        }
    }

    private Apolice buildApolice(ApoliceRequest request, Integer usuarioId) {
        Apolice apolice = apoliceRepository.findById(request.id())
                .orElseThrow(() -> new ApoliceNaoEncontradaException("Apólice não encontrada com id: " + request.id()));

        apolice.setDescricao(request.descricao());
        apolice.setCpf(request.cpf());
        apolice.setSituacao(request.situacao());
        apolice.setPremioTotal(request.premioTotal());
        apolice.setDataAlteracaoRegistro(LocalDate.now());
        apolice.setUsuarioAlteracaoRegistro(usuarioId);

        List<Parcela> parcelas = request.parcelas().stream()
                .map(p -> {
                    Parcela parcela = new Parcela();
                    parcela.setId(p.id());
                    parcela.setPremio(p.premio());
                    parcela.setFormaPagamento(p.formaPagamento());
                    parcela.setDataPagamento(p.dataPagamento());
                    parcela.setSituacao(SituacaoParcela.PENDENTE);
                    parcela.setApolice(apolice);

                    if (request.id() != null) {
                        parcela.setDataAlteracaoRegistro(LocalDate.now());
                        parcela.setUsuarioAlteracaoRegistro(usuarioId);
                    } else {
                        parcela.setDataCriacaoRegistro(LocalDate.now());
                        parcela.setUsuarioCriacaoRegistro(usuarioId);
                    }

                    return parcela;

                }).collect(Collectors.toList());

        apolice.setParcelas(parcelas);
        return apolice;
    }
}