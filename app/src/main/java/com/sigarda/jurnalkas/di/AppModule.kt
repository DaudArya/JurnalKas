package com.sigarda.jurnalkas.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sigarda.jurnalkas.data.repository.FirebaseAuthenticator
import com.sigarda.jurnalkas.data.repository.FirebaseFirestoreDatabase
import com.sigarda.jurnalkas.data.repository.impl.FirebaseAuthenticatorImpl
import com.sigarda.jurnalkas.data.repository.impl.FirebaseFirestoreDatabaseImpl
import com.sigarda.jurnalkas.model.usecase.UseCases
import com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.forgot_password.ForgotPasswordImpl
import com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.get_current_user.GetCurrentUserImpl
import com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.get_current_user_info.GetCurrentUserInfoImpl
import com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.sign_in.SignInImpl
import com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.sign_out.SignOutImpl
import com.sigarda.jurnalkas.model.usecase.firebase_auth_usecase.sign_up.SignUpImpl
import com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.delete_budget.DeleteBudgetImpl
import com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.get_budget_from_firestore.GetBudgetFromFirestoreImpl
import com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.save_budget.SaveBudgetImpl
import com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.save_user.SaveUserImpl
import com.sigarda.jurnalkas.model.usecase.firebase_firestore_usecase.update_budget.UpdateBudgetImpl
import com.sigarda.jurnalkas.wrapper.TransactionMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseUser() = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseAuthenticator(auth: FirebaseAuth): FirebaseAuthenticator {
        return FirebaseAuthenticatorImpl(auth)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase(firestore: FirebaseFirestore): FirebaseFirestoreDatabase {
        return FirebaseFirestoreDatabaseImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideUseCases(
        auth: FirebaseAuthenticator,
        firestore: FirebaseFirestoreDatabase,
        mapper: TransactionMapper
    ): UseCases {
        return UseCases(
            getCurrentUser = GetCurrentUserImpl(auth = auth),
            signIn = SignInImpl(auth = auth),
            signOut = SignOutImpl(auth = auth),
            signUp = SignUpImpl(auth = auth),
            getCurrentUserInfo = GetCurrentUserInfoImpl(auth = auth),
            forgotPassword = ForgotPasswordImpl(auth = auth),
            getBudgetFromFirestore = GetBudgetFromFirestoreImpl(
                firestore = firestore,
                auth = auth,
                mapper = mapper
            ),
            saveBudget = SaveBudgetImpl(firestore = firestore),
            saveUser = SaveUserImpl(firestore = firestore),
            deleteBudget = DeleteBudgetImpl(firestore = firestore),
            updateBudget = UpdateBudgetImpl(firestore = firestore)
        )
    }

    @Provides
    @Singleton
    fun provideBudgetMapper(): TransactionMapper{
        return TransactionMapper()
    }
}