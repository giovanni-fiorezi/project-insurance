package br.com.project.insurance.entity.enums;

public enum FormaPagamento {

    DINHEIRO("DINHEIRO"),
    CARTAO("CARTAO"),
    BOLETO("BOLETO");

    private final String desc;

    FormaPagamento(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
