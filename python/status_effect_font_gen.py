import json

# Taken from mcasset.cloud in assets/minecraft/textures/mob_effect directory
STATUS_EFFECT_LIST = "./_list.json"
STATUS_EFFECT_TEXTURE_PATH = "minecraft:mob_effect/"
STATUS_EFFECT_NAMESPACE = "minecraft"

FONT_OUT = "./default.json"
MAP_OUT = "./map.json"
UNICODE_START = 0xE100


def main():
    with open(STATUS_EFFECT_LIST, "r") as list_file:
        status_effect_list: list = json.load(list_file)["files"]

    status_effect_font_provider = {
        "providers": []
    }
    status_effect_map = {}

    i = 0
    for status_effect in status_effect_list:
        status_effect_font_provider["providers"].append({
            "type": "bitmap",
            "file": STATUS_EFFECT_TEXTURE_PATH + status_effect,
            "ascent": 8,
            "chars": [
                chr(UNICODE_START + i)
            ]
        })
        status_effect: str = status_effect
        status_effect_map[STATUS_EFFECT_NAMESPACE + ":"
                          + status_effect.removesuffix(".png")] = chr(UNICODE_START + i)
        i += 1

    with open(FONT_OUT, "w") as font_provider:
        json.dump(status_effect_font_provider, font_provider, indent=2)
    with open(MAP_OUT, "w") as map_file:
        json.dump(status_effect_map, map_file, indent=2)


if __name__ == '__main__':
    main()
