package ao.tcc.projetofinal.jecuz.enums;

import lombok.Getter;

@Getter
public enum Icone {
    TWF_CLEANING_1("twf_cleaning-1"),
    TWF_CLEANING_2("twf_cleaning-2"),
    TWF_CLEANING_3("twf_cleaning-3");
    private String nome;
    private Icone(String nome){
        this.nome=nome;
    }
}
