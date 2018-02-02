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
        val xmlfolder = File("./app/src/main/res/", "layout/").apply { mkdirs() }
        File(xmlfolder, "generated_layout_file_dsl.xml").writeText(generatedXML())

    }

    private fun generateSourceSource(`package`: String, className: String) =
            """
        package $`package`

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Generated : Donot edit
 */
public class Generated$className extends AppCompatTextView {

    public UCTextView(Context context) {
        super(context);
        init(null);
    }

    public UCTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UCTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.UCTextView);
        String fontFamily = ta.getString(R.styleable.UCTextView_fontFamily);
        ta.recycle();
        if (isInEditMode()) {
            FontCacheUtils.init(getContext());
        }
        if (TextUtils.isEmpty(fontFamily)) {
            fontFamily = FontCacheUtils.TYPE_REGULAR;
        }
        Typeface typeFace = FontCacheUtils.getTypeFace(fontFamily);
        setTypeface(typeFace);
    }
}
        """.trimIndent()


    private fun generatedXML() =
            """




            """


}