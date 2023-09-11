package earth.terrarium.adastra.common.blockentities;

import earth.terrarium.adastra.common.blockentities.base.PoweredMachineBlockEntity;
import earth.terrarium.adastra.common.blockentities.base.sideconfig.Configuration;
import earth.terrarium.adastra.common.blockentities.base.sideconfig.ConfigurationEntry;
import earth.terrarium.adastra.common.blockentities.base.sideconfig.ConfigurationType;
import earth.terrarium.adastra.common.blocks.BatteryBlock;
import earth.terrarium.adastra.common.blocks.base.MachineBlock;
import earth.terrarium.adastra.common.constants.ConstantComponents;
import earth.terrarium.adastra.common.menus.BatteryMenu;
import earth.terrarium.adastra.common.utils.ModUtils;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.impl.SimpleEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatteryBlockEntity extends PoweredMachineBlockEntity {

    public BatteryBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, 5);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new BatteryMenu(id, inventory, this);
    }

    @Override
    public WrappedBlockEnergyContainer getEnergyStorage() {
        if (this.energyContainer != null) return this.energyContainer;
        return this.energyContainer = new WrappedBlockEnergyContainer(
            this,
            new SimpleEnergyContainer(2_000_000) {
                @Override
                public long maxInsert() {
                    return 5_000;
                }

                @Override
                public long maxExtract() {
                    return 5_000;
                }

                @Override
                public void setEnergy(long energy) {
                    super.setEnergy(energy);
                    onEnergyChange();
                }
            });
    }

    @Override
    public void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos) {
        extractBatterySlot();
        distributeNearby(pair -> !pair.getFirst().equals(Direction.UP));
        distributeToChargeSlots();
    }

    public void onEnergyChange() {
        if (BatteryBlockEntity.this.level().getGameTime() % 10 != 0) return;
        int charge = Math.round(this.getEnergyStorage().getStoredEnergy() / (float) this.getEnergyStorage().getMaxCapacity());
        this.level().setBlock(this.getBlockPos(), this.getBlockState().setValue(BatteryBlock.CHARGE, charge), Block.UPDATE_CLIENTS);
    }

    public void distributeToChargeSlots() {
        int filledSlots = 0;
        for (int i = 0; i < 4; i++) {
            if (EnergyApi.isEnergyItem(this.getItem(i + 1))) {
                filledSlots++;
            }
        }
        if (filledSlots == 0) return;

        for (int i = 0; i < 4; i++) {
            ItemStack stack = this.getItem(i + 1);
            if (stack.isEmpty()) continue;
            if (!EnergyApi.isEnergyItem(stack)) continue;
            ItemStackHolder holder = new ItemStackHolder(stack);
            EnergyApi.moveEnergy(this, null, holder, getEnergyStorage().maxExtract() / filledSlots, false);
            if (holder.isDirty()) {
                this.setItem(i + 1, holder.getStack());
            }
        }
    }

    @Override
    public List<ConfigurationEntry> defaultConfig() {
        return List.of(
            new ConfigurationEntry(ConfigurationType.SLOT, Configuration.INPUT, ConstantComponents.SIDE_CONFIG_SLOTS),
            new ConfigurationEntry(ConfigurationType.ENERGY, Configuration.INPUT_OUTPUT, ConstantComponents.SIDE_CONFIG_ENERGY)
        );
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction side) {
        return new int[]{1, 2, 3, 4};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        if (direction == null) return false;
        var config = this.getSideConfig().get(0);
        Direction facing = getBlockState().getValue(MachineBlock.FACING).getOpposite();
        return config.get(ModUtils.relative(facing, direction)).isInput();
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        var config = this.getSideConfig().get(0);
        Direction facing = getBlockState().getValue(MachineBlock.FACING).getOpposite();
        return config.get(ModUtils.relative(facing, direction)).isOutput();
    }
}
