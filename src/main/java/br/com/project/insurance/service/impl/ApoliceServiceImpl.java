package br.com.project.insurance.service.impl;

import br.com.project.insurance.dto.request.ApoliceRequest;
import br.com.project.insurance.dto.response.ApoliceResponse;
import br.com.project.insurance.entity.Apolice;
import br.com.project.insurance.entity.Parcela;
import br.com.project.insurance.entity.enums.SituacaoParcela;
import br.com.project.insurance.mapper.ApoliceMapper;
import br.com.project.insurance.repository.ApoliceRepository;
import br.com.project.insurance.service.ApoliceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApoliceServiceImpl implements ApoliceService {

    private final ApoliceRepository apoliceRepository;

    public ApoliceServiceImpl(ApoliceRepository apoliceRepository) {
        this.apoliceRepository = apoliceRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void criarApolice(ApoliceRequest request, Integer usuarioId) {
        Apolice apolice = ApoliceMapper.toEntity(request, usuarioId);
        apoliceRepository.save(apolice);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void atualizarApolice(ApoliceRequest request, Integer usuarioId) {
        if (request.id() == null) {
            throw new RuntimeException("Id da request não pode ser null"); //Todo -> criar Exception personalizada
        }

        Apolice apolice;

        apolice = apoliceRepository.findById(request.id())
                .orElseThrow(() -> new RuntimeException("Apólice não encontrada com id: " + request.id()));

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

                }).toList();

        apolice.setParcelas(parcelas);
        apoliceRepository.save(apolice);
    }

    @Override
    public List<ApoliceResponse> buscaApolicePorIdOuTodas(Integer apoliceId) {
        if(apoliceId != null) {
            Apolice apolice = apoliceRepository.findById(apoliceId)
                    .orElseThrow(() -> new RuntimeException("Id de apolice não existe"));

            return List.of(ApoliceMapper.toResponse(apolice));
        }

        return apoliceRepository.findAll()
                .stream()
                .map(ApoliceMapper::toResponse)
                .toList();
    }


}
