import tkinter as tk

def factorial(n):
    if n <= 1:
        return 1
    resultado = 1
    for i in range(2, n + 1):
        resultado *= i
    return resultado

def coeficiente_binomial(n, k):
    if k < 0 or k > n:
        return 0
    return factorial(n) // (factorial(k) * factorial(n - k))

def polinomio_bernstein(grado, indice, t):
    coeficiente = coeficiente_binomial(grado, indice)
    return coeficiente * (t ** indice) * ((1 - t) ** (grado - indice))

def punto_en_curva(puntos_control, t):
    grado = len(puntos_control) - 1
    x = 0.0
    y = 0.0
    
    for i in range(grado + 1):
        bern = polinomio_bernstein(grado, i, t)
        x += puntos_control[i][0] * bern
        y += puntos_control[i][1] * bern
    
    return (x, y)

def generar_puntos_curva(puntos_control, num_puntos=200):
    puntos = []
    for i in range(num_puntos + 1):
        t = i / num_puntos
        puntos.append(punto_en_curva(puntos_control, t))
    return puntos

def dibujar_letra_c():
    ventana = tk.Tk()
    ventana.title("Letra C con Curvas de Bezier - Carlos Javier Blanco Moreira")
    
    lienzo = tk.Canvas(ventana, width=800, height=600, bg='white')
    lienzo.pack()
    
    p0 = (200, 150)
    p1 = (180, 130)
    p2 = (150, 140)
    p3 = (120, 180)
    p4 = (120, 280)
    p5 = (130, 380)
    p6 = (170, 420)
    p7 = (230, 420)
    p8 = (280, 390)
    p9 = (300, 330)
    p10 = (290, 270)
    p11 = (250, 230)
    
    puntos_control = [p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11]
    grado = len(puntos_control) - 1
    
    for i in range(len(puntos_control) - 1):
        x1, y1 = puntos_control[i]
        x2, y2 = puntos_control[i + 1]
        lienzo.create_line(x1, y1, x2, y2, fill='#CCCCCC', width=1, dash=(5, 3))
    
    for i, (x, y) in enumerate(puntos_control):
        lienzo.create_oval(x - 4, y - 4, x + 4, y + 4, fill='#FF6666', outline='darkred', width=1)
        lienzo.create_text(x + 6, y - 6, text=str(i), font=('Arial', 8, 'bold'), fill='darkred')
    
    puntos_curva = generar_puntos_curva(puntos_control, num_puntos=250)
    
    for i in range(len(puntos_curva) - 1):
        x1, y1 = puntos_curva[i]
        x2, y2 = puntos_curva[i + 1]
        lienzo.create_line(x1, y1, x2, y2, fill='black', width=5, capstyle=tk.ROUND)
    
    lienzo.create_oval(195, 145, 205, 155, fill='green', outline='darkgreen', width=2)
    lienzo.create_text(215, 140, text="Inicio", font=('Arial', 8, 'bold'), fill='green')
    
    fx, fy = p11
    lienzo.create_oval(fx - 5, fy - 5, fx + 5, fy + 5, fill='blue', outline='darkblue', width=2)
    lienzo.create_text(fx + 15, fy - 5, text="Final", font=('Arial', 8, 'bold'), fill='blue')
    
    lienzo.create_text(
        400, 35,
        text="Letra C - Carlos Javier Blanco Moreira",
        font=('Arial', 18, 'bold'),
        fill='#2C3E50'
    )
    
    lienzo.create_text(
        400, 62,
        text=f"Curva de Bezier de Grado {grado} | {len(puntos_control)} Puntos de Control",
        font=('Arial', 11),
        fill='#7F8C8D'
    )
    
    lienzo.create_text(
        50, 550,
        text="Punto de control (rojo)     Poligono de control (gris)     Curva Bezier (negra)",
        font=('Arial', 9),
        fill='#555555',
        anchor='w'
    )
    
    lienzo.create_text(
        50, 575,
        text="Inicio (verde)     Final (azul)     La abertura queda hacia la derecha",
        font=('Arial', 9),
        fill='#555555',
        anchor='w'
    )
    
    lienzo.create_rectangle(70, 95, 530, 490, outline='#E0E0E0', width=1)
    
    ventana.mainloop()

if __name__ == "__main__":
    dibujar_letra_c()