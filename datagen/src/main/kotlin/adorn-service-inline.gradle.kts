import juuxel.adorn.asm.InlineServiceLoaderAction

tasks.named("remapJar") {
    doLast(InlineServiceLoaderAction)
}
