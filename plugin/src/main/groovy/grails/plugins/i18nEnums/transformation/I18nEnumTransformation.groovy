package grails.plugins.i18nEnums.transformation

import grails.plugins.i18nEnums.DefaultNameCase
import grails.plugins.i18nEnums.traits.I18nEnumTrait
import groovy.transform.CompilationUnitAware
import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.MapEntryExpression
import org.codehaus.groovy.ast.expr.MapExpression
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.control.CompilationUnit
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.trait.TraitComposer

import java.lang.reflect.Modifier

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
@CompileStatic
class I18nEnumTransformation implements ASTTransformation, CompilationUnitAware {

    /**
     * Name of the static field this transformation adds to an annotated enum to carry the
     * annotation's members. I18nEnumTrait reads it reflectively at runtime.
     *
     * A field rather than a method on purpose: the trait cannot declare a member of the same
     * name (it would shadow the enum's own on any call made from inside the trait), and unlike
     * trait method composition, a plain static field is not subject to Groovy version changes.
     */
    public static final String AST_CONFIG_FIELD = '$i18nEnumASTConfig'

    CompilationUnit compilationUnit

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
            throw new RuntimeException("Internal error: wrong types: ${nodes[0].class} / ${nodes[1].class}")
        }

        AnnotationNode annotationNode = nodes[0] as AnnotationNode
        def annotatedNode = nodes[1]

        if (annotatedNode instanceof ClassNode) {
            ClassNode classNode = annotatedNode as ClassNode
            addTrait(classNode, source)
            addi18nEnumASTConfig(classNode, annotationNode)
        }
    }

    private static void addi18nEnumASTConfig(ClassNode classNode, AnnotationNode annotationNode) {
        Map<String, Expression> annotationConfig = extractAnnotationConfig(annotationNode)
        if (!annotationConfig || classNode.getDeclaredField(AST_CONFIG_FIELD)) {
            return
        }

        classNode.addField(
                AST_CONFIG_FIELD,
                Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL,
                ClassHelper.MAP_TYPE.plainNodeReference,
                buildMapExpression(annotationConfig)
        )
    }

    /**
     * Add tje Trait that implements MessageSourceResolvable
     * @param classNode
     * @param source
     */
    private void addTrait(ClassNode classNode, SourceUnit source) {
        def clazz = ClassHelper.make(I18nEnumTrait)
        classNode.addInterface(clazz)
        TraitComposer.doExtendTraits(classNode, source, compilationUnit)
    }

    /**
     * Build a map expression from config
     * @param config
     * @return
     */
    private static MapExpression buildMapExpression(Map<String, Expression> config) {
        def mapEntryExpressions = config.collect { key, value ->
            createMapEntryExpression(key, value)
        }

        return new MapExpression(mapEntryExpressions)
    }

    private static MapEntryExpression createMapEntryExpression(String key, Expression value) {
        new MapEntryExpression(new ConstantExpression(key), value)
    }

    /**
     * Extract configuration from the annotation node
     * @param annotationNode
     * @return a config. Empty if no annotation values
     */
    private static Map<String, Expression> extractAnnotationConfig(AnnotationNode annotationNode) {
        Expression prefix = annotationNode.getMember('prefix')
        Expression postfix = annotationNode.getMember('postfix')
        Expression shortName = annotationNode.getMember('shortName')
        Expression defaultNameCase = annotationNode.getMember('defaultNameCase')

        Map<String, Expression> config = [:]

        if (prefix) {
            config.prefix = prefix
        }
        if (postfix) {
            config.postfix = postfix
        }
        if (shortName) {
            config.shortName = shortName
        }
        if (defaultNameCase && resolveDefaultNameCase(defaultNameCase) != DefaultNameCase.UNCHANGED) {
            config.defaultNameCase = defaultNameCase
        }
        return config
    }

    private static DefaultNameCase resolveDefaultNameCase(Expression expression) {
        if (expression instanceof PropertyExpression) {
            PropertyExpression pe = expression as PropertyExpression
            String enumConstant = pe.propertyAsString          // "UNCHANGED"

            // Then load the enum value if needed
            return DefaultNameCase.valueOf(enumConstant)
        }
    }
}
