package com.codingeveryday.calcapp.data.mapper

import com.codingeveryday.calcapp.data.database.HistoryItemDbModel
import com.codingeveryday.calcapp.domain.entities.HistoryItem
import javax.inject.Inject

class HistoryItemMapper @Inject constructor() {
    fun mapHistoryItemToDbModel(item: HistoryItem) = HistoryItemDbModel(
        id = item.id,
        expr = item.expr,
        base = item.base,
        result = item.result
    )

    fun mapHistoryItemDbModelToItem(model: HistoryItemDbModel) = HistoryItem(
        id = model.id,
        expr = model.expr,
        base = model.base ?: 10,
        result = model.result
    )

    fun mapListDbModelToListEntity(list: List<HistoryItemDbModel>) = list.map {
        mapHistoryItemDbModelToItem(it)
    }
}