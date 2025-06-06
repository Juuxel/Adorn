package juuxel.adorn.client.renderer;

import juuxel.adorn.entity.ConeEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

import java.util.List;

public final class ConeEntityRenderer extends EntityRenderer<ConeEntity, ConeEntityRenderState> {
    public static final SpriteIdentifier STONE_TEXTURE = new SpriteIdentifier(
        SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.ofVanilla("block/white_wool")
    );

    private static final List<Quad> QUADS;

    static {
        var coneTop00 = new Vector3f(-2, 12, -2);
        var coneTop10 = new Vector3f(2, 12, -2);
        var coneTop11 = new Vector3f(2, 12, 2);
        var coneTop01 = new Vector3f(-2, 12, 2);
        var coneBottom00 = new Vector3f(-4, 2, -4);
        var coneBottom10 = new Vector3f(4, 2, -4);
        var coneBottom11 = new Vector3f(4, 2, 4);
        var coneBottom01 = new Vector3f(-4, 2, 4);

        QUADS = List.of(
            // BASE
            // top
            Quad.ofParallelogram(-6, 2, -6, 0, 0, 12, 12, 0, 0, 0, 0, 1, 1),
            // bottom
            Quad.ofParallelogram(-6, 0, -6, 12, 0, 0, 0, 0, 12, 0, 0, 1, 1),
            // west
            Quad.ofParallelogram(-6, 0, -6, 0, 0, 12, 0, 2, 0, 0, 0, 1, 1),
            // north
            Quad.ofParallelogram(6, 0, -6, -12, 0, 0, 0, 2, 0, 0, 0, 1, 1),
            // east
            Quad.ofParallelogram(6, 0, 6, 0, 0, -12, 0, 2, 0, 0, 0, 1, 1),
            // south
            Quad.ofParallelogram(-6, 0, 6, 12, 0, 0, 0, 2, 0, 0, 0, 1, 1),
            // CONE
            // top
            Quad.ofParallelogram(-2, 12, -2, 0, 0, 4, 4, 0, 0, 0, 0, 1, 1),
            // west
            makeSlantedFace(coneBottom00, coneBottom01, coneTop01, coneTop00, 0, 0, 1, 1),
            // east
            makeSlantedFace(coneBottom11, coneBottom10, coneTop10, coneTop11, 0, 0, 1, 1),
            // north
            makeSlantedFace(coneBottom10, coneBottom00, coneTop00, coneTop10, 0, 0, 1, 1),
            // south
            makeSlantedFace(coneBottom01, coneBottom11, coneTop11, coneTop01, 0, 0, 1, 1)
        );
    }

    // Note: all 4 vertices must be coplanar; vectors 12 and 14 are used to compute the normal
    private static Quad makeSlantedFace(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3, Vector3f vertex4, float u1, float v1, float u2, float v2) {
        Vector3f normal = Quad.computeNormal(new Vector3f(vertex2).sub(vertex1), new Vector3f(vertex4).sub(vertex1));
        return new Quad(vertex1, vertex2, vertex3, vertex4, u1, v1, u2, v2, normal);
    }

    public ConeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(ConeEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.scale(0.0625f, 0.0625f, 0.0625f);
        var vertexConsumer = STONE_TEXTURE.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        var matrix = matrices.peek();
        for (Quad quad : QUADS) {
            quad.render(matrix, vertexConsumer, state.color, light);
        }
        matrices.pop();
        super.render(state, matrices, vertexConsumers, light);
    }

    @Override
    public ConeEntityRenderState createRenderState() {
        return new ConeEntityRenderState();
    }

    @Override
    public void updateRenderState(ConeEntity entity, ConeEntityRenderState state, float tickProgress) {
        super.updateRenderState(entity, state, tickProgress);
        state.color = entity.getColor();
    }

    private record Quad(
        float x1, float y1, float z1,
        float x2, float y2, float z2,
        float x3, float y3, float z3,
        float x4, float y4, float z4,
        float u1, float v1,
        float u2, float v2,
        Vector3f normal
    ) {
        Quad(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3, Vector3f vertex4, float u1, float v1, float u2, float v2, Vector3f normal) {
            this(
                vertex1.x, vertex1.y, vertex1.z,
                vertex2.x, vertex2.y, vertex2.z,
                vertex3.x, vertex3.y, vertex3.z,
                vertex4.x, vertex4.y, vertex4.z,
                u1, v1, u2, v2,
                normal
            );
        }

        static Quad ofParallelogram(float offsetX, float offsetY, float offsetZ, float xa, float ya, float za, float xb, float yb, float zb, float u1, float v1, float u2, float v2) {
            var normal = computeNormal(xa, ya, za, xb, yb, zb);
            return new Quad(
                offsetX, offsetY, offsetZ,
                offsetX + xa, offsetY + ya, offsetZ + za,
                offsetX + xa + xb, offsetY + ya + yb, offsetZ + za + zb,
                offsetX + xb, offsetY + yb, offsetZ + zb,
                u1, v1,
                u2, v2,
                normal
            );
        }

        static Vector3f computeNormal(float xa, float ya, float za, float xb, float yb, float zb) {
            return new Vector3f(xa, ya, za).cross(xb, yb, zb).normalize();
        }

        static Vector3f computeNormal(Vector3f sideA, Vector3f sideB) {
            return computeNormal(sideA.x, sideA.y, sideA.z, sideB.x, sideB.y, sideB.z);
        }

        void render(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, DyeColor color, int light) {
            vertex(matrix, vertexConsumer, color, x1, y1, z1, u1, v1, light);
            vertex(matrix, vertexConsumer, color, x2, y2, z2, u2, v1, light);
            vertex(matrix, vertexConsumer, color, x3, y3, z3, u2, v2, light);
            vertex(matrix, vertexConsumer, color, x4, y4, z4, u1, v2, light);
        }

        private void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, DyeColor color, float x, float y, float z, float u, float v, int light) {
            vertexConsumer.vertex(matrix, x, y, z);
            vertexConsumer.color(color.getEntityColor());
            vertexConsumer.texture(u, v);
            vertexConsumer.overlay(OverlayTexture.DEFAULT_UV);
            vertexConsumer.light(light);
            vertexConsumer.normal(matrix, normal);
        }
    }
}
