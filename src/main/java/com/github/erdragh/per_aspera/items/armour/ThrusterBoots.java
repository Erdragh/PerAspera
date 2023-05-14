package com.github.erdragh.per_aspera.items.armour;

import com.github.alexnijjar.ad_astra.items.armour.SpaceSuit;
import com.github.erdragh.per_aspera.PerAspera;
import com.github.erdragh.per_aspera.config.PerAsperaConfig;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThrusterBoots extends SpaceSuit {
    public ThrusterBoots() {
        super(PerAspera.THRUSTER_BOOTS_MATERIAL, EquipmentSlot.FEET, new FabricItemSettings().group(CreativeModeTab.TAB_TRANSPORTATION));
    }

    public static boolean playerIsWearingThrusterBoots(Player player) {
        return player.getItemBySlot(EquipmentSlot.FEET).is(PerAspera.THRUSTER_BOOTS);
    }

    @Nullable
    public static ItemStack getWornThrusterBoots(Player player) {
        return player.getItemBySlot(EquipmentSlot.FEET);
    }

    public static void boostPlayer(Player player, ItemStack thrusterBoots) {
        if (PerAsperaConfig.THRUSTER_BOOTS_ENABLED.get()
                && !player.getCooldowns().isOnCooldown(PerAspera.THRUSTER_BOOTS)
                && thrusterBoots.getOrCreateTag().getBoolean("toggle_on")
                && player.level.isClientSide()
        ) {
            player.setDeltaMovement(player.getDeltaMovement().add(0.0, PerAsperaConfig.THRUSTER_BOOTS_JUMP_STRENGTH.get(), 0.0));
            player.getCooldowns().addCooldown(PerAspera.THRUSTER_BOOTS, PerAsperaConfig.THRUSTER_BOOTS_TIMEOUT_TICKS.get());
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        if (stack.is(PerAspera.THRUSTER_BOOTS)) {
            boolean turnedOn = stack.getOrCreateTag().getBoolean("toggle_on");
            Component turnedOnText = new TranslatableComponent(PerAspera.MODID + ".msg.thruster_boots_toggle").append(new TranslatableComponent(PerAspera.MODID + ".msg.jet_suit_" + (turnedOn ? "on" : "off")).setStyle(Style.EMPTY.withBold(true).withColor(turnedOn ? ChatFormatting.GREEN : ChatFormatting.RED)));
            tooltip.add(turnedOnText);
        }
    }
}
