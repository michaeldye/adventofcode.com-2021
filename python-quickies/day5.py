def rangei(start, end, step=1):
    if end < start and step > 0:
        step *= -1
    end += step
    return range(start, end, step)


assert (list(rangei(1, 5)) == [1, 2, 3, 4, 5])
assert (list(rangei(5, 1, -1)) == [5, 4, 3, 2, 1])
assert (list(rangei(5, 1)) == [5, 4, 3, 2, 1])


class ventmap:
    def __init__(self, w, h, diags=False):
        self.w = w
        self.h = h
        self.m = [[0 for x in range(0, w)] for y in range(0, h)]
        self.diags = diags

    def process(self, x1, y1, x2, y2):
        run = []
        if x1 == x2:
            run = [(x1, y) for y in rangei(y1, y2)]
        elif y1 == y2:
            run = [(x, y1) for x in rangei(x1, x2)]
        elif self.diags:
            if y2 > y1:
                dy = 1
            else:
                dy = -1

            if x2 > x1:
                dx = 1
            else:
                dx = -1

            run = [(x1 + dx * i, y1 + dy * i) for i in rangei(0, abs(x1-x2))]

        if len(run) > 0:
            for x, y in run:
                self.m[y][x] += 1

    def solve_part_one(self):
        acc = 0
        for x in range(0, self.w):
            for y in range(0, self.h):
                if self.m[y][x] > 1:
                    acc += 1
        return acc

    @classmethod
    def process_vecs(cls, input_text, diags=False):
        coords = []
        lines = input_text.strip().splitlines()
        for line in lines:
            if '->' not in line:
                continue
            coords.append(lmap(int, lmap(str.strip, line.replace(' -> ', ',').split(','))))
        print(coords)
        x1m, y1m, x2m, y2m = lmap(max, zip(*coords))
        w = max(x1m, x2m)
        h = max(y1m, y2m)
        vm = ventmap(w + 1, h + 1, diags)

        lcoord = len(coords)

        for ix, c in enumerate(coords):
            vm.process(*c)
            print(vm)
            print(f"{ix+1:5d}/{lcoord}")

        print(vm.solve_part_one())

    def __str__(self):
        s = ""
        for y in range(0, self.h):
            for x in range(0, self.w):
                s += '.' if self.m[y][x] == 0 else str(self.m[y][x])
            s += '\n'
        return s


def lmap(pred, it):
    return list(map(pred, it))


EXAMPLE = """
0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
"""

ventmap.process_vecs(EXAMPLE)

with open("input/day5.txt", 'r') as fh:
    puzzle_input = fh.read()

# ventmap.process_vecs(puzzle_input)

print("======= part two =======")

ventmap.process_vecs(EXAMPLE, diags=True)
ventmap.process_vecs(puzzle_input, diags=True)
