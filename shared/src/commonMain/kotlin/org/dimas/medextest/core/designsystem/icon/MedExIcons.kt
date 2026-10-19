package org.dimas.medextest.core.designsystem.icon

object MedExIcons {
    private const val PHONE_BODY =
        "M22 16.92v3a2 2 0 0 1-2.18 2a19.79 19.79 0 0 1-8.63-3.07a19.5 19.5 0 0 1-6-6" +
            "a19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72a12.84 12.84 0 0 0 .7 2.81" +
            "a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45a12.84 12.84 0 0 0 2.81.7" +
            "A2 2 0 0 1 22 16.92z"

    private const val HEART = "M12 20.5S3 15 3 8.8A4.8 4.8 0 0 1 12 6.3A4.8 4.8 0 0 1 21 8.8C21 15 12 20.5 12 20.5z"

    val Search by lazy { lineIcon("search", "M11 3a8 8 0 1 0 0 16a8 8 0 1 0 0-16z", "M21 21l-4.35-4.35") }
    val Calendar by lazy {
        lineIcon(
            "calendar",
            "M8 2v4", "M16 2v4",
            "M5 4h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2z",
            "M3 10h18",
        )
    }
    val CalendarPlus by lazy {
        lineIcon(
            "calendar-plus",
            "M8 2v4", "M16 2v4",
            "M21 13V6a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h8",
            "M3 10h18", "M16 19h6", "M19 16v6",
        )
    }
    val House by lazy { lineIcon("house", "M3 10.5L12 3l9 7.5V19a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z") }
    val Phone by lazy { lineIcon("phone", PHONE_BODY) }
    val PhoneIncoming by lazy { lineIcon("phone-incoming", "M16 2v6h6", "M22 2l-6 6", PHONE_BODY) }
    val ChevronRight by lazy { lineIcon("chevron-right", "M9 18l6-6-6-6") }
    val ChevronDown by lazy { lineIcon("chevron-down", "M6 9l6 6 6-6") }
    val ArrowLeft by lazy { lineIcon("arrow-left", "M19 12H5", "M12 19l-7-7 7-7") }
    val Pencil by lazy { lineIcon("pencil", "M17 3a2.83 2.83 0 1 1 4 4L7.5 20.5L2 22l1.5-5.5z", "M15 5l4 4") }
    val Plus by lazy { lineIcon("plus", "M12 5v14", "M5 12h14") }
    val Heart by lazy { lineIcon("heart", HEART) }
    val HeartFilled by lazy { filledIcon("heart-filled", HEART) }
    val MapPin by lazy { lineIcon("map-pin", "M12 21s-7-6.2-7-11.5a7 7 0 0 1 14 0C19 14.8 12 21 12 21z", "M12 7a2.5 2.5 0 1 0 0 5a2.5 2.5 0 1 0 0-5z") }
    val Image by lazy {
        lineIcon(
            "image",
            "M5 3h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2z",
            "M9 7.5a1.5 1.5 0 1 0 0 3a1.5 1.5 0 1 0 0-3z",
            "M21 15l-5-5L5 21",
        )
    }
    val Camera by lazy {
        lineIcon(
            "camera",
            "M14.5 4h-5L7 7H4a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2h-3z",
            "M12 10a3 3 0 1 0 0 6a3 3 0 1 0 0-6z",
        )
    }
    val File by lazy { lineIcon("file", "M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z", "M14 2v6h6") }
    val Copy by lazy {
        lineIcon(
            "copy",
            "M10 8h10a2 2 0 0 1 2 2v10a2 2 0 0 1-2 2H10a2 2 0 0 1-2-2V10a2 2 0 0 1 2-2z",
            "M4 16a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2",
        )
    }
    val Map by lazy { lineIcon("map", "M3 6l6-3 6 3 6-3v15l-6 3-6-3-6 3z", "M9 3v15", "M15 6v15") }
    val Sliders by lazy {
        lineIcon(
            "sliders",
            "M4 21v-7", "M4 10V3", "M12 21v-9", "M12 8V3", "M20 21v-5", "M20 12V3",
            "M1 14h6", "M9 8h6", "M17 16h6",
        )
    }
    val Info by lazy { lineIcon("info", "M12 2a10 10 0 1 0 0 20a10 10 0 1 0 0-20z", "M12 16v-4", "M12 8h.01") }

    val TabHome by lazy { lineIcon("tab-home", "M3 10l9-7 9 7v10a2 2 0 0 1-2 2h-4v-6h-6v6H5a2 2 0 0 1-2-2z") }
    val TabShowcase by lazy {
        lineIcon(
            "tab-showcase",
            "M6 3h12a3 3 0 0 1 3 3v12a3 3 0 0 1-3 3H6a3 3 0 0 1-3-3V6a3 3 0 0 1 3-3z",
            "M10 7.5h4v3h3v4h-3v3h-4v-3H7v-4h3z",
        )
    }
    val TabMedCard by lazy {
        lineIcon(
            "tab-medcard",
            "M9 3h6a1 1 0 0 1 1 1v1a1 1 0 0 1-1 1H9a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1z",
            "M16 4h1a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h1",
        )
    }
    val TabMore by lazy { lineIcon("tab-more", "M4 6h16", "M4 12h16", "M4 18h16") }
}
