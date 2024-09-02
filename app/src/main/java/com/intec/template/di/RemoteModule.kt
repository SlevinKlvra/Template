package com.intec.template.di

import android.content.Context
import android.content.SharedPreferences
import com.ainirobot.coreservice.client.speech.SkillApi
import com.intec.template.api.IntecApi
import com.intec.template.api.NubimedApi
import com.intec.template.chatGPT.ChatGPTManager
import com.intec.template.preferences.PreferencesRepository
import com.intec.template.robot.RobotConnectionService
import com.intec.template.robot.RobotManager
import com.intec.template.robot.SkillApiService
import com.intec.template.util.Constants.BASE_URL
import com.intec.template.util.Constants.BASE_URL_BACK
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun provideRobotManager(robotConnectionService: RobotConnectionService): RobotManager {
        return RobotManager(robotConnectionService) // Asumiendo que RobotManager no tiene dependencias en su constructor
    }

    @Singleton
    @Provides
    fun provideRobotConnectionService(@ApplicationContext context: Context): RobotConnectionService {
        return RobotConnectionService(context, skillApi = SkillApi())
    }

    @Singleton
    @Provides
    fun provideSkillApi(@ApplicationContext context: Context): SkillApiService {
        // Suponiendo que SkillApi tiene un método estático `getInstance()` y requiere inicialización
        return SkillApiService(context)
    }

    @Provides
    @Singleton
    fun provideApi(): NubimedApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NubimedApi::class.java)
    }
    @Provides
    @Singleton
    fun provideIntecApi(): IntecApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_BACK)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IntecApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun providePreferenceRepository(sharedPreferences: SharedPreferences) : PreferencesRepository {
        return PreferencesRepository(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideChatGPTManager(): ChatGPTManager {
        return ChatGPTManager("")
    }

}