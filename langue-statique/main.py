f = open("input.txt")
noms = []
langues = []
alp = "abcdefghijklmnopqrstuvwxyz"
stats = []
texts = []

def charToInt(c):
    c = c.lower()
    for i in range(len(alp)):
        if c == alp[i]:
            return i
    return None

def langueToStat(j, L):
    nom = noms[j]
    stats.append({})
    stats[j]["Nom"] = nom
    somme = 0
    for i in L:
        somme += i
    for i in range(len(L)):
        stats[j][alp[i]] = L[i]/somme

def textToStat(t):
    stat = {}
    for i in alp:
        stat[i] = 0
    somme = len(t)
    for i in t:
        stat[i] += 1
    for i in alp:
        stat[i] = stat[i]/somme
    return stat


x=-2
y=-1
for i in f.readlines():
    if "=" in i:
        x = -3
        continue
    if x == -3:
        texts.append(i.strip())
        continue
    if x == -2:
        noms.append(i.strip())
        x = -1
    elif x == -1:
        x = int(i)-1
        langues.append([])
        y += 1
    elif x == 0:
        langues[y].append(int(i.split(" ")[1]))
        x = -2
    else:
        langues[y].append(int(i.split(" ")[1]))
        x -= 1
    
for i in range(len(langues)):
    langueToStat(i, langues[i])
    
for i in stats:
    i["Nom"] = charToInt(i["Nom"])
    f = {k: v for k, v in sorted(i.items(), key=lambda item: item[1])}
    f["Nom"] = alp[f["Nom"]]
    text = ""
    for j in f.keys():
        if j != "Nom":
            text += j
    print(f["Nom"] + " " + text)

print("======")

for i in range(len(texts)-1):
    unsorted = textToStat(texts[i])
    f = {k: v for k, v in sorted(unsorted.items(), key=lambda item: item[1])}
    text = ""
    for j in f.keys():
        if j != "Nom":
            text += j
    print(text)