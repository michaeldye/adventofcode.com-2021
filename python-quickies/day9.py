from pprint import pprint
from copy import deepcopy
import operator
import math
from collections import Counter
from itertools import chain

EXAMPLE = """
2199943210
3987894921
9856789892
8767896789
9899965678
"""


def parse_heightmap(txt):
    lines = txt.strip().splitlines()
    nums = []
    for row in lines:
        row = list(map(int, row))
        nums.append(row)
    return nums


def lowpoints(heights):
    for y in range(0, len(heights)):
        for x in range(0, len(heights[y])):
            is_min = True
            for x2, y2 in [(x - 1, y), (x + 1, y), (x, y - 1), (x, y + 1)]:
                try:
                    if heights[y2][x2] <= heights[y][x]:
                        is_min = False
                        break
                except IndexError:
                    pass
            if is_min:
                yield (x, y)


def part_one(txt):
    heights = parse_heightmap(txt)
    pprint(heights)
    risk = 0
    for x, y in lowpoints(heights):
        risk += heights[y][x] + 1
    print(risk)


part_one(EXAMPLE)
with open("input/day9.txt", "r") as fh:
    puzzle_input = fh.read()
part_one(puzzle_input)


def basins(heights):
    mapped = [[-1 for x in range(0, len(heights[y]))] for y in range(0, len(heights))]

    basin_num = [1]

    def traverse(x, y):
        bnum = basin_num[0]

        try:
            if heights[y][x] != 9 and mapped[y][x] == -1:
                mapped[y][x] = bnum
            elif heights[y][x] == 9:
                mapped[y][x] = 0
                return
        except IndexError:
            raise Exception('oof')

        for x2, y2 in [(x - 1, y), (x + 1, y), (x, y - 1), (x, y + 1)]:
            if x2 < 0:
                continue
            if y2 < 0:
                continue
            try:
                if heights[y2][x2] != 9 and mapped[y2][x2] == -1:
                    traverse(x2, y2)
                elif heights[y2][x2] == 9:
                    mapped[y2][x2] = 0
            except IndexError:
                pass

    for y in range(len(heights)):
        for x in range(len(heights[y])):
            traverse(x, y)
            # print(f"{x},{y}")
            # pprint(mapped)
            basin_num[0] += 1

    return mapped


def printmap(m):
    for row in m:
        print(''.join(map(str, row)))
    print()


def part_two(txt):
    heights = parse_heightmap(txt)
    basinmap = basins(heights)
    counts = Counter(chain(*basinmap))
    counts.pop(0)
    counts = sorted(counts.items(), key=operator.itemgetter(1), reverse=True)
    pprint(counts)

    bord = 1
    for bnum, bsize in counts[0:3]:
        thisbasin = deepcopy(basinmap)
        for row in thisbasin:
            for col in range(len(row)):
                if row[col] == 0:
                    row[col] = '+'
                elif row[col] != bnum:
                    row[col] = '.'
                elif row[col] == bnum:
                    row[col] = bord
        bord += 1
        printmap(thisbasin)

    top_three_sizes = map(operator.itemgetter(1), counts[0:3])
    basin_product = math.prod(top_three_sizes)
    print(f"{basin_product}")


part_two(EXAMPLE)
part_two(puzzle_input)
