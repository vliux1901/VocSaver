package com.vliux.vocsaver

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.vliux.vocsaver.data.AppDatabase
import com.vliux.vocsaver.data.Word
import com.vliux.vocsaver.data.WordRepository
import com.vliux.vocsaver.ui.MainViewModel
import com.vliux.vocsaver.ui.MainViewModelFactory
import com.vliux.vocsaver.ui.theme.VocSaverTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val db by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { WordRepository(db.wordDao()) }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(repository)
    }

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
                    val words by viewModel.words.collectAsState(initial = emptyList())
                    WordList(words = words, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WordList(words: List<Word>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(words) { word ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = word.word,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
