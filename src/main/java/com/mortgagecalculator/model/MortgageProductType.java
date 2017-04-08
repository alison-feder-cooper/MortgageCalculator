package com.mortgagecalculator.model;

import java.util.HashMap;
import java.util.Map;

public enum MortgageProductType {

    THIRTY_YEAR_FIXED("FNMA30YRFXCF"),
    FIFTEEN_YEAR_FIXED("FNMA15YRFXCF"),
    FIVE_ONE_ADJUSTABLE("FNMA51ARMCF"),
    SEVEN_ONE_ADJUSTABLE("FNMA71ARMCF"),
    TEN_ONE_ADJUSTABLE("FNMA101ARMCF");

    private static final Map<String, MortgageProductType> CODE_LOOKUP_MAP;

    static {
        CODE_LOOKUP_MAP = new HashMap<>();
        for (MortgageProductType productType : MortgageProductType.values()) {
            CODE_LOOKUP_MAP.put(productType.getCode(), productType);
        }
    }

    private String code;

    MortgageProductType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static MortgageProductType getByCode(String code) {
        return CODE_LOOKUP_MAP.get(code);
    }
}
