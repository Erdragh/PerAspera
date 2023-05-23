package com.github.erdragh.per_aspera.items.armour;

import com.github.alexnijjar.ad_astra.items.armour.NetheriteSpaceSuit;
import com.github.alexnijjar.ad_astra.mixin.gravity.CommonGravityEntityMixin;
import com.github.alexnijjar.ad_astra.util.MathUtil;
import com.github.alexnijjar.ad_astra.util.ModUtils;
import com.github.erdragh.per_aspera.PerAspera;
import com.github.erdragh.per_aspera.config.PerAsperaConfig;
import com.github.erdragh.per_aspera.networking.C2SPackets;
import com.github.erdragh.per_aspera.particle.JetSuitParticles;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ThrusterBoots extends NetheriteSpaceSuit {

    private static final Random particleRandom = new Random();

    private static double jumpCharge = 0;

    public ThrusterBoots() {
        super(PerAspera.THRUSTER_BOOTS_MATERIAL, EquipmentSlot.FEET, new FabricItemSettings().group(ItemGroup.TRANSPORTATION));
    }

    public static boolean playerIsWearingThrusterBoots(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.FEET).isOf(PerAspera.THRUSTER_BOOTS);
    }

    public static ItemStack getWornThrusterBoots(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.FEET);
    }

    public static void chargeJump() {
        if (jumpCharge < 1) jumpCharge += 0.01;
        PerAspera.LOGGER.info("Jump charged to: " + jumpCharge);
    }

    public static void resetCharge() {
        jumpCharge = 0;
    }

    public static double getJumpCharge() {
        return jumpCharge;
    }

    public static void boostPlayer(PlayerEntity player, ItemStack thrusterBoots) {
        if (PerAsperaConfig.THRUSTER_BOOTS_ENABLED.get()
                && !player.getItemCooldownManager().isCoolingDown(PerAspera.THRUSTER_BOOTS)
                && thrusterBoots.getOrCreateNbt().getBoolean("toggle_on")
        ) {
            if (player.world.isClient()) {
                var gravity = ModUtils.getPlanetGravity(player.world);
                var multiplier = MathHelper.fastInverseCbrt(gravity) * jumpCharge;
                player.setVelocity(player.getVelocity().add(new Vec3d(0.0, PerAsperaConfig.THRUSTER_BOOTS_JUMP_STRENGTH.get(), 0.0).multiply(multiplier)));
                player.getItemCooldownManager().set(PerAspera.THRUSTER_BOOTS, PerAsperaConfig.THRUSTER_BOOTS_TIMEOUT_TICKS.get());
                player.velocityDirty = true;
                jumpCharge = 0;
                ClientPlayNetworking.send(C2SPackets.PLAYER_BOOSTED_C2S, PacketByteBufs.create());
            }
        }
    }

    public static void spawnParticleBurst(Vec3d position, ClientWorld world) {
        for (int i = 0; i < 180; i++) {
            double particleSpeedMultiplier = particleRandom.nextDouble() * 0.1;
            Vec3d particleSpeed = new Vec3d(particleRandom.nextDouble() - 0.5, .2, particleRandom.nextDouble() - 0.5).normalize().multiply(particleSpeedMultiplier, particleSpeedMultiplier, particleSpeedMultiplier);
            world.addParticle(JetSuitParticles.SOUL_FIRE_FLAME.get(), true, position.x, position.y, position.z, particleSpeed.x, particleSpeed.y, particleSpeed.z);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (stack.isOf(PerAspera.THRUSTER_BOOTS)) {
            boolean turnedOn = stack.getOrCreateNbt().getBoolean("toggle_on");
            Text turnedOnText = new TranslatableText(PerAspera.MODID + ".msg.thruster_boots_toggle").append(new TranslatableText(PerAspera.MODID + ".msg.jet_suit_" + (turnedOn ? "on" : "off")).setStyle(Style.EMPTY.withBold(true).withColor(turnedOn ? Formatting.GREEN : Formatting.RED)));
            tooltip.add(turnedOnText);
        }
    }
}
