"""
the string would grow beyond the amount of memory and be very slow for part 2 by the looks of the description

the key observation for a solution that uses a bounded amount of memory is that the complete ordering is not
important given the output they want.

so starting with the example start string NNCB

all that matters is the set of pairs
(NN, NC, CB)

expands to a different set of pairs
(NC, CN, NB, BC, CH, HB)

when scoring the numbers of occurrences in the final string, we know that the first and last letters will
be the same as the starting ones, and that

so that in scoring:
(NC: 1, CN: 1, NB: 1, BC: 1, CH: 1, HB: 1)

we can consider either the first or last letters in each pair (last here):
C: 1, N: 1, B: 1, C: 1, H: 1, B: 1

and then add the starting letter N
"""

from pprint import pprint
from collections import defaultdict
from day14 import EXAMPLE, parse_input
from common import window, lmap


def expand_prodrules(prodrules):
    new_rules = {}
    for k, v in prodrules.items():
        assert (len(k) == 2)
        new_rules[''.join(k)] = (f"{k[0]}{v}", f"{v}{k[1]}")
    return new_rules


def window_strs(t):
    dd = defaultdict(lambda: 0)
    for v in lmap(lambda x: f"{x[0]}{x[1]}", window(t, 2)):
        dd[v] += 1
    return dd


def do_step(dct, rules):
    new_dct = defaultdict(lambda: 0)
    for k, v in dct.items():
        l, r = rules[k]
        new_dct[l] += v
        new_dct[r] += v
    return new_dct


def score(start, dct):
    score = defaultdict(lambda: 0)
    score[start[0]] += 1
    for k, v in dct.items():
        assert(len(k) == 2)
        print(f"{k[1]} += {v}")
        score[k[1]] += v
    return max(score.values()) - min(score.values())


def part_two(txt, n=40):
    start, prodrules = parse_input(txt)
    new_rules = expand_prodrules(prodrules)
    pair_dct = window_strs(start)
    pprint(pair_dct)

    for _ in range(n):
        pair_dct = do_step(pair_dct, new_rules)

    pprint(pair_dct)

    print(score(start, pair_dct))


with open("input/day14.txt", "r") as fh:
    puzzle = fh.read()

part_two(EXAMPLE, 10)
part_two(EXAMPLE)
part_two(puzzle)
