package com.github.saturnvolv.core.block;

import com.github.saturnvolv.core.item.ToolType;

public class BlockSettings {
    private float hardness;
    private int miningLvl;
    private boolean requiresTool;
    private boolean breakByHand;
    private boolean isTransparent;
    private ToolType brokenBy;
    public BlockSettings() {}

    public float hardness() {
        return this.hardness;
    }
    public int miningLvl() {
        return this.miningLvl;
    }
    public boolean requiresTool() {
        return this.requiresTool;
    }

    public boolean breakByHand() {
        return breakByHand;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public ToolType brokenBy() {
        return brokenBy;
    }

    public static class Builder {
        protected BlockSettings parent;
        public Builder() {
            this.parent = new BlockSettings();
        }
        public Builder hardness(float hardness) {
            this.parent.hardness = hardness;
            return this;
        }
        public Builder miningLvl(int miningLvl) {
            this.parent.miningLvl = miningLvl;
            return this;
        }
        public Builder requiresTool(boolean requiresTool) {
            this.parent.requiresTool = requiresTool;
            return this;
        }
        public Builder breakByHand(boolean breakByHand) {
            this.parent.breakByHand = breakByHand;
            return this;
        }
        public Builder isSolid(boolean isSolid) {
            this.parent.isTransparent = !isSolid;
            return this;
        }
        public Builder brokenBy(ToolType brokenBy) {
            this.parent.brokenBy = brokenBy;
            return this;
        }
        public BlockSettings build() {
            return parent;
        }
    }
}
