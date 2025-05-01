package br.com.project.insurance.entity.enums;

public enum SituacaoApolice {

    ATIVA("ATIVA"),
    INATIVA("INATIVA");

    private final String desc;

    SituacaoApolice(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
