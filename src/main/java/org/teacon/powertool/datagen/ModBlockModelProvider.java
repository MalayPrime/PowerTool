package org.teacon.powertool.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.teacon.powertool.block.CosmeticFurnace;
import org.teacon.powertool.block.PowerToolBlocks;

import java.util.Objects;


public class ModBlockModelProvider extends BlockStateProvider {

    public ModBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DataGenerators.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        PowerToolBlocks.SIMPLE_BLOCKS.forEach(block -> simpleBlockWithItem(cosmeticBlock(block), models().getExistingFile(mcLoc(name(block)))));
        cosmeticHorizontalBlockWithItem(Blocks.BEEHIVE);
        cosmeticFurnaceBlockWithItem(Blocks.FURNACE);
        cosmeticFurnaceBlockWithItem(Blocks.BLAST_FURNACE);
        cosmeticDirectionalBlockWithItem(Blocks.BARREL);
    }

    private Block cosmeticBlock(Block block){
        return BuiltInRegistries.BLOCK.get(modLoc("cosmetic_" + name(block)));
    }

    private static String name(Block block){
        return Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).getPath();
    }

    @SuppressWarnings("SameParameterValue")
    private void cosmeticHorizontalBlockWithItem(Block block){
        horizontalBlock(cosmeticBlock(block), models().getExistingFile(mcLoc(name(block))));
        item(block);
    }

    @SuppressWarnings("SameParameterValue")
    private void cosmeticDirectionalBlockWithItem(Block block){
        directionalBlock(cosmeticBlock(block), models().getExistingFile(mcLoc(name(block))));
        item(block);
    }

    private void cosmeticFurnaceBlockWithItem(Block block){
        getVariantBuilder(cosmeticBlock(block)).forAllStates(state -> {
            var model = state.getValue(CosmeticFurnace.LIT) ? models().getExistingFile(mcLoc(name(block) + "_on")) : models().getExistingFile(mcLoc(name(block)));
            return ConfiguredModel.builder().modelFile(model).rotationY((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180).build();
        });
        item(block);
    }

    private void item(Block block) {
        itemModels().getBuilder(name(cosmeticBlock(block))).parent(models().getExistingFile(mcLoc(name(block))));
    }


}
