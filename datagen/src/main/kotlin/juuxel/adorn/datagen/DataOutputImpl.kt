package juuxel.adorn.datagen

import java.io.BufferedReader
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.TreeMap
import kotlin.io.path.reader
import kotlin.io.path.writer

class DataOutputImpl(private val directory: Path) : DataOutput {
    private val existing: MutableList<Path> = ArrayList()
    val remaining: List<Path> get() = existing
    private val originalHashes: MutableMap<String, String> = HashMap()
    private val newHashes: MutableMap<String, String> = TreeMap()

    fun loadHashes(reader: Reader) {
        val buffered = BufferedReader(reader)
        for (line in buffered.lineSequence()) {
            val (hash, file) = line.split('\t', limit = 2)
            originalHashes[file] = hash

            val path = directory.resolve(file)
            if (Files.exists(path)) {
                existing.add(path)
            }
        }
    }

    fun saveHashes(app: Appendable) {
        for ((key, value) in newHashes) {
            app.append(value).append('\t').append(key).append('\n')
        }
    }

    override fun write(path: String, content: String) {
        val hashBytes = MessageDigest.getInstance("SHA-256").digest(content.toByteArray())
        val humanReadableHash = hashBytes.joinToString(separator = "") { it.toUByte().toString(radix = 16).padStart(2, '0') }
        val outputPath = directory.resolve(path)
        existing.remove(outputPath.toAbsolutePath())
        newHashes[path] = humanReadableHash
        if (originalHashes[path] != humanReadableHash) {
            Files.createDirectories(outputPath.parent)
            Files.writeString(outputPath, content, Charsets.UTF_8)
        }
    }

    fun finish() {
        directory.resolve(".cache").writer().use { saveHashes(it) }

        for (path in remaining) {
            Files.delete(path)

            var parent = path.parent
            while (parent != null && !Files.isSameFile(parent, directory) && Files.list(parent).use { it.findAny().isEmpty }) {
                Files.delete(parent)
                parent = parent.parent
            }
        }
    }

    companion object {
        fun load(directory: Path): DataOutputImpl {
            val output = DataOutputImpl(directory)
            val cachePath = directory.resolve(".cache")
            if (Files.exists(cachePath)) {
                cachePath.reader().use { output.loadHashes(it) }
            }
            return output
        }
    }
}
