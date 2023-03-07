package com.github.erdragh.jet_suit_additions.client;

import com.github.erdragh.jet_suit_additions.JetSuitAdditions;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.mixin.client.accessor.ParticleEngineAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.*;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.ticks.BlackholeTickAccess;
import net.minecraft.world.ticks.LevelTickAccess;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class GUIWorld extends Level {
    protected Level world;
    protected ChunkSource chunkSource;

    protected Map<BlockPos, BlockState> blocks;
    protected Map<BlockPos, BlockEntity> tileEntities;
    protected List<BlockEntity> renderedTileEntities;
    protected List<Entity> entities;
    protected BoundingBox bounds;

    public BlockPos anchor;
    public boolean renderMode;

    protected LevelEntityGetter<Entity> entityGetter = new DummyLevelEntityGetter<>();

    protected GUIWorldParticles particles;
    private final Int2ObjectMap<ParticleProvider<?>> particleProviders;

    private Supplier<ClientLevel> asClientWorld = Suppliers.memoize(() -> GUIClientWorld.of(this));

    public GUIWorld(Level world) {
        super((WritableLevelData) world.getLevelData(), world.dimension(), world.dimensionTypeRegistration(),
                world::getProfiler, world.isClientSide, world.isDebug(), 0);
        this.world = world;
        this.blocks = new HashMap<>();
        this.tileEntities = new HashMap<>();
        this.bounds = new BoundingBox(BlockPos.ZERO);
        this.anchor = BlockPos.ZERO;
        this.entities = new ArrayList<>();
        this.renderedTileEntities = new ArrayList<>();

        particles = new GUIWorldParticles(this);
        particleProviders = ((ParticleEngineAccessor) Minecraft.getInstance().particleEngine).port_lib$getProviders();
    }

    public Set<BlockPos> getAllPositions() {
        return blocks.keySet();
    }

    public void setChunkSource(ChunkSource chunkSource) {
        this.chunkSource = chunkSource;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return world.getLightEngine();
    }

    @Override
    public BlockState getBlockState(BlockPos globalPos) {
        BlockPos pos = globalPos.subtract(anchor);

        if (pos.getY() - bounds.minY() == -1 && !renderMode)
            return Blocks.GRASS_BLOCK.defaultBlockState();
        if (getBounds().isInside(pos) && blocks.containsKey(pos))
            return processBlockStateForPrinting(blocks.get(pos));
        return Blocks.AIR.defaultBlockState();
    }

    public Map<BlockPos, BlockState> getBlockMap() {
        return blocks;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return getBlockState(pos).getFluidState();
    }

    @Override
    public Holder<Biome> getBiome(BlockPos pos) {
        return BuiltinRegistries.BIOME.getHolder(Biomes.PLAINS)
                .orElse(null);
    }

    @Override
    public int getBrightness(LightLayer lightType, BlockPos blockPos) {
        return 15;
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate) {
        return predicate.test(getBlockState(pos));
    }

    @Override
    public boolean destroyBlock(BlockPos arg0, boolean arg1) {
        return setBlock(arg0, Blocks.AIR.defaultBlockState(), 3);
    }

    @Override
    public boolean removeBlock(BlockPos arg0, boolean arg1) {
        return setBlock(arg0, Blocks.AIR.defaultBlockState(), 3);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState arg1, int arg2) {
        pos = pos.immutable()
                .subtract(anchor);
        bounds = BBHelper.encapsulate(bounds, pos);
        blocks.put(pos, arg1);
        if (tileEntities.containsKey(pos)) {
            BlockEntity tileEntity = tileEntities.get(pos);
            if (!tileEntity.getType()
                    .isValid(arg1)) {
                tileEntities.remove(pos);
                renderedTileEntities.remove(tileEntity);
            }
        }

        BlockEntity tileEntity = getBlockEntity(pos);
        if (tileEntity != null)
            tileEntities.put(pos, tileEntity);

        return true;
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        if (isOutsideBuildHeight(pos))
            return null;
        if (tileEntities.containsKey(pos))
            return tileEntities.get(pos);
        if (!blocks.containsKey(pos.subtract(anchor)))
            return null;

        BlockState blockState = getBlockState(pos);
        if (blockState.hasBlockEntity()) {
            try {
                BlockEntity tileEntity = ((EntityBlock) blockState.getBlock()).newBlockEntity(pos, blockState);
                if (tileEntity != null) {
                    onTEadded(tileEntity, pos);
                    tileEntities.put(pos, tileEntity);
                    renderedTileEntities.add(tileEntity);
                }
                return tileEntity;
            } catch (Exception e) {
                JetSuitAdditions.LOGGER.debug("Could not create TE of block " + blockState, e);
            }
        }
        return null;
    }
    protected void onTEadded(BlockEntity tileEntity, BlockPos pos) {
        tileEntity.setLevel(this);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPos pos) {
        return 15;
    }

    @Override
    public void sendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, int flags) {
        world.sendBlockUpdated(pos, oldState, newState, flags);
    }

    @Override
    public void playSound(@Nullable Player player, double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch) {

    }

    @Override
    public void playSound(@Nullable Player player, Entity entity, SoundEvent event, SoundSource category, float volume, float pitch) {

    }

    @Override
    public String gatherChunkSourceStats() {
        return world.gatherChunkSourceStats();
    }

    @Nullable
    @Override
    public Entity getEntity(int id) {
        return null;
    }

    @Nullable
    @Override
    public MapItemSavedData getMapData(String mapName) {
        return null;
    }

    @Override
    public void setMapData(String mapId, MapItemSavedData data) {

    }

    @Override
    public int getFreeMapId() {
        return world.getFreeMapId();
    }

    @Override
    public void destroyBlockProgress(int breakerId, BlockPos pos, int progress) {

    }

    @Override
    public Scoreboard getScoreboard() {
        return world.getScoreboard();
    }

    @Override
    public RecipeManager getRecipeManager() {
        return world.getRecipeManager();
    }

    @Override
    protected LevelEntityGetter<Entity> getEntities() {
        return entityGetter;
    }

    @Override
    public List<Entity> getEntities(Entity arg0, AABB arg1, Predicate<? super Entity> arg2) {
        return Collections.emptyList();
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> arg0, AABB arg1, Predicate<? super T> arg2) {
        return Collections.emptyList();
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return BlackholeTickAccess.emptyLevelList();
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return BlackholeTickAccess.emptyLevelList();
    }

    @Override
    public ChunkSource getChunkSource() {
        return chunkSource != null ? chunkSource : world.getChunkSource();
    }

    @Nullable
    @Override
    public BlockGetter getChunkForCollisions(int chunkX, int chunkZ) {
        return this;
    }

    @Override
    public void levelEvent(@Nullable Player player, int type, BlockPos pos, int data) {
    }

    @Override
    public void gameEvent(@Nullable Entity entity, GameEvent event, BlockPos pos) {

    }

    @Override
    public RegistryAccess registryAccess() {
        return world.registryAccess();
    }

    @Override
    public float getShade(Direction direction, boolean shade) {
        return 1f;
    }

    @Override
    public List<? extends Player> players() {
        return Collections.emptyList();
    }

    @Override
    public int getSkyDarken() {
        return 0;
    }

    @Override
    public boolean addFreshEntity(Entity entity) {
        if (entity instanceof ItemFrame itemFrame)
            itemFrame.getItem()
                    .setTag(null);
        if (entity instanceof ArmorStand armorStand) {
            armorStand.getAllSlots()
                    .forEach(stack -> stack.setTag(null));
        }

        return entities.add(entity);
    }

    public Stream<Entity> getEntityStream() {
        return entities.stream();
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int x, int y, int z) {
        return world.getUncachedNoiseBiome(x, y, z);
    }

    @Override
    public void updateNeighbourForOutputSignal(BlockPos pos, Block block) {
    }

    @Override
    public int getMaxBuildHeight() {
        return this.getMinBuildHeight() + this.getHeight();
    }

    @Override
    public int getSectionsCount() {
        return this.getMaxSection() - this.getMinSection();
    }

    @Override
    public int getMinSection() {
        return SectionPos.blockToSectionCoord(this.getMinBuildHeight());
    }

    @Override
    public int getMaxSection() {
        return SectionPos.blockToSectionCoord(this.getMaxBuildHeight() - 1) + 1;
    }

    @Override
    public boolean isOutsideBuildHeight(BlockPos pos) {
        return this.isOutsideBuildHeight(pos.getY());
    }

    @Override
    public boolean isOutsideBuildHeight(int y) {
        return y < this.getMinBuildHeight() || y >= this.getMaxBuildHeight();
    }

    @Override
    public int getSectionIndex(int y) {
        return this.getSectionIndexFromSectionY(SectionPos.blockToSectionCoord(y));
    }

    @Override
    public int getSectionIndexFromSectionY(int sectionY) {
        return sectionY - this.getMinSection();
    }

    @Override
    public int getSectionYFromSectionIndex(int sectionIndex) {
        return sectionIndex + this.getMinSection();
    }


    public BoundingBox getBounds() {
        return bounds;
    }

    public Iterable<BlockEntity> getRenderedTileEntities() {
        return renderedTileEntities;
    }

    protected BlockState processBlockStateForPrinting(BlockState state) {
        if (state.getBlock() instanceof AbstractFurnaceBlock && state.hasProperty(BlockStateProperties.LIT))
            state = state.setValue(BlockStateProperties.LIT, false);
        return state;
    }

    public ServerLevel getLevel() {
        if (this.world instanceof ServerLevel) {
            return (ServerLevel) this.world;
        }
        throw new IllegalStateException("Cannot use IServerWorld#getWorld in a client environment");
    }

    public void renderEntities(PoseStack ms, SuperRenderTypeBuffer buffer, Camera camera, float pt) {
        Vec3 Vector3d = camera.getPosition();
        double d0 = Vector3d.x();
        double d1 = Vector3d.y();
        double d2 = Vector3d.z();

        for (Entity entity : entities) {
            if (entity.tickCount == 0) {
                entity.xOld = entity.getX();
                entity.yOld = entity.getY();
                entity.zOld = entity.getZ();
            }
            renderEntity(entity, d0, d1, d2, pt, ms, buffer);
        }

        buffer.draw(RenderType.entitySolid(InventoryMenu.BLOCK_ATLAS));
        buffer.draw(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS));
        buffer.draw(RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS));
        buffer.draw(RenderType.entitySmoothCutout(InventoryMenu.BLOCK_ATLAS));
    }

    private void renderEntity(Entity entity, double x, double y, double z, float pt, PoseStack ms,
                              MultiBufferSource buffer) {
        double d0 = Mth.lerp((double) pt, entity.xOld, entity.getX());
        double d1 = Mth.lerp((double) pt, entity.yOld, entity.getY());
        double d2 = Mth.lerp((double) pt, entity.zOld, entity.getZ());
        float f = Mth.lerp(pt, entity.yRotO, entity.getYRot());
        EntityRenderDispatcher renderManager = Minecraft.getInstance()
                .getEntityRenderDispatcher();
        int light = renderManager.getRenderer(entity)
                .getPackedLightCoords(entity, pt);
        renderManager.render(entity, d0 - x, d1 - y, d2 - z, f, pt, ms, buffer, light);
    }

    public void renderParticles(PoseStack ms, MultiBufferSource buffer, Camera ari, float pt) {
        particles.renderParticles(ms, buffer, ari, pt);
    }

    public void tick() {
        particles.tick();

        for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext();) {
            Entity entity = iterator.next();

            entity.tickCount++;
            entity.xOld = entity.getX();
            entity.yOld = entity.getY();
            entity.zOld = entity.getZ();
            entity.tick();

            if (entity.getY() <= -.5f)
                entity.discard();

            if (!entity.isAlive())
                iterator.remove();
        }
    }

    @Override
    public void addParticle(ParticleOptions data, double x, double y, double z, double mx, double my, double mz) {
        addParticle(makeParticle(data, x, y, z, mx, my, mz));
    }

    @Override
    public void addAlwaysVisibleParticle(ParticleOptions data, double x, double y, double z, double mx, double my, double mz) {
        addParticle(data, x, y, z, mx, my, mz);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <T extends ParticleOptions> Particle makeParticle(T data, double x, double y, double z, double mx, double my,
                                                              double mz) {
        int id = Registry.PARTICLE_TYPE.getId(data.getType());
        ParticleProvider<T> particleProvider = (ParticleProvider<T>) particleProviders.get(id);
        return particleProvider == null ? null
                : particleProvider.createParticle(data, asClientWorld.get(), x, y, z, mx, my, mz);
    }


    public void addParticle(Particle p) {
        if (p != null)
            particles.addParticle(p);
    }


    public void addBlockDestroyEffects(BlockPos pos, BlockState state) {
        VoxelShape voxelshape = state.getShape(this, pos);
        if (voxelshape.isEmpty())
            return;

        AABB bb = voxelshape.bounds();
        double d1 = Math.min(1.0D, bb.maxX - bb.minX);
        double d2 = Math.min(1.0D, bb.maxY - bb.minY);
        double d3 = Math.min(1.0D, bb.maxZ - bb.minZ);
        int i = Math.max(2, Mth.ceil(d1 / 0.25D));
        int j = Math.max(2, Mth.ceil(d2 / 0.25D));
        int k = Math.max(2, Mth.ceil(d3 / 0.25D));

        for (int l = 0; l < i; ++l) {
            for (int i1 = 0; i1 < j; ++i1) {
                for (int j1 = 0; j1 < k; ++j1) {
                    double d4 = (l + 0.5D) / i;
                    double d5 = (i1 + 0.5D) / j;
                    double d6 = (j1 + 0.5D) / k;
                    double d7 = d4 * d1 + bb.minX;
                    double d8 = d5 * d2 + bb.minY;
                    double d9 = d6 * d3 + bb.minZ;
                    addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), pos.getX() + d7, pos.getY() + d8,
                            pos.getZ() + d9, d4 - 0.5D, d5 - 0.5D, d6 - 0.5D);
                }
            }
        }
    }


    @Override
    public boolean hasChunkAt(BlockPos pos) {
        return true; // fix particle lighting
    }

    @Override
    public boolean hasChunk(int x, int y) {
        return true; // fix particle lighting
    }

    @Override
    public boolean isLoaded(BlockPos pos) {
        return true; // fix particle lighting
    }

    @Override
    public boolean hasNearbyAlivePlayer(double p_217358_1_, double p_217358_3_, double p_217358_5_, double p_217358_7_) {
        return true; // always enable spawner animations
    }

    // In case another mod (read: Lithium) has overwritten noCollision and would break PonderWorlds, force vanilla behavior in PonderWorlds
    @Override
    public boolean noCollision(@Nullable Entity entity, AABB collisionBox) {
        // Vanilla copy
        Iterator var3 = this.getBlockCollisions(entity, collisionBox).iterator();

        while(var3.hasNext()) {
            VoxelShape voxelShape = (VoxelShape)var3.next();
            if (!voxelShape.isEmpty()) {
                return false;
            }
        }

        if (!this.getEntityCollisions(entity, collisionBox).isEmpty()) {
            return false;
        } else if (entity == null) {
            return true;
        } else {
            VoxelShape voxelShape2 = this.borderCollision(entity, collisionBox);
            return voxelShape2 == null || !Shapes.joinIsNotEmpty(voxelShape2, Shapes.create(collisionBox), BooleanOp.AND);
        }
    }

    VoxelShape borderCollision(Entity entity, AABB aABB) {
        WorldBorder worldBorder = this.getWorldBorder();
        return worldBorder.isInsideCloseToBorder(entity, aABB) ? worldBorder.getCollisionShape() : null;
    }

}
