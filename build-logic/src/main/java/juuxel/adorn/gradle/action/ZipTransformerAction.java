package juuxel.adorn.gradle.action;

import juuxel.adorn.gradle.util.zip.ZipTransformer;
import org.gradle.api.Action;
import org.gradle.api.Task;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class ZipTransformerAction implements Action<Task> {
    private final ZipTransformer transformer = new ZipTransformer();

    public ZipTransformerAction(Action<? super ZipTransformer> transformerConfig) {
        transformerConfig.execute(transformer);
    }

    @Override
    public void execute(Task task) {
        try {
            var jar = task.getOutputs().getFiles().getSingleFile();
            transformer.transform(jar.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
