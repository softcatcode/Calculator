package com.codingeveryday.calcapp.data.implementations

import com.codingeveryday.calcapp.data.database.HistoryItemDbModel
import com.codingeveryday.calcapp.domain.entities.HistoryItem
import com.codingeveryday.calcapp.domain.interfaces.HistoryItemMapper
import javax.inject.Inject

class HistoryItemMapperImpl @Inject constructor(): HistoryItemMapper {
    override fun mapHistoryItemToDbModel(item: HistoryItem) = HistoryItemDbModel(
        id = item.id,
        expr = item.expr,
        base = item.base,
        result = item.result
    )

    override fun mapHistoryItemDbModelToItem(model: HistoryItemDbModel) = HistoryItem(
        id = model.id,
        expr = model.expr,
        base = model.base ?: 10,
        result = model.result
    )

    override fun mapListDbModelToListEntity(list: List<HistoryItemDbModel>) = list.map {
        mapHistoryItemDbModelToItem(it)
    }
}