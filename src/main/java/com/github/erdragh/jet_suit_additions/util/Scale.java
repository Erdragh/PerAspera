package com.github.erdragh.jet_suit_additions.util;

public interface Scale<Self> {
    Self scale(float factorX, float factorY, float factorZ);

    default Self scale(float factor) {
        return scale(factor, factor, factor);
    }
}