package com.intec.template.di

import android.content.Context
import com.ainirobot.coreservice.client.speech.SkillApi
import com.intec.template.robot.RobotConnectionService
import com.intec.template.robot.RobotManager
import com.intec.template.robot.SkillApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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

}