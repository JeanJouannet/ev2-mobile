package com.example.ev2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.ev2.db.AppDatabase
import com.example.ev2.db.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ListProductsUI()
        }
    }

    @Composable
    fun ListProductsUI(){
        val (products, setProducts) = remember { mutableStateOf(emptyList<Product>())}
        val context = LocalContext.current

        LaunchedEffect(products) {
            withContext(Dispatchers.IO) {
                val dao = AppDatabase.getInstace( context ).productDao()
                setProducts(dao.findAll())
            }

        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(products) {product ->
                ProductUI(product) {
                    setProducts( emptyList<Product>())
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, AddProduct::class.java)
                    startActivity(intent);
                },
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            )
        }
    }

    @Composable
    fun ProductUI(product:Product, onSave:() -> Unit = {}) {
        val routineScope = rememberCoroutineScope()
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 20.dp)
        ) {
            if(product.bought) {
                Image(painter = painterResource(id = R.drawable.tick),
                    contentDescription = resources.getString(R.string.bought),
                    modifier = Modifier.clickable {
                        routineScope.launch( Dispatchers.IO ) {
                            val dao = AppDatabase.getInstace( context ).productDao()
                            product.bought = false
                            dao.update( product )
                            onSave()
                        }
                    })
            } else
            {
                Image(painter = painterResource(id = R.drawable.shopping_cart),
                    contentDescription = resources.getString(R.string.notBought),
                    modifier = Modifier.clickable {
                        routineScope.launch( Dispatchers.IO ) {
                            val dao = AppDatabase.getInstace( context ).productDao()
                            product.bought = true
                            dao.update( product )
                            onSave()
                        }
                    })
            }

            Spacer(modifier = Modifier.width(20.dp))

            Text(text = product.name, modifier = Modifier.weight(2f), fontSize = 5.em)

            Button(
                onClick = {

                    routineScope.launch( Dispatchers.IO ) {
                        val dao = AppDatabase.getInstace( context ).productDao()
                        dao.delete( product )
                        onSave()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ){
                Text(text = resources.getString(R.string.delete))
            }
        }

    }
}
