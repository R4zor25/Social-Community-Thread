package hu.bme.aut.android.socialcommunitythread.util.tagrecommender

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

@ExperimentalGetImage class ImageAnalyzer : ImageAnalysis.Analyzer {

    fun analyze(bitmap: Bitmap) {
        //val mediaImage = imageProxy.image
        if (bitmap != null) {
            val image = InputImage.fromBitmap(bitmap, 0)
           // val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    for(label in labels)
                        Log.d("LABELS", label.text)
                }
                .addOnFailureListener { e ->
                    Log.d("LABELS", e.localizedMessage)
                }
        }
    }

    override fun analyze(image: ImageProxy) {
        TODO("Not yet implemented")
    }
}
