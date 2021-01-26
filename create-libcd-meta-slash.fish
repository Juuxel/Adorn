. create-libcd-meta.fish

function create-libcd-meta
    echo "Mod ID:" $argv[1]
    echo "Directory:" $argv[2]
    echo "Prefix:" $argv[3]
    for name in $argv[4..-1]
        echo \{\n'  '\"when\": [\n'    '\{\n'      '\"libcd:mod_loaded\": \"$argv[1]\"\n'    '\}\n'  ']\n'}' > ./src/main/resources/data/adorn/$argv[2]/$argv[1]/$argv[3]_$name.json.mcmeta
    end
end
