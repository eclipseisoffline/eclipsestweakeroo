import json
import requests

# This script queries the mob effect icons in the vanilla assets/minecraft/textures/mob_effect folder, and builds a font for them

MINECRAFT_VERSION_COMMIT = "585997f30c3a1f85542d1a637d9307011c7e7fc4" # 1.21.8

# Thanks Misode!
STATUS_EFFECT_ICONS_QUERY = f"https://api.github.com/repos/misode/mcmeta/contents/assets/minecraft/textures/mob_effect?ref={MINECRAFT_VERSION_COMMIT}"
VANILLA_NAMESPACE = "minecraft"
STATUS_EFFECT_TEXTURE_PATH = f"{VANILLA_NAMESPACE}:mob_effect/"

FONT_OUT = "./default.json"
MAP_OUT = "./map.json"

# Private Unicode block
UNICODE_START = 0xE100


def main():
    status_effect_list = [effect_file["name"] for effect_file in requests.get(STATUS_EFFECT_ICONS_QUERY).json()]

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
        status_effect_map[VANILLA_NAMESPACE + ":"
                          + status_effect.removesuffix(".png")] = chr(UNICODE_START + i)
        i += 1

    with open(FONT_OUT, "w") as font_provider:
        json.dump(status_effect_font_provider, font_provider, indent=2)
    with open(MAP_OUT, "w") as map_file:
        json.dump(status_effect_map, map_file, indent=2)


if __name__ == '__main__':
    main()
