package com.danser.meetup_2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import java.math.BigDecimal

class Imperative : AppCompatActivity() {

    interface IUserManager {
        fun getUser(): User
        fun getUserBalance(userId: String): BigDecimal
        fun updateUserBalance(userId: String, balance: BigDecimal)
    }

    private val manager: IUserManager = UserManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = manager.getUser()
        val balance = manager.getUserBalance(user.id)
        val newBalance = balance - payment
        manager.updateUserBalance(user.id, newBalance)

        view.showSnack("Balance has been updated to $newBalance")
    }


    class User(
        val id: String
    )

    class UserManager: IUserManager {
        override fun getUser(): User {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun updateUserName(userId: String, name: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun updateUserBalance(userId: String, balance: BigDecimal) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}
