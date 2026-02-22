import math

def significant_figures(x, x_aprox, beta=10):
    s = math.floor(math.log(abs(x), beta))
    error_abs = abs(x - x_aprox)
    
    if error_abs == 0:
        return float('inf')
    
    r_max = s + 1 - math.log(2 * error_abs, beta)
    return max(0, int(math.floor(r_max)))

var = significant_figures(123.456, 123.45)
print(var)