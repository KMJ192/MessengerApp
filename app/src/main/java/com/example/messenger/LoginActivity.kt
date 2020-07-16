package com.example.messenger

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class LoginActivity : AppCompatActivity() {
    //파이어베이스
    var auth : FirebaseAuth? = null
    //구글 로그인 Client
    var googleSignInClient : GoogleSignInClient? = null
    var GOOGLE_LOGIN_CODE = 9001
    var callbackManager : CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

        //구글 로그인 옵션
        var gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //옵션값을 GoogleSignInClient에 입력
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //구글 로그인하는 Button 클릭시 GoogleLogin Function Call
        google_signin_button.setOnClickListener{
            GoogleLogin()
        }
        //페이스북 로그인 하는 Button 클릭시 FacebookLogin Function Call
        facebook_signin_button.setOnClickListener{
            FacebookLogin()
        }

        //PrintHashKey()
        callbackManager = CallbackManager.Factory.create()
    }

    //구글 Login을 위한 Function
    fun GoogleLogin(){
        var signInIntent = googleSignInClient?.signInIntent
        //구글로부터 ID/PW값을 받아오기 위함
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }

    //Facebook HashKey Get
    fun PrintHashKey() {
        try {
            val info = packageManager
                .getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("Tag", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("Tag", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("Tag", "printHashKey()", e)
        }
    }

    fun FacebookLogin(){
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("프로필 이미지", "이메일"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
                //페이스 북 로그인 성공 -> 페이스북 data를 firebase로 연결
                override fun onSuccess(result: LoginResult?) {
                    //
                    HandleFacebookAccessToken(result?.accessToken)
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {

                }

            })
    }

    fun HandleFacebookAccessToken(token : AccessToken?){
        var credential = FacebookAuthProvider.getCredential(token?.token!!)
        //firebase로 로그인 Data를 넘겨줌
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener{
                    task -> if(task.isSuccessful) {
                //ID Password가 일치할 경우, MainPage로 이동하는 Function 호출
                MoveMainPage(task.result?.user)
            }else{
                //ID Password가 일치하지 않을 경우, 실패 Message 출력
                Toast.makeText(this,
                    "ID와 PW가 맞지 않습니다. 다시 입력해주세요",
                    Toast.LENGTH_LONG
                ).show()

                Toast.makeText(
                    this,
                    task.exception?.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    //로그인 Data를 가져오는 override Function
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_LOGIN_CODE){
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result != null) {
                if(result.isSuccess){
                    var account = result.signInAccount
                    FirebaseGoogleAuth(account)
                }
            }else{
                Toast.makeText(
                    this,
                    "구글 로그인 실패",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun FirebaseGoogleAuth(account : GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener{
                task -> if(task.isSuccessful) {
                    //ID Password가 일치할 경우, MainPage로 이동하는 Function 호출
                    MoveMainPage(task.result?.user)
                }else{
                    //ID Password가 일치하지 않을 경우, 실패 Message 출력
                    Toast.makeText(this,
                        "ID와 PW가 맞지 않습니다. 다시 입력해주세요",
                        Toast.LENGTH_LONG
                    ).show()

                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    //Login을 위한 Function
    fun SinginEmail(){
        auth?.signInWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )?.addOnCompleteListener{
            task -> if(task.isSuccessful) {
                //ID Password가 일치할 경우, MainPage로 이동하는 Function 호출
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
            startActivity(Intent(
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
}