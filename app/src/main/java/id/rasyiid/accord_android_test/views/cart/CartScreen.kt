package id.rasyiid.accord_android_test.views.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CartScreen() {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp, 24.dp, 16.dp, 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Cart Screen")
    }
}