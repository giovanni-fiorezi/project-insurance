package br.com.project.insurance.entity;

import br.com.project.insurance.entity.enums.FormaPagamento;
import br.com.project.insurance.entity.enums.SituacaoParcela;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "parcela")
public class Parcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_apolice", nullable = false)
    private Apolice apolice;

    private BigDecimal premio;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    private LocalDate dataPagamento;

    private LocalDate dataPago;

    private BigDecimal juros;

    @Enumerated(EnumType.STRING)
    private SituacaoParcela situacao;

    private LocalDate dataCriacaoRegistro;

    private LocalDate dataAlteracaoRegistro;

    private Integer usuarioCriacaoRegistro;

    private Integer usuarioAlteracaoRegistro;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Apolice getApolice() {
        return apolice;
    }

    public void setApolice(Apolice apolice) {
        this.apolice = apolice;
    }

    public BigDecimal getPremio() {
        return premio;
    }

    public void setPremio(BigDecimal premio) {
        this.premio = premio;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public LocalDate getDataPago() {
        return dataPago;
    }

    public void setDataPago(LocalDate dataPago) {
        this.dataPago = dataPago;
    }

    public BigDecimal getJuros() {
        return juros;
    }

    public void setJuros(BigDecimal juros) {
        this.juros = juros;
    }

    public SituacaoParcela getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoParcela situacao) {
        this.situacao = situacao;
    }

    public LocalDate getDataCriacaoRegistro() {
        return dataCriacaoRegistro;
    }

    public void setDataCriacaoRegistro(LocalDate dataCriacaoRegistro) {
        this.dataCriacaoRegistro = dataCriacaoRegistro;
    }

    public LocalDate getDataAlteracaoRegistro() {
        return dataAlteracaoRegistro;
    }

    public void setDataAlteracaoRegistro(LocalDate dataAlteracaoRegistro) {
        this.dataAlteracaoRegistro = dataAlteracaoRegistro;
    }

    public Integer getUsuarioCriacaoRegistro() {
        return usuarioCriacaoRegistro;
    }

    public void setUsuarioCriacaoRegistro(Integer usuarioCriacaoRegistro) {
        this.usuarioCriacaoRegistro = usuarioCriacaoRegistro;
    }

    public Integer getUsuarioAlteracaoRegistro() {
        return usuarioAlteracaoRegistro;
    }

    public void setUsuarioAlteracaoRegistro(Integer usuarioAlteracaoRegistro) {
        this.usuarioAlteracaoRegistro = usuarioAlteracaoRegistro;
    }
}
