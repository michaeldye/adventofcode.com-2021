from common import *

EXAMPLE = '3,4,3,1,2'


def step_fish(s):
    v = lmap(int, s.strip().split(','))
    oldv = []
    newv = []

    for i in v:
        if i == 0:
            newv.append(8)
            oldv.append(6)
        else:
            oldv.append(i - 1)

    return ','.join(lmap(str, oldv + newv))


def fish_game(fishstr, days, doprint=True):
    s = fishstr
    print(f"Initial state: {s}")
    for i in rangei(1, days):
        s = step_fish(s)
        if doprint:
            print(f"After {i:2d} day{': ' if i == 1 else 's:'} {s}")

    print(f"{len(s.strip().replace(',', ''))} fushes")


fish_game(EXAMPLE, 18)
fish_game(EXAMPLE, 80)

with open("input/day6.txt", "r") as fh:
    puzzle_input = fh.read()

fish_game(puzzle_input, 80)

print("===== part two =====")

## gon explode
# fish_game(EXAMPLE, 256, False)
# import time
# time.sleep(5)
# fish_game(puzzle_input, 256, False)
