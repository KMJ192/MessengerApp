package com.example.messenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.Model.User
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*



class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null

    var callbackManager : CallbackManager? = null

    //private val TAG : String = LoginActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        auth = FirebaseAuth.getInstance()

        //로그인 버튼 클릭시  SinginEmail Function Call
        email_login.setOnClickListener {
            if (email_edittext.text.toString() != "" && password_edittext.text.toString() != "") {
                //ID PW
                SinginEmail()
            } else {
                Toast.makeText(
                    this,
                    "ID와 Password를 입력하세요",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //회원 가입 버튼 클릭시 MoveSignupPage Function Call
        signup.setOnClickListener{
            MoveSignupPage()
        }

    }

    //Login을 위한 Function
    fun SinginEmail(){
        auth?.signInWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )?.addOnCompleteListener{
            task -> if(task.isSuccessful) {
                UserDataGet()
                //ID Password가 일치할 경우, MainPage로 이동하는 Function 호출(Login 성공)
                MoveMainPage(task.result?.user)
            }else{
                Toast.makeText(
                    this,
                    task.exception?.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //로그인 성공 후 MainPage로 이동하는 Function
    fun MoveMainPage(user : FirebaseUser?){
        if(user != null){
            startActivity(
                Intent(
                this@LoginActivity,
                MainActivity::class.java)
            )
        }
    }

    //회원가입 페이지로 이동
    fun MoveSignupPage(){
        startActivity(Intent(
            this@LoginActivity,
            SignUp::class.java))
    }

    fun UserDataGet(){
        //DB에 Data Input
        val uid = FirebaseAuth.getInstance().uid ?: ""
        //uid => 파이어베이스 Authentication
        //username => 이메일
        val user = User(uid, email_edittext.text.toString())
        val db = FirebaseFirestore.getInstance().collection("users")//collection이름을 "users"로 설정
        db.document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.d("DB유무", "DB 성공, uid : $uid")
            }
            .addOnFailureListener{
                Log.d("DB유무", "DB 실패, uid : $uid")
            }
    }
}