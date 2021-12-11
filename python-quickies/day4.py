"""
squid bingo?
"""

from pprint import pprint
import itertools

EXAMPLE = """
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7"""


def parse_input(puzzle_input):
    lines = puzzle_input.strip().splitlines()

    it = iter(lines)

    called_numbers = list(map(int, next(it).strip().split(',')))

    next(it)  # consume blank line

    boards = []
    board = []

    try:
        while True:
            for _ in range(0, 5):
                brow = list(map(int, next(it).split()))
                if len(brow) != 5:
                    raise Exception('oof')
                board.append(brow)
            boards.append(board)

            next(it)  # consume blank line or possibly run into the end of the input
            board = []
    except StopIteration:
        pass

    if len(board) == 5:
        boards.append(board)
    elif len(board) > 0:
        raise Exception('oof')

    return called_numbers, boards


class SquidBingoBoard(object):
    def __init__(self, board):
        self.board = board
        self.markers = [[0 for _ in range(0, 5)] for _ in range(0, 5)]

    def __str__(self):
        bstr = ""
        for row in range(0, 5):
            for col in range(0, 5):
                marked = '_' if self.markers[row][col] else ' '
                bstr += f'{marked}{self.board[row][col]:3d}{marked} '
            bstr += '\n'

        return bstr

    def bob_fossil(self, n):
        """
        62 avian flu
        """
        for row in range(0, 5):
            for col in range(0, 5):
                if self.board[row][col] == n:
                    self.markers[row][col] = 1

    def check_bingo(self):
        row_sums = map(sum, self.markers)
        col_sums = map(sum, zip(*self.markers))

        for s in itertools.chain(row_sums, col_sums):
            if s == 5:
                return True

        return False

    def score(self):
        acc = 0

        for row in range(0, 5):
            for col in range(0, 5):
                if not self.markers[row][col]:
                    acc += self.board[row][col]

        return acc

    @classmethod
    def play(cls, input_text):
        calls, boards = parse_input(input_text)
        boards = list(map(SquidBingoBoard, boards))

        for num, board in [(c, b) for c in calls for b in boards]:
            board.bob_fossil(num)
            if board.check_bingo():
                print(f"bringo on {num}")
                print(board)
                print(board.score())
                print(board.score() * num)
                break

    @classmethod
    def play_to_lose_cuck(cls, input_text):
        calls, boards = parse_input(input_text)
        boards = list(map(SquidBingoBoard, boards))

        winning_boards = []

        for num, board in [(c, b) for c in calls for b in boards]:
            if board in winning_boards:
                continue
            board.bob_fossil(num)
            if board.check_bingo():
                winning_boards.append(board)
                if len(winning_boards) == len(boards):
                    break

        winning_board = winning_boards[-1]
        print(f"final bringo on {num}")
        print(winning_board)
        print(winning_board.score())
        print(winning_board.score() * num)

with open("input/day4.txt", "r") as fh:
    puzzle = fh.read()

SquidBingoBoard.play(EXAMPLE)
SquidBingoBoard.play(puzzle)


print("======= part two =======")
SquidBingoBoard.play_to_lose_cuck(EXAMPLE)
SquidBingoBoard.play_to_lose_cuck(puzzle)