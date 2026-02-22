def derivada(f, x0, h=1e-8):
    return (f(x0 + h) - f(x0 - h)) / (2 * h)