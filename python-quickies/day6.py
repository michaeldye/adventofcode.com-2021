from pprint import pprint

from common import *


def parse_fush(input_text):
    fush = {k: 0 for k in rangei(0, 8)}
    for d in lmap(int, input_text.strip().split(',')):
        fush[d] += 1
    return fush


EXAMPLE = "3,4,3,1,2"

def step_fush(fush, *args):
    new_fush = {k: 0 for k in rangei(0, 8)}
    for k,v in fush.items():
        if k == 0:
            new_fush[8] += v
            new_fush[6] += v
        else:
            new_fush[k-1] += v
    return new_fush

def run_fush(input_text, days, debug=False):
    fush = parse_fush(input_text)
    if debug:
        pprint(fush)
        print(sum(fush.values()))
    for day in rangei(1, days):
        fush = step_fush(fush)
        if debug:
            print(f"day {day}")
            pprint(fush)
            print(sum(fush.values()))

    print(f"{days} days is {sum(fush.values())} fush" )

run_fush(EXAMPLE, 18, True)
run_fush(EXAMPLE, 80)

with open("input/day6.txt", "r") as fh:
    puzzle_input = fh.read()

run_fush(EXAMPLE, 256)
run_fush(puzzle_input, 256)
