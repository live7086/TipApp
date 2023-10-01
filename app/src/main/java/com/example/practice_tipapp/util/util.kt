package com.example.practice_tipapp.util

fun calculateTotalTip(totalBillState: Double, tipPer: Int): Double {
    return if(
        totalBillState > 1 &&
        totalBillState.toString().isNotEmpty()
    )
        (totalBillState * tipPer) / 100
    else 0.0

}

fun calTotalPerPerson (totalBillState: Double, tipPer: Int, split: Int) : Double {
        val bill = calculateTotalTip(totalBillState = totalBillState, tipPer = tipPer) + totalBillState
        return (bill/split)
}