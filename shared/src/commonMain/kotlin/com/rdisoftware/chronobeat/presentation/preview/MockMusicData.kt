package com.rdisoftware.chronobeat.presentation.preview

import com.rdisoftware.chronobeat.domain.models.Track

object MockMusicData {

    val songs = listOf(
        Track("1", "Sweet Child O' Mine", "Guns N' Roses", emptyList(), 1987, true),
        Track("2", "Para", "Dzsudló", emptyList(), 2021, true),
        Track("3", "Make It Happen", "RÜFÜS DU SOL", emptyList(), 2021, true),
        Track("4", "Paris", "The Chainsmokers", emptyList(), 2017, true),
        Track("5", "Young", "The Chainsmokers", emptyList(), 2017, true),
        Track("6", "After Hours", "The Weeknd", emptyList(), 2020, true),
        Track("7", "Blinding Lights", "The Weeknd", emptyList(), 2020, true),
        Track("8", "Thunderstruck", "AC/DC", emptyList(), 1990, true),
        Track("9", "Moneytalks", "AC/DC", emptyList(), 1990, true),
        Track("10", "Pump it", "Black Eyed Peas", emptyList(), 2005, true),

        // Magyar kedvencek
        Track("11", "Azahriah", "Desh", listOf("Young Fly"), 2022, true),
        Track("12", "Úristen", "VALMAR", listOf("Szikora Róbert"), 2022, true),
        Track("13", "Mindenki táncol", "Majka", emptyList(), 2018, true),
        Track("14", "Azt mondtad", "Szabó Benedek és a Galaxisok", emptyList(), 2014, true),
        Track("15", "Várni vártunk", "Krúbi", emptyList(), 2020, true),
        Track("16", "Lehetnék én is", "Carson Coma", emptyList(), 2020, true),
        Track("17", "Szélcsend", "Elefánt", emptyList(), 2017, true),
        Track("18", "Buborék", "Beton.Hofi", emptyList(), 2021, true),

        // Rock & Metal Classics
        Track("19", "Bohemian Rhapsody", "Queen", emptyList(), 1975, true),
        Track("20", "Smells Like Teen Spirit", "Nirvana", emptyList(), 1991, true),
        Track("21", "Enter Sandman", "Metallica", emptyList(), 1991, true),
        Track("22", "In the End", "Linkin Park", emptyList(), 2000, true),
        Track("23", "Seven Nation Army", "The White Stripes", emptyList(), 2003, true),
        Track("24", "Hotel California", "Eagles", emptyList(), 1976, true),
        Track("25", "Mr. Brightside", "The Killers", emptyList(), 2004, true),

        // Pop & Electronic
        Track("26", "Levitating", "Dua Lipa", emptyList(), 2020, true),
        Track("27", "Bad Guy", "Billie Eilish", emptyList(), 2019, true),
        Track("28", "Wake Me Up", "Avicii", emptyList(), 2013, true),
        Track("29", "Starlight", "Muse", emptyList(), 2006, true),
        Track("30", "Get Lucky", "Daft Punk", listOf("Pharrell Williams"), 2013, true),
        Track("31", "Flowers", "Miley Cyrus", emptyList(), 2023, true),
        Track("32", "Shape of You", "Ed Sheeran", emptyList(), 2017, true),

        // Hip-Hop & Rap
        Track("33", "Lose Yourself", "Eminem", emptyList(), 2002, true),
        Track("34", "SICKO MODE", "Travis Scott", emptyList(), 2018, true),
        Track("35", "HUMBLE.", "Kendrick Lamar", emptyList(), 2017, true),
        Track("36", "God's Plan", "Drake", emptyList(), 2018, true),
        Track("37", "Old Town Road", "Lil Nas X", emptyList(), 2019, true),

        // 80s & 90s Nostalgia
        Track("38", "Take on Me", "a-ha", emptyList(), 1985, true),
        Track("39", "Billie Jean", "Michael Jackson", emptyList(), 1982, true),
        Track("40", "Africa", "Toto", emptyList(), 1982, true),
        Track("41", "Wonderwall", "Oasis", emptyList(), 1995, true),
        Track("42", "Torn", "Natalie Imbruglia", emptyList(), 1997, true),
        Track("43", "No Diggity", "Blackstreet", listOf("Dr. Dre", "Queen Pen"), 1996, true),

        // Modern & Indie
        Track("44", "Midnight City", "M83", emptyList(), 2011, true),
        Track("45", "Do I Wanna Know?", "Arctic Monkeys", emptyList(), 2013, true),
        Track("46", "Heat Waves", "Glass Animals", emptyList(), 2020, true),
        Track("47", "As It Was", "Harry Styles", emptyList(), 2022, true),
        Track("48", "Running Up That Hill", "Kate Bush", emptyList(), 1985, true),
        Track("49", "Stay", "The Kid LAROI", listOf("Justin Bieber"), 2021, true),
        Track("50", "Save Your Tears", "The Weeknd", listOf("Ariana Grande"), 2021, true)
    )
}