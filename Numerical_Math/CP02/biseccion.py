def biseccion(f, a, b, e):
    while (b - a) / 2 > e:
        c = (a + b) / 2
        if f(c) == 0: return c
        if f(a) * f(c) < 0: b = c
        else: a = c
    return (a + b) / 2