package twistedgate.immersiveposts.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraftforge.registries.RegistryObject;
import twistedgate.immersiveposts.api.posts.IPostMaterial;
import twistedgate.immersiveposts.common.blocks.HorizontalTrussBlock;
import twistedgate.immersiveposts.common.blocks.MetalFenceBlock;
import twistedgate.immersiveposts.common.blocks.MetalFenceBlock.FenceItemBlock;
import twistedgate.immersiveposts.common.blocks.PostBaseBlock;
import twistedgate.immersiveposts.common.blocks.PostBlock;
import twistedgate.immersiveposts.common.items.IPOItem;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

/**
 * @author TwistedGate
 */
public class IPOContent{
	public static final boolean containsBlockOrItem(@Nullable final Block block, @Nullable final Item item){
		if(block != null){
			return IPORegistries.BLOCK_REGISTER.getEntries().stream().anyMatch(r -> r.get() == block);
		}
		if(item != null){
			return IPORegistries.ITEM_REGISTER.getEntries().stream().anyMatch(r -> r.get() == item);
		}
		return false;
	}
	
	protected static final <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> constructor){
		return IPORegistries.BLOCK_REGISTER.register(name, constructor);
	}
	
	protected static final RegistryObject<PostBlock> registerPostBlock(EnumPostMaterial material){
		return registerBlock(material.getBlockName(), () -> new PostBlock(material));
	}
	
	protected static final RegistryObject<HorizontalTrussBlock> registerTrussBlock(EnumPostMaterial material){
		return registerBlock(material.getBlockName() + "_truss", () -> new HorizontalTrussBlock(material));
	}
	
	protected static final RegistryObject<FenceBlock> registerMetalFence(String materialName){
		materialName = "fence_" + materialName;
		
		RegistryObject<FenceBlock> block = IPORegistries.BLOCK_REGISTER.register(materialName, MetalFenceBlock::new);
		IPORegistries.ITEM_REGISTER.register(materialName, () -> new FenceItemBlock(block.get()));
		return block;
	}
	
	protected static final <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> constructor){
		return IPORegistries.ITEM_REGISTER.register(name, constructor);
	}
	
	public static class Blocks{
		public static final RegistryObject<PostBaseBlock> POST_BASE;
		
		static{
			POST_BASE = registerBlock("postbase", PostBaseBlock::new);
			registerItem("postbase", () -> new PostBaseBlock.PostBaseBlockItem(POST_BASE.get()));
		}
		
		private static void forceClassLoad(){
			Blocks.Posts.forceClassLoad();
			Blocks.HorizontalTruss.forceClassLoad();
			Blocks.Fences.forceClassLoad();
			
			Items.forceClassLoad();
		}
		
		public static class Fences{
			
			/** Unmodifiable List of all Fence Blocks added by IPO */
			public static final List<RegistryObject<FenceBlock>> ALL_FENCES;
			
			public final static RegistryObject<FenceBlock> IRON;
			public final static RegistryObject<FenceBlock> GOLD;
			public final static RegistryObject<FenceBlock> COPPER;
			public final static RegistryObject<FenceBlock> LEAD;
			public final static RegistryObject<FenceBlock> SILVER;
			public final static RegistryObject<FenceBlock> NICKEL;
			public final static RegistryObject<FenceBlock> CONSTANTAN;
			public final static RegistryObject<FenceBlock> ELECTRUM;
			public final static RegistryObject<FenceBlock> URANIUM;
			
			static{
				List<RegistryObject<FenceBlock>> list = new ArrayList<>();
				
				list.add(IRON = registerMetalFence("iron"));
				list.add(GOLD = registerMetalFence("gold"));
				list.add(COPPER = registerMetalFence("copper"));
				list.add(LEAD = registerMetalFence("lead"));
				list.add(SILVER = registerMetalFence("silver"));
				list.add(NICKEL = registerMetalFence("nickel"));
				list.add(CONSTANTAN = registerMetalFence("constantan"));
				list.add(ELECTRUM = registerMetalFence("electrum"));
				list.add(URANIUM = registerMetalFence("uranium"));
				
				ALL_FENCES = Collections.unmodifiableList(list);
			}
			
			private static void forceClassLoad(){
			}
		}
		
		public static class Posts{
			/** Contains all Post Blocks added by IPO */
			static final EnumMap<EnumPostMaterial, RegistryObject<PostBlock>> ALL = Util.make(new EnumMap<>(EnumPostMaterial.class), map -> {
				for(EnumPostMaterial material:EnumPostMaterial.values()){
					map.put(material, registerPostBlock(material));
				}
			});
			
			@CheckForNull
			public static PostBlock get(@Nonnull IPostMaterial material){
				if(!ALL.containsKey(material))
					return null;
				return ALL.get(material).get();
			}
			
			@CheckForNull
			public static RegistryObject<PostBlock> getRegObject(@Nonnull IPostMaterial material){
				if(!ALL.containsKey(material))
					return null;
				return ALL.get(material);
			}
			
			private static void forceClassLoad(){
			}
		}
		
		public static class HorizontalTruss{
			/** Contains all Truss Blocks added by IPO */
			static EnumMap<EnumPostMaterial, RegistryObject<HorizontalTrussBlock>> ALL = Util.make(new EnumMap<>(EnumPostMaterial.class), map -> {
				for(EnumPostMaterial material:EnumPostMaterial.values()){
					map.put(material, registerTrussBlock(material));
				}
			});
			
			public static HorizontalTrussBlock get(@Nonnull IPostMaterial material){
				if(!ALL.containsKey(material))
					return null;
				return ALL.get(material).get();
			}
			
			public static RegistryObject<HorizontalTrussBlock> getRegObject(@Nonnull IPostMaterial material){
				if(!ALL.containsKey(material))
					return null;
				return ALL.get(material);
			}
			
			private static void forceClassLoad(){
			}
		}
	}
	
	public static class Items{
		public final static RegistryObject<Item> ROD_GOLD;
		public final static RegistryObject<Item> ROD_COPPER;
		public final static RegistryObject<Item> ROD_LEAD;
		public final static RegistryObject<Item> ROD_SILVER;
		public final static RegistryObject<Item> ROD_NICKEL;
		public final static RegistryObject<Item> ROD_CONSTANTAN;
		public final static RegistryObject<Item> ROD_ELECTRUM;
		public final static RegistryObject<Item> ROD_URANIUM;
		
		static{
			ROD_GOLD = registerItem("stick_gold", IPOItem::new);
			ROD_COPPER = registerItem("stick_copper", IPOItem::new);
			ROD_LEAD = registerItem("stick_lead", IPOItem::new);
			ROD_SILVER = registerItem("stick_silver", IPOItem::new);
			ROD_NICKEL = registerItem("stick_nickel", IPOItem::new);
			ROD_CONSTANTAN = registerItem("stick_constantan", IPOItem::new);
			ROD_ELECTRUM = registerItem("stick_electrum", IPOItem::new);
			ROD_URANIUM = registerItem("stick_uranium", IPOItem::new);
		}
		
		private static void forceClassLoad(){
		}
	}
	
	public static final void modConstruction(){
		Blocks.forceClassLoad();
		
		IPOTileTypes.forceClassLoad();
	}
}
