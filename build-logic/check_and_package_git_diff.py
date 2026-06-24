from os import remove
from sys import stdin
from zipfile import ZipFile


written = False

with ZipFile("diff.zip", "w") as zip:
    for line in stdin:
        line = line.removesuffix("\n")
        print(line)
        written = True
        status = line[0:2]
        file = line[line.index(" -> ") + 4:] if " -> " in line else line[3:]
        if status[1] != "D":
            zip.write(file)

if written:
    exit(1)
else:
    remove("diff.zip")
