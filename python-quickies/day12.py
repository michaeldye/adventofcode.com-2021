from copy import deepcopy
from pprint import pprint
from collections import Counter

import networkx as nx

EXAMPLE = """
start-A
start-b
A-c
A-b
b-d
A-end
b-end
"""

PUZZLE = """
mx-IQ
mx-HO
xq-start
start-HO
IE-qc
HO-end
oz-xq
HO-ni
ni-oz
ni-MU
sa-IE
IE-ni
end-sa
oz-sa
MU-start
MU-sa
oz-IE
HO-xq
MU-xq
IE-end
MU-mx
"""


def traverse(G, nodename, visited, pathcb):
    edges = G[nodename]
    visited = visited[:]
    visited.append(nodename)
    if nodename == 'end':
        pathcb(visited)
        return
    for edge in sorted(edges):
        if edge in visited:
            if edge.isupper():
                traverse(G, edge, visited, pathcb)
        else:
            traverse(G, edge, visited, pathcb)


def traverse_p2(G, nodename, visited, pathcb):
    edges = G[nodename]
    visited = visited[:]
    visited.append(nodename)
    if nodename == 'end':
        pathcb(visited)
        return

    for edge in sorted(edges):
        if edge == 'start':
            # cannot revisit start in p2
            continue

        def pred(x):
            if x.isupper():
                return False
            if x in ('start','end'):
                return False
            return True


        c = Counter(filter(pred, visited))

        if edge.islower():
            if c.get(edge, 0) == 1 and 2 in c.values():
                # someone else double dipped already
                pass
            elif c.get(edge, 0) == 2:
                # you double dipped
                pass
            else:
                # lucky boy, take a(nother) dip
                traverse_p2(G, edge, visited, pathcb)
        else:
            traverse_p2(G, edge, visited, pathcb)


def part_one(txt, p2=False):
    G = nx.Graph()
    lines = txt.strip().splitlines()
    nodes = {}
    for line in lines:
        to, fro = line.split('-')
        if to not in nodes:
            G.add_node(to)
            nodes[to] = 1
        if fro not in nodes:
            G.add_node(fro)
            nodes[fro] = 1
        G.add_edge(to, fro)

    paths = set()

    def add_path(p):
        paths.add(','.join(p))

    if p2:
        traverse_p2(G, 'start', [], add_path)
    else:
        traverse(G, 'start', [], add_path)

    pprint(paths)
    print(len(paths))


def part_two(txt):
    part_one(txt, p2=True)


part_one(EXAMPLE)
part_one(PUZZLE)
part_two(EXAMPLE)
part_two(PUZZLE)
