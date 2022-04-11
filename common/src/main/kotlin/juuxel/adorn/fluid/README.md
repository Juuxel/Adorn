# `juuxel.adorn.fluid`

Adorn's state-of-the-art fluid crafting and syncing code ("API").
If you see this, please clean up the code. Thanks.

## Units

The "API" handles fluid amounts in two different units: litres/mB on Forge
and droplets on Fabric.

Since recipes are common and can be specified in either unit,
the `FluidUnit` enum can be used to identify and compare fluid amounts of
(possibly) different units.
It also has a `convert` function for converting amounts between units.

`HasFluidAmount` contains a fluid amount and its unit for fluid amount comparisons.
It's implemented by all fluid references and `FluidIngredient`.

## Fluid references and volumes

`FluidReference` is a publicly mutable reference to a fluid with NBT and an amount+unit.
This is typically a `FluidVolume` which is like an `ItemStack` for fluids,
but can also be a reference to a machine's or tank's internal fluid state.

## Fluid crafting

`FluidKey` identifies a group of fluids (without NBT). In other words,
it's a single fluid, a tag or a group of fluids. The class mainly exists
for the fluid recipe JSON formats.

`FluidIngredient` is like an `Ingredient` but for fluids. It has a `FluidKey`
as well as NBT and an amount+unit.
