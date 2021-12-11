from common import *
from collections import Counter
from functools import partial

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

EXAMPLE = EXAMPLE.strip()


def most_common(c):
    if c['1'] >= c['0']:
        return '1'
    return '0'


def least_common(c):
    if c['0'] <= c['1']:
        return '0'
    return '1'


def epsilon_rate(nums):
    nums = list(map(list, map(str.strip, nums.splitlines())))
    transpose = list(zip(*nums))
    return int(''.join((map(least_common, map(Counter, transpose)))), 2)


def gamma_rate(nums):
    nums = list(map(list, map(str.strip, nums.splitlines())))
    transpose = list(zip(*nums))
    return int(''.join((map(most_common, map(Counter, transpose)))), 2)


def power_consumption(nums):
    return gamma_rate(nums) * epsilon_rate(nums)


print("===== example =====")
print(power_consumption(EXAMPLE))

print("===== puzzle input =====")
with open("input/day3.txt", "r") as fh:
    puzzle_input = fh.read().strip()
print(power_consumption(puzzle_input))

print("======= part two =======")
most_common_bits = bin(gamma_rate(EXAMPLE))


def mask(r, pos):
    return r & (1 << pos)


def bit_at(n, pos):
    return (n >> pos) & 0b1  # my only hopez


assert (bit_at(0b1, 0) == 1)
assert (bit_at(0b10, 1) == 1)
assert (bit_at(0b100, 2) == 1)
assert (bit_at(0b100, 1) == 0)

pred_not_zero = lambda n: n != 0


def solution_part_two(nums, rate=gamma_rate):
    rem_nums = nums[:]

    msb_offset = 0

    len_of_nums = len(nums.splitlines()[0].strip())
    print(f"numlen: {len_of_nums}")

    while len(rem_nums.strip().splitlines()) > 1 and msb_offset < len_of_nums:
        the_rate = rate(rem_nums)

        rem_nums_int = map(partial(int, base=2), map(str.strip, rem_nums.strip().splitlines()))

        bit_pos = len_of_nums + -msb_offset + -1
        selector = bit_at(the_rate, bit_pos)

        print(f"remaining: {len(rem_nums)} rate: {the_rate} {bin(the_rate)[2:]}")

        bit_at_pos_1 = lambda x: 1 if bit_at(x, bit_pos) == 1 else 0

        # ones, zeroes = list(map(lambda l: list(map(bin, l)), partition(bit_at_pos_1, rem_nums_int)))
        ones, zeroes = list(map(list, partition(bit_at_pos_1, rem_nums_int)))

        if selector:
            rem_nums = ones[:]
        else:
            rem_nums = zeroes[:]
        rem_nums = "\n".join(map(lambda n: bin(n)[2:], rem_nums))

        msb_offset += 1

    print(rem_nums)


print("===== example =====")
solution_part_two(EXAMPLE)
solution_part_two(EXAMPLE, rate=epsilon_rate)

print("===== puzzle input =====")
solution_part_two(puzzle_input)
solution_part_two(puzzle_input, rate=epsilon_rate)
