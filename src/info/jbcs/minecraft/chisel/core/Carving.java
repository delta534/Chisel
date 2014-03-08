package info.jbcs.minecraft.chisel.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Carving {
	class CarvingGroup{
		public CarvingGroup(String n) {
			name=n;
		}

		String name;
		String className;
		String sound;
		String oreName;
		
		ArrayList<CarvingVariation> variations=new ArrayList<CarvingVariation>();
	};
	
	HashMap<String,CarvingGroup> carvingGroupsByName=new HashMap<String,CarvingGroup>();
	HashMap<String,CarvingGroup> carvingGroupsByOre=new HashMap<String,CarvingGroup>();
	HashMap<String,CarvingGroup> carvingGroupsByVariation=new HashMap<String,CarvingGroup>();

	public static Carving chisel=new Carving();
	public static Carving needle=new Carving();
	
	String key(int blockId,int metadata){
		return blockId+"|"+metadata;
	}
	
	public boolean isVariationOfSameClass(int blockId1,int metadata1,int blockId2,int metadata2){
		CarvingGroup group1=carvingGroupsByVariation.get(key(blockId1,metadata1));
		if(group1==null) return false;
		
		CarvingGroup group2=carvingGroupsByVariation.get(key(blockId2,metadata2));
		if(group2==null) return false;
		
		if(group1==group2)
			return true;
		
		return group1.className.equals(group2.className) && ! group1.className.isEmpty();
	}
	
	public CarvingVariation[] getVariations(int blockId,int metadata){
		CarvingGroup group=getGroup(blockId,metadata);
		if(group==null) return null;
		
		CarvingVariation[] res=group.variations.toArray(new CarvingVariation[group.variations.size()]);
		return res;
	}
	
	public String getOre(int blockId,int metadata){
		CarvingGroup group=getGroup(blockId,metadata);
		if(group==null) return null;
		
		return group.oreName;
	}

	public ArrayList<ItemStack> getItems(ItemStack chiseledItem) {
		ArrayList<ItemStack> items=new ArrayList<ItemStack>();
		int damage=chiseledItem.getItemDamage();
		
		CarvingGroup group=getGroup(chiseledItem.itemID,damage);
		if(group==null) return items;
		
		HashMap<String,Integer> mapping=new HashMap<String,Integer>();
		
		if (group.variations != null) {
			for (CarvingVariation v: group.variations) {

				String key=v.blockId+"|"+v.damage;
				if(mapping.containsKey(key)) continue;
				mapping.put(key, 1);
				
				items.add(new ItemStack(v.blockId, 1, v.damage));
			}
		}
		
		ArrayList<ItemStack> ores;
		if(group.oreName!=null && ((ores=OreDictionary.getOres(group.oreName))!=null)){	
			for(ItemStack stack: ores){
				
				String key=stack.itemID+"|"+stack.getItemDamage();
				if(mapping.containsKey(key)) continue;
				mapping.put(key, 2);
				
				items.add(stack);
			}
		}
		
		return items;
	}
	
	public CarvingGroup getGroup(int blockId,int metadata) {
		if(blockId==Block.stone.blockID) blockId=Block.stoneBrick.blockID;
		
		CarvingGroup res;
		
		if((res=carvingGroupsByOre.get(OreDictionary.getOreName(OreDictionary.getOreID(new ItemStack(blockId,1,metadata)))))!=null)
			return res;
		
		if((res=carvingGroupsByVariation.get(key(blockId,metadata)))!=null)
			return res;
	
		return null;
	}

	CarvingGroup getGroup(String name){
		CarvingGroup group=carvingGroupsByName.get(name);
		if(group==null){
			group=new CarvingGroup(name);
			group.className=name;
			carvingGroupsByName.put(name,group);
		}
		
		return group;
	}
	
	public CarvingVariation addVariation(String name,int blockId,int metadata,int order){
		CarvingGroup group=getGroup(name);
		
		CarvingGroup blockGroup=carvingGroupsByVariation.get(key(blockId,metadata));
		if(blockGroup!=null || blockGroup==group) return null;
		
		CarvingVariation variation=new CarvingVariation(blockId,metadata,order);
		group.variations.add(variation);
		Collections.sort(group.variations);
		carvingGroupsByVariation.put(key(blockId,metadata), group);
		
		return variation;
	}
	

	public void registerOre(String name, String oreName) {
		CarvingGroup group=getGroup(name);
		
		carvingGroupsByOre.put(oreName, group);
		
		group.oreName=oreName;
	}
	
	public void setGroupClass(String name,String className){
		CarvingGroup group=getGroup(name);
		
		group.className=className;
	}

	public void setVariationSound(String name, String sound) {
		CarvingGroup group=getGroup(name);
		group.sound=sound;
	}
	
	public String getVariationSound(int blockId,int metadata) {
		CarvingGroup blockGroup=carvingGroupsByVariation.get(key(blockId,metadata));
		if(blockGroup==null || blockGroup.sound==null) return "chisel:chisel";
		
		return blockGroup.sound;
	}
}

