def secante(f, x0, x1, e):
    while abs(x1 - x0) > e:
        x2 = x1 - f(x1) * (x1 - x0) / (f(x1) - f(x0))
        x0, x1 = x1, x2
    return x1