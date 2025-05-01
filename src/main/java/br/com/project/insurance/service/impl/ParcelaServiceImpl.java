package br.com.project.insurance.service.impl;

import br.com.project.insurance.entity.Parcela;
import br.com.project.insurance.entity.enums.FormaPagamento;
import br.com.project.insurance.entity.enums.SituacaoParcela;
import br.com.project.insurance.listener.dto.PagamentoApoliceParcelaEvent;
import br.com.project.insurance.listener.dto.PagamentoParcelaItemEvent;
import br.com.project.insurance.repository.ParcelaRepository;
import br.com.project.insurance.service.ParcelaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ParcelaServiceImpl implements ParcelaService {

    private final ParcelaRepository repository;

    public ParcelaServiceImpl(ParcelaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void processarPagamentoParcela(PagamentoApoliceParcelaEvent apoliceEvent) {

        for (PagamentoParcelaItemEvent event : apoliceEvent.parcelas()) {

            Parcela parcela = repository.findById(event.id())
                            .orElseThrow(() -> new RuntimeException("Parcela não encontrada: " + event.id()));

            String formaPagamento = event.formaPagamento().toUpperCase();

            LocalDate dataPagamento = parcela.getDataPagamento();
            LocalDate dataPago = apoliceEvent.dataAlteracao();

            parcela.setFormaPagamento(FormaPagamento.valueOf(formaPagamento));
            parcela.setJuros(calcularJurosAtraso(FormaPagamento.valueOf(formaPagamento), dataPagamento, dataPago));
            parcela.setSituacao(SituacaoParcela.PAGO);
            parcela.setDataPago(dataPago);
            parcela.setDataAlteracaoRegistro(apoliceEvent.dataAlteracao());
            parcela.setUsuarioAlteracaoRegistro(apoliceEvent.usuarioAlteracao());

            repository.save(parcela);
        }

    }

    private static BigDecimal calcularJurosAtraso(FormaPagamento formaPagamento,
                                                  LocalDate dataPagamento,
                                                  LocalDate dataPago) {

        if(dataPago.isAfter(dataPagamento)) {
            long diasAtraso = ChronoUnit.DAYS.between(dataPagamento, dataPago);
            BigDecimal taxa = switch (formaPagamento) {
                case CARTAO -> new BigDecimal("0.03");
                case BOLETO -> new BigDecimal("0.01");
                case DINHEIRO -> new BigDecimal("0.005");
            };
            return taxa.multiply(new BigDecimal(diasAtraso));
        }

        return BigDecimal.ZERO;
    }
}
