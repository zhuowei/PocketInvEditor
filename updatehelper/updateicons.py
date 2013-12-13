from update4me import readMCCSV
import re
import os
import json

def translate(blockinfo, terrainMap):
    instr = blockinfo[2].split("|")
    w = int(float(instr[4]) + 0.5)
    h = int(float(instr[5]) + 0.5)
    x1 = int(float(instr[0]) * w + 0.5) 
    y1 = int(float(instr[1]) * h + 0.5)
    x2 = int(float(instr[2]) * w + 0.5)
    y2 = int(float(instr[3]) * h + 0.5)
    wi = x2 - x1
    if wi == 0:
        return guessUV(blockinfo, terrainMap)
    row = y1 // wi
    col = x1 // wi
    return (row, col)

def guessUV(blockInfo, terrainMap):
    name = blockInfo[1].split(".", 1)[1].lower()
    print(name)
    for i in terrainMap:
        if i["name"] == name:
            instr = i["uv"]
            w = int(float(instr[4]) + 0.5)
            h = int(float(instr[5]) + 0.5)
            x1 = int(float(instr[0]) * w + 0.5) 
            y1 = int(float(instr[1]) * h + 0.5)
            x2 = int(float(instr[2]) * w + 0.5)
            y2 = int(float(instr[3]) * h + 0.5)
            wi = x2 - x1
            if wi == 0:
                return (0, 0)
            row = y1 // wi
            col = x1 // wi
            return (row, col)
    myown = [("litpumpkin", [0.3125,0.25,0.3437,0.3125,512,256]), ("hayblock", [0.3438,0.375,0.375,0.4375,512,256]), 
             ("cake", [0.5938,0.3125,0.625,0.375,512,256])]
    for i in myown:
        if i[0] == name:
            instr = i[1]
            w = int(float(instr[4]) + 0.5)
            h = int(float(instr[5]) + 0.5)
            x1 = int(float(instr[0]) * w + 0.5) 
            y1 = int(float(instr[1]) * h + 0.5)
            x2 = int(float(instr[2]) * w + 0.5)
            y2 = int(float(instr[3]) * h + 0.5)
            wi = x2 - x1
            if wi == 0:
                return (0, 0)
            row = y1 // wi
            col = x1 // wi
            return (row, col)
    print("FAIL " + name + ":" + str(blockInfo[0]))
    return (0, 0)

def readXML(name):
    mylist = []
    with open(name, "r") as myfile:
        for l in myfile:
            m = re.search(r'typeId="([^"]*)"', l)
            if m is None:
                continue
            num = int(m.group(1))
            if num in mylist:
                continue
            mylist.append(num)
    #print(mylist)
    return mylist

def rebuildXML(curdat, itemList):
    mylist = []
    for i in itemList:
        if not i[0] in curdat:
            mylist.append(i)
    print(mylist)
    return mylist

def rewriteXML(name, listToAdd):
    listToAdd = listToAdd[:]
    mylist = []
    with open(name, "r") as myfile, open(name + ".new", "w") as outfile:
        prevLine = None
        prevNum = -1
        for l in myfile:
            m = re.search(r'typeId="([^"]*)"', l) # current line number
            if m is None:
                if not '</items>' in l:
                    outfile.write(l)
                    continue
                num = 512
            else:
                num = int(m.group(1))
            for item in listToAdd:
                listId = item[0]
                texName = "items-opaque" if listId >= 256 else "terrain-atlas"
                if listId > prevNum and listId < num:
                    outfile.write('    <item typeId="{}" icon="{},{},{}"/>\n'.format(item[0], texName, item[1][0], item[1][1]))
                    prevNum = listId
            outfile.write(l)
    os.rename(name + ".new", name)

def main():
    rawDat = readMCCSV("items.csv")
    newDat = []
    with open("terrain.meta") as metafile:
        terrainMap = json.load(metafile)
    for i in rawDat:
        newDat.append((i[0], translate(i, terrainMap)))
    curIcons = readXML("../res/xml/item_icon.xml")
    listToAdd = rebuildXML(curIcons, newDat)
    rewriteXML("../res/xml/item_icon.xml", listToAdd)

if __name__ == "__main__":
    main()
