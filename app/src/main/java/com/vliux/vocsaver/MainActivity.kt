package com.vliux.vocsaver

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.vliux.vocsaver.data.AppDatabase
import com.vliux.vocsaver.data.Word
import com.vliux.vocsaver.data.WordRepository
import com.vliux.vocsaver.ui.theme.VocSaverTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val db by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { WordRepository(db.wordDao()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedText = when (intent?.action) {
            Intent.ACTION_SEND -> intent.getStringExtra(Intent.EXTRA_TEXT)
            else -> null
        }
        if (sharedText != null) {
            lifecycleScope.launch {
                try {
                    repository.insert(Word(word = sharedText, added_ts = System.currentTimeMillis(), translation = null))
                    Toast.makeText(this@MainActivity, "Saved", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Failed to save: $e", Toast.LENGTH_SHORT).show()
                }
            }
        }
        setContent {
            VocSaverTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = sharedText ?: "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VocSaverTheme {
        Greeting("Android")
    }
}