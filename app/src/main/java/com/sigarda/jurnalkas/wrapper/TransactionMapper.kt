package com.sigarda.jurnalkas.wrapper

import android.graphics.Color
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.data.local.entity.Transaction
import com.sigarda.jurnalkas.utils.enums.Type
import com.sigarda.jurnalkas.utils.enums.UiColor
import com.sigarda.jurnalkas.model.TransactionUiModel

class TransactionMapper {

    fun map(entity: Transaction): TransactionUiModel {
        return entity.toUiModel()
    }

    private fun Transaction.toUiModel() = TransactionUiModel(
        amount = getAmount(),
        isIncome = getIncome(),
        type = getType(),
        date = getDate(),
        icon = getIcon(),
        textColor = getTextColor(),
        strokeColor = getStrokeColor(),
        chartColor = getChartColor(),
        cardColor = getCardColor(),
        id = id
    )
    private fun Transaction.getAmount() = amount
    private fun Transaction.getIncome() = isIncome
    private fun Transaction.getType() = type
    private fun Transaction.getDate() = date
    private fun Transaction.getIcon() = when (type) {
        Type.FOOD.type -> R.drawable.icons8_chocolate_bar_emoji_96
        Type.EAT.type -> R.drawable.icons8_steaming_bowl_96
        Type.DAILY.type -> R.drawable.icons8_broom_96
        Type.TRAFFIC.type -> R.drawable.icons8_bus_96
        Type.PRESENT.type -> R.drawable.icons8_wrapped_gift_96
        Type.HOUSING.type -> R.drawable.icons8_house_96
        Type.EDUCATION.type -> R.drawable.icons8_graduation_cap_96
        Type.FUN.type -> R.drawable.icons8_admission_96
        Type.SALARY.type -> R.drawable.icons8_money_with_wings_96
        Type.PARTTIME.type -> R.drawable.icons8_money_with_wings_96
        Type.INVEST.type -> R.drawable.icons8_money_with_wings_96
        Type.BONUS.type -> R.drawable.icons8_money_with_wings_96
        else -> R.drawable.ic_others}
    }

    private fun Transaction.getTextColor() = when (isIncome) {
        true -> Color.GREEN
        else -> Color.RED
    }

    private fun Transaction.getStrokeColor() = when (isIncome) {
        true -> Color.GREEN
        else -> Color.RED
    }

    private fun Transaction.getChartColor() = when (type) {
        Type.SALARY.type -> UiColor.Green.color
        Type.PARTTIME.type -> UiColor.Green.color
        Type.BONUS.type -> UiColor.Green.color
        Type.INVEST.type -> UiColor.Green.color
        else -> UiColor.Red.color
    }

    private fun Transaction.getCardColor() = when (isIncome) {
        true -> UiColor.Green.color
        else -> UiColor.Red.color
    }

//Type.FOOD.type -> R.drawable.icons8_chocolate_bar_emoji_96
//Type.EAT.type -> R.drawable.icons8_steaming_bowl_96
//Type.DAILY.type -> R.drawable.icons8_broom_96
//Type.TRAFFIC.type -> R.drawable.icons8_bus_96
//Type.PRESENT.type -> R.drawable.icons8_wrapped_gift_96
//Type.HOUSING.type -> R.drawable.icons8_house_96
//Type.EDUCATION.type -> R.drawable.icons8_graduation_cap_96
//Type.FUN.type -> R.drawable.icons8_admission_96
//Type.SALARY.type -> R.drawable.icons8_money_with_wings_96
//Type.PARTTIME.type -> R.drawable.icons8_money_with_wings_96
//Type.INVEST.type -> R.drawable.icons8_money_with_wings_96
//Type.BONUS.type -> R.drawable.icons8_money_with_wings_96
//else -> R.drawable.ic_others}





