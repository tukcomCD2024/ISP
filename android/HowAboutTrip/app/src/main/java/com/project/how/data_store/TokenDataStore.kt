package com.project.how.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.project.how.data_class.Tokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object TokenDataStore {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")

    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

    suspend fun saveTokens(context: Context, tokens : Tokens){
        context.dataStore.edit {
            it[ACCESS_TOKEN] = tokens.accessToken
            it[REFRESH_TOKEN] = tokens.refreshToken
        }
    }
    fun getTokens(context: Context): Flow<Tokens?> = flow {
        context.dataStore.data.collect {
            val accessToken = it[ACCESS_TOKEN]
            val refreshToken = it[REFRESH_TOKEN]
            if(accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()){
                this.emit(null)
            }else{
                this.emit(Tokens(accessToken, refreshToken))
            }
        }
    }
}