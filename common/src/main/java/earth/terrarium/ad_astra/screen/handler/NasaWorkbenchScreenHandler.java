package earth.terrarium.ad_astra.screen.handler;

import earth.terrarium.ad_astra.blocks.machines.entity.NasaWorkbenchBlockEntity;
import earth.terrarium.ad_astra.networking.NetworkHandling;
import earth.terrarium.ad_astra.networking.packets.server.MachineInfoPacket;
import earth.terrarium.ad_astra.recipes.NasaWorkbenchRecipe;
import earth.terrarium.ad_astra.registry.ModRecipes;
import earth.terrarium.ad_astra.registry.ModScreenHandlers;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NasaWorkbenchScreenHandler extends AbstractMachineScreenHandler<NasaWorkbenchBlockEntity> {

    private ItemStack output = ItemStack.EMPTY;
    private List<Integer> stackCounts = new ArrayList<>();

    public NasaWorkbenchScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf) {
        this(syncId, inventory, (NasaWorkbenchBlockEntity) inventory.player.level.getBlockEntity(buf.readBlockPos()));
    }

    public NasaWorkbenchScreenHandler(int syncId, Inventory inventory, NasaWorkbenchBlockEntity entity) {
        super(ModScreenHandlers.NASA_WORKBENCH_SCREEN_HANDLER.get(), syncId, inventory, entity, new Slot[]{

                // Nose
                new Slot(entity, 0, 56, 20),

                // Body
                new Slot(entity, 1, 47, 18 * 2 + 2), //
                new Slot(entity, 2, 65, 18 * 2 + 2), //
                new Slot(entity, 3, 47, 18 * 3 + 2), //
                new Slot(entity, 4, 65, 18 * 3 + 2), //
                new Slot(entity, 5, 47, 18 * 4 + 2), //
                new Slot(entity, 6, 65, 18 * 4 + 2), //

                // Left fin
                new Slot(entity, 7, 29, 18 * 5 + 2),

                // Tank
                new Slot(entity, 8, 47, 18 * 5 + 2), //
                new Slot(entity, 9, 65, 18 * 5 + 2), //

                // Right fin
                new Slot(entity, 10, 83, 18 * 5 + 2),

                // Left fin
                new Slot(entity, 11, 29, 18 * 6 + 2),

                // Engine
                new Slot(entity, 12, 56, 18 * 6 + 2),

                // Right fin
                new Slot(entity, 13, 83, 18 * 6 + 2),

                // Output
                new Slot(entity, 14, 129, 56) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                }});
        this.updateContent();
    }

    @Override
    public int getPlayerInventoryOffset() {
        return 58;
    }

    @Override
    public void clicked(int slotIndex, int button, ClickType actionType, Player player) {

        if (slotIndex == 14) {
            if (!machine.getItems().get(14).isEmpty()) {
                machine.spawnResultParticles();
                machine.spawnOutputAndClearInput(this.stackCounts, this.output);
            }
        } else {
            super.clicked(slotIndex, button, actionType, player);
        }
        this.updateContent();
    }

    @Override
    public void slotsChanged(Container inventory) {
        this.updateContent();
    }

    public void updateContent() {

        NasaWorkbenchRecipe recipe = ModRecipes.NASA_WORKBENCH_RECIPE.get().findFirst(level, f -> f.test(this.machine));

        ItemStack output = ItemStack.EMPTY;
        if (recipe != null) {
            output = recipe.getResultItem();
            this.stackCounts = recipe.getStackCounts();
        }
        this.output = output;
        this.machine.setItem(14, output);
        this.broadcastFullState();
    }

    @Override
    public void syncClientScreen() {
        NetworkHandling.CHANNEL.sendToPlayer(new MachineInfoPacket(0L, List.of()), this.player);
    }
}