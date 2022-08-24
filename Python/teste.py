import array as arr
def getMaiorrPalavra(frase):
    frase = frase.toLower().split(" ")
    contaVogais = 0
    nrVogais = 0
    palavra = ""
    for i in range(len(frase)):
        contaVogais = 0
        palavra = frase.split("")
        for j in range(len(palavra)):
            for vogail in vogais:
                if (j.equals(k)):
                    contaConsoantes = contaConsonantes+1
        contaConsoantes[j] = contaConsoantes
    
    for l in contaConsoantes:
        print(l)
    return frase

vogais = ("a", "e", "i", "o", "u")
frase = input("Insira uma frase: ")
print(getMenorPalavra(frase))


