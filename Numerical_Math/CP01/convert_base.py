def convertir_base(numero, base_origen, base_destino):
    if isinstance(numero, int) and base_origen == 10:
        decimal = numero
    else:
        decimal = int(str(numero), base_origen)
    
    if base_destino == 10:
        return str(decimal)
    
    digitos = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    resultado = ""
    
    while decimal > 0:
        resultado = digitos[decimal % base_destino] + resultado
        decimal //= base_destino
    
    return resultado if resultado else "0"