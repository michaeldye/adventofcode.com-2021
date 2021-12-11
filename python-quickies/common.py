def rangei(start, end, step=1):
    if end < start and step > 0:
        step *= -1
    end += step
    return range(start, end, step)


def lmap(pred, it):
    return list(map(pred, it))
