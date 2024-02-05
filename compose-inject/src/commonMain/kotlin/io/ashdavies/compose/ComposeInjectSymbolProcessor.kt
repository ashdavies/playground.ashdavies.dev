package io.ashdavies.compose

import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.Visibility
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

private const val REMEMBER_ANNOTATION = "io.ashdavies.compose.Remember"

internal class ComposeInjectSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val kspLogger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(REMEMBER_ANNOTATION).toList()
        if (symbols.isEmpty()) kspLogger.warn("[compose-inject] No symbols found")
        return symbols.filterNot { generateFactory(it) }
    }

    private fun generateFactory(element: KSAnnotated): Boolean {
        if (element !is KSFunctionDeclaration) return false

        val packageName = element.packageName.asString()
        val returnType = calculateReturnType(element)
        val returnTypeName = returnType
            .asType(emptyList())
            .toTypeName()

        val returnTypeVisibility = returnType
            .getVisibility()
            .toKModifier()

        if (returnTypeVisibility == null) {
            error("Cannot generate factory with non-obvious visibility")
        }

        val shortName = returnType.simpleName.getShortName()
        val fileName = "${shortName}Factory"

        kspLogger.warn("[compose-inject] Generating ${packageName}/${fileName}.kt for type $returnTypeName")

        val funSpec = FunSpec.builder("remember${shortName}")
            .addModifiers(returnTypeVisibility)
            .returns(returnTypeName)
            .addStatement("throw NotImplementedError()")
            .build()

        val fileSpec = FileSpec.builder(packageName, fileName)
            .addFunction(funSpec)
            .build()

        fileSpec.writeTo(
            codeGenerator = codeGenerator,
            aggregating = false,
            originatingKSFiles = listOfNotNull(element.containingFile),
        )

        return true
    }

    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return ComposeInjectSymbolProcessor(
                codeGenerator = environment.codeGenerator,
                kspLogger = environment.logger,
            )
        }
    }

}

private fun calculateReturnType(element: KSFunctionDeclaration): KSClassDeclaration {
    val type = requireNotNull(element.returnType) { "Function must have a return type" }
    val declaration = type.resolve().declaration

    if (declaration.getVisibility() == Visibility.PRIVATE) {
        error("Return type (${declaration.simpleName.getShortName()}) must not be private")
    }

    return declaration as KSClassDeclaration
}

private inline fun <reified T : Any> qualifiedName(): String {
    return requireNotNull(T::class.qualifiedName) {
        "Underlying class does not have a qualified name"
    }
}
