from common import lmap
from itertools import chain

EXAMPLE = """
6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5
"""


def parse_instructions(txt):
    coords = []
    folds = []
    for line in map(str.strip, txt.strip().splitlines()):
        if len(line) == 0:
            continue
        if ',' in line:
            coords.append(tuple(lmap(int, line.strip().split(','))))
        elif line.startswith("fold"):
            _, along = line.split(' along ')
            axis, coord = along.split('=')
            coord = int(coord)
            folds.append((axis, coord))
    return coords, folds


def start_board(coords):
    (maxx, maxy) = lmap(max, zip(*coords))
    maxx += 1
    maxy += 1
    b = [[0 for x in range(0, maxx)] for y in range(0, maxy)]
    for x, y in coords:
        b[y][x] = 1
    return b


def do_fold(old_board, fold):
    axis, coord = fold
    if axis == 'x':
        b = []
        for row in old_board:
            new_row = [v | v2 for v, v2 in zip(row[:coord], row[coord:][::-1])]
            b.append(new_row)
    elif axis == 'y':
        b = []
        for (r1,r2) in zip(old_board[:coord], old_board[coord:][::-1]):
            new_row = [v | v2 for v, v2 in zip(r1, r2)]
            b.append(new_row)
    return b


def print_board(board):
    chmap = {
        0: ' ',
        1: '#',
    }
    for row in board:
        print(''.join(map(chmap.get, row)))


def do_puzzle(txt, p2=False):
    coords, folds = parse_instructions(txt)
    b = start_board(coords)
    for fold in folds:
        b = do_fold(b, fold)
        if not p2:
            break
    if p2:
        print_board(b)
    else:
        print(sum(chain(*b)))


puzzle = open('input/day13.txt', 'r').read()
do_puzzle(puzzle)
do_puzzle(EXAMPLE, True)
do_puzzle(puzzle, True)
