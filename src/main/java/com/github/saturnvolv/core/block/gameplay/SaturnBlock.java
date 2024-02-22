package com.github.saturnvolv.core.block.gameplay;

import com.github.saturnvolv.core.SaturnCore;
import com.github.saturnvolv.core.block.BlockBreakContext;
import com.github.saturnvolv.core.block.BlockRegistry;
import com.github.saturnvolv.core.block.BlockSettings;
import com.github.saturnvolv.core.item.ToolTier;
import com.github.saturnvolv.core.item.ToolType;
import net.kyori.adventure.translation.Translatable;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SaturnBlock implements Translatable {
    private int ID;
    private final boolean isTransparent;
    private final float hardness;
    private final boolean requiresTool;
    private final boolean breakByHand;
    private final ToolType brokenBy;
    private final int miningLvl;
    private @NotNull final NamespacedKey key;
    private static int solidBlockCount = 0;
    private static int transparentBlockCount = 0;


    private BlockData blockData;
    public SaturnBlock(String key, BlockSettings blockSettings) {
        this.key = new NamespacedKey(SaturnCore.MOD_ID, key);
        this.hardness = blockSettings.hardness();
        this.miningLvl = blockSettings.miningLvl();
        this.requiresTool = blockSettings.requiresTool();
        this.breakByHand = blockSettings.breakByHand();
        this.isTransparent = blockSettings.isTransparent();
        this.brokenBy = blockSettings.brokenBy();
    }

    public boolean isTransparent() {
        return isTransparent;
    }
    public float hardness() {
        return hardness;
    }
    public boolean requiresTool() {
        return requiresTool;
    }
    public boolean breakByHand() {
        return breakByHand;
    }
    public ToolType brokenBy() {
        return brokenBy;
    }
    public int miningLvl() {
        return miningLvl;
    }
    public @Nullable BlockData blockData() {
        return blockData;
    }
    public void register() {
        this.ID = this.isTransparent ?
                transparentBlockCount++ :
                solidBlockCount++;
        try {
            this.blockData = blockDataFromID(ID, this.isTransparent);
        } catch (Exception e) { SaturnCore.get().getServer().getPluginManager().disablePlugin(SaturnCore.get()); }
    }
    @Override
    public @NotNull String translationKey() {
        return String.format("block.%1$s.%2$s", key.namespace(), key.value());
    }

    public static boolean exists( Block block ) {
        return switch (block.getType()) {
            case NOTE_BLOCK -> exists((NoteBlock) block.getBlockData());
            case TRIPWIRE -> exists((Tripwire) block.getBlockData());
            default -> false;
        };
    }
    static boolean exists( NoteBlock data ) {
        try {
            return data != noteBlockFromID(0);
        } catch (Exception ignored) { }
        return false;
    }
    static boolean exists( Tripwire data ) {
        try {
            return data != tripwireFromID(0);
        } catch (Exception ignored) { }
        return false;
    }
    public static @Nullable SaturnBlock get( @NotNull Block block ) {
        BlockData data = block.getBlockData();
        return BlockRegistry.getBlock(data);
    }

    public static BlockData blockDataFromID(int ID, boolean isSolid) throws Exception {
        return isSolid ? noteBlockFromID(ID) : tripwireFromID(ID);
    }
    static final Integer NOTE_COUNT = 25;
    static final Integer INSTRUMENT_COUNT = 22;
    static final int MAX_COUNT = NOTE_COUNT * INSTRUMENT_COUNT * 2;
    private static @NotNull NoteBlock noteBlockFromID(int ID) throws Exception {
        if (ID > MAX_COUNT) throw new Exception("Exceeded maximum number of usable Noteblock states.");

        int note = 0;
        int instrument = 0;
        boolean powered = false;
        for (int i = 0; i < ID; i++) {
            if (++note >= NOTE_COUNT) {
                note = 0; instrument++;
            }
            if (instrument >= INSTRUMENT_COUNT) {
                instrument = 0;
                powered = true;
            }
        }
        NoteBlock blockData = (NoteBlock) Bukkit.createBlockData(Material.NOTE_BLOCK);
        blockData.setPowered(powered);
        blockData.setNote(new Note(note));
        blockData.setInstrument(Instrument.getByType((byte) instrument));
        return blockData;
    }
    static final double MAX_TRANS_BLOCKS = Math.pow(2, 5);
    private static @NotNull Tripwire tripwireFromID(int ID) throws Exception {
        if (ID > MAX_TRANS_BLOCKS) throw new Exception("Exceeded maximum number of usable Tripwire states.");

        String unpadded = Integer.toBinaryString(ID);
        String padded = "000000".substring(unpadded.length()) + unpadded;

        padded = StringUtils.reverse(padded);
        boolean disarmed = padded.charAt(0) == '1';
        boolean east = padded.charAt(1) == '1';
        boolean north = padded.charAt(2) == '1';
        boolean south = padded.charAt(3) == '1';
        boolean west = padded.charAt(4) == '1';
        boolean attached = padded.charAt(5) == '1';

        Tripwire blockData = (Tripwire) Bukkit.createBlockData(Material.TRIPWIRE);
        blockData.setAttached(attached);
        blockData.setDisarmed(disarmed);
        blockData.setFace(BlockFace.EAST, east);
        blockData.setFace(BlockFace.NORTH, north);
        blockData.setPowered(true);
        blockData.setFace(BlockFace.SOUTH, south);
        blockData.setFace(BlockFace.WEST, west);

        return blockData;
    }
    public float getBreakSpeed( BlockBreakContext context ) {
        ItemStack item = context.item();
        return getBreakSpeed(ToolType.fromItemStack(item), ToolTier.fromItemStack(item), context);
    }
    private float getBreakSpeed(ToolType provided, ToolTier tier, BlockBreakContext context) {
        if (isTransparent) return 0;
        boolean isBestTool = provided == this.brokenBy;
        boolean canHarvest = false;
        if (tier != null) canHarvest = this.miningLvl() >= tier.miningLvl();
        float hardness = this.hardness();
        return getBreakSpeed(hardness, tier, isBestTool, canHarvest, context);
    }
    public static float getBreakSpeed(float hardness, Block block, BlockBreakContext context) {
        ItemStack item = context.item();
        boolean isBestTool = ToolTier.fromItemStack(item).miningLvl() >= ToolTier.getMiningLevel(block) && ToolType.fromItemStack(item) == ToolType.fromBlock(block);
        return getBreakSpeed(hardness,
                ToolTier.fromItemStack(item),
                isBestTool,
                block.isValidTool(item),
                context
        );
    }
    public static float getBreakSpeed(float hardness, SaturnBlock block, BlockBreakContext context) {
        ItemStack item = context.item();
        boolean isBestTool = block.miningLvl() <= ToolTier.fromItemStack(item).miningLvl() && ToolType.fromItemStack(item) == block.brokenBy;
        return getBreakSpeed(hardness,
                ToolTier.fromItemStack(item),
                isBestTool,
                !block.requiresTool || isBestTool,
                context
                );
    }

    private static float getBreakSpeed( float hardness, ToolTier tier, boolean isBestTool, boolean canHarvest, BlockBreakContext context ) {
        return BreakSpeedCalc.get().calc(hardness, tier, isBestTool, canHarvest, context);
    }
    static BreakSpeedCalc breakSpeedCalc;
    public interface BreakSpeedCalc {
        float calc(float hardness, ToolTier tier, boolean isBestTool, boolean canHarvest, BlockBreakContext context);
        static BreakSpeedCalc get() {
            return SaturnBlock.breakSpeedCalc;
        }
        static void set( BreakSpeedCalc breakCalc ) {
            SaturnBlock.breakSpeedCalc = breakCalc;
        }
        static float vanilla( float hardness, ToolTier tier, boolean isBestTool, boolean canHarvest, BlockBreakContext context) {
            float speedMulti = 1;
            if (isBestTool) {
                speedMulti = tier.speedMultiplier();
                if (!canHarvest) speedMulti = 1;
                else if (context.hasEfficiency()) {
                    speedMulti += (float) (Math.pow(context.efficiencyLevel(), 2) + 1);
                }
            }

            if (context.hasHaste())
                speedMulti *= 0.2f * context.getHasteLevel() + 1;
            if (context.hasFatigue())
                speedMulti *= 0.3f * Math.min(context.getFatigueLevel(), 4);
            if (context.inWater() && !context.hasAquaAffinity()) speedMulti /= 5;
            if (context.inAir()) speedMulti /= 5;

            float damage = speedMulti / hardness;
            if (canHarvest) damage /= 30; else damage /= 100;
            if (damage > 1) return 0.0f;
            return 1 / damage / 20 * 1000;
        }
    }
}
