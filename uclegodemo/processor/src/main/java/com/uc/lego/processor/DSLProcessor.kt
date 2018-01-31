package com.uc.lego.processor
import java.io.File

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class DSLProcessor : AbstractProcessor() {

    private val kaptKotlinGeneratedOption = "kapt.kotlin.generated"
    private lateinit var kaptKotlinGenerated: File

    private val mainAnnotation = "com.uc.lego.api.DSLAnnotation"

    override fun getSupportedAnnotationTypes() = setOf(mainAnnotation)

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        kaptKotlinGenerated = File(processingEnv.options[kaptKotlinGeneratedOption])
    }

    override fun process(annotations: Set<TypeElement>, env: RoundEnvironment): Boolean {
        val annotation = annotations.firstOrNull { it.toString() == mainAnnotation } ?: return false
        for (element in env.getElementsAnnotatedWith(annotation)) {
            val className = element.simpleName.toString()
            val `package` = processingEnv.elementUtils.getPackageOf(element).toString()
            generateClass(className, `package`)
        }
        return true
    }

    private fun generateClass(className: String, `package`: String) {
        val source = generateSourceSource(`package`, className)
        val relativePath = `package`.replace('.', File.separatorChar)
        val folder = File(kaptKotlinGenerated, relativePath).apply { mkdirs() }
        File(folder, "Generated$className.kt").writeText(source)
        //val xmlfolder = File("./app/src/main/res/", "layout/").apply { mkdirs() }
        //File(xmlfolder, "generated_layout_file.xml").writeText(generatedXML())

    }

    private fun generateSourceSource(`package`: String, className: String) =
            """
        package $`package`

        class Generated$className {

        }

        """.trimIndent()


    /*private fun generatedXML() =
            """<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android"
android:layout_width="match_parent"
android:layout_height="match_parent"
android:orientation="vertical">


<FrameLayout
android:id=@+id/container
android:layout_width="match_parent"
android:layout_height="wrap_content"/>

</LinearLayout>
            """*/


}