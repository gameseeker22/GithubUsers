package com.example.githubusers.utils

/**
 *  data class for handling status
 */
data class Network private constructor(val status: Status) {

    companion object {
        val LOADED = Network(Status.SUCCESS)
        val LOADING = Network(Status.LOADING)
        val FAILED = Network(Status.FAILED)
    }
}