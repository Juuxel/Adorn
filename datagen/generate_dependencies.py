import json
import pathlib


def generate_dependencies_for_dir(dir):
    dir = pathlib.Path(dir)
    for model in dir.glob("block-models/**/*.json"):
        generate_model_dependencies_for_template(model)
    for model in dir.glob("item-models/**/*.json"):
        generate_model_dependencies_for_template(model)
    for recipe in dir.glob("recipes/**/*.json"):
        generate_recipe_dependencies_for_template(recipe)
    for advancement in dir.glob("recipe-advancements/**/*.json"):
        generate_recipe_advancement_dependencies_for_template(advancement)


def generate_model_dependencies_for_template(template: pathlib.Path):
    print(f"Processing {template}")
    textures = []

    with open(template) as file:
        text = file.read().replace('<model_condition>', '')
        data = json.loads(text)
        textures.extend(data['textures'].values())

    with open(template.with_name(template.name.replace('.json', '.dependencies.properties')), "w") as file:
        file.write("textures = ")
        file.write(", ".join(textures))
        file.write("\n")


def generate_recipe_dependencies_for_template(template: pathlib.Path):
    print(f"Processing {template}")
    items: list[str] = []

    with open(template) as file:
        text = file.read().replace('<recipe-condition>', '')
        data = json.loads(text)
        
        match data['type'].replace('minecraft:', ''):
            case "crafting_shaped":
                items.extend(data['key'].values())
            case "crafting_shapeless":
                items.extend(data['ingredients'])
            case "stonecutting":
                items.append(data['ingredient'])

    items = filter(lambda item: not item.startswith("#"), items)

    with open(template.with_name(template.name.replace('.json', '.dependencies.properties')), "w") as file:
        file.write("items = ")
        file.write(", ".join(items))
        file.write("\n")


def generate_recipe_advancement_dependencies_for_template(template: pathlib.Path):
    print(f"Processing {template}")
    items: list[str] = []

    with open(template) as file:
        text = file.read().replace('<advancement-condition>', '')
        data = json.loads(text)
        for criterion in data['criteria'].values():
            if criterion['trigger'] == "minecraft:inventory_changed":
                for i in criterion['conditions']['items']:
                    items.extend(i['items'])

    items = filter(lambda item: not item.startswith("#"), items)

    with open(template.with_name(template.name.replace('.json', '.dependencies.properties')), "w") as file:
        file.write("items = ")
        file.write(", ".join(items))
        file.write("\n")


if __name__ == "__main__":
    generate_dependencies_for_dir("src/main/resources/adorn/templates")
