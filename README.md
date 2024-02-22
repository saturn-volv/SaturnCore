# SaturnCore
**NOT IN FINAL STATE**<br>
 <sub> Built initially for use with the MythosCraft project. </sub>

 A Core library plugin for server-sided blocks, items and enchantments, which can be dynamically generated. 
 
 As of right now this does remove the functionality of Tripwires and Note Blocks, but Note Blocks **are** planned for implementation again.

 Blocks have not been parented to use `SaturnBlocks` without needing to be an instance of a custom block, but this functionality isn't meant to be final. All internal custom objects are planned to have the same behaviour.

## Items
 Items internally have been moved to a native class `SaturnItemStack`. This object allows for parenting to a custom item or to a vanilla one. Setting types of an item does not change the parent SaturnItemStack, just simply changes how the item is natively shown without a resource pack.

 Items have planned functionality for tick, click, and food interactions. However, these are not implemented as of right now.

 Custom Tools are possible, as the method for breaking blocks has been moved entirely server-side.

## Blocks
 Blocks interally can be constructed to have custom properties, including custom hardness values and their tool type requirements for being mined.

 Loot table and light support are planned.

## Enchantments
 Enchantments are still being developed but do generate.
