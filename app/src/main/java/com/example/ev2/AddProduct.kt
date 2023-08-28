package com.example.ev2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ev2.db.AppDatabase
import com.example.ev2.db.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddProduct : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AddProductUI()
        }


    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddProductUI() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val routineScope = rememberCoroutineScope()
            val context = LocalContext.current
            var text by remember { mutableStateOf("") }
            var isSnackbarVisible by remember { mutableStateOf(false) }
            val focusManager = LocalFocusManager.current

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = {
                    Text(text = resources.getString(R.string.add_product))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { routineScope.launch( Dispatchers.IO ) {
                        val dao = AppDatabase.getInstace( context ).productDao()

                        dao.insert(Product(0, text, false))
                        text = ""
                    }
                        focusManager.clearFocus()
                        isSnackbarVisible = true
                    }
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = resources.getString(R.string.add_product)
                    )
                }
            )
            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green
                )
            ){
                Text(resources.getString(R.string.back))
            }

            if (isSnackbarVisible) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = Color.Gray, // Customize the color

                ) {
                    Text(text = resources.getString(R.string.product_added))
                }
                LaunchedEffect(isSnackbarVisible) {
                    if (isSnackbarVisible) {
                        // Delay for approximately 1 second (1000 milliseconds)
                        delay(1500)
                        isSnackbarVisible = false
                    }
                }
            }
        }
    }
}