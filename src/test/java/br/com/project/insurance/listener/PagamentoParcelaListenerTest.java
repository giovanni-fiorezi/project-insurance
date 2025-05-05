package br.com.project.insurance.listener;

import br.com.project.insurance.listener.dto.PagamentoApoliceParcelaEvent;
import br.com.project.insurance.listener.dto.PagamentoParcelaItemEvent;
import br.com.project.insurance.service.impl.ParcelaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;

import static org.mockito.Mockito.*;

class PagamentoParcelaListenerTest {

    @Mock
    private ParcelaServiceImpl service;

    @InjectMocks
    private PagamentoParcelaListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveConsumirMensagemEProcessarPagamento() {
        PagamentoParcelaItemEvent itemEvent = new PagamentoParcelaItemEvent(1, "CARTAO");
        PagamentoApoliceParcelaEvent evento = new PagamentoApoliceParcelaEvent(
                1,
                java.time.LocalDate.now(),
                123,
                java.util.List.of(itemEvent)
        );

        Message<PagamentoApoliceParcelaEvent> message = mock(Message.class);
        when(message.getPayload()).thenReturn(evento);

        listener.listen(message);

        verify(service, times(1)).processarPagamentoParcela(evento);
    }
}