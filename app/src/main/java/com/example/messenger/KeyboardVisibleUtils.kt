package com.example.messenger

import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.Window

class KeyboardVisibleUtils(
    private val window : Window,
    private val onShowKeyboard : ((KeyboardHeight : Int) -> Unit)? = null,
    private val onHideKeyboard: (() -> Unit)? = null
)
{
    private val MIN_KEYBOARD_HEIGHT_PX = 150

    private val windowVisibleDisplayFrame = Rect()
    private var lastVisibleDecorViewHeight : Int = 0;

    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener{
        window.decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
        //현재화면의 height value
        val visibleDecorViewHeight = windowVisibleDisplayFrame.height()

        //키보드가 올라온 상태
        if(lastVisibleDecorViewHeight != 0) {
            if(lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX){
                //이전에 보여준 화면 height > 현재 화면 height + 최소 키보드 크기 값 -> 키보드가 enable
                onShowKeyboard?.invoke(window.decorView.height - windowVisibleDisplayFrame.bottom)
            }else if(lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight){
                //이전에 보여준 화면 heigth + 최소 키보드 크기 값 < 현재 화면 height -> 키보드 disable
                onHideKeyboard?.invoke()
            }
        }
        lastVisibleDecorViewHeight = visibleDecorViewHeight
    }

    init{
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun detachKeyboardListener(){
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }
}