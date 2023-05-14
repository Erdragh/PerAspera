package com.github.erdragh.per_aspera.items.armour;

import com.github.alexnijjar.ad_astra.items.armour.SpaceSuit;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ThrusterBoots extends SpaceSuit {

    private static final Random particleRandom = new Random();

    public ThrusterBoots() {
        super(PerAspera.THRUSTER_BOOTS_MATERIAL, EquipmentSlot.FEET, new FabricItemSettings().group(ItemGroup.TRANSPORTATION));
    }

    public static boolean playerIsWearingThrusterBoots(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.FEET).isOf(PerAspera.THRUSTER_BOOTS);
    }

    public static ItemStack getWornThrusterBoots(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.FEET);
    }

    public static void boostPlayer(PlayerEntity player, ItemStack thrusterBoots) {
        if (PerAsperaConfig.THRUSTER_BOOTS_ENABLED.get()
                && !player.getItemCooldownManager().isCoolingDown(PerAspera.THRUSTER_BOOTS)
                && thrusterBoots.getOrCreateNbt().getBoolean("toggle_on")
        ) {
            if (player.world.isClient()) {
                player.setVelocity(player.getVelocity().add(0.0, PerAsperaConfig.THRUSTER_BOOTS_JUMP_STRENGTH.get(), 0.0));
                player.getItemCooldownManager().set(PerAspera.THRUSTER_BOOTS, PerAsperaConfig.THRUSTER_BOOTS_TIMEOUT_TICKS.get());
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
