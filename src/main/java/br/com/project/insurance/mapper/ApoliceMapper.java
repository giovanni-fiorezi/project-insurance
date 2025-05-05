package br.com.project.insurance.mapper;

import br.com.project.insurance.dto.request.ApoliceRequest;
import br.com.project.insurance.dto.response.ApoliceResponse;
import br.com.project.insurance.dto.response.ParcelaResponse;
import br.com.project.insurance.entity.Apolice;
import br.com.project.insurance.entity.Parcela;
import br.com.project.insurance.entity.enums.SituacaoParcela;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ApoliceMapper {

    public Apolice toEntity(ApoliceRequest dto, Integer usuarioId) {
        Apolice apolice = new Apolice();
        apolice.setDescricao(dto.descricao());
        apolice.setCpf(dto.cpf());
        apolice.setSituacao(dto.situacao());
        apolice.setPremioTotal(dto.premioTotal());
        apolice.setDataCriacaoRegistro(LocalDate.now());
        apolice.setUsuarioCriacaoRegistro(usuarioId);

        List<Parcela> parcelas = dto.parcelas().stream()
                .map(p -> {
                    Parcela parcela = new Parcela();
                    parcela.setPremio(p.premio());
                    parcela.setFormaPagamento(p.formaPagamento());
                    parcela.setDataPagamento(p.dataPagamento());
                    parcela.setSituacao(SituacaoParcela.PENDENTE);
                    parcela.setDataCriacaoRegistro(LocalDate.now());
                    parcela.setUsuarioCriacaoRegistro(usuarioId);
                    parcela.setApolice(apolice);
                    return parcela;
                }).toList();

        apolice.setParcelas(parcelas);

        return apolice;
    }

    public ApoliceResponse toResponse(Apolice apolice) {
        List<ParcelaResponse> parcelas = apolice.getParcelas()
                .stream()
                .map(p -> new ParcelaResponse(
                        p.getId(),
                        p.getPremio(),
                        p.getFormaPagamento(),
                        p.getDataPagamento(),
                        p.getDataPago(),
                        p.getJuros(),
                        p.getSituacao(),
                        p.getDataCriacaoRegistro(),
                        p.getDataAlteracaoRegistro(),
                        p.getUsuarioCriacaoRegistro(),
                        p.getUsuarioAlteracaoRegistro()
                ))
                .toList();

        return new ApoliceResponse(
                apolice.getId(),
                apolice.getDescricao(),
                apolice.getCpf(),
                apolice.getSituacao(),
                apolice.getPremioTotal(),
                apolice.getDataCriacaoRegistro(),
                apolice.getDataAlteracaoRegistro(),
                apolice.getUsuarioCriacaoRegistro(),
                apolice.getUsuarioAlteracaoRegistro(),
                parcelas
        );
    }
}
