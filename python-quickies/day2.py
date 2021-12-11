from pprint import pprint
from operator import mul

EXAMPLE = """
forward 5
down 5
forward 8
up 3
down 8
forward 2
"""

# expressed in x,z
distance_map = {
    'forward': (1, 0),
    'down': (0, 1),
    'up': (0, -1),
}


def convert_directions(d):
    lines = list(map(str.split, d.strip().splitlines()))
    return list(map(lambda t: (distance_map.get(t[0]), int(t[1])), lines))


def solve(dirs):
    vecs = [tuple(map(mul, direction, (magnitude, magnitude,))) for direction, magnitude in dirs]
    sums = list(map(sum, zip(*vecs)))
    pprint(sums)
    print(mul(*sums))


print("===== example =====")
solve(convert_directions(EXAMPLE))

print("===== puzzle input =====")

with open("input/day2.txt") as fh:
    puzzle_input = fh.read()

solve(convert_directions(puzzle_input))

print("===== part two =====")


class lolsubmarine:
    def __init__(self):
        self.x = 0
        self.depth = 0
        self.aim = 0

    def forward(self, mag):
        self.x += mag
        self.depth += self.aim * mag

    def up(self, mag):
        self.aim -= mag

    def down(self, mag):
        self.aim += mag

    def run(self, input):
        for line in input.strip().splitlines():
            self.step(line.strip())

        return self

    def step(self, instruction):
        lolunsafe, mag = instruction.split()
        mag = int(mag)
        f = self.__getattribute__(lolunsafe)
        f(mag)
        print(f"{lolunsafe} {str(mag)} {repr(self)}")

    def solve(self):
        soln = self.x * self.depth
        print(soln)
        return self

    def __repr__(self):
        return f"(lolsubmarine x={self.x} depth={self.depth} aim={self.aim})"


print("===== example ======")
submarine = lolsubmarine()
submarine.run(EXAMPLE).solve()

print("===== puzzle input =====")
submarine = lolsubmarine()
submarine.run(puzzle_input).solve()