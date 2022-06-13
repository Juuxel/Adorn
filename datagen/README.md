# Adorn Data Generator

My own data generator that is specifically geared towards Adorn's needs.
This is specifically in the form of a slightly messy Gradle plugin that
is applied in the common/fabric/forge subprojects.

Please don't copy my system, it probably doesn't work for your mod.
The documentation here is mostly for people interested in Adorn and/or
contributing.

## Why not use Mojang's data generator?

I've never learned how to use it. 😅 Also, mine is probably faster (since it doesn't launch the game)
and works best for my own needs.

## Design

The generator revolves around *materials* (corresponding to `BlockVariant`s in the mod)
that are substituted into JSON file templates in the generator. The materials are either

- wood (e.g. oak, spruce, crimson fungi)
- stone (e.g. stone bricks, sandstone)
- wool/colour (the different colours of wool)

Each material provides multiple properties usable in data file templates.
For wood and stone, these are:

- `main-texture`: the main texture of the main block of this material
  - For wood, this is the texture of the planks
  - For stone, this is the side texture of the block
- `planks`: the block/item ID of the main block of this material
  - For wood, this is obviously the planks block
  - For stone, this is the block form of the material
- `stick`: the tag ID of the stick/rod used with this material
  - For wood, this is `#c:wooden_rods`
  - For stone, this is `#c:stone_rods`
- `slab`: the block/item ID of the slab of this material

Wood materials also have the following properties:
- `log`: the block/item ID of the log/stem of this wood type
- `log_side`: the side (bark) texture of the log/stem
- `log_end`: the end/top texture of the log/stem

The wood and stone materials are listed in various XML files stored in each subproject's
`src/data` directories.

When the JSON data files are generated, it uses templates from the data generators resources
(`src/main/resources/adorn/templates`) and replaces the placeholders in angle brackets (`<>`)
with the actual properties such as needed item IDs or texture paths.
If the properties are namespaced IDs, they can additionally be split to their namespace and path:
`<my-id.namespace>` gives the namespace of `my-id`, and similarly for `<my-id.path>`.

## Usage

Run the Gradle task `generateData`. It depends on these two tasks:

- `generateMainData`: apply the regular data generators
- `generateTags` (only in the common subproject): apply the tag generators
  which also include all materials from other subprojects

## XML format

Each data generator config file is an XML file in `<project dir>/src/data` with the following format:

- The root tag is `<data_generators>`
  - Attributes
    - `condition_type`: which type of conditions for conditional data loading to generate
      for modded compat content
      - Values: `none` (default), `fabric`, `forge`
    - `wool`: whether to include the wool material, this is intended for the common subproject only
      - Values: `true`, `false` (default)
- Material tags
  - Nested directly in the root tag
  - `<wood>`: a wood material
    - Attributes:
      - attributes common to all materials
      - `fungus`: whether the wood is actually a huge fungus
        - Values: `true`, `false` (default)
      - `non_flammable`: whether the wood is unusable as fuel
        - Values: `true`, `false` (default)
  - `<stone>`: a stone material
    - Attributes:
      - attributes common to all materials
      - `bricks`: whether the base block is a brick block
        - Used for determining the IDs of the slab form
        - Values: `true`, `false` (default)
      - `has_sided_texture`: whether the base block has different textures for top/side/bottom
        - Used for 3D models
        - Values: `true`, `false` (default)
  - Attributes common to all materials:
    - `id` (required): the unique namespaced ID of the material from which all the main properties are derived
- "Fine tuning tags"
  - `<exclude>`: excludes a generator from being used for this material
    - Nested directly in material tags
    - Attributes:
      - `generator` (required): the ID of the excluded generator, see `Generator.kt` in this subproject
  - `<replace>`: replaces the value of a property
    - Nested directly in the root tag or material tags
      - Replacements in material tags take precedence over ones in the root tag
    - Attributes:
      - `key` (required): the property key/name
      - `value` (required): the property value
