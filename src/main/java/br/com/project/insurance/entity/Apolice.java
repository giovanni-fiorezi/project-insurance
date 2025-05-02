package br.com.project.insurance.entity;

import br.com.project.insurance.entity.enums.SituacaoApolice;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "apolice")
public class Apolice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descricao;

    private Integer cpf; //Desenvolvi usando Inter pq veio no desafio, porém eu normalmente colocaria como String

    @Enumerated(EnumType.STRING)
    private SituacaoApolice situacao;

    @Column(name = "premio_total", precision = 10, scale = 2)
    private BigDecimal premioTotal;

    private LocalDate dataCriacaoRegistro;

    private LocalDate dataAlteracaoRegistro;

    private Integer usuarioCriacaoRegistro;

    private Integer usuarioAlteracaoRegistro;

    @OneToMany(mappedBy = "apolice", cascade = CascadeType.ALL)
    private List<Parcela> parcelas = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCpf() {
        return cpf;
    }

    public void setCpf(Integer cpf) {
        this.cpf = cpf;
    }

    public SituacaoApolice getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoApolice situacao) {
        this.situacao = situacao;
    }

    public BigDecimal getPremioTotal() {
        return premioTotal;
    }

    public void setPremioTotal(BigDecimal premioTotal) {
        this.premioTotal = premioTotal;
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

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }
}
