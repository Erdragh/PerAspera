package com.github.erdragh.jet_suit_additions.util;

import com.mojang.blaze3d.vertex.PoseStack;

public interface TransformStack extends Transform<TransformStack>, TStack<TransformStack> {
    static TransformStack cast(PoseStack stack) {
        return (TransformStack) stack;
    }
}
