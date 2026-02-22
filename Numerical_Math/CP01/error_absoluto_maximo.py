import math

def derivada_parcial_x(f, x, y, h=1e-8):
    return (f(x + h, y) - f(x - h, y)) / (2 * h)

def derivada_parcial_y(f, x, y, h=1e-8):
    return (f(x, y + h) - f(x, y - h)) / (2 * h)

def error_absoluto_maximo(f, x, y, x_aprox, y_aprox):
    delta_mx = abs(x - x_aprox)
    delta_my = abs(y - y_aprox)
    fx = derivada_parcial_x(f, x, y)
    fy = derivada_parcial_y(f, x, y)
    return abs(fx) * delta_mx + abs(fy) * delta_my