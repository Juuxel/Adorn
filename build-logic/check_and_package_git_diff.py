import subprocess
from zipfile import ZipFile

process = subprocess.run(["git", "status", "--porcelain"], capture_output=True, text=True)

if process.stdout == "":
    exit(0)

with ZipFile("diff.zip", "w") as zip:
    for line in process.stdout.splitlines():
        print(line)
        status = line[0:2]
        file = line[line.index(" -> ") + 4 :] if " -> " in line else line[3:]
        if status[1] != "D":
            zip.write(file)

exit(1)
