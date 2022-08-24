consoantes = {"b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z"}
getFrase()


def getFrase():
    frase = input(eval("Insira uma frase: "))
    getMenorPalavra(frase)

def getMenorPalavra(frase):
    frase = frase.split(" ")
    print(frase)
