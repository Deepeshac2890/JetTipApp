package com.example.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GetCanvas()
        }
    }
    
    @OptIn(ExperimentalUnitApi::class)
    @Preview(showBackground = true)
    @Composable
    fun GetCanvas() {
        val totalCostPerPerson = remember{
            mutableStateOf(13.6789)
        }
        JetTipAppTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                ) {
                    GetHeader(totalCostPerPerson.value)
                    Box(modifier = Modifier.height(5.dp))
                    getFooter()
                }
            }
        }

    }

    @OptIn(ExperimentalUnitApi::class)
    @Composable
    fun GetHeader(costPerPerson: Double) {
        Card(modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color(0xffE9D7F7)) {
            Column(modifier = Modifier.padding(vertical = 50.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally ) {
                Text("Total Per Person", style = TextStyle(fontSize = TextUnit(20F,
                    TextUnitType.Sp), fontWeight = FontWeight.Bold, color = Color.Black))
                Text("$ ${"%.2f".format(costPerPerson)}", style = TextStyle(fontSize = TextUnit(22F,
                    TextUnitType.Sp), fontWeight = FontWeight.ExtraBold, color = Color.Black))
            }
        }
    }

    @Composable
    fun getFooter() {
        BillForm(){
            print(it)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun BillForm(modifier: Modifier = Modifier,
                 onValChange: (String) -> Unit = {}){

        val billAmount = remember {
            mutableStateOf("0.00")
        }

        val peopleCount = remember {
            mutableStateOf(3)
        }

        val tipPercent = remember {
            mutableStateOf(1)
        }

        val validState = remember(billAmount.value) {
            billAmount.value.trim().isNotEmpty()
        }

        val keyboardController = LocalSoftwareKeyboardController.current;

        Card(border = BorderStroke(1.dp,Color.LightGray)) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally){
                InputField(
                    valueState = billAmount,
                    labelId = "Enter Amount",
                    enabled = true,
                    isSingleLine = true,
                    action = KeyboardActions{
                        if (!validState) {
                            return@KeyboardActions
                        }
                        onValChange(billAmount.value.trim())
                        keyboardController?.hide()
                    }
                )
            }
        }
        Row() {
            
        }
    }
}
