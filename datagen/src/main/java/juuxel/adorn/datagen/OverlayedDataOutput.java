package juuxel.adorn.datagen;

public final class OverlayedDataOutput implements DataOutput {
    private final DataOutput next;
    private final String directory;

    public OverlayedDataOutput(DataOutput next, Overlay overlay) {
        this.next = next;
        this.directory = ensureFinalSlash(overlay.directory());
    }

    private static String ensureFinalSlash(String directory) {
        return directory.endsWith("/") ? directory : directory + "/";
    }

    @Override
    public void write(String path, String content) {
        next.write(directory + path, content);
    }
}
