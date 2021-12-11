import requests
import operator
from pprint import pprint

EXAMPLE = """
199
200
208
210
200
207
240
269
260
263
""".strip().splitlines()

EXAMPLE = list(map(int, EXAMPLE))


def window(seq, n=2):
    it = iter(seq)

    try:
        win = [next(it) for _ in range(0, n)]
        yield win[:]

        while True:
            win.pop(0)
            win.append(next(it))
            yield win[:]
    except StopIteration:
        pass


def solve(nums):
    increasing = 0
    for last, cur in window(nums):
        if cur > last:
            increasing += 1
    print(increasing)


def solve_part_two(nums):
    inner_window = list(window(nums, n=3))
    sum_inner = list(map(sum, inner_window))
    # pprint(inner_window, indent=2)
    # pprint(sum_inner, indent=2)
    solve(sum_inner)


print("===== example =====")
pprint(EXAMPLE)
solve(EXAMPLE)

print("===== puzzle input =====")
with open("input/day1.txt", "r") as fh:
    puzzle_input = list(map(int, fh.read().strip().splitlines()))
solve(puzzle_input)

### part 2
print("========== part two ========")

EXAMPLE_TWO = """
199  A      
200  A B    
208  A B C  
210    B C D
200  E   C D
207  E F   D
240  E F G  
269    F G H
260      G H
263        H
"""

EXAMPLE_TWO = list(map(int, map(operator.itemgetter(0), map(str.split, EXAMPLE_TWO.strip().splitlines()))))


print("===== example =====")
pprint(EXAMPLE_TWO)
solve_part_two(EXAMPLE_TWO)

print("===== puzzle input =====")
solve_part_two(puzzle_input)


