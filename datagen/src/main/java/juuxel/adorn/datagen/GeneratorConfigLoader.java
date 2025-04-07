package juuxel.adorn.datagen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class GeneratorConfigLoader {
    public static GeneratorConfig read(Path path) throws IOException {
        Document document;
        try (var in = Files.newInputStream(path)) {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

        var root = document.getDocumentElement();
        if (!Tags.ROOT.equals(root.getTagName())) {
            throw new IllegalArgumentException("Generator config root element must be %s, found %s".formatted(Tags.ROOT, root.getTagName()));
        }

        var woods = getElementStreamByTagName(root, Tags.WOOD)
            .map(GeneratorConfigLoader::readWood)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        var stones = getElementStreamByTagName(root, Tags.STONE)
            .map(GeneratorConfigLoader::readStone)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        var colors = Boolean.parseBoolean(root.getAttribute(Attributes.COLOR)) ? ColorMaterial.values() : new ColorMaterial[0];
        var conditionType = ConditionType.parse(root.getAttribute(Attributes.CONDITION_TYPE));
        if (conditionType == null) throw new IllegalArgumentException("Unknown condition type in %s: %s".formatted(path, root.getAttribute(Attributes.CONDITION_TYPE)));
        var rootReplacements = getReplacements(root);
        var overlay = readOverlay(root);
        return new GeneratorConfig(
            woods, stones,
            Arrays.stream(colors)
                .map(material -> new GeneratorConfig.MaterialEntry<>(material, Set.of(), Map.of()))
                .collect(Collectors.toCollection(LinkedHashSet::new)),
            conditionType,
            rootReplacements,
            overlay
        );
    }

    private static Map<String, String> getReplacements(Element element) {
        return TemplateContext.buildSubstitutions(context -> {
            getElementStreamByTagName(element, Tags.REPLACE).forEach(replaceElement -> {
                var key = replaceElement.getAttribute(Attributes.KEY);
                var value = replaceElement.getAttribute(Attributes.WITH);
                var id = Boolean.parseBoolean(replaceElement.getAttribute(Attributes.ID));
                if (id) {
                    context.setId(key, value);
                } else {
                    context.set(key, value);
                }
            });
        });
    }

    private static <M extends Material> GeneratorConfig.MaterialEntry<M> readMaterialEntry(Element element, M material) {
        var exclude = getElementStreamByTagName(element, Tags.EXCLUDE)
            .map(excludeElement -> excludeElement.getAttribute(Attributes.GENERATOR))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        var replace = getReplacements(element);
        return new GeneratorConfig.MaterialEntry<>(material, exclude, replace);
    }

    private static GeneratorConfig.MaterialEntry<WoodMaterial> readWood(Element element) {
        var id = Id.parse(element.getAttribute(Attributes.ID));
        // parseBoolean is luckily false by default (so also for empty strings)
        var fungus = Boolean.parseBoolean(element.getAttribute(Attributes.FUNGUS));
        var nonFlammable = Boolean.parseBoolean(element.getAttribute(Attributes.NON_FLAMMABLE));
        return readMaterialEntry(element, new SimpleWoodMaterial(id, fungus, nonFlammable));
    }

    private static GeneratorConfig.MaterialEntry<StoneMaterial> readStone(Element element) {
        var id = Id.parse(element.getAttribute(Attributes.ID));
        var bricks = Boolean.parseBoolean(element.getAttribute(Attributes.BRICKS));
        var sidedTexture = Boolean.parseBoolean(element.getAttribute(Attributes.SIDED_TEXTURE));
        return readMaterialEntry(element, new StoneMaterial(id, bricks, sidedTexture));
    }

    @SuppressWarnings("unchecked")
    private static Stream<Element> getElementStreamByTagName(Element parent, String tag) {
        var elements = parent.getElementsByTagName(tag);
        return (Stream<Element>) (Object) IntStream.range(0, elements.getLength())
            .mapToObj(elements::item)
            .filter(node -> node.getNodeType() == Node.ELEMENT_NODE);
    }

    private static Overlay readOverlay(Element parent) {
        var overlayElements = parent.getElementsByTagName(Tags.OVERLAY);
        return switch (overlayElements.getLength()) {
            case 0 -> null;
            case 1 -> {
                var element = (Element) overlayElements.item(0);
                var directory = element.getAttribute(Attributes.DIRECTORY);
                var modId = element.getAttribute(Attributes.MOD_ID);
                yield new Overlay(directory, modId);
            }
            default -> throw new IllegalArgumentException("There should be at most one <overlay> element in a data config");
        };
    }

    private static final class Tags {
        static final String ROOT = "data_generators";
        static final String WOOD = "wood";
        static final String STONE = "stone";
        static final String REPLACE = "replace";
        static final String EXCLUDE = "exclude";
        static final String OVERLAY = "overlay";
    }

    private static final class Attributes {
        static final String CONDITION_TYPE = "condition_type";
        static final String COLOR = "color";
        static final String ID = "id";
        static final String FUNGUS = "fungus";
        static final String NON_FLAMMABLE = "non_flammable";
        static final String BRICKS = "bricks";
        static final String SIDED_TEXTURE = "sided_texture";
        static final String GENERATOR = "generator";
        static final String KEY = "key";
        static final String WITH = "with";
        static final String DIRECTORY = "directory";
        static final String MOD_ID = "mod_id";
    }
}
