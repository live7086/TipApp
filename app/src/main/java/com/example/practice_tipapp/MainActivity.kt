package com.example.practice_tipapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice_tipapp.components.InputField
import com.example.practice_tipapp.ui.theme.Practice_TipAPPTheme
import com.example.practice_tipapp.util.calTotalPerPerson
import com.example.practice_tipapp.util.calculateTotalTip
import com.example.practice_tipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {


            }
        }
    }
}
@Composable
fun MyApp(content: @Composable () -> Unit) {
    Practice_TipAPPTheme {
        Surface(color = Color.White) {
            Column{
                MainContent()
            }
        }
    }
}

@Preview
@Composable
fun TopHeader (totalPerPers : Double = 0.0){
    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
        color = Color.White
        ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            val total = "%.2f".format(totalPerPers)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.bodySmall,
                //color = Color.Black,
                modifier = Modifier
                  //.fillMaxWidth()
                  .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.bodyMedium,
                //color = Color.Black,
                modifier = Modifier
                  //.fillMaxWidth()
                  .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun MainContent() {
    val splitState = remember {
        mutableStateOf(1)
    }
    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    BillForm(splitState = splitState,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState)
}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    splitState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    onValChange: (String) -> Unit = {},
){

    //State
    val totalBillState = remember{
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value){
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }
    val tipPer = (sliderPositionState.value * 100).toInt()

TopHeader(totalPerPers = totalPerPersonState.value)
    Surface (
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape =  RoundedCornerShape(10.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
    )  {
        Surface (color = Color.White) {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                InputField(
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    singleLine = true,
                    onAction = KeyboardActions {
                        if(!validState) return@KeyboardActions
                        onValChange(totalBillState.value.trim())
                        keyboardController?.hide()
                    }
                )
                //if(validState){
                    Row (
                        modifier = Modifier.padding(3.dp),
                        horizontalArrangement = Arrangement.Start) {
                        Text(
                            color = Color.Black,
                            text = "Split",
                            modifier = Modifier.align( alignment = Alignment.CenterVertically),
                        )
                        Spacer(modifier = Modifier.width(120.dp)
                        )
                        Row (
                            modifier = Modifier.padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            val splitState = remember {
                                mutableStateOf(1)
                            }
                            val minValue = 1
                            val maxValue = 10
                            RoundIconButton(imageVector = Icons.Default.Delete,
                                onClick =   {

                                    if (splitState.value > minValue)
                                    {
                                        splitState.value -= 1
                                        totalPerPersonState.value = calTotalPerPerson(totalBillState.value.toDouble(), tipPer, split = splitState.value)

                                    }
                                }
                            )
                            Text(
                                text = "${splitState.value}",
                                modifier = Modifier
                                    .padding(start = 6.dp, end = 6.dp)
                                    .align(Alignment.CenterVertically),
                                color = Color.Black)
                            RoundIconButton(imageVector = Icons.Default.Add,
                                onClick = {if (splitState.value < maxValue){
                                    splitState.value += 1
                                    totalPerPersonState.value = calTotalPerPerson(totalBillState.value.toDouble(), tipPer, split = splitState.value)

                                } })
                        }
                    }
                    Row {
                        Text(text = "Tip",modifier = Modifier.align(Alignment.CenterVertically),color = Color.Black)
                        Spacer(modifier = Modifier.width(200.dp))
                        Text(text = "${tipAmountState.value}",modifier = Modifier.align(Alignment.CenterVertically),color = Color.Black)

                    }
                Column {
                    Text(text = "$${tipAmountState.value}",modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.Black)
                }
                    Spacer(modifier = Modifier.height(14.dp))

                    Slider(
                            modifier = Modifier.padding(16.dp),
                            value = sliderPositionState.value,
                            onValueChange = { newVal ->
                                sliderPositionState.value = newVal
                                tipAmountState.value =
                                    calculateTotalTip(totalBillState.value.toDouble(),tipPer)

                                totalPerPersonState.value =
                                    calTotalPerPerson(
                                    totalBillState.value.toDouble(),
                                    tipPer,
                                    splitState.value
                                )

                            },
                        onValueChangeFinished = {}
                        )
                //}else{

                //}
            }
        }

    }
}




