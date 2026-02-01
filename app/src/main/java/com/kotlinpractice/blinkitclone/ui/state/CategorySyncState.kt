package com.kotlinpractice.blinkitclone.ui.state

sealed class CategorySyncState {
    object Idle : CategorySyncState()
    object Loading : CategorySyncState()
    object Error : CategorySyncState()
}
