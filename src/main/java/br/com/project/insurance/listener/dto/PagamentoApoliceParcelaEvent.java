package br.com.project.insurance.listener.dto;

import java.time.LocalDate;
import java.util.List;

public record PagamentoApoliceParcelaEvent(Integer id,
                                           LocalDate dataAlteracao,
                                           Integer usuarioAlteracao,
                                           List<PagamentoParcelaItemEvent> parcelas) {
}
