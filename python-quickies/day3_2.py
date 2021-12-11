"""
last one was a mess and very disappoint
"""

from collections import Counter
from functools import partial
from operator import itemgetter
from common import partition

import logging

logging.basicConfig(level='DEBUG')

log = logging.getLogger('day3')


def something_common(s1, default, most=True):
    c = sorted(Counter(s1).items(), key=itemgetter(1), reverse=most)
    assert (len(c) >= 1)
    if len(c) > 1 and c[0][1] == c[1][1]:
        return default
    return c[0][0]


def least_common(s1, default):
    return something_common(s1, default, most=False)


def most_common(s1, default):
    return something_common(s1, default, most=True)


def bit_at(n, i):
    assert (i >= 0)
    return 1 if (n >> i) & 0b1 == 1 else 0


def bits_at(nums, i):
    return list(map(partial(bit_at, i=i), nums))


def some_rating(nums, bit_criteria):
    f, d = bit_criteria
    rem_nums = nums[:]
    assert (len(nums) > 0)
    num_len = max(*map(lambda n: len(bin(n)[2:]), nums))

    for i in range(num_len, -1, -1):
        annotated = [(n, bit_at(n, i)) for n in rem_nums]
        criteria = f(map(itemgetter(1), annotated), default=d)
        ones, zeroes = partition(lambda t: t[1] == 1, annotated)

        if criteria == 1:
            rem_nums = list(map(itemgetter(0), ones))
        else:
            rem_nums = list(map(itemgetter(0), zeroes))

        if len(rem_nums) <= 1:
            break

    assert (len(rem_nums) == 1)

    return rem_nums[0]


def oxygen_rating(nums):
    return some_rating(nums, (most_common, 1))


def co2_rating(nums):
    return some_rating(nums, (least_common, 0))


def life_support_rating(nums):
    oxy = oxygen_rating(nums)
    co2 = co2_rating(nums)
    return oxy * co2


def nums_to_dec(numstrs):
    return [int(line.strip(), 2) for line in numstrs.strip().splitlines()]


EXAMPLE = """
00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010
"""

example_nums = nums_to_dec(EXAMPLE)
print(life_support_rating(example_nums))

with open("input/day3.txt", "r") as fh:
    puzzle_input = fh.read()

puzzle_nums = nums_to_dec(puzzle_input)
print(life_support_rating(puzzle_nums))
