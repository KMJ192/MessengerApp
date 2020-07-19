package com.example.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        signup_button.setOnClickListener{
            Signup()
        }
        signup_before.setOnClickListener{
            MoveSignupPage()
        }
    }

    //회원 가입을 위한 Function
    fun Signup(){
        auth?.createUserWithEmailAndPassword(
            signup_email.text.toString(),
            signup_password.text.toString()
            )?.addOnCompleteListener{
                    task -> if(task.isSuccessful) {
                //회원가입 성공
                MoveLoginPage(task.result?.user)
            }else if(!task.exception?.message.isNullOrEmpty()){
                //회원가입 실패
                Toast.makeText(
                    this,
                    task.exception?.message,
                    Toast.LENGTH_LONG
                ).show()
            }else{
                //회원가입과 에러가 아닐 경우, 로그인 화면으로 이동
                //Login Function
                startActivity(
                    Intent(
                        this,
                        LoginActivity::class.java)
                    )
            }
        }
    }

    //회원가입 성공 후 Main페이지로 이동
    fun MoveLoginPage(user : FirebaseUser?){
        if(user != null){
            startActivity(Intent(
                this,
                LoginActivity::class.java)
            )
        }
    }
    //회원가입 페이지로 이동
    fun MoveSignupPage(){
        startActivity(Intent(
            this,
            LoginActivity::class.java))
    }
}