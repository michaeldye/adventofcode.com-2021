from itertools import permutations

EXAMPLE = """
be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
"""

seg_map = {
    1: 'cf',

    7: 'acf',

    4: 'bcdf',

    2: 'acdeg',  # 2,3,5 are ambiguous
    3: 'acdfg',  # they share a,g
    5: 'abdfg',  # (cde,cdf,bdf) are s.t. b only appears in 5, and e only appears in 2

    6: 'abdefg',  # 6,8,9 are ambiguous
    0: 'abcefg',  # they share a,b,f,g
    9: 'abcdfg',  # (de,ce,cd) are s.t 2 of the 3 (c,d,e) appear in each one!

    8: 'abcdefg',
}

from pprint import pprint

segment_lut = []

for p in permutations('abcdefg'):
    scramble_map = str.maketrans('abcdefg', ''.join(p))
    scrambles = {k: ''.join(sorted(v.translate(scramble_map))) for k, v in seg_map.items()}
    segment_lut.append((p, scramble_map, scrambles))


# print(len(segment_lut))


def parse_displays(txt):
    displays = []
    lines = list(map(str.strip, txt.strip().splitlines()))
    for line in lines:
        # print(line)
        obs, digis = list(map(str.strip, line.split(' | ')))
        obs = list(map(str.strip, obs.split()))
        digis = list(map(str.strip, digis.split()))
        # print(obs)
        # print(digis)
        displays.append((set(obs), digis))
    return displays


# pprint(parse_displays(EXAMPLE))

def solve_display(d, accum_func=None):
    obs, digis = d
    obs = {''.join(sorted(o)) for o in obs}
    for p, scrambles, mixmap in segment_lut:
        mixset = set(mixmap.values())
        d1 = (obs - mixset)
        d2 = (mixset - obs)
        if len(d1) == 0 or len(d2) == 0:
            print("=======")
            print()
            print(p)
            print(mixmap)
            reverse_map = {v: k for k, v in mixmap.items()}
            real_digis = [reverse_map.get(''.join(sorted(x))) for x in digis]
            print(real_digis)
            if accum_func:
                accum_func(real_digis)
            break


solve_display(parse_displays(EXAMPLE)[0])


def part_one(txt):
    acc = [0]

    def handle_soln(soln):
        uniqies = [1,4,7,8]
        acc[0] += sum([1 if x in uniqies else 0 for x in soln])

    displays = parse_displays(txt)
    for d in displays:
        solve_display(d, handle_soln)

    print(acc)

def part_two(txt):
    acc = [0]

    def handle_soln(soln):
        s = ''.join(map(str, soln))
        s = int(s)
        print(s)
        acc[0] += s

    displays = parse_displays(txt)
    for d in displays:
        solve_display(d, handle_soln)

    print(acc)


part_one(EXAMPLE)

with open("input/day8.txt", "r") as fh:
    puzzle_input = fh.read()

part_one(puzzle_input)
part_two(puzzle_input)