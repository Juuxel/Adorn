package juuxel.adorn

import net.fabricmc.loader.launch.knot.KnotClient

fun main(args: Array<String>) {
    print("Enter username: ")

    val usernameArray = when (val name = readLine()) {
        "random", "" -> emptyArray()
        else -> arrayOf("--username", name)
    }

    KnotClient.main(arrayOf(*args, *usernameArray))
}
