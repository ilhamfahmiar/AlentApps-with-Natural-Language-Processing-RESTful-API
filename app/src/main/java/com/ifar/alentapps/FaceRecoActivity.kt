package com.ifar.alentapps

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.*
import android.media.ExifInterface
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.*
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.nnapi.NnApiDelegate
import org.tensorflow.lite.support.common.FileUtil
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.math.floor

class FaceRecoActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    // Initialize the MLKit FaceDetector
    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .build()
    private val firebaseFaceDetector = FaceDetection.getClient(realTimeOpts)
    private lateinit var textToSpeech: TextToSpeech
    // UI elements
    private lateinit var sampleImageView : ImageView
    private lateinit var infoTextView : TextView
    private lateinit var ageOutputTextView : TextView
//    private lateinit var genderOutputTextView : TextView
    private lateinit var inferenceSpeedTextView : TextView
    private lateinit var resultsLayout : ConstraintLayout
    private lateinit var progressDialog : ProgressDialog
    // CoroutineScope in which we'll run our coroutines.
    private val coroutineScope = CoroutineScope( Dispatchers.Main )

    // For reading the full-sized picture
    private val requestimagepicture = 101
    private val requestimageselect = 102
    private lateinit var currentPhotoPath : String

    // TFLite interpreters for both the models
    private lateinit var ageModelInterpreter: Interpreter
//    lateinit var genderModelInterpreter: Interpreter
    private lateinit var ageEstimationModel: AgeEstimationModel
//    private lateinit var genderClassificationModel: GenderClassificationModel
    // Boolean values to check for NNAPI and Gpu Delegates
    private var useNNApi : Boolean = false
    private var useGpu : Boolean = false
    private val compatList = CompatibilityList()

    // Default model filename
    private var modelFilename = arrayOf( "model_age_q.tflite")

    private val shift = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_reco)

        // Initialize the UI elements
        sampleImageView = findViewById(R.id.sample_input_imageview)
        infoTextView = findViewById( R.id.info_textView )
        ageOutputTextView = findViewById( R.id.age_output_textView )
