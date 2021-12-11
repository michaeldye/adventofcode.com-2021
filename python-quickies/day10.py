from pprint import pprint
import math

EXAMPLE = """
[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]
"""


def each_nav(txt):
    lines = txt.strip().splitlines()
    return lines


def parse_nav(nav, completion=False):
    stack = []
    for ch in nav:
        b = VALID_BRACKETS.get(ch)
        if b:
            stack.append(b)
        else:
            b = stack.pop()
            if b != ch:
                if not completion:
                    return SCORE.get(ch)
                return None

    if not completion:
        return 0

    completion_score = 0

    print(''.join(reversed(stack)))

    for ch in reversed(stack):
        completion_score *= 5
        completion_score += COMPLETION_SCORES.get(ch)

    return completion_score


def part_one(txt):
    navs = each_nav(txt)
    print(sum([parse_nav(nav, completion=False) for nav in navs]))


def part_two(txt):
    navs = each_nav(txt)
    scores = [parse_nav(nav, completion=True) for nav in navs]
    scores = list(filter(None, scores))
    print(scores)
    print(len(scores))
    print(sorted(scores)[math.floor(len(scores)/2)])


VALID_BRACKETS = {k: v for k, v in ('{}', '[]', '<>', '()')}
SCORE = {
    ')': 3,
    ']': 57,
    '}': 1197,
    '>': 25137,
}

COMPLETION_SCORES = {
    ')': 1,
    ']': 2,
    '}': 3,
    '>': 4,
}

part_one(EXAMPLE)

with open('input/day10.txt', 'r') as fh:
    puzzle = fh.read()

part_one(puzzle)

part_two(EXAMPLE)

part_two(puzzle)