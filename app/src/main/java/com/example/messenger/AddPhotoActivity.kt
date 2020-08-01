package com.example.messenger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.messenger.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.text.SimpleDateFormat
import java.util.Date

class AddPhotoActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 0
    var storage : FirebaseStorage? = null
    var photoUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        //storage 초기화
        storage = FirebaseStorage.getInstance()

        //앨범 open
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"

        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        //upload버튼을 눌렀을 경우
        profile_image_upload_button.setOnClickListener {
            contentUpload()
        }

    }

    //선택한 Image Get
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_FROM_ALBUM){
            if(resultCode == Activity.RESULT_OK){
                //사진을 선택했을 경우
                photoUri = data?.data
                upload_image.setImageURI(photoUri)

            }else{
                //취소했을 경우, activity를 닫아줌
                finish()
            }
        }
    }

    fun contentUpload(){
        //파일 이름 생성
        //Image파일의 이름에 key값으로 설정
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        var imageFileName = "IMAGE" + timestamp + "_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //FileUpload
        storageRef?.putFile(photoUri!!)?.
        addOnSuccessListener {
            Toast.makeText(this, getString(R.string.success), Toast.LENGTH_LONG).show()
        }
    }
}