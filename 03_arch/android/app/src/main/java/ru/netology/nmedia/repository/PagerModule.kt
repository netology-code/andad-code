package ru.netology.nmedia.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.entity.PostEntity

@InstallIn(SingletonComponent::class)
@Module
class PagerModule {

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    fun providePager(
        postRemoteMediator: PostRemoteMediator,
        postDao: PostDao,
    ): Pager<Int, PostEntity> = Pager(
        config = PagingConfig(pageSize = 25),
        remoteMediator = postRemoteMediator,
        pagingSourceFactory = postDao::pagingSource,
    )
}
