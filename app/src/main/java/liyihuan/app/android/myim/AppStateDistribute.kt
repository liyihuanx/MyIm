package liyihuan.app.android.myim

import android.app.Activity
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList


object AppStateDistribute {


    private var mUserStateListeners = ArrayList<UserStateListener>()


    fun addListener(userStateListener: UserStateListener) {
        if (!mUserStateListeners.contains(userStateListener)) {
            mUserStateListeners.add(userStateListener)
        }
    }

    fun removeListener(userStateListener: UserStateListener) {
        if (mUserStateListeners.contains(userStateListener)) {
            mUserStateListeners.remove(userStateListener)
        }
    }

    fun onUserLogin(user: String) {
        mUserStateListeners.forEach {
            it.onUserLogin(user)
        }
    }

    fun onUserLoginOut(user: String) {
        mUserStateListeners.forEach {
            it.onUserLoginOut(user)
        }
    }

    fun onUserInfoChange() {
        mUserStateListeners.forEach {
            it.onUserInfoChange()
        }
    }
    private val userStack = Stack<String>()

    fun onUserStateChange(state: String) {
        // 1. 一个状态进入后，通知所有人
        mUserStateListeners.forEach {
            it.onUserStateChange(state)
        }
        // 2. 截取状态: 这样就规定，一个状态的结束，必须是用[ XXXX_FINHSH ]为规范
        val substringBefore = state.substringBefore("_")

        // 3. 堆栈有截取后对应的状态pop一个，回退到上一状态，否则push状态
        if (userStack.contains(substringBefore)){
            userStack.pop()
        } else {
            userStack.push(state)
        }
        Log.d("QWER", "当前状态堆栈中有: $userStack")

    }

    /**
     * 推出所有状态，恢复到初始状态
     */
    fun popToStateBegin(){
        repeat(userStack.size - 1) {
            userStack.pop()
        }

        mUserStateListeners.forEach {
            it.onUserStateChange(userStack.peek())
        }

        Log.d("QWER", "当前状态堆栈中有: $userStack")
    }

    /**
     * 推出该状态前的所有状态
     */
    fun popToState(state: String){
        repeat(userStack.size){
            if (userStack.peek() == state){
                return@repeat
            } else {
                userStack.pop()
            }
        }

        mUserStateListeners.forEach {
            it.onUserStateChange(userStack.peek())
        }

        Log.d("QWER", "当前状态堆栈中有: $userStack")

    }



    interface UserStateListener {
        fun onUserLogin(user: String) {}
        fun onUserLoginOut(user: String) {}
        fun onUserInfoChange() {}
        fun onUserStateChange(state: String) {}
    }
}