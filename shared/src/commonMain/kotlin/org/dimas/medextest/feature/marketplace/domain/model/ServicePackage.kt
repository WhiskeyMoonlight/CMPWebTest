package org.dimas.medextest.feature.marketplace.domain.model

data class ServicePackage(val id: String, val title: String, val price: Int, val oldPrice: Int?, val palette: Palette)
