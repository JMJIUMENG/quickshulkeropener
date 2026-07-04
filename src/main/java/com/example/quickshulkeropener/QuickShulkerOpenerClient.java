package com.example.quickshulkeropener;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class QuickShulkerOpenerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.contains(DataComponentTypes.CONTAINER)) {
                    openContainerGui(player, stack);
                    return ActionResult.PASS;
                }
            }
            return ActionResult.PASS;
        });
    }

    private void openContainerGui(PlayerEntity player, ItemStack stack) {
        var container = stack.get(DataComponentTypes.CONTAINER);
        if (container == null) return;
        SimpleInventory inventory = new SimpleInventory(container.size());
        for (int i = 0; i < container.size(); i++) {
            inventory.setStack(i, container.get(i));
        }
        ScreenHandlerFactory factory = (syncId, inv, p) ->
                GenericContainerScreenHandler.createGeneric9x6(syncId, inv, inventory);
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                factory,
                Text.literal(stack.getName().getString())
        ));
    }
}