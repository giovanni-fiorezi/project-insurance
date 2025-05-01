package br.com.project.insurance.service;

import br.com.project.insurance.listener.dto.PagamentoApoliceParcelaEvent;

public interface ParcelaService {

    void processarPagamentoParcela(PagamentoApoliceParcelaEvent apoliceParcelaEvent);

}
