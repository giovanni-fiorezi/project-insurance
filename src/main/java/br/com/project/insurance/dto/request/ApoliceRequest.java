package br.com.project.insurance.dto.request;

import br.com.project.insurance.entity.enums.SituacaoApolice;

import java.math.BigDecimal;
import java.util.List;

public record ApoliceRequest(Integer id,
                             String descricao,
                             Integer cpf,
                             SituacaoApolice situacao,
                             BigDecimal premioTotal,
                             List<ParcelaRequest> parcelas) {
}
