package com.github.erdragh.jet_suit_additions.client;

import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;

public class DummyLevelEntityGetter<T extends EntityAccess> implements LevelEntityGetter<T> {
    @Nullable
    @Override
    public T get(int id) {
        return null;
    }

    @Nullable
    @Override
    public T get(UUID uuid) {
        return null;
    }

    @Override
    public Iterable<T> getAll() {
        return Collections.emptyList();
    }

    @Override
    public <U extends T> void get(EntityTypeTest<T, U> test, Consumer<U> consumer) {

    }

    @Override
    public void get(AABB boundingBox, Consumer<T> consumer) {

    }

    @Override
    public <U extends T> void get(EntityTypeTest<T, U> test, AABB bounds, Consumer<U> consumer) {

    }
}
