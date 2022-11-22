package juuxel.adorn

import juuxel.adorn.platform.PlatformBridges
import java.util.ServiceLoader

fun foo() {
    val foo = ServiceLoader.load(PlatformBridges::class.java).findFirst()
    println(foo.get())
}
