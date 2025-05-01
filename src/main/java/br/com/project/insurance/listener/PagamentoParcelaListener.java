package br.com.project.insurance.listener;

import br.com.project.insurance.service.impl.ParcelaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.project.insurance.config.RabbitMqConfig.PAGAMENTO_PARCELA_QUEUE;

@Component
public class PagamentoParcelaListener {

    private final Logger log = LoggerFactory.getLogger(PagamentoParcelaListener.class);

    private final ParcelaServiceImpl service;

    public PagamentoParcelaListener(ParcelaServiceImpl service) {
        this.service = service;
    }

//    @RabbitListener(queues = PAGAMENTO_PARCELA_QUEUE)
//    public void listen(Message<?> message) {
//        log.info("Mensagem consumida: {}", message);
//    }
}
