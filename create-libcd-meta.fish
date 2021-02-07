function create-libcd-meta
    echo "Mod ID:" $argv[1]
    echo "Directory:" $argv[2]
    echo "Prefix:" $argv[3]
    for name in $argv[4..-1]
        echo \{\n'  '\"when\": [\n'    '\{\n'      '\"libcd:mod_loaded\": \"$argv[1]\"\n'    '\}\n'  ']\n'}' > ./src/main/resources/data/adorn/$argv[2]/$argv[1]_$argv[3]_$name.json.mcmeta
    end
end

function create-wood-libcd-meta
    for name in $argv[3..-1]
        create-libcd-meta $argv[1] $argv[2] $name chair drawer kitchen_counter kitchen_cupboard platform post shelf step table
    end
end

function create-1-6-wood-libcd-meta
    for name in $argv[3..-1]
        create-libcd-meta $argv[1] $argv[2] $name kitchen_sink coffee_table
    end
end

function create-stone-libcd-meta
    for name in $argv[3..-1]
        create-libcd-meta $argv[1] $argv[2] $name platform post step
    end
end

function create-1-14-wood-libcd-meta
    for name in $argv[3..-1]
        create-libcd-meta $argv[1] $argv[2] $name bench
    end
end

function create-all-wood-libcd-meta
    create-wood-libcd-meta $argv
    create-1-6-wood-libcd-meta $argv
    create-1-14-wood-libcd-meta $argv
end

# Usage for all of the functions:
# <function-name> <mod-id> <directory> <prefixes...>
