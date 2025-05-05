package br.com.project.insurance.service.impl;

import br.com.project.insurance.entity.Parcela;
import br.com.project.insurance.entity.enums.SituacaoParcela;
import br.com.project.insurance.listener.dto.PagamentoApoliceParcelaEvent;
import br.com.project.insurance.listener.dto.PagamentoParcelaItemEvent;
import br.com.project.insurance.repository.ParcelaRepository;
import br.com.project.insurance.service.exception.ParcelaNaoEncontradaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ParcelaServiceImplTest {

    @Mock
    private ParcelaRepository parcelaRepository;

    @InjectMocks
    private ParcelaServiceImpl parcelaService;

    private PagamentoApoliceParcelaEvent apoliceEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        apoliceEvent = new PagamentoApoliceParcelaEvent(1, LocalDate.now(), 1,
                        List.of(new PagamentoParcelaItemEvent(1, "CARTAO")));
    }

    @Test
    void deveProcessarPagamentoParcelaComSucesso() {
        Parcela parcela = new Parcela();
        parcela.setId(1);
        parcela.setSituacao(SituacaoParcela.PENDENTE);
        parcela.setDataPagamento(LocalDate.now().minusDays(2));

        when(parcelaRepository.findById(1)).thenReturn(Optional.of(parcela));
        when(parcelaRepository.save(any(Parcela.class))).thenReturn(parcela);

        parcelaService.processarPagamentoParcela(apoliceEvent);

        verify(parcelaRepository, times(1)).save(parcela);
        assertEquals(SituacaoParcela.PAGO, parcela.getSituacao());
        assertNotNull(parcela.getDataPago());
        assertEquals("CARTAO", parcela.getFormaPagamento().name());
        assertTrue(parcela.getJuros().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void deveLancarExcecaoQuandoParcelaNaoEncontrada() {
        PagamentoParcelaItemEvent evento = new PagamentoParcelaItemEvent(999, "CARTAO");
        apoliceEvent = new PagamentoApoliceParcelaEvent(1, LocalDate.now(), 1, List.of(evento));

        when(parcelaRepository.findById(999)).thenReturn(Optional.empty());

        ParcelaNaoEncontradaException exception = assertThrows(ParcelaNaoEncontradaException.class, () -> {
            parcelaService.processarPagamentoParcela(apoliceEvent);
        });

        assertEquals("Parcela não encontrada: 999", exception.getMessage());
    }
}