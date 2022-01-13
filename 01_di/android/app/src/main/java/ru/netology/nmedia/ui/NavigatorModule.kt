package ru.netology.nmedia.ui

import android.app.Activity
import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import ru.netology.nmedia.R

@InstallIn(FragmentComponent::class)
@Module
class NavigatorModule {

    @FragmentScoped
    @Provides
    fun provideNavController(@ActivityContext context: Context): NavController =
        (context as Activity).findNavController(
            R.id.nav_host_fragment
        )
}
