package br.com.project.insurance.entity.enums;

public enum SituacaoParcela {

    PAGO("PAGO"),
    PENDENTE("PENDENTE"),
    ATRASADA("ATRASADA");

    private final String desc;

    SituacaoParcela(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
