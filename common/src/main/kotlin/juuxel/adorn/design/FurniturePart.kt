package juuxel.adorn.design

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import juuxel.adorn.util.AdornCodecs
import juuxel.adorn.util.getWithCodec
import juuxel.adorn.util.logger
import juuxel.adorn.util.putWithCodec
import juuxel.adorn.util.readNbtWithCodec
import juuxel.adorn.util.writeNbtWithCodec
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import org.joml.Vector3d

data class FurniturePart(
    val origin: Vector3d,
    var sizeX: Int, var sizeY: Int, var sizeZ: Int,
    var yaw: Double, var pitch: Double, var roll: Double,
    var material: FurniturePartMaterial
) {
    constructor(origin: Vector3d, sizeX: Int, sizeY: Int, sizeZ: Int, material: FurniturePartMaterial) :
        this(origin, sizeX, sizeY, sizeZ, material = material, yaw = 0.0, pitch = 0.0, roll = 0.0)

    inline fun <R> withMinMax(consumer: (minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double) -> R): R {
        val minX = origin.x - sizeX * 0.5
        val minY = origin.y - sizeY * 0.5
        val minZ = origin.z - sizeZ * 0.5
        val maxX = minX + sizeX
        val maxY = minY + sizeY
        val maxZ = minZ + sizeZ
        return consumer(minX, minY, minZ, maxX, maxY, maxZ)
    }

    fun forEachFace(consumer: FaceConsumer) = withMinMax { minX, minY, minZ, maxX, maxY, maxZ ->
        consumer.acceptFace(minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, maxX, minY, minZ)
        consumer.acceptFace(maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, minX, minY, maxZ)
        consumer.acceptFace(maxX, minY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, maxX, minY, maxZ)
        consumer.acceptFace(minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, minX, minY, minZ)
        consumer.acceptFace(minX, maxY, minZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ)
        consumer.acceptFace(minX, minY, maxZ, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ)
    }

    fun deepCopy(): FurniturePart = copy(origin = Vector3d(origin))

    fun write(buf: PacketByteBuf) {
        buf.writeDouble(origin.x)
        buf.writeDouble(origin.y)
        buf.writeDouble(origin.z)
        buf.writeByte(sizeX)
        buf.writeByte(sizeY)
        buf.writeByte(sizeZ)
        buf.writeDouble(yaw)
        buf.writeDouble(pitch)
        buf.writeDouble(roll)
        buf.writeNbtWithCodec(FurniturePartMaterial.CODEC, material)
    }

    companion object {
        private val LOGGER = logger()

        val CODEC: Codec<FurniturePart> = RecordCodecBuilder.create { builder ->
            builder.group(
                AdornCodecs.VECTOR_3D.fieldOf("Origin").forGetter { it.origin },
                AdornCodecs.clampedIntRange(1..16).fieldOf("SizeX").forGetter { it.sizeX },
                AdornCodecs.clampedIntRange(1..16).fieldOf("SizeY").forGetter { it.sizeY },
                AdornCodecs.clampedIntRange(1..16).fieldOf("SizeZ").forGetter { it.sizeZ },
                Codec.DOUBLE.fieldOf("Yaw").forGetter { it.yaw },
                Codec.DOUBLE.fieldOf("Pitch").forGetter { it.pitch },
                Codec.DOUBLE.fieldOf("Roll").forGetter { it.roll },
                FurniturePartMaterial.CODEC.fieldOf("Material").forGetter { it.material }
            ).apply(builder, ::FurniturePart)
        }

        fun load(buf: PacketByteBuf): FurniturePart {
            val originX = buf.readDouble()
            val originY = buf.readDouble()
            val originZ = buf.readDouble()
            val origin = Vector3d(originX, originY, originZ)
            val sizeX = buf.readByte().toInt()
            val sizeY = buf.readByte().toInt()
            val sizeZ = buf.readByte().toInt()
            val yaw = buf.readDouble()
            val pitch = buf.readDouble()
            val roll = buf.readDouble()
            val material = buf.readNbtWithCodec(FurniturePartMaterial.CODEC)
            return FurniturePart(origin, sizeX, sizeY, sizeZ, yaw, pitch, roll, material)
        }

        fun write(buf: PacketByteBuf, part: FurniturePart) =
            part.write(buf)

        fun fromNbt(nbt: NbtCompound, key: String): List<FurniturePart>? =
            nbt.getWithCodec(key, CODEC.listOf())
                .resultOrPartial { LOGGER.warn("[Adorn] Could not read furniture parts: {}", it) }
                .orElse(null)

        fun writeNbt(nbt: NbtCompound, key: String, parts: List<FurniturePart>) {
            nbt.putWithCodec(key, parts, CODEC.listOf())
        }
    }

    fun interface FaceConsumer {
        fun acceptFace(
            x1: Double, y1: Double, z1: Double,
            x2: Double, y2: Double, z2: Double,
            x3: Double, y3: Double, z3: Double,
            x4: Double, y4: Double, z4: Double
        )
    }
}
