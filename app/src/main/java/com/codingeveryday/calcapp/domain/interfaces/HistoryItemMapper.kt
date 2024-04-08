package com.codingeveryday.calcapp.domain.interfaces

import com.codingeveryday.calcapp.data.HistoryItemDbModel
import com.codingeveryday.calcapp.domain.entities.HistoryItem

interface HistoryItemMapper {
    fun mapHistoryItemToDbModel(item: HistoryItem): HistoryItemDbModel

    fun mapHistoryItemDbModelToItem(model: HistoryItemDbModel): HistoryItem

    fun mapListDbModelToListEntity(list: List<HistoryItemDbModel>): List<HistoryItem>
}