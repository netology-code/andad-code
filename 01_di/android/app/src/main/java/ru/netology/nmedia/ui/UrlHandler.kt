package ru.netology.nmedia.ui

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import ru.netology.nmedia.R
import javax.inject.Inject

class UrlHandler @Inject constructor(private val navController: NavController) {

    fun navigate() {
        navController.navigate(
            R.id.action_feedFragment_to_newPostFragment,
            bundleOf("test" to "arg")
        )
    }
}