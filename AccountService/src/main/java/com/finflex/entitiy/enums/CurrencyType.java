package com.finflex.entitiy.enums;

import lombok.Getter;

@Getter
public enum CurrencyType {

    TRY("Türk Lirası"),
    USD("Amerikan Doları"),
    EUR("Euro"),
    GBP("İngiliz Sterlini"),
    CAD("Kanada Doları");

    private final String description;

    CurrencyType(String description) {
        this.description = description;
    }

    public CurrencyType convert(String source) {
        return CurrencyType.valueOf(source.toUpperCase());
    }

}
