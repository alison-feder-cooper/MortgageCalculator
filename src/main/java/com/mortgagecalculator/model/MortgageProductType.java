package com.mortgagecalculator.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum MortgageProductType {

    THIRTY_YEAR_FIXED("FNMA30YRFXCF", 30),
    FIFTEEN_YEAR_FIXED("FNMA15YRFXCF", 15),
    FIVE_ONE_ADJUSTABLE("FNMA51ARMCF", 5),
    SEVEN_ONE_ADJUSTABLE("FNMA71ARMCF", 7),
    TEN_ONE_ADJUSTABLE("FNMA101ARMCF", 10);

    private static final Map<String, MortgageProductType> CODE_LOOKUP_MAP;
    private static final Set<MortgageProductType> FIXED_TYPES;

    static {
        CODE_LOOKUP_MAP = new HashMap<>();
        for (MortgageProductType productType : MortgageProductType.values()) {
            CODE_LOOKUP_MAP.put(productType.getCode(), productType);
        }

        FIXED_TYPES = new HashSet<>();
        FIXED_TYPES.add(THIRTY_YEAR_FIXED);
        FIXED_TYPES.add(FIFTEEN_YEAR_FIXED);
    }

    private final String code;
    private final int fixedYears;

    MortgageProductType(String code, int fixedYears) {
        this.code = code;
        this.fixedYears = fixedYears;
    }

    public String getCode() {
        return code;
    }

    public int getFixedYears() {
        return fixedYears;
    }

    public boolean isFixed() {
        return FIXED_TYPES.contains(this);
    }

    public static MortgageProductType getByCode(String code) {
        return CODE_LOOKUP_MAP.get(code);
    }
}
