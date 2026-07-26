package juuxel.adorn.data;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.JsonOps;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.client.resources.ColorManager;
import net.minecraft.data.CachedOutput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class AdornDataUtil {
    private static final Gson GSON = new Gson();

    public static void writeIterationOrderedJson(CachedOutput dataWriter, Path path, JsonElement json) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HashingOutputStream hashing = new HashingOutputStream(Hashing.sha256(), baos);
        OutputStreamWriter writer = new OutputStreamWriter(hashing, StandardCharsets.UTF_8);
        JsonWriter jsonWriter = new JsonWriter(writer);
        jsonWriter.setIndent("  ");
        GSON.toJson(json, jsonWriter);
        writer.append('\n');
        writer.close();
        dataWriter.writeIfNeeded(path, baos.toByteArray(), hashing.hash());
    }
}
