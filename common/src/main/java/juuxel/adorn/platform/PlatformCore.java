package juuxel.adorn.platform;

final class PlatformCore {
    static <T> T expected() {
        throw new AssertionError("untransformed @ExpectPlatform");
    }
}
