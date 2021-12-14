package com.example.cameraapp5

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Build a button to open camera to take picture
        findViewById<Button>(R.id.button).setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    //Get Image thumbnail from camera guide

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

    // get bitmap from MLKit
            findViewById<ImageView>(R.id.imageView).setImageBitmap(imageBitmap)
            val image = InputImage.fromBitmap(imageBitmap, 0)
            // get label
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            var labelResults  = " "
            labeler.process(image)
                .addOnSuccessListener { labels ->
                        for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence.toString()
                        val index = label.index
                        Log.i("Tina", "Image processed successfully")
                            var textView= findViewById<TextView>(R.id.textView)
                                textView.text=labelResults
                                labelResults += "$text : $confidence\n"

                    }

                    // Task completed successfully
                    // ...
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                    Log.i("Tina", "Error Processing")
                }

        }
    }
}