//        genderOutputTextView = findViewById( R.id.gender_output_textview )
        resultsLayout = findViewById( R.id.results_layout )
        inferenceSpeedTextView = findViewById( R.id.inference_speed_textView )

        //Text to Speech
        textToSpeech = TextToSpeech(this@FaceRecoActivity, this@FaceRecoActivity)

        // A ProgressDialog to notify the user that the images are being processed.
        progressDialog = ProgressDialog( this )
        progressDialog.setCancelable( false )
        progressDialog.setMessage( "Searching for faces ...")

        showModelInitDialog()
    }

    // `onClick` method for R.id.button
    fun openCamera(v: View) {
        dispatchTakePictureIntent()
    }

    // `onClick` method for R.id.button2
    fun selectImage(v: View) {
        dispatchSelectPictureIntent()
    }

    // `onClick` method for R.id.reinitialize_button
    fun reInitModel(v: View) {
        showModelInitDialog()
    }

    private fun showModelInitDialog() {

        // Check for NNAPI and GPUDelegate compatibility.
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
            useNNApi = true
        }
        if(compatList.isDelegateSupportedOnThisDevice){
            useGpu = true
        }

        val options = Interpreter.Options().apply {
            if (useGpu) {
                addDelegate(GpuDelegate( compatList.bestOptionsForThisDevice ) )
            }
            if ( useNNApi ) {
                addDelegate(NnApiDelegate())
            }
        }
        // Initialize the models in a coroutine.
        coroutineScope.launch {
            initModels(options)
        }
    }

    // Suspending function to initialize the TFLite interpreters.
    private suspend fun initModels(options: Interpreter.Options) = withContext( Dispatchers.Default ) {
        ageModelInterpreter = Interpreter(FileUtil.loadMappedFile( applicationContext , modelFilename[0]), options )
        withContext( Dispatchers.Main ){
            ageEstimationModel = AgeEstimationModel().apply {
                interpreter = ageModelInterpreter
            }

            // Notify the user once the models have been initialized.
            Toast.makeText( applicationContext , "Models initialized." , Toast.LENGTH_LONG ).show()
        }
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
        ageModelInterpreter.close()
//        genderModelInterpreter.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // If the user opened the camera
        if ( resultCode == RESULT_OK && requestCode == requestimagepicture ) {
            // Get the full-sized Bitmap from `currentPhotoPath`.
            var bitmap = BitmapFactory.decodeFile( currentPhotoPath )
            val exifInterface = ExifInterface( currentPhotoPath )
            bitmap =
                when (exifInterface.getAttributeInt( ExifInterface.TAG_ORIENTATION , ExifInterface.ORIENTATION_UNDEFINED )) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap( bitmap , 90f )
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap( bitmap , 180f )
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap( bitmap , 270f )
                    else -> bitmap
                }
            progressDialog.show()
            // Pass the clicked picture to `detectFaces`.
            detectFaces( bitmap!! )
        }
        // if the user selected an image from the gallery
        else if ( resultCode == RESULT_OK && requestCode == requestimageselect ) {
            val inputStream = contentResolver.openInputStream( data?.data!! )
            val bitmap = BitmapFactory.decodeStream( inputStream )
            inputStream?.close()
            progressDialog.show()
            // Pass the clicked picture to `detectFaces`.
            detectFaces( bitmap!! )
        }
    }

    private fun detectFaces(image: Bitmap) {
        val inputImage = InputImage.fromBitmap(image, 0)
        // Pass the clicked picture to MLKit's FaceDetector.
        firebaseFaceDetector.process(inputImage)
            .addOnSuccessListener { faces ->
                if ( faces.size != 0 ) {
                    // Set the cropped Bitmap into sampleImageView.
                    sampleImageView.setImageBitmap(cropToBBox(image, faces[0].boundingBox))
                    // Launch a coroutine
                    coroutineScope.launch {

                        // Predict the age and the gender.
                        val age = ageEstimationModel.predictAge(cropToBBox(image, faces[0].boundingBox))
//                        val gender = genderClassificationModel.predictGender(cropToBBox(image, faces[0].boundingBox))

                        // Show the inference time to the user via `inferenceSpeedTextView`.
                        inferenceSpeedTextView.text = "Age Detection model inference time : ${ageEstimationModel.inferenceTime} ms"

                        // Show the final output to the user.
                        ageOutputTextView.text = floor( age.toDouble() ).toInt().toString()
                        val ageOut = floor( age.toDouble() ).toInt()

                        val nama = "Silakan masukan nama anda."
                        speakOut(nama)

                        val dataAge = Intent(this@FaceRecoActivity, Popupfaceactivity::class.java)
                        dataAge.putExtra("AgeOut", ageOut)
                        startActivity(dataAge)

//                        genderOutputTextView.text = if ( gender[ 0 ] > gender[ 1 ] ) { "Male" } else { "Female" }
                        resultsLayout.visibility = View.VISIBLE
                        infoTextView.visibility = View.GONE
                        progressDialog.dismiss()
                    }
                }
                else {
                    // Show a dialog to the user when no faces were detected.
                    progressDialog.dismiss()
                    val dialog = AlertDialog.Builder( this ).apply {
                        title = "No Faces Found"
                        setMessage( "We could not find any faces in the image you just clicked. " +
                                "Try clicking another image or improve the lightning or the device rotation." )
                        setPositiveButton( "OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        setCancelable( false )
                        create()
                    }
                    dialog.show()
                }


            }
    }

    private fun cropToBBox(image: Bitmap, bbox: Rect) : Bitmap {
        return Bitmap.createBitmap(
            image,
            bbox.left - 0 * shift,
            bbox.top + shift,
            bbox.width() + 0 * shift,
            bbox.height() + 0 * shift
        )
    }


    // Create a temporary file, for storing the full-sized picture taken by the user.
    private fun createImageFile() : File {
        val imagesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("image", ".jpg", imagesDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    // Dispatch an Intent which opens the gallery application for the user.
    private fun dispatchSelectPictureIntent() {
        val selectPictureIntent = Intent( Intent.ACTION_OPEN_DOCUMENT ).apply {
            type = "image/*"
            addCategory( Intent.CATEGORY_OPENABLE )
        }
        startActivityForResult( selectPictureIntent , requestimageselect )
    }

    // Dispatch an Intent which opens the camera application for the user.
    // The code is from -> https://developer.android.com/training/camera/photobasics#TaskPath
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if ( takePictureIntent.resolveActivity(packageManager) != null ) {
            val photoFile: File? = try {
                createImageFile()
            }
            catch (ex: IOException) {
                null
            }
            photoFile?.also {
                val photoURI = FileProvider.getUriForFile(
                    this,
                    "com.ifar.alentapps", it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, requestimagepicture)
            }
        }
    }


    private fun rotateBitmap(original: Bitmap, degrees: Float): Bitmap? {
        val matrix = Matrix()
        matrix.preRotate(degrees)
        return Bitmap.createBitmap(original, 0, 0, original.width, original.height, matrix, true)
    }

    private fun speakOut(data:String) {
        textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            textToSpeech.language = Locale("id","ID")

            val salam = "Halo, Selamat Datang di fitur Face Recognition.. Silakan Memulai mengambil foto"
            speakOut(salam)
        }else{
            Toast.makeText(applicationContext, "Gagal Coba Lagi!", Toast.LENGTH_SHORT).show()
        }
    }
}