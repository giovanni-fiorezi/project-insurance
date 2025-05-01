package br.com.project.insurance.dto.response;

import br.com.project.insurance.entity.enums.SituacaoApolice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ApoliceResponse(Integer id,
                              String descricao,
                              Integer cpf,
                              SituacaoApolice situacao,
                              BigDecimal premioTotal,
                              LocalDate dataCriacao,
                              LocalDate dataAlteracao,
                              Integer usuarioCriacao,
                              Integer usuarioAlteracao,
                              List<ParcelaResponse> parcelas) {
}
