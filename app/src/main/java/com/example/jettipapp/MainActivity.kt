package com.example.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.widgets.RoundIconButton

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
            mutableStateOf(0.00)
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
                    GetFooter(){
                        totalCostPerPerson.value = it.toDouble()
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalUnitApi::class)
    @Composable
    fun GetHeader(costPerPerson: Double) {
        Card(modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 20.dp)
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
    fun GetFooter(updateTotalCostPerPerson: (String) -> Unit) {
        BillForm(){
            updateTotalCostPerPerson(it)
        }
    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun BillForm(onValChange: (String) -> Unit = {}){

        val billAmount = remember {
            mutableStateOf("")
        }

        val peopleCount = remember {
            mutableStateOf(1)
        }

        val tipPercent = remember {
            mutableStateOf(0f)
        }

        val tipValue = remember{
            mutableStateOf(0.00)
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
                    labelId = "Enter Bill Amount",
                    enabled = true,
                    isSingleLine = true,
                    action = KeyboardActions{
                        if (!validState) {
                            return@KeyboardActions
                        }
                        onValChange(
                            calculateBillAmount(
                            totalBill = billAmount.value.toDouble(),
                            peopleCount = peopleCount.value,
                            tipValue = tipValue.value).toString()
                        )
                        keyboardController?.hide()
                    }
                )
                if (validState)
                {
                    PeopleCount(
                        peopleCount = peopleCount,
                        billAmount = billAmount,
                        tipValue = tipValue)
                    {
                            onValChange(it)
                    }

                    Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)){
                        Text("Tip : " , modifier = Modifier.align(Alignment.CenterVertically))
                        Spacer(modifier = Modifier.width(200.dp))
                        Text("$${"%.2f".format(tipValue.value)}" , modifier = Modifier.align(Alignment.CenterVertically))
                    }

                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "%.2f".format(tipPercent.value * 100) + " %")
                        Spacer(modifier = Modifier.height(14.dp))
                        Slider(
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            steps= 5,
                            value = tipPercent.value, onValueChange = {
                            tipPercent.value = it
                            tipValue.value = tipPercent.value * billAmount.value.toDouble()
                            onValChange(
                                calculateBillAmount(
                                    totalBill = billAmount.value.toDouble(),
                                    peopleCount = peopleCount.value,
                                    tipValue = tipValue.value).toString()
                            )
                        })
                    }
                }
                else {
                    Box() {}
                }
            }
        }
    }

    @Composable
    fun PeopleCount(peopleCount: MutableState<Int>,
                    billAmount: MutableState<String>,
                    tipValue: MutableState<Double>,
                    onValChange: (String) -> Unit) {
        Row(modifier= Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start) {
            Text("Split", modifier = Modifier.align(alignment = Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(120.dp))
            Row(modifier = Modifier.padding(horizontal = 3.dp),
                horizontalArrangement = Arrangement.End) {
                RoundIconButton(
                    imageVector = Icons.Default.Remove,
                    onClick = {
                        if (peopleCount.value > 1) {
                            peopleCount.value -= 1
                            onValChange(
                                calculateBillAmount(
                                    totalBill = billAmount.value.toDouble(),
                                    peopleCount = peopleCount.value,
                                    tipValue = tipValue.value).toString()
                            )
                        }
                    })
                Text(text= peopleCount.value.toString() ,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 9.dp, end = 9.dp))
                RoundIconButton(
                    imageVector = Icons.Default.Add,
                    onClick = {
                        peopleCount.value += 1
                        onValChange(
                            calculateBillAmount(
                                totalBill = billAmount.value.toDouble(),
                                peopleCount = peopleCount.value,
                                tipValue = tipValue.value).toString()
                        )
                    })
            }
        }
    }

    private fun calculateBillAmount(totalBill: Double, peopleCount: Int, tipValue: Double) : Double = (totalBill + tipValue) / peopleCount
}
