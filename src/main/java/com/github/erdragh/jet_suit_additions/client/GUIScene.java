package com.github.erdragh.jet_suit_additions.client;

import com.github.erdragh.jet_suit_additions.particle.JetSuitParticles;
import com.github.erdragh.jet_suit_additions.util.AnimationTickHolder;
import com.github.erdragh.jet_suit_additions.util.LerpedFloat;
import com.github.erdragh.jet_suit_additions.util.TransformStack;
import com.github.erdragh.jet_suit_additions.util.VecHelper;
import com.github.erdragh.jet_suit_additions.util.gui.UIRenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class GUIScene {

    private GUIWorld world;
    private Entity renderViewEntity;

    private SceneCamera camera;
    private SceneTransform transform;

    int basePlateOffsetX;
    int basePlateOffsetZ;
    int basePlateSize;
    float scaleFactor;
    float yOffset;

    private boolean stoppedCounting;
    private int totalTime;
    private int currentTime;

    public GUIScene(Level world) {
        this.world = new GUIWorld(world);
        this.camera = new SceneCamera();
        this.transform = new SceneTransform();

        this.renderViewEntity = world != null ? new ArmorStand(world, 0, 0, 0) : null;
    }

    public void renderScene(SuperRenderTypeBuffer buffer, PoseStack ms, float pt) {
        ForcedDiffuseState.pushCalculator(DiffuseLightCalculator.NETHER);
        ms.pushPose();

        Minecraft mc = Minecraft.getInstance();
        var prevRVE = mc.cameraEntity;
        mc.cameraEntity = this.renderViewEntity;
        camera.set(90, 180);

        world.renderEntities(ms, buffer, camera, pt);
        world.renderParticles(ms, buffer, camera, pt);

        mc.cameraEntity = prevRVE;

        ms.popPose();
        ForcedDiffuseState.popCalculator();
    }

    public void tick() {
        world.tick();
        transform.tick();
        this.world.addParticle(JetSuitParticles.BUBBLE.get(), 0, 0, 0, 0, 0, 0);

        if (currentTime < totalTime)
            currentTime++;
    }

    public GUIWorld getWorld() {
        return world;
    }

    public class SceneTransform {

        public LerpedFloat xRotation, yRotation;

        // Screen params
        private int width, height;
        private double offset;
        private Matrix4f cachedMat;

        public SceneTransform() {
            xRotation = LerpedFloat.angular()
                    .disableSmartAngleChasing()
                    .startWithValue(-35);
            yRotation = LerpedFloat.angular()
                    .disableSmartAngleChasing()
                    .startWithValue(55 + 90);
        }

        public void tick() {
            xRotation.tickChaser();
            yRotation.tickChaser();
        }

        public void updateScreenParams(int width, int height, double offset) {
            this.width = width;
            this.height = height;
            this.offset = offset;
            cachedMat = null;
        }

        public PoseStack apply(PoseStack ms) {
            return apply(ms, AnimationTickHolder.getPartialTicks());
        }

        public PoseStack apply(PoseStack ms, float pt) {
            ms.translate(width / 2, height / 2, 200 + offset);

            TransformStack.cast(ms)
                    .rotateX(-35)
                    .rotateY(55)
                    .translate(offset, 0, 0)
                    .rotateY(-55)
                    .rotateX(35)
                    .rotateX(xRotation.getValue(pt))
                    .rotateY(yRotation.getValue(pt));

            UIRenderHelper.flipForGuiRender(ms);
            float f = 30 * scaleFactor;
            ms.scale(f, f, f);
            ms.translate((basePlateSize + basePlateOffsetX) / -2f, -1f + yOffset,
                    (basePlateSize + basePlateOffsetZ) / -2f);

            return ms;
        }

        public void updateSceneRVE(float pt) {
            Vec3 v = screenToScene(width / 2, height / 2, 500, pt);
            if (renderViewEntity != null)
                renderViewEntity.setPos(v.x, v.y, v.z);
        }

        public Vec3 screenToScene(double x, double y, int depth, float pt) {
            refreshMatrix(pt);
            Vec3 vec = new Vec3(x, y, depth);

            vec = vec.subtract(width / 2, height / 2, 200 + offset);
            vec = VecHelper.rotate(vec, 35, Direction.Axis.X);
            vec = VecHelper.rotate(vec, -55, Direction.Axis.Y);
            vec = vec.subtract(offset, 0, 0);
            vec = VecHelper.rotate(vec, 55, Direction.Axis.Y);
            vec = VecHelper.rotate(vec, -35, Direction.Axis.X);
            vec = VecHelper.rotate(vec, -xRotation.getValue(pt), Direction.Axis.X);
            vec = VecHelper.rotate(vec, -yRotation.getValue(pt), Direction.Axis.Y);

            float f = 1f / (30 * scaleFactor);

            vec = vec.multiply(f, -f, f);
            vec = vec.subtract((basePlateSize + basePlateOffsetX) / -2f, -1f + yOffset,
                    (basePlateSize + basePlateOffsetZ) / -2f);

            return vec;
        }

        public Vec2 sceneToScreen(Vec3 vec, float pt) {
            refreshMatrix(pt);
            Vector4f vec4 = new Vector4f((float) vec.x, (float) vec.y, (float) vec.z, 1);
            vec4.transform(cachedMat);
            return new Vec2(vec4.x(), vec4.y());
        }

        protected void refreshMatrix(float pt) {
            if (cachedMat != null)
                return;
            cachedMat = apply(new PoseStack(), pt).last()
                    .pose();
        }

    }

    public class SceneCamera extends Camera {

        public void set(float xRotation, float yRotation) {
            setRotation(yRotation, xRotation);
        }

    }
}
