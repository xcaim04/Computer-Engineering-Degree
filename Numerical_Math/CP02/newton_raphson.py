def newton_raphson(f, df, x0, e):
    x = x0
    while abs(f(x)) > e:
        x = x - f(x) / df(x)
    return x