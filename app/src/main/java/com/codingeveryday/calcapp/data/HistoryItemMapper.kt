package com.codingeveryday.calcapp.data

import com.codingeveryday.calcapp.domain.entities.HistoryItem

class HistoryItemMapper {
    companion object {
        fun mapHistoryItemToDbModel(item: HistoryItem) = HistoryItemDbModel(
            id = item.id,
            expr = item.expr,
            result = item.result
        )

        fun mapHistoryItemDbModelToItem(model: HistoryItemDbModel) = HistoryItem(
            id = model.id,
            expr = model.expr,
            result = model.result
        )

        fun mapListDbModelToListEntity(list: List<HistoryItemDbModel>) = list.map {
            mapHistoryItemDbModelToItem(it)
        }

    }
}