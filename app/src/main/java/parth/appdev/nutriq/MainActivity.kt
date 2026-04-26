package parth.appdev.nutriq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import dagger.hilt.android.AndroidEntryPoint
import parth.appdev.nutriq.presentation.navigation.NavGraph
import parth.appdev.nutriq.ui.theme.NutriQTheme
import parth.appdev.nutriq.ui.theme.Surface

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriQTheme {
                // Temporary UI (placeholder)
                Surface {
                    NavGraph()
                }
            }
        }
    }
}