def regula_falsi(f, a, b, e):
    while True:
        c = b - f(b) * (b - a) / (f(b) - f(a))
        if abs(f(c)) < e: return c
        if f(a) * f(c) < 0: b = c
        else: a = c