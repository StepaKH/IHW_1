package infrastructure.auth.utils

import infrastructure.auth.entities.UserOptions
import infrastructure.auth.AuthServer

fun activateAuthentication(authServer: AuthServer, currentSession: UserOptions): UserOptions {
    var choice: String

    if (!authServer.tryToLogin(currentSession.userName, currentSession.password)) {
        while (true) {
            println()
            println("Session does not exist, try to log in or sign up")
            println("1) sign up")
            println("2) log in")
            print("Push button - ")
            choice = readln()
            println()

            if (choice == "1") {
                val data = inputUserData()
                if (authServer.signUp(data.first, data.second)) {
                    return UserOptions(data.first, data.second)
                } else {
                    println("User with this name is already exist")
                }
            } else if (choice == "2") {
                val data = inputUserData()
                if (authServer.tryToLogin(data.first, data.second)) {
                    return UserOptions(data.first, data.second)
                } else {
                    println("User does not exist")
                }
            } else {
                println("Invalid input")
            }
        }
    }
    return currentSession
}

fun inputUserData(): Pair<String, String> {
    print("Input user name: ")
    val name = readln()
    print("Input password: ")
    val password = readln()

    return name to password
}