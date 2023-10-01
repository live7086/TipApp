package com.example.practice_tipapp

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.practice_tipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column{
                    TopHeader()
                    MainContent()
                }

            }
        }
    }
}
@Composable
fun MyApp(content: @Composable () -> Unit) {
    Practice_TipAPPTheme {
        Surface(color = Color.White) {
            content()
        }
    }
}

@Preview
@Composable
fun TopHeader (totalPerPerson : Double = 0.0){
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
            val total = "%.2f".format(totalPerPerson)
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
    // 創建一個可觀察的狀態來儲存帳單金額
    val billAmtState = remember { mutableStateOf("") }

    // 呼叫 BillForm 並更新 billAmtState
    BillForm { billAmt ->
        billAmtState.value = billAmt
    }

    // 使用 billAmtState 來顯示文字
    Text(text = "帳單金額：${billAmtState.value}")
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValChange: (String) -> Unit={},
){
    val totalBillState = remember{
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value){
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
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
                if(validState){
                    Row (
                        modifier = Modifier.padding(3.dp),
                        horizontalArrangement = Arrangement.Start) {
                        Text(
                            text = "Split",
                            modifier = Modifier.align( alignment = Alignment.CenterVertically),
                        )
                        Spacer(modifier = Modifier.width(120.dp)
                        )
                        Row (
                            modifier = Modifier.padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            RoundIconButton(imageVector = Icons.Default.Delete,
                                onClick = {})
                            RoundIconButton(imageVector = Icons.Default.Add,
                                onClick = {})
                        }
                    }
                }else{

                }
            }
        }

    }
}