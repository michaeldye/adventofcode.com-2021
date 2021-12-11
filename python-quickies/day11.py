EXAMPLE = """
5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526
"""

PUZZLE = """
4438624262
6263251864
2618812434
2134264565
1815131247
2612457325
8585767584
7217134556
2825456563
8248473584
"""

from collections import deque
from itertools import chain


class OctoFlashers(object):
    def __init__(self, w=10, h=10):
        self.w = w
        self.h = h
        self.m = [[0 for x in range(w)] for y in range(h)]

    def ingrid(self, x, y):
        if x < 0 or y < 0:
            return False
        if x >= self.w or y >= self.h:
            return False
        assert (0 <= x < 10)
        assert (0 <= y < 10)
        return True

    @classmethod
    def parsegrid(cls, txt):
        lines = txt.strip().splitlines()
        h = len(lines)
        w = len(lines[0])
        of = OctoFlashers(w, h)
        for y, line in enumerate(lines):
            for x, v in enumerate(map(int, line.strip())):
                of.m[y][x] = v
        return of

    def step(self):
        flashingvictims = deque()
        already_flashed = [[0 for x in range(self.w)] for y in range(self.h)]

        for y in range(self.h):
            for x in range(self.w):
                self.m[y][x] += 1
                if self.m[y][x] > 9:
                    flashingvictims.append((x, y))

        def doflash(x, y):
            if already_flashed[y][x]:
                return

            if self.m[y][x] <= 9:
                return

            self.m[y][x] = 0
            already_flashed[y][x] = 1

            for x2, y2 in (
                    (x - 1, y + 1), (x, y + 1), (x + 1, y + 1),
                    (x - 1, y), (x + 1, y),
                    (x - 1, y - 1), (x, y - 1), (x + 1, y - 1),
            ):
                if self.ingrid(x2, y2):
                    if not already_flashed[y2][x2]:
                        self.m[y2][x2] += 1
                        flashingvictims.append((x2, y2))

        while len(flashingvictims) > 0:
            v = flashingvictims.popleft()
            doflash(*v)

        return sum(chain(*already_flashed))

    def __str__(self):
        s = ""
        for y in range(len(self.m)):
            s += ''.join(map(str, self.m[y]))
            s += "\n"
        return s


def run(txt, steps=100, part_2=False):
    of = OctoFlashers.parsegrid(txt)
    print(of)
    flashes = 0
    for i in range(1, steps+1):
        new_flashes = of.step()
        if i <= 10 or i % 10 == 0:
            print(f"After step {i}:")
            print(str(of).strip())
            print(new_flashes)
            print()
        if new_flashes == 100:
            print(of)
            print(i)
            print("everybody flashed yo")
            input()
        flashes += new_flashes
    print(flashes)


run(EXAMPLE)
run(PUZZLE)


run(PUZZLE, 1000, True)