package cn.grainalcohol.temp;

import cn.grainalcohol.OAPMod;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TempRecipe implements Recipe<CraftingInventory> {
    private final ItemStack result;
    private final RecipeType<?> recipeType;

    public TempRecipe(ItemStack result, String recipeType) {
        this.result = result.copy();
        this.recipeType = getRecipeType(recipeType);
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        return false;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory, DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return result;
    }

    @Override
    public Identifier getId() {
        return OAPMod.id("temp_recipe_" + System.currentTimeMillis());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return recipeType;
    }

    private static RecipeType<?> getRecipeType(String name) {
        return switch (name) {
            case "crafting" -> RecipeType.CRAFTING;
            case "smelting" -> RecipeType.SMELTING;
            case "blasting" -> RecipeType.BLASTING;
            case "smithing" -> RecipeType.SMITHING;
            case "smoking" -> RecipeType.SMOKING;
            case "campfire_cooking" -> RecipeType.CAMPFIRE_COOKING;
            case "stonecutting" -> RecipeType.STONECUTTING;
            default -> RecipeType.CRAFTING;
        };
    }
}
