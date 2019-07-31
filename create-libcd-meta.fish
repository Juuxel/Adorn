function create-libcd-meta
    echo "Mod ID:" $argv[1]
    echo "Directory:" $argv[2]
    for name in $argv[3..-1]
        echo \{\n'  '\"when\": [\n'    '\{\n'      '\"libcd:mod_loaded\": \"$argv[1]\"\n'    '\}\n'  ']\n'}' > ./src/main/resources/data/adorn/$argv[2]/$name.json.mcmeta
    end
end
