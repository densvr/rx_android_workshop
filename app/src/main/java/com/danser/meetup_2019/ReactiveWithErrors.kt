package com.danser.meetup_2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import java.math.BigDecimal

class ReactiveWithErrors : AppCompatActivity() {

    interface IUserManager {
        fun getUser(onSuccess: (User) -> Unit, onError: (Throwable) -> Unit)
        fun getUserBalance(userId: String, onSuccess: (BigDecimal) -> Unit, onError: (Throwable) -> Unit)
        fun updateUserBalance(userId: String, balance: BigDecimal, onSuccess: () -> Unit, onError: (Throwable) -> Unit)
    }

    private val manager: IUserManager = UserManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager.getUser(
            onSuccess = { user ->
                manager.getUserBalance(
                    userId = user.id,
                    onSuccess = { balance ->
                        val newBalance = balance - payment
                        manager.updateUserBalance(
                            userId = user.id,
                            balance = newBalance,
                            onSuccess = { view.showSnack("balance updated") },
                            onError = ::processError
                        )
                    },
                    onError = ::processError
                )
            },
            onError = ::processError
        )
    }

    private fun processError(th: Throwable) { ... }


    class User(
        val id: String
    )

    class UserManager : IUserManager {
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
