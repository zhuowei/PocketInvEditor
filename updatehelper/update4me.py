import os
import re

# First, read in the english XML file
# Find any missing entries
# then, auto-generate the entries and write back

# secondly, load english XML
# For every other XML:
# Find any missing entries
# Auto-insert that in

def readPropFile(loc):
    propmap = {}
    with open(loc, "r") as propfile:
        for l in propfile:
            prop = l.strip().split("=", 1)
            if len(prop) != 2:
                continue
            propmap[prop[0]] = prop[1]
    return propmap

def readAllLangs():
    langlist = os.listdir("lang")
    langmap = {}
    for lang in langlist:
        prop = readPropFile("lang/" + lang)
        langmap[lang] = prop
    return langmap

def readMCCSV(name):
    mylist = []
    with open(name, "r") as myfile:
        for l in myfile:
            fields = l.strip().split(",")
            mylist.append((int(fields[0]), fields[1]))
    return mylist

def readXML(name):
    mylist = []
    with open(name, "r") as myfile:
        for l in myfile:
            m = re.search(r'dec="([^"]*)"', l)
            if m is None:
                continue
            num = int(m.group(1))
            if num in mylist:
                continue
            mylist.append(num)
    #print(mylist)
    return mylist

def rebuildXML(curdat, itemList, langProps):
    mylist = []
    for i in itemList:
        if not i[0] in curdat:
            r = i[1] + ".name"
            myName = r
            for lang in langProps:
                if r in lang:
                    myName = lang[r]
                    break
            mylist.append((i[0], myName))
    print(mylist)
    return mylist

def rewriteXML(name, listToAdd):
    listToAdd = listToAdd[:]
    mylist = []
    with open(name, "r") as myfile, open(name + ".new", "w") as outfile:
        prevLine = None
        prevNum = -1
        for l in myfile:
            m = re.search(r'dec="([^"]*)"', l) # current line number
            if m is None:
                if not '</items>' in l:
                    outfile.write(l)
                    continue
                num = 512
            else:
                num = int(m.group(1))
            for item in listToAdd:
                listId = item[0]
                if listId > prevNum and listId < num:
                    outfile.write('        <item hex="{:X}" dec="{:d}" name="{}" />\n'.format(listId, listId, item[1]))
                    prevNum = listId
            outfile.write(l)
    os.rename(name + ".new", name)

def addOverrides(madProps):
    fallback = madProps["en_US.lang.desktop"]
    fallback["tile.pumpkinStem.name"] = "Pumpkin Stem"
                    

def main():
    madProps = readAllLangs()
    addOverrides(madProps)
    itemList = readMCCSV("items.csv")
    #print(itemList)
    langPairs = [
        ("", "en_US"),
        ("de", "de_DE"),
        ("es", "es_MX"),
        ("ko", "ko_KR"),
        ("pt", "pt_BR"),
        ("ru", "ru_RU"),
        ("zh-rCN", "zh_CN")
    ]
    for l in langPairs:
        fixXML(l[0], l[1], madProps, itemList)
def fixXML(xmlDirName, langName, madProps, itemList):
    langDash = "-" if len(xmlDirName) > 0 else ""
    initXML = readXML("../res/xml" + langDash + xmlDirName + "/item_data.xml")
    listToAdd = rebuildXML(initXML, itemList, [madProps[langName + ".lang"], madProps["en_US.lang"], madProps["en_US.lang.desktop"]])
    rewriteXML("../res/xml" + langDash + xmlDirName + "/item_data.xml", listToAdd)

if __name__ == "__main__":
    main()
