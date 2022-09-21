package com.github.alexnijjar.ad_astra.registry;

import com.github.alexnijjar.ad_astra.AdAstra;
import com.github.alexnijjar.ad_astra.world.processor.StructureVoidProcessor;
import com.github.alexnijjar.ad_astra.world.structures.LargeJigsawStructure;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.structure.StructureType;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(AdAstra.MOD_ID, Registry.STRUCTURE_TYPE_KEY);
    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSOR_TYPES = DeferredRegister.create(AdAstra.MOD_ID, Registry.STRUCTURE_PROCESSOR_KEY);
    
    public static final StructureProcessorType<StructureVoidProcessor> STRUCTURE_VOID_PROCESSOR = () -> StructureVoidProcessor.CODEC;
    public static final StructureType<LargeJigsawStructure> LARGE_JIGSAW_STRUCTURE = () -> LargeJigsawStructure.CODEC;

    public static void register() {
        STRUCTURE_TYPES.register("structure_void_processor", () -> LARGE_JIGSAW_STRUCTURE);
        STRUCTURE_PROCESSOR_TYPES.register("structure_void_processor", () -> STRUCTURE_VOID_PROCESSOR);
        STRUCTURE_PROCESSOR_TYPES.register();
        STRUCTURE_TYPES.register();
    }


}