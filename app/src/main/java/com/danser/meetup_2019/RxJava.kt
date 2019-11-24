package com.danser.meetup_2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import java.math.BigDecimal

class RxJava : AppCompatActivity() {

    interface IUserManager {
        fun getUser(): Single<User>
        fun getUserBalance(userId: String): Single<BigDecimal>
        fun updateUserBalance(userId: String, balance: BigDecimal): Completable
    }

    private val manager: IUserManager = UserManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager.getUser()
            .flatMapCompletable { user ->
                manager.getUserBalance(user.id)
                    .map { balance -> balance - cost }
                    .flatMap { balance -> updateUserBalance(user.id, balance) }
            }
            .subscribe(
                onSuccess = { view.showSnack("balance updated") }
                onError = ::processError
            )
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
