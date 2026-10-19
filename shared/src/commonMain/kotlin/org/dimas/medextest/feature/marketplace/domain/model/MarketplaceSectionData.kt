package org.dimas.medextest.feature.marketplace.domain.model

sealed interface MarketplaceSectionData {
    val isEmpty: Boolean

    data class Promotions(val items: List<Promotion>) : MarketplaceSectionData {
        override val isEmpty: Boolean get() = items.isEmpty()
    }

    data class Doctors(val items: List<DoctorSpecialty>) : MarketplaceSectionData {
        override val isEmpty: Boolean get() = items.isEmpty()
    }

    data class LabTests(val items: List<ServiceOffer>) : MarketplaceSectionData {
        override val isEmpty: Boolean get() = items.isEmpty()
    }

    data class Packages(val items: List<ServicePackage>) : MarketplaceSectionData {
        override val isEmpty: Boolean get() = items.isEmpty()
    }

    data class Examinations(val items: List<ServiceOffer>) : MarketplaceSectionData {
        override val isEmpty: Boolean get() = items.isEmpty()
    }

    data class Services(val items: List<ServiceCategory>) : MarketplaceSectionData {
        override val isEmpty: Boolean get() = items.isEmpty()
    }
}
