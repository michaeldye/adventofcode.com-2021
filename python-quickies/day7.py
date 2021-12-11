import operator

from common import *

EXAMPLE = """
16,1,2,0,4,2,7,1,2,14
"""


def parse_crabs(input_text):
    crabs = {}
    for pos in lmap(int, input_text.strip().split(',')):
        crabs.setdefault(pos, 0)
        crabs[pos] += 1
    return crabs


def solve_crabs(crabs):
    min_pos = min(crabs.keys())
    max_pos = max(crabs.keys())

    costs = []
    for c in rangei(min_pos, max_pos):
        cost = 0
        for k, v in crabs.items():
            cost += abs(c - k) * v
        costs.append((c, cost))

    costs = sorted(costs, key=operator.itemgetter(1))

    return costs[0]


def solve_crabs_p2(crabs):
    min_pos = min(crabs.keys())
    max_pos = max(crabs.keys())

    costs = []
    for c in rangei(min_pos, max_pos):
        cost = 0
        for k, v in crabs.items():
            cost += sum([i for i in rangei(1, abs(c - k))]) * v
        costs.append((c, cost))

    costs = sorted(costs, key=operator.itemgetter(1))

    return costs[0]


print(solve_crabs(parse_crabs(EXAMPLE)))

with open("input/day7.txt", "r") as fh:
    puzzle_text = fh.read()

print(solve_crabs(parse_crabs(puzzle_text)))

print(solve_crabs_p2(parse_crabs(EXAMPLE)))
print(solve_crabs_p2(parse_crabs(puzzle_text)))
