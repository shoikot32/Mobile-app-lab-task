package com.university.newsapp.network

import com.university.newsapp.model.*
import retrofit2.http.*

interface ApiService {

    @GET("posts")
    suspend fun getAllPosts(): List<Post>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): Post

    @GET("posts/{id}/comments")
    suspend fun getCommentsByPost(@Path("id") postId: Int): List<Comment>

    @GET("users")
    suspend fun getAllUsers(): List<User>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User

    @GET("users/{id}/posts")
    suspend fun getPostsByUser(@Path("id") userId: Int): List<Post>
}