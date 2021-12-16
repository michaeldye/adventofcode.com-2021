def rangei(start, end, step=1):
    if end < start and step > 0:
        step *= -1
    end += step
    return range(start, end, step)


def lmap(pred, it):
    return list(map(pred, it))


def window(seq, n=2):
    it = iter(seq)
    try:
        win = [ next(it) for _ in range(n) ]
        yield win[:]

        while True:
            win.pop(0)
            win.append(next(it))
            yield win[:]
    except StopIteration:
        pass