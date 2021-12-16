import operator

from common import window
from collections import Counter

EXAMPLE = """
NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C
"""


def parse_input(txt):
    lines = txt.strip().splitlines()

    start = lines.pop(0)
    lines.pop(0)

    productions = {}
    for line in lines:
        ch, c = line.split(' -> ')
        a, b = ch
        productions[(a, b)] = c

    return start, productions


def step(s, p):
    new_s = ""
    for a, b in window(s, n=2):
        new_s += f"{a}{p[(a, b)]}"
    new_s += f"{b}"
    return new_s


def do_example():
    s, p = parse_input(EXAMPLE)
    print(f"Template:     {s}")
    for i in range(1, 5):
        s = step(s, p)
        print(f"After step {i}: {s}")


def part_one():
    with open("input/day14.txt", "r") as fh:
        s, p = parse_input(fh.read())
    for i in range(1, 11):
        s = step(s, p)
    ranked = sorted(Counter(s).items(), key=operator.itemgetter(1))
    print(ranked[-1][1] - ranked[0][1])


do_example()
part_one()